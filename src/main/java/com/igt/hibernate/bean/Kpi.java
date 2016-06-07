package com.igt.hibernate.bean;

public class Kpi {

	private int id;
	private Resource resource;
	private Step step;
	private double kpiFMeasure;
	private double kpiPrecision;
	private double kpiRecall;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
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
