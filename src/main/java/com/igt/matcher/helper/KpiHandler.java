package com.igt.matcher.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Kpi;
import com.igt.hibernate.bean.Method;
import com.igt.hibernate.bean.Resource;
import com.igt.hibernate.bean.Servlet;
import com.igt.hibernate.bean.Step;
import com.igt.hibernate.bean.Process;

public class KpiHandler {

	protected static final Logger log = LogManager.getLogger(KpiHandler.class);

	public List<Kpi> calculateKPI(int bpmnID, int servletId, DatabaseManager databaseManager) {

		List<Kpi> kpis = new ArrayList<Kpi>();
		
		Servlet servlet = this.getServletFromDatabase(servletId, databaseManager);

		List<Resource> resources = this.getResourcesFromDatabase(servlet, databaseManager);
		List<Step> steps = this.getStepsFromDatabase(bpmnID, databaseManager);

		Map<String, ArrayList<String>> compareMap = this.createCompareMap(resources, databaseManager);

		for (Step step : steps) {
			Kpi kpi = this.compare(servlet, step, compareMap);
			kpis.add(kpi);
		}

		return kpis;
	}

	private Servlet getServletFromDatabase(int servletId, DatabaseManager dbManager) {
		Servlet servlet = null;

		try {
			dbManager.beginTransaction();
			servlet = dbManager.getServletById(servletId);

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}
		return servlet;

	}

	private List<Resource> getResourcesFromDatabase(Servlet servlet, DatabaseManager dbManager) {
		List<Resource> resources = null;

		try {
			dbManager.beginTransaction();
			resources = dbManager.getResourcesByServlet(servlet);

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}
		return resources;
	}

	private List<Step> getStepsFromDatabase(int bpmnID, DatabaseManager dbManager) {
		List<Step> steps = null;

		try {
			dbManager.beginTransaction();
			Process process = dbManager.getProcessById(bpmnID);
			steps = dbManager.getStepsByProcess(process);

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}
		return steps;
	}

	private String splitCamelCase(String s) {
		return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])",
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ").toLowerCase();
	}

	private ArrayList<String> splitStringBySpace(String string) {
		return new ArrayList<String>(Arrays.asList(string.toLowerCase().split(" ")));
	}

	private Map<String, ArrayList<String>> createCompareMap(List<Resource> resources, DatabaseManager dbManager) {
		Map<String, ArrayList<String>> compareMap = new HashMap<String, ArrayList<String>>();

		try {
			dbManager.beginTransaction();

			for (Resource resource : resources) {
				Set<Method> methods = resource.getMethods();
				for (Method method : methods) {
					String key = method.getName();
					ArrayList<String> value = this.splitStringBySpace(this.splitCamelCase(key));

					compareMap.put(key, value);
				}
			}

		} catch (Exception e) {
			log.fatal("Could not connect to Databse", e);
		} finally {
			dbManager.endTransaction();
		}

		return compareMap;
	}

	private Kpi compare(Servlet servlet, Step step, Map<String, ArrayList<String>> compareMap) {
		String stepName = step.getName();
		ArrayList<String> stepKeywords = this.splitStringBySpace(stepName);

		ArrayList<ArrayList<String>> foundList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> relevantList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> foundRelevantList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> foundNotRelevantList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> notFoundList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> notRelevantList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> totalList = new ArrayList<ArrayList<String>>();

		// values for calculating recall, precision and f-measure
		int found;
		int relevant;
		int foundRelevent;

		// find relevant methods
		// iterate keywords from step
		for (int i = 0; i < stepKeywords.size(); i++) {
			// iterate wadl map
			for (Entry<String, ArrayList<String>> entry : compareMap.entrySet()) {
				ArrayList<String> wadlKeywords = entry.getValue();
				// iterate wadl values
				if (wadlKeywords.contains(stepKeywords.get(i))) {
					if (!relevantList.contains(wadlKeywords)) {
						relevantList.add(wadlKeywords);
					}
				}
			}
		}

		// find found methods with different length but all keywords in it
		for (ArrayList<String> methodKeywords : relevantList) {
			boolean success = true;
			int keywordSize = stepKeywords.size();
			for (int stepKeywordPosition = 0; stepKeywordPosition < keywordSize; stepKeywordPosition++) {
				if (methodKeywords.size() == keywordSize) {
					if (methodKeywords.contains(stepKeywords.get(stepKeywordPosition))) {
						success = success && true;
					} else {
						success = success && false;
					}
				}
				if (methodKeywords.size() < stepKeywords.size()) {
					int methodKeywordSize = methodKeywords.size();
					int counter = 0;
					while (counter < methodKeywordSize) {
						if (methodKeywords.contains(stepKeywords.get(counter))) {
							success = success && true;
						} else {
							success = success && false;
						}
						counter++;
					}

				}
				if (stepKeywords.size() < methodKeywords.size()) {
					if (methodKeywords.contains(stepKeywords.get(stepKeywordPosition))) {
						success = success && true;
					} else {
						success = success && false;
					}
				}
			}

			if (success) {
				foundList.add(methodKeywords);
			}
		}
		// calculate notFound and notRelevantList and totalList
		totalList.addAll(compareMap.values());

		notFoundList.addAll(totalList);
		notFoundList.removeAll(foundList);

		notRelevantList.addAll(totalList);
		notRelevantList.removeAll(relevantList);

		// set values for calculating recall, precision and f measure
		found = foundList.size();
		relevant = relevantList.size();

		// create intersection of found and relevant list
		foundRelevantList.addAll(relevantList);
		foundRelevantList.retainAll(foundList);
		foundRelevent = foundRelevantList.size();

		// create intersection of found and notRelevant list
		foundNotRelevantList.addAll(foundList);
		foundNotRelevantList.retainAll(notRelevantList);

		// calculate kpis
		double recall = (double) foundRelevent / (double) relevant;
		// double recall = foundRelevent/((double)foundRelevent +
		// (double)foundNotRelevant);
		double precision = (double) foundRelevent / (double) found;
		double fMeasure = 2 * ((double) (precision * recall) / (double) (precision + recall));

		// get method name; needed for finding the suitable resource to create a
		// kpi object
		// String methodName = this.getMethodNameByKeywords(foundList.get(0));
		// Resource resource = this.getResourceByMethodName(methodName);

		ArrayList<String> pathsList = this.getFoundResourcesPath(servlet, foundList);
		
		String resourceName = "";
		
		for(String path : pathsList){
			resourceName = resourceName + " " + path;
		}
	
		// create kpis and save to database
		Kpi kpi = new Kpi();
		kpi.setResourceName(resourceName);
		kpi.setStepName(step.getName());
		kpi.setKpiFMeasure(round(fMeasure, 3));
		kpi.setKpiPrecision(round(precision, 3));
		kpi.setKpiRecall(round(recall, 3));
		kpi.setFound(found);
		kpi.setIntersection_found_relevant(foundRelevent);
		kpi.setRelevant(relevant);

		this.saveToDatabase(kpi);

		return kpi;
	}

	private ArrayList<String> getFoundResourcesPath(Servlet servlet, ArrayList<ArrayList<String>> foundList) {
		
		ArrayList<String> paths = new ArrayList<String>();
		
		ArrayList<String> methodNames = new ArrayList<String>();
		ArrayList<Resource> resourceList = new ArrayList<Resource>();
		
		for (ArrayList<String> found : foundList){
			String methodNameRest = "";
			String firstWord = found.get(0);
			for(int i = 1; i< found.size(); i++){
				methodNameRest += " " + found.get(i);	
			}
			methodNameRest = WordUtils.capitalize(methodNameRest);
			String methodName = firstWord + methodNameRest;
			methodName = methodName.replaceAll(" ", "");
			
			methodNames.add(methodName);
		}
		
		for (String methodName : methodNames){
			Resource resource = this.getResourceByMethodName(servlet, methodName);
			resourceList.add(resource);
		}
		
		for(Resource resource : resourceList){
			//Servlet servlet = resource.getServlet();
			String basePath = servlet.getBaseUrl();
			String servletPath = servlet.getPath();
			String resourcePath = resource.getPath();
			String completePath = basePath + servletPath + resourcePath;
			
			paths.add(completePath);
		}
		
		return paths;
	}

	private Resource getResourceByMethodName(Servlet servlet, String methodName) {
		Resource resource = null;
		DatabaseManager dbManager = new DatabaseManager();

		try {
			dbManager.beginTransaction();
			int resourceId = dbManager.getResourceIdByMethodNameAndServletId(methodName, servlet.getId());
			resource = dbManager.getResourceById(resourceId);
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}

		return resource;
	}

	private Kpi saveToDatabase(Kpi kpi) {

		DatabaseManager dbManager = new DatabaseManager();

		try {
			dbManager.beginTransaction();
			dbManager.saveEntity(kpi);
			dbManager.commitTransaction();

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}

		return kpi;
	}

	private double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}
}
