package com.igt.manager;

import com.igt.helper.RestResponse;
import com.igt.hibernate.DatabaseManager;
import com.igt.matcher.helper.BpmnHandler;
import com.igt.matcher.helper.KpiHandler;
import com.igt.matcher.helper.WadlHandler;

public class MatcherManager {
	
	//TODO zwei servlets erstellen (eines um in die datenbank zu schreiben und eins um zu vergleichen)
	public RestResponse match(String bpmnPath, String wadlPath, DatabaseManager databaseManager){
		
		BpmnHandler bpmnHandler = new BpmnHandler();
		WadlHandler wadlHandler = new WadlHandler();
		
		wadlHandler.handleWadl(wadlPath, databaseManager);
		bpmnHandler.handleBpmn(bpmnPath, databaseManager);
		
		KpiHandler kpi = new KpiHandler();
		kpi.calculateKPI(bpmnPath, wadlPath, databaseManager);
		
		return null;
	}
}
