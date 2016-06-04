package com.igt.servlet;

import java.nio.file.Paths;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import com.igt.manager.ValidatorManager;
import com.igt.matcher.helper.RestResponse;

@Path("validator/")
public class ValidatorServlet {
	
	static final Logger log = (Logger) LogManager.getLogger(ValidatorServlet.class);
	
	@GET
	@Path("wsdl/{path}")
	public RestResponse getCostumer(@PathParam("path") String path) {
		
		ValidatorManager validatorManager = new ValidatorManager();
		
		String tempPath = Paths.get("/wsdl/correct.wsdl").toString();
		
		return validatorManager.checkWSDL(tempPath);
	}

}