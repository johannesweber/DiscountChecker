package com.igt.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.igt.hibernate.DatabaseManager;
import com.igt.manager.MatcherManager;
import com.igt.matcher.helper.RestResponse;

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
		
		wadlPath = uriInfo.getBaseUri().toString().split("api")[0] + "application.wadl";
		
		return manager.match(bpmnPath, wadlPath, this.databaseManager);
	}

}
