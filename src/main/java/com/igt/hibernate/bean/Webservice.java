package com.igt.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

public class Webservice {

	private int id;
	private String baseUrl;
	private String pathUrl;
	private Set<Method> methods = new HashSet<Method>();

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

	public Set<Method> getMethods() {
		return methods;
	}

	public void setMethods(Set<Method> methods) {
		this.methods = methods;
	}
}
