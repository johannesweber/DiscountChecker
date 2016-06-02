package com.igt.hibernate.bean;

import java.util.HashSet;
import java.util.Set;

public class PeerGroup implements java.io.Serializable {
	
	private static final long serialVersionUID = 3057323842513692467L;
	
	private int peerGroupId;
	private int ownerId;
	private Set<Costumer> costumers = new HashSet<Costumer>();
	
	public PeerGroup() {
		
	}
	
	public int getPeerGroupId() {
		return peerGroupId;
	}
	public void setPeerGroupId(int peerGroupId) {
		this.peerGroupId = peerGroupId;
	}

	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int costumerId) {
		this.ownerId = costumerId;
	}
	public Set<Costumer> getCostumers() {
		return costumers;
	}
	public void setCostumers(Set<Costumer> costumers) {
		this.costumers = costumers;
	}
}
