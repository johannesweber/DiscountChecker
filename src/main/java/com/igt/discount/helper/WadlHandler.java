package com.igt.discount.helper;

import com.igt.hibernate.DatabaseManager;
import com.igt.hibernate.bean.Webservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WadlHandler extends DefaultHandler {

	static final Logger log = (Logger) LogManager.getLogger(BpmnHandler.class);

	// element strings
	private String recources = "resources";
	private String recource = "recource";
	private String param = "param";
	private String method = "method";
	// attribute strings
	private String base = "base";
	private String path = "path";
	private String name = "name";

	private DatabaseManager dbm = new DatabaseManager();

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		System.out.println("Start Element :" + qName);

		try {
			dbm.beginTransaction();

			if (qName.equalsIgnoreCase(recources)) {
				Webservice webservice = new Webservice();
				String value = searchInAttributes(attributes, base);
				if (!value.equals(null)) {
					webservice.setBaseUrl(value);
				}
				if (qName.equalsIgnoreCase(recource)) {
					value = searchInAttributes(attributes, path);
					if (!value.equals(null)) {
						webservice.setPathUrl(value);
					}
					if (qName.equalsIgnoreCase(recource)) {
						value = searchInAttributes(attributes, path);
						if (!value.equals(null)) {
							webservice.setPathUrl(value);
						}
					}
				}
				

			}
			dbm.commitTransaction();

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

	private String searchInAttributes(Attributes attributes, String toSearchStr) {
		String value = null;
		for (int i = 0; i < attributes.getLength(); i++) {
			String attributeNameFromRecources = attributes.getQName(i);
			if (attributeNameFromRecources.equals(toSearchStr)) {
				value = attributes.getValue(i);
			}
		}
		return value;
	}

}
