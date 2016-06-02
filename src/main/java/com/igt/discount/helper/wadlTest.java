package com.igt.discount.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.igt.hibernate.bean.Webservice;

public class wadlTest {
	public static void main(String[] args) {
		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				// element strings
				private String recources = "resources";
				private String recource = "recource";
				private String param = "param";
				private String method = "method";
				// attribute strings
				private String base = "base";
				private String path = "path";
				private String name = "name";
				private String id = "id";

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {

					System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase(recources)) {
						
						String value = searchInAttributes(attributes, base);
						if (!value.equals(null)) {
							System.out.println(value);
						}
						if (qName.equalsIgnoreCase(recource)) {
							value = searchInAttributes(attributes, path);
							if (!value.equals(null)) {
								System.out.println(value);
							}
							if (qName.equalsIgnoreCase(recource)) {
								value = searchInAttributes(attributes, path);
								if (!value.equals(null)) {
									System.out.println(value);
								}
								if (qName.equalsIgnoreCase(param)) {
									value = searchInAttributes(attributes, name);
									if (!value.equals(null)) {
										System.out.println(value);
									}
								}
								if (qName.equalsIgnoreCase(method)) {
									value = searchInAttributes(attributes, id);
									if (!value.equals(null)) {
										System.out.println(value);
									}
								}
							}
						}
					}

				}

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

			};

			File file = new File("c:\\Users\\Marco\\Desktop\\application.wadl");
			InputStream inputStream = new FileInputStream(file);
			Reader reader = new InputStreamReader(inputStream, "UTF-8");

			InputSource is = new InputSource(reader);
			is.setEncoding("UTF-8");

			saxParser.parse(is, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
