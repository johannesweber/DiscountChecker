package com.igt.hibernate.bean;

public class Kpi {

	private int id;
	private String resourceName;
	private String stepName;
	private int found;
	private int relevant;
	private int intersection_found_relevant;
	private double kpiFMeasure;
	private double kpiPrecision;
	private double kpiRecall;
	
	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public int getFound() {
		return found;
	}

	public void setFound(int found) {
		this.found = found;
	}

	public int getRelevant() {
		return relevant;
	}

	public void setRelevant(int relevant) {
		this.relevant = relevant;
	}

	public int getIntersection_found_relevant() {
		return intersection_found_relevant;
	}

	public void setIntersection_found_relevant(int intersection_found_relevant) {
		this.intersection_found_relevant = intersection_found_relevant;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getKpiFMeasure() {
		return kpiFMeasure;
	}

	public void setKpiFMeasure(double kpiFMeasure) {
		this.kpiFMeasure = kpiFMeasure;
	}

	public double getKpiPrecision() {
		return kpiPrecision;
	}

	public void setKpiPrecision(double kpiPrecision) {
		this.kpiPrecision = kpiPrecision;
	}

	public double getKpiRecall() {
		return kpiRecall;
	}

	public void setKpiRecall(double kpiRecall) {
		this.kpiRecall = kpiRecall;
	}
}
