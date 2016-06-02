package com.igt.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

public class Method {

	private int id;
	private String name;
	private HttpVerb type;
	private Set<Param> params = new HashSet<Param>();
	private Webservice webservice;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HttpVerb getType() {
		return type;
	}

	public void setType(HttpVerb type) {
		this.type = type;
	}

	public Set<Param> getParams() {
		return params;
	}

	public void setParams(Set<Param> params) {
		this.params = params;
	}

	public Webservice getWebservice() {
		return webservice;
	}

	public void setWebservice(Webservice webservice) {
		this.webservice = webservice;
	}

}
