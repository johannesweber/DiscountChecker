package com.igt.hibernate.bean;

public class IgtOrder {
	
	private int igtOrderId;
	private double value;
	private Costumer costumer;
	
	public IgtOrder(){
		
	}
	
	public int getIgtOrderId() {
		return igtOrderId;
	}
	public void setIgtOrderId(int igtOrderId) {
		this.igtOrderId = igtOrderId;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Costumer getCostumer() {
		return costumer;
	}
	public void setCostumer(Costumer costumer) {
		this.costumer = costumer;
	}
}
