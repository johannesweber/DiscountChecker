package com.igt.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

public class Resource {
	
	private int id;
	private String path;
	private Servlet servlet;
	private Set<Method> methods = new HashSet<Method>();
	
	public Set<Method> getMethods() {
		return methods;
	}
	public void setMethods(Set<Method> methods) {
		this.methods = methods;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public void addMethod(Method method){
		this.methods.add(method);
	}
	public Servlet getServlet() {
		return servlet;
	}
	public void setServlet(Servlet servlet) {
		this.servlet = servlet;
	}
}
