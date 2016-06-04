package com.igt.matcher.helper;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.HttpVerb;
import com.igt.hibernate.bean.Method;
import com.igt.hibernate.bean.Param;
import com.igt.hibernate.bean.Resource;
import com.igt.hibernate.bean.Servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WadlHandler {

	static final Logger log = (Logger) LogManager.getLogger(WadlHandler.class);

	// element strings
	private String resourcesString = "resources";
	private String resourceString = "resource";
	private String paramString = "param";
	private String typeString = "type";
	private String methodString = "method";
	// attribute strings
	private String baseString = "base";
	private String pathString = "path";
	private String nameString = "name";
	private String idString = "id";
	
	String baseUrl = null;

	public void handleWadl(String path, DatabaseManager databaseManager) {

		this.iterateThroughWadl(path, databaseManager);
	}

	private void iterateThroughWadl(String path, DatabaseManager databaseManager) {

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new URL(path).openStream());

			saveNode(document.getDocumentElement());

		} catch (ParserConfigurationException e) {
			log.fatal("Could not parse Document", e);
		} catch (MalformedURLException e) {
			log.fatal("Could not read url" + path, e);
		} catch (SAXException e) {
			log.fatal(e);
		} catch (IOException e) {
			log.fatal("Could not read Document", e);
		} catch (RuntimeException e) {
			log.fatal("Could not connect to Database", e);
		}
	}

	private void saveNode(Node node) {
		if (node.getNodeName() == resourcesString) {
			this.getBaseUrl(node);
		} else if (node.getNodeName() == resourceString && node.getParentNode().getNodeName() == resourcesString) {
			this.createServlet(node);
		} else if (node.getNodeName() == resourceString && node.getParentNode().getNodeName() == resourceString) {
			this.createResource(node);
		} else if (node.getNodeName() == paramString) {
			this.createParam(node);
		} else if (node.getNodeName() == methodString) {
			this.createMethod(node);
		}

		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node currentNode = nodeList.item(i);
			if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
				// calls this method for all the children which is Element
				saveNode(currentNode);
			}
		}
	}

	private void createMethod(Node node) {
		Element methodElement = (Element) node;
		String methodName = methodElement.getAttribute(idString);
		System.out.println("Method Name: " + methodName);
		String methodType = methodElement.getAttribute(nameString);
		System.out.println("Method Type Type: " + methodType);
		
		DatabaseManager dbManager = new DatabaseManager();
		
		try {
			dbManager.beginTransaction();
			//get parent path
			String parentPath = this.getParentValueByAttribute(node, pathString);
			//get parent from database
			Resource resource = dbManager.getResourceByPath(parentPath);
			//check if method isnt available yet
			Set<Method> methods = resource.getMethods();
			boolean success = true;
			for(Method method : methods){
				if (method.getName() == methodName){
					success = false;
				}
			}
			if(success){
				Method method = new Method();
				method.setName(methodName);
				method.setType(HttpVerb.valueOf(methodType));
				method.setResource(resource);
				dbManager.saveEntity(method);
			}
			dbManager.commitTransaction();
			
			
			
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		}
	}

	private void createParam(Node node) {
		Element paramElement = (Element) node;
		String paramName = paramElement.getAttribute(nameString);
		System.out.println("Param Name: " + paramName);
		String paramType = paramElement.getAttribute(typeString).split(":")[1];
		System.out.println("Param Type: " + paramType);
		
		
		DatabaseManager dbManager = new DatabaseManager();
		
		try {
			dbManager.beginTransaction();
			//get parent path
			String parentPath = this.getParentValueByAttribute(node, pathString);
			//get parent from database
			Resource resource = dbManager.getResourceByPath(parentPath);
			//check if param isnt available yet
			Set<Param> params = resource.getParams();
			boolean success = true;
			for(Param param : params){
				if (param.getName() == paramName){
					success = false;
				}
			}
			if(success){
				Param param = new Param();
				param.setName(paramName);
				param.setType(paramType);
				param.setResource(resource);
				dbManager.saveEntity(param);
			}
			dbManager.commitTransaction();
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		}
	}

	private void createServlet(Node node) {
		Element resourceElement = (Element) node;
		String resourcePath = resourceElement.getAttribute(pathString);
		System.out.println("Servlet Class Path: " + resourcePath);

		// get Resource by parent
		DatabaseManager dbManager = new DatabaseManager();

		try {
			dbManager.beginTransaction();
			Servlet servlet = dbManager.getServletByPath(resourcePath);
			if (servlet == null) {
				Servlet newServlet = new Servlet();
				newServlet.setBaseUrl(baseUrl);
				newServlet.setPath(resourcePath);
				dbManager.saveEntity(newServlet);
			}
			dbManager.commitTransaction();
		} catch (Exception e){
			log.fatal("Could not connect to Database", e);
		} finally{
			dbManager.endTransaction();
		}

	}

	private void getBaseUrl(Node node) {
		Element webserviceElement = (Element) node;
		String baseUrl = webserviceElement.getAttribute(baseString);
		System.out.println("Webservice Path: " + baseUrl);
		
		this.baseUrl = baseUrl;
	}

	private void createResource(Node node) {
		Element resourceElement = (Element) node;
		String resourcePath = resourceElement.getAttribute(pathString);
		System.out.println("Resource Path: " + resourcePath);

		DatabaseManager dbManager = new DatabaseManager();

		try {
			dbManager.beginTransaction();
			Resource resource = dbManager.getResourceByPath(resourcePath);
			// if its already exists dont save it to database/do nothing
			if (resource == null) {
				resource = new Resource();
				resource.setPath(resourcePath);
				// find parent servlet by path
				String parentPath = this.getParentValueByAttribute(node, pathString);
				Servlet servlet = dbManager.getServletByPath(parentPath);
				// add parent servlet to resource
				resource.setServlet(servlet);
				// save in database
				dbManager.saveEntity(resource);
				dbManager.commitTransaction();
			}

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			dbManager.endTransaction();
		}
	}

	private String getParentValueByAttribute(Node child, String attribute) {
		String value = null;
		Element parentElement = (Element) child.getParentNode();
		value = parentElement.getAttribute(attribute);
		return value;
	}
}
