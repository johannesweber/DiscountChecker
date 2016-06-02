package com.igt.hibernate.bean;

public class Kpi {

	private int id;
	private Process process;
	private Webservice webservice;
	private double kpiFallout;
	private double kpiPrecision;
	private double kpiRecall;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Process getProcess() {
		return process;
	}

	public void setProcess(Process process) {
		this.process = process;
	}

	public Webservice getWebservice() {
		return webservice;
	}

	public void setWebservice(Webservice webservice) {
		this.webservice = webservice;
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
