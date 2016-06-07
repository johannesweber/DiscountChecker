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
	public RestResponse createBpmn(@PathParam("bpmnPath") String bpmnPath) {
		
		MatcherManager manager = new MatcherManager();
		
		String baseUrl = uriInfo.getBaseUri().toString().split("api")[0];
		bpmnPath = baseUrl + "approve_discount_request.xml";
		
		return manager.createBpmn(bpmnPath, databaseManager);
	}
	
	@POST
	@Path("wadl/{wadlPath}")
	@Produces("application/json")
	public RestResponse createWadl(@PathParam("wadlPath") String wadlPath) {
		
		MatcherManager manager = new MatcherManager();
		
		String baseUrl = uriInfo.getBaseUri().toString().split("api")[0];
		wadlPath = baseUrl + "application.wadl";
		
		return manager.createWadl(wadlPath, databaseManager);
	}
	@GET
	@Path("match/{bpmnID}/{wadlID}")
	@Produces("application/json")
	public RestResponse getCostumer(@PathParam("wadlID") int wadlID,
			@PathParam("bpmnID") int bpmnID) {
		
		MatcherManager manager = new MatcherManager();
		
		return manager.match(bpmnID, wadlID, databaseManager);
	}

}
