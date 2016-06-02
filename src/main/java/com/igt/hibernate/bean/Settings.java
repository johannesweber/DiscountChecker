package com.igt.hibernate.bean;

public class Settings {
	
	private int id;
	private String baseUrl;
	private String pathUrl;
	private String wsdlPathUrl;
	private String bpmnPathUrl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBaseUrl() {
		return baseUrl;
	}
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public String getPathUrl() {
		return pathUrl;
	}
	public void setPathUrl(String pathUrl) {
		this.pathUrl = pathUrl;
	}
	public String getWsdlPathUrl() {
		return wsdlPathUrl;
	}
	public void setWsdlPathUrl(String pathWsdl) {
		this.wsdlPathUrl = pathWsdl;
	}
	public String getBpmnPathUrl() {
		return bpmnPathUrl;
	}
	public void setBpmnPathUrl(String pathBpmn) {
		this.bpmnPathUrl = pathBpmn;
	}
}
