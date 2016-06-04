package com.igt.manager;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.igt.matcher.helper.RestResponse;
import com.igt.validator.helper.PPErrorHandler;

public class ValidatorManager {

	static final Logger log = (Logger) LogManager.getLogger(ValidatorManager.class);
	public static Document document;
	
	public RestResponse checkWSDL(String path){
		boolean success = false;
		String message = null;
		
		// create a factory that understands namespaces and validates the XML input
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);

		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			
			builder.setErrorHandler(new PPErrorHandler());
			document = builder.parse(new InputSource(path));
			message = "XML file is well formed.";
			success = true;
		} catch (ParserConfigurationException e) {
			log.fatal("Could not parse input source", e);
		} catch (SAXException e) {
			log.fatal(e);
		} catch (IOException e) {
			log.fatal("Could not read " + path, e);
		}

		return new RestResponse(success, message, null);
	}
}
