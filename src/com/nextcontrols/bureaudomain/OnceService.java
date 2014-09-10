package com.nextcontrols.bureaudomain;

import java.io.Serializable;



public class OnceService implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String serviceDescription;
	private String serviceType;
	private double minimumCharge;
	private double unitCost;
	private String country;
	private String currency;
	private boolean showCost;
	
	public OnceService() {}
	public OnceService(int pID, String pServiceDescription, double pminimumCharge,
			 double punitCost, String pcountry, String pcurrency,String pServiceType) {
		this.id = pID;
		this.serviceDescription = pServiceDescription;
		this.minimumCharge = pminimumCharge;
		this.unitCost = punitCost;
		this.country = pcountry;
		this.currency = pcurrency;
		this.serviceType = pServiceType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}

	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	public double getMinimumCharge() {
		return minimumCharge;
	}

	public void setMinimumCharge(double minimumCharge) {
		this.minimumCharge = minimumCharge;
	}

	public double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	public boolean isShowCost() {
		return showCost;
	}
	public void setShowCost(boolean showCost) {
		this.showCost = showCost;
	}
	@Override
	public String toString() {
	    return this.serviceDescription;
	}
	
}
