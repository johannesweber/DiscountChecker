package com.igt.hibernate.bean;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Costumer implements java.io.Serializable{

	private static final long serialVersionUID = 3119327224560233409L;
	
	private int costumerId;
	private String lastname;
	private String firstname;
	private Date birthday;
	private Set<IgtOrder> igtOrders = new HashSet<IgtOrder>();
	private Set<PeerGroup> peerGroups = new HashSet<PeerGroup>();
	
	public Costumer() {
		
	}
	
	public int getCostumerId() {
		return costumerId;
	}
	public void setCostumerId(int costumerId) {
		this.costumerId = costumerId;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public Set<IgtOrder> getIgtOrders() {
		return igtOrders;
	}
	public void setIgtOrders(Set<IgtOrder> igtOrders) {
		this.igtOrders = igtOrders;
	}
	public void addIgtOrder(IgtOrder igtOrder){
		this.igtOrders.add(igtOrder);
	}
	public Set<PeerGroup> getPeerGroups() {
		return peerGroups;
	}
	public void setPeerGroups(Set<PeerGroup> peerGroups) {
		this.peerGroups = peerGroups;
	}
	public void addPeerGroup(PeerGroup peerGroup){
		this.peerGroups.add(peerGroup);
	}	
}
