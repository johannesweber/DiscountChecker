package com.igt.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.igt.helper.RestResponse;
import com.igt.hibernate.DatabaseManager;
import com.igt.manager.MatcherManager;

@Path("matcher/")
public class MatcherServlet {
	
	static final Logger log = (Logger) LogManager.getLogger(MatcherServlet.class);
	DatabaseManager databaseManager = new DatabaseManager();
	
	@Context
    UriInfo uriInfo;
	
	@GET
	@Path("match/{bpmnPath}/{wadlPath}")
	public RestResponse getCostumer(@PathParam("bpmnPath") String bpmnPath,
			@PathParam("wadlPath") String wadlPath) {
		
		MatcherManager manager = new MatcherManager();
		
		String baseUrl = uriInfo.getBaseUri().toString().split("api")[0];
		wadlPath = baseUrl + "application.wadl";
		bpmnPath = baseUrl + "approve_discount_request.xml";
		
		return manager.match(bpmnPath, wadlPath, this.databaseManager);
	}

}
