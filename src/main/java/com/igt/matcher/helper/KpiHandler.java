package com.igt.matcher.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	
	public List<Kpi> calculateKPI(int bpmnID, int wadlID, DatabaseManager databaseManager) {

		List<Kpi> kpis = new ArrayList<Kpi>();
		
		List<Resource> resources = this.getResourcesFromDatabase(wadlID, databaseManager);
		List<Step> steps = this.getStepsFromDatabase(bpmnID, databaseManager);
		
		Map<String, ArrayList<String>> compareMap = this.createCompareMap(resources, databaseManager);
		
		for(Step step : steps){
			Kpi kpi = this.compare(step, compareMap);
			kpis.add(kpi);
		}
		
		return kpis;
	}

	private List<Resource> getResourcesFromDatabase(int wadlID, DatabaseManager dbManager) {
		List<Resource> resources = null;

		try {
			dbManager.beginTransaction();
			Servlet servlet = dbManager.getServletById(wadlID);
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
			
			for (Resource resource : resources){
				Set <Method> methods = resource.getMethods();
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

	private Kpi compare(Step step, Map<String, ArrayList<String>> resourceCompareMap) {
		String stepName = step.getName();
		ArrayList<String> stepKeywords = this.splitStringBySpace(stepName);

		ArrayList<ArrayList<String>> foundList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> relevantList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> foundRelevantList = new ArrayList<ArrayList<String>>();

		// kpi's for recall, precision and f-measure
		int found;
		int relevant;
		int foundRelevent;

		// find relevant methods
		// iterate keywords from step
		for (int i = 0; i < stepKeywords.size(); i++) {
			// iterate wadl map
			for (Entry<String, ArrayList<String>> entry : resourceCompareMap.entrySet()) {
				ArrayList<String> wadlKeywords = entry.getValue();
				// iterate wadl values
				if (wadlKeywords.contains(stepKeywords.get(i))) {
					if (!relevantList.contains(wadlKeywords)) {
						relevantList.add(wadlKeywords);
					}
				}
			}
		}

		// find found methods
		for (ArrayList<String> entry : relevantList) {
			if (stepKeywords.size() == entry.size()) {
				boolean success = true;
				for (int position = 0; position < entry.size(); position++) {
					if (entry.get(position).equals(stepKeywords.get(position))) {
						success = success && true;
					} else {
						success = success && false;
					}
				}
				if (success) {
					foundList.add(entry);
				}
			}
		}

		// set kpi's
		found = foundList.size();
		relevant = relevantList.size();
		
		foundRelevantList.addAll(relevantList);
		foundRelevantList.retainAll(foundList);
		foundRelevent = foundRelevantList.size();
		
		double recall = foundRelevent/(double)relevant;
		double precision = foundRelevent/(double)found;
		double fMeasure = 2*(precision*recall)/(double)(precision+recall);
		
		String methodName = (String) resourceCompareMap.keySet().toArray()[0];
		
		return this.createKpi(step, methodName, round(recall, 3), round(precision, 3), round(fMeasure, 3));
	}
	
	private Kpi createKpi(Step step, String methodName, double recall, double precision, double fMeasure){
		
		Kpi kpi = null;
		
		DatabaseManager dbManager = new DatabaseManager();
		
		try {
			dbManager.beginTransaction();
			
			Method method = dbManager.getMethodByName(methodName);
			Resource resource = method.getResource();
			
			//create kpis and save to database
			kpi = new Kpi();
			
			kpi.setKpiFMeasure(fMeasure);
			kpi.setKpiPrecision(precision);
			kpi.setKpiRecall(recall);
			kpi.setResource(resource);
			kpi.setStep(step);
			
			dbManager.saveEntity(kpi);
			dbManager.commitTransaction();
			
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally{
			dbManager.endTransaction();
		}
		
		return kpi;
	}
	
	private double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
}
