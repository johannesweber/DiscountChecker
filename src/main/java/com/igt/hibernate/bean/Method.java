package com.igt.hibernate.bean;

public class Method {

	private int id;
	private String name;
	private HttpVerb type;
	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

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
}
