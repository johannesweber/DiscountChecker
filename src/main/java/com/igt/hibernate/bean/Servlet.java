package com.igt.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

public class Servlet {

	private int id;
	private String baseUrl;
	private String path;
	private Set<Resource> resources = new HashSet<Resource>();
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Set<Resource> getResources() {
		return resources;
	}

	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}

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
	
	public void addResource(Resource resource) {
		this.resources.add(resource);
	}
}
