package com.igt.matcher.helper;

import java.io.IOException;
import org.w3c.dom.Element;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Step;
import com.igt.hibernate.bean.Process;

public class BpmnHandler {

	static final Logger log = (Logger) LogManager.getLogger(BpmnHandler.class);

	public void handleBpmn(String path, DatabaseManager databaseManager) {

		this.iterateThroughBpmn(path, databaseManager);
	}

	private void iterateThroughBpmn(String path, DatabaseManager databaseManager) {

		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setNamespaceAware(true);
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new URL(path).openStream());

			Process process = this.createProcess(document, databaseManager);

			NodeList tasks = this.getTasks(document);
			this.saveTasksToDatabase(process, tasks, databaseManager);

		} catch (ParserConfigurationException e) {
			log.fatal("Could not parse Document", e);
		} catch (MalformedURLException e) {
			log.fatal("Could not read url" + path, e);
		} catch (SAXException e) {
			log.fatal(e);
		} catch (IOException e) {
			log.fatal("Could not read Document", e);
		}
	}

	private Process createProcess(Document document, DatabaseManager databaseManager) {
		Process process = null;

		try {
			databaseManager.beginTransaction();
			// save bpmn to database
			String bpmnName = this.getBpmnName(document);
			// check if bmpn already exists
			process = databaseManager.getProcessByName(bpmnName);
			if (process == null) {
				process = new Process();
				process.setName(bpmnName);
				databaseManager.saveEntity(process);
				databaseManager.commitTransaction();
			}

		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally {
			databaseManager.endTransaction();
		}
		return process;

	}

	private void saveTasksToDatabase(Process process, NodeList tasks, DatabaseManager databaseManager) {

		try {
			databaseManager.beginTransaction();

			for (int i = 0; i < tasks.getLength(); i++) {
				Node node = tasks.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element taskElement = (Element) node;
					String taskName = taskElement.getAttribute("name");
					//check if step already exists
					Step step = databaseManager.getStepByNameAndProcess(process, taskName);
					if (step == null){
						step = new Step();
						step.setName(taskName);
						step.setProcess(process);
						databaseManager.saveEntity(step);
					}
				}
			}
			databaseManager.commitTransaction();
		} catch (Exception e) {
			log.fatal("Could not connect to Database", e);
		} finally{
			databaseManager.endTransaction();
		}
	}

	private NodeList getTasks(Document document) {
		String searchString = "/*[name()='bpmn:definitions']/*[name()='bpmn:process']/*[name()='bpmn:serviceTask']";
		NodeList tasks = null;
		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			tasks = (NodeList) xPath.compile(searchString).evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			log.fatal("Nodes with XPath " + searchString + " not found", e);
		}

		return tasks;
	}

	private String getBpmnName(Document document) {
		String searchString = "/*[name()='bpmn:definitions']/*[name()='bpmn:process']/@name";
		String name = null;
		XPath xPath = XPathFactory.newInstance().newXPath();

		try {
			name = (String) xPath.compile(searchString).evaluate(document);
		} catch (XPathExpressionException e) {
			log.fatal("Nodes with XPath " + searchString + " not found", e);
		}

		return name;
	}
}
