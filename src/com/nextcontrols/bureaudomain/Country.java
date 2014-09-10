package com.nextcontrols.bureaudomain;

import java.io.Serializable;



public class Country implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String country="";
	private String fullName="";
	private String currency="£";
	private double conversionratefrompounds=0;
	private double freight=0;
	private double duty=0;
	public Country() {

	}
	
	public Country(String pcountry, String pfullName, String pcurrency,
			 double pconversionratefrompounds, double pfreight, double pduty) {
		this.country = pcountry;
		this.fullName = pfullName;
		this.currency = pcurrency;
		this.conversionratefrompounds = pconversionratefrompounds;
		this.freight = pfreight;
		this.duty = pduty;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getConversionratefrompounds() {
		return conversionratefrompounds;
	}

	public void setConversionratefrompounds(double conversionratefrompounds) {
		this.conversionratefrompounds = conversionratefrompounds;
	}
	
	public double getFreight() {
		return freight;
	}

	public void setFreight(double freight) {
		this.freight = freight;
	}

	public double getDuty() {
		return duty;
	}

	public void setDuty(double duty) {
		this.duty = duty;
	}
	@Override
	  public String toString() {
	    return this.country;
	  }
	
}
