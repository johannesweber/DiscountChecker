package com.igt.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.igt.helper.RestResponse;
import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Kpi;
import com.igt.matcher.helper.BpmnHandler;
import com.igt.matcher.helper.KpiHandler;
import com.igt.matcher.helper.WadlHandler;

public class MatcherManager {
	
	public RestResponse match(int bpmnID, int wadlID, DatabaseManager databaseManager){
		
		KpiHandler kpi = new KpiHandler();
		List<Kpi> kpis = kpi.calculateKPI(bpmnID, wadlID, databaseManager);
		
		return new RestResponse(true, null, kpis);
	}
	
	public RestResponse analyseBpmn(String bpmnPath, DatabaseManager databaseManager){
		BpmnHandler bpmnHandler = new BpmnHandler();
		int bpmnID = bpmnHandler.handleBpmn(bpmnPath, databaseManager);
		
		Map<String, Integer> ids = new HashMap<String, Integer>();
		ids.put("bpmnID", bpmnID);
		
		return new RestResponse(true, null, ids);
		
	}
	
	public RestResponse analyseWadl(String wadlPath, DatabaseManager databaseManager){
		WadlHandler wadlHandler = new WadlHandler();
		Map<String, Integer> servletIDs = wadlHandler.handleWadl(wadlPath, databaseManager);
		
		Map<String, Integer> ids = new HashMap<String, Integer>();
		ids.putAll(servletIDs);
		
		return new RestResponse(true, null, ids);
		
	}

}
