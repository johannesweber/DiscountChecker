package com.igt.discount.helper;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Process;
import com.igt.hibernate.bean.Step;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BpmnHandler extends DefaultHandler {

	static final Logger log = (Logger) LogManager.getLogger(BpmnHandler.class);

	private String participant = "bpmn:participant";
	private String serviceTask = "bpmn:servicTask";
	private String attribute = "name";
	private DatabaseManager dbm = new DatabaseManager();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		
		System.out.println("Start Element :" + qName);
		
		try {
			dbm.beginTransaction();

			if (qName.equalsIgnoreCase(participant)) {
				int length = attributes.getLength();
				for (int i = 0; i < length; i++) {
					String attributeNameFromParticipant = attributes.getQName(i);
					if (attributeNameFromParticipant.equals(attribute)) {
						String pValue = attributes.getValue(i);
						Process process = new Process();
						process.setName(pValue);
						System.out.println("Created new process with name :" + pValue);
						
						//save set in temporary process object
						if (qName.equalsIgnoreCase(serviceTask)) {
							int setLength = attributes.getLength();
							for (int j = 0; j < setLength; j++) {
								String attributeNameFromSet = attributes.getQName(j);
								if (attributeNameFromSet.equals(attribute)) {
									String sValue = attributes.getValue(j);
									Step tempStep = new Step();
									tempStep.setName(sValue);
									tempStep.setProcess(process);
									process.getSteps().add(tempStep);
									System.out.println("Created new step with name :" + sValue + " and added to " + pValue);
								}
							}
						}

					}
				}
				dbm.commitTransaction();
			}

		} catch (Exception e) {
			log.fatal(e);
		} finally {
			dbm.endTransaction();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("End Element :" + qName);
	}

}
