package com.igt.discount.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Costumer;
import com.igt.hibernate.bean.IgtOrder;
import com.igt.hibernate.bean.PeerGroup;

public class DiscountManager {

	static final Logger log = (Logger) LogManager.getLogger(DiscountManager.class);

	public DiscountManager() {
		log.debug("DiscountManager initialized");
	}

	protected RestResponse approveDiscountRequest(int costumerId, DatabaseManager databaseManager) {
		boolean success = false;

		double totalSales = 0;
		double costumerSales = 0;
		double totalSalesPeerGroup = 0;
		double firstRatio = 0;
		double secondRatio = 0;

		String message = null;
		List<Costumer> allCostumers = null;
		Costumer costumer = null;
		PeerGroup peerGroup = null;
		Map<String, Object> response = new HashMap<String, Object>();

		costumer = this.getCostumer(costumerId, databaseManager);
		costumerSales = this.getTotalSalesFromCostumer(costumer.getCostumerId(), databaseManager);
		totalSales = this.getTotalSales(databaseManager);

		firstRatio = this.createRatioBetween(totalSales, costumerSales);

		if (firstRatio > 10) {
			message = "first Ratio success";
			success = true;
		} else {
			allCostumers = this.getAllCostumers(databaseManager);
			peerGroup = this.getPeerGroup(costumerId, databaseManager);
			peerGroup = this.createOrUpdatePeerGroupForCostumer(peerGroup, costumer, allCostumers, databaseManager);
			totalSalesPeerGroup = this.getTotalSalesFromPeerGroup(peerGroup.getPeerGroupId(), databaseManager);
			secondRatio = this.createRatioBetween(totalSalesPeerGroup, costumerSales);

			if (secondRatio > 20) {
				message = "second Ratio success";
				success = true;
			} else {
				message = "costumer is not authorized to get discount";
			}
		}

		response.put("Costumer Sales", costumerSales);
		response.put("Peer Group Total Sales", totalSalesPeerGroup);
		response.put("Total Sales", totalSales);
		response.put("First Ratio", firstRatio);
		response.put("Second Ratio", secondRatio);

		return new RestResponse(success, message, response);
	}

	protected boolean createOrUpdatePeerGroup(PeerGroup peerGroup, DatabaseManager databaseManager) {
		boolean success = false;

		try {
			databaseManager.beginTransaction();
			databaseManager.saveOrUpdateEntity(peerGroup);
			databaseManager.commitTransaction();
			success = true;
		} catch (Exception e) {
			log.debug("Could not connect to Database", e);
		} finally {
			databaseManager.endTransaction();
		}

		return success;
	}

	protected PeerGroup createOrUpdatePeerGroupForCostumer(PeerGroup peerGroup, Costumer peerGroupOwner,
			List<Costumer> allCostumers, DatabaseManager databaseManager) {
		int peerGroupOwnerAge = this.getAgeFromCostumer(peerGroupOwner.getCostumerId(), databaseManager);
		Set<Costumer> tempCostumers = new HashSet<Costumer>();

		// create peer group if not yet available if available save all existing
		// costumers in temp costumer set
		if (peerGroup == null) {
			peerGroup = new PeerGroup();
			peerGroup.setOwnerId(peerGroupOwner.getCostumerId());
		} else {
			tempCostumers.addAll(peerGroup.getCostumers());
		}

		// find out if costumer is in the valid age range to be in the peer
		// group
		// if so add him to the peer group but only if he is not yet in the peer
		// group
		for (Costumer costumer : allCostumers) {
			int costumerAge = this.getAgeFromCostumer(costumer.getCostumerId(), databaseManager);
			boolean success = this.checkAge(peerGroupOwnerAge, costumerAge);
			if (success) {
				if (tempCostumers.contains(costumer)) {
					log.debug("Costumer is already in this Peer Group");
				} else {
					tempCostumers.add(costumer);
				}
			} else {
				log.debug("The Costumer is not in the valid age range");
			}
		}

		peerGroup.setCostumers(tempCostumers);
		// because of the many to many mapping we need to set both ends of the
		// relationship
		for (Costumer costumer : tempCostumers) {
			costumer.addPeerGroup(peerGroup);
		}

		return peerGroup;
	}

	public boolean checkAge(int peerGroupOwnerAge, int costumerAge) {
		boolean success = false;

		if (peerGroupOwnerAge != 0 || costumerAge != 0) {
			if (peerGroupOwnerAge >= (costumerAge - 5) && peerGroupOwnerAge <= (costumerAge + 5)) {
				success = true;
			}
		}

		return success;
	}

	public double getTotalSalesFromCostumer(int costumerId, DatabaseManager databaseManager) {
		double totalSales = 0;
		Set<IgtOrder> orders = null;
		Costumer costumer = null;

		costumer = this.getCostumer(costumerId, databaseManager);

		if (costumer != null) {
			if (costumer.getIgtOrders() != null) {
				orders = costumer.getIgtOrders();
				for (IgtOrder order : orders) {
					totalSales += order.getValue();
				}
			}
		}

		return totalSales;
	}

	public double getTotalSalesFromPeerGroup(int costumerId, DatabaseManager databaseManager) {
		double totalSales = 0;
		Set<Costumer> costumers = null;
		PeerGroup peerGroup = null;

		peerGroup = this.getPeerGroup(costumerId, databaseManager);
		costumers = peerGroup.getCostumers();

		if (costumers != null) {
			for (Costumer costumer : costumers) {
				costumerId = costumer.getCostumerId();
				totalSales = this.getTotalSalesFromCostumer(costumerId, databaseManager);
				totalSales += totalSales;
			}
		} else {
			totalSales = 0;
		}

		return totalSales;
	}

	public double getTotalSales(DatabaseManager databaseManager) {
		double totalSales = 0;
		List<IgtOrder> orders = null;

		try {
			databaseManager.beginTransaction();
			orders = databaseManager.getAllOrders();
			for (IgtOrder order : orders) {
				totalSales += order.getValue();
			}
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		}
		return totalSales;
	}

	public int getAgeFromCostumer(int costumerId, DatabaseManager databaseManager) {
		Calendar calendar = Calendar.getInstance();
		Costumer costumer = null;
		int age = 0;

		costumer = this.getCostumer(costumerId, databaseManager);

			Date birthday = costumer.getBirthday();

			// get year of birth from costumer
			calendar.setTime(birthday);
			int yearOfBirth = calendar.get(Calendar.YEAR);
			int dayOfYearInYearOfBirth = calendar.get(Calendar.DAY_OF_YEAR);

			// get current date
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			int currentDayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

			if (dayOfYearInYearOfBirth > currentDayOfYear) {
				age = (currentYear - yearOfBirth) - 1;
			} else {
				age = currentYear - yearOfBirth;
			}

		return age;
	}

	public double createRatioBetween(Double total, Double part) {
		double ratio = 0;

		if (total != 0.0) {
			ratio = ((part * 100) / total);
		}

		return ratio;
	}

	public Costumer getCostumer(int costumerId, DatabaseManager databaseManager) {
		Costumer costumer = null;

		try {
			databaseManager.beginTransaction();
			costumer = databaseManager.getCostumerById(costumerId);
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			databaseManager.endTransaction();
		}

		return costumer;
	}

	public List<Costumer> getAllCostumers(DatabaseManager databaseManager) {
		List<Costumer> costumers = null;

		try {
			databaseManager.beginTransaction();
			costumers = databaseManager.getAllCostumers();
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			databaseManager.endTransaction();
		}

		return costumers;
	}

	public PeerGroup getPeerGroup(int costumerId, DatabaseManager databaseManager) {
		PeerGroup peerGroup = null;

		try {
			databaseManager.beginTransaction();
			peerGroup = databaseManager.getPeerGroupByCostumerId(costumerId);
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			databaseManager.endTransaction();
		}

		return peerGroup;
	}
}