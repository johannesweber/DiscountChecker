package com.igt.manager;

import com.igt.helper.RestResponse;
import com.igt.hibernate.DatabaseManager;
import com.igt.matcher.helper.BpmnHandler;
import com.igt.matcher.helper.WadlHandler;

public class MatcherManager {
	
	public RestResponse match(String bpmnPath, String wadlPath, DatabaseManager databaseManager){
		
		//BpmnHandler bpmnHandler = new BpmnHandler();
		WadlHandler wadlHandler = new WadlHandler();
		
		wadlHandler.handleWadl(wadlPath, databaseManager);
		//bpmnHandler.handleBpmn(bpmnPath, databaseManager);
		
		return null;
	}
}
