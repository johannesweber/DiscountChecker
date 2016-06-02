package com.igt.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

public class Process {
	
	private int id;
	private String name;
	private Set<Step> steps = new HashSet<Step>();
	
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
	public Set<Step> getSteps() {
		return steps;
	}
	public void setSteps(Set<Step> steps) {
		this.steps = steps;
	}	
}
