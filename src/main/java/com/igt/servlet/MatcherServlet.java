package com.igt.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	
	@POST
	@Path("bpmn/{bpmnPath}")
	@Produces("application/json")
	public RestResponse analyseBpmn(@PathParam("bpmnPath") String bpmnPath) {
		
		MatcherManager manager = new MatcherManager();
		
		String baseUrl = uriInfo.getBaseUri().toString().split("api")[0];
		bpmnPath = baseUrl + "approve_discount_request.xml";
		
		return manager.analyseBpmn(bpmnPath, databaseManager);
	}
	
	@POST
	@Path("wadl/{wadlPath}")
	@Produces("application/json")
	public RestResponse analyseWadl(@PathParam("wadlPath") String wadlPath) {
		
		MatcherManager manager = new MatcherManager();
		
		String baseUrl = uriInfo.getBaseUri().toString().split("api")[0];
		wadlPath = baseUrl + "application.wadl";
		
		return manager.analyseWadl(wadlPath, databaseManager);
	}
	@GET
	@Path("match/{bpmnID}/{servletID}")
	@Produces("application/json")
	public RestResponse match(@PathParam("servletID") int servletID,
			@PathParam("bpmnID") int bpmnID) {
		
		MatcherManager manager = new MatcherManager();
		
		return manager.match(bpmnID, servletID, databaseManager);
	}

}
