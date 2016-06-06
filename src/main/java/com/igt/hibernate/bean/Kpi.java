package com.igt.hibernate.bean;

public class Kpi {

	private int id;
	private String methodName;
	private String stepName;
	private double kpiFallout;
	private double kpiPrecision;
	private double kpiRecall;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public double getKpiFallout() {
		return kpiFallout;
	}

	public void setKpiFallout(double kpiFallout) {
		this.kpiFallout = kpiFallout;
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
