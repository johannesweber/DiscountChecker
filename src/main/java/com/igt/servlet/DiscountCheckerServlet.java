package com.igt.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Costumer;
import com.igt.manager.DiscountManager;

@Path("discount/")
public class DiscountCheckerServlet {
	
	static final Logger log = (Logger) LogManager.getLogger(DiscountCheckerServlet.class);
	DatabaseManager databaseManager = new DatabaseManager();
	DiscountManager manager = new DiscountManager();
	
	@GET
	@Path("costumer/{costumerId}")
	public Costumer getCostumer(@PathParam("costumerId") int costumerId) {
		
		return manager.getCostumer(costumerId, databaseManager);
	}
	
	@GET
	@Path("sales/")
	public double getTotalSales() {
		
		return manager.getTotalSales(databaseManager);
	}
	
	@GET
	@Path("sales/costumer/{costumerId}")
	public double getTotalSalesFromCostumer(@PathParam("costumerId") int costumerId) {
		
		return manager.getTotalSalesFromCostumer(costumerId, databaseManager);
	}
	
	@GET
	@Path("sales/peerGroup/costumer/{costumerId}")
	public double getTotalSalesFromPeerGroup(@PathParam("costumerId") int costumerId) {
		
		return manager.getTotalSalesFromPeerGroup(costumerId, databaseManager);
	}
	
	@GET
	@Path("ratio/{total}/{part}")
	public double getRatio(@PathParam("total") double total, @PathParam("part") double part) {
		
		return manager.createRatioBetween(total, part);
	}
	
	@GET
	@Path("check/age/{peerGroupOwnerAge}/{costumerAge}")
	public boolean checkAgeDifference(@PathParam("peerGroupOwnerAge") int peerGroupOwnerAge,
			@PathParam("costumerAge") int costumerAge) {
		
		return manager.checkAge(peerGroupOwnerAge, costumerAge);
	}
	
	@GET
	@Path("age/costumer/{costumerId}")
	public int getAge(@PathParam("costumerId") int costumerId) {
		
		return manager.getAgeFromCostumer(costumerId, databaseManager);
	}
	
}
