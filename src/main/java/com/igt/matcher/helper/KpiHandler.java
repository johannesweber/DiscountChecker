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
	
	public Kpi calculateKPI(String bpmnPath, String wadlPath, DatabaseManager databaseManager) {

		List<Resource> resources = this.getResourcesFromDatabase(wadlPath, databaseManager);
		List<Step> steps = this.getStepsFromDatabase(bpmnPath, databaseManager);
		
		Map<String, ArrayList<String>> compareMap = this.createResourceCompareMap(resources);
		
		for(Step step : steps){
			this.compare(step, compareMap);
			//was passiert jetzt?
		}
		
		return null;
	}

	private List<Resource> getResourcesFromDatabase(String wadlPath, DatabaseManager dbManager) {
		List<Resource> resources = null;

		try {
			dbManager.beginTransaction();
			Servlet servlet = dbManager.getServletByFilepath(wadlPath);

			resources = dbManager.getResourcesByServlet(servlet);

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}
		return resources;
	}

	private List<Step> getStepsFromDatabase(String bpmnPath, DatabaseManager dbManager) {
		List<Step> steps = null;
		
		try {
			dbManager.beginTransaction();
			Process process = dbManager.getProcessByFilepath(bpmnPath);

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
				"(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
	}

	private ArrayList<String> splitStringBySpace(String string) {
		return new ArrayList<String>(Arrays.asList(string.split(" ")));
	}

	private Map<String, ArrayList<String>> createResourceCompareMap(List<Resource> resources) {
		Map<String, ArrayList<String>> wadlCompareMap = new HashMap<String, ArrayList<String>>();
		
		for (Resource resource : resources){
			Set <Method> methods = resource.getMethods();
			for (Method method : methods) {
				String key = method.getName();
				ArrayList<String> value = this.splitStringBySpace(this.splitCamelCase(key));

				wadlCompareMap.put(key, value);
			}
		}

		
		return wadlCompareMap;
	}

	private Kpi compare(Step step, Map<String, ArrayList<String>> resourceCompareMap) {
		String stepName = step.getName();
		ArrayList<String> stepKeywords = this.splitStringBySpace(stepName);

		ArrayList<ArrayList<String>> foundList = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> relevantList = new ArrayList<ArrayList<String>>();

		// kpi's for recall, precision and f-measure
		int found;
		int notFound;
		int relevant;
		int notRelevant;
		int total = resourceCompareMap.size();

		// find relevant methods
		// iterate keywords from step
		for (int i = 0; i < stepKeywords.size(); i++) {
			// iterate wadl map
			for (Entry<String, ArrayList<String>> entry : resourceCompareMap.entrySet()) {
				ArrayList<String> wadlKeywords = entry.getValue();
				// iterate wadl values
				if (wadlKeywords.contains(stepKeywords.get(i))) {
					if (!relevantList.contains(wadlKeywords)) {
						relevantList.add(stepKeywords);
					}
				}
			}
		}

		// find found methods
		for (ArrayList<String> entry : relevantList) {
			if (stepKeywords.size() == entry.size()) {
				boolean success = true;
				for (int position = 0; position < entry.size(); position++) {

					if (entry.get(position).equals(relevantList.get(position))) {
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
		notFound = total - found;
		notRelevant = total - relevant;

		// TODO calculate recall, precission and f-measure

		return null;

	}
}
