package com.igt.validator.helper;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class PPErrorHandler implements ErrorHandler{
	
	public void error(SAXParseException e) throws SAXException
	{
		throw e;
	}

	public void fatalError(SAXParseException e) throws SAXException
	{
		throw e;
	}

	public void warning(SAXParseException e) throws SAXException
	{
		throw e;
	}
}
