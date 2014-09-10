package com.nextcontrols.bureaudomain;



public class AnnualRate {
	
	private int id;
	private String description;
	private int costperday;
	private int annualcalibration;
	private int annualdeadtraveldaycost;
	private double threeyearfactor;
	private double oneyearfactor;
	private String country;
	private String currency;
	
	
	public AnnualRate() {

	}
	
	public AnnualRate(int pid, String pdescription, int pcostperday, int pannualcalibration, int pannualdeadtraveldaycost,
			double pthreeyearfactor,double poneyearfactor, String pcountry, String pcurrency) {
		super();
		this.id = pid;
		this.description = pdescription;
		this.costperday = pcostperday;
		this.annualcalibration = pannualcalibration;
		this.annualdeadtraveldaycost = pannualdeadtraveldaycost;
		this.threeyearfactor = pthreeyearfactor;
		this.oneyearfactor = poneyearfactor;
		this.country = pcountry;
		this.currency = pcurrency;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCostperday() {
		return costperday;
	}

	public void setCostperday(int costperday) {
		this.costperday = costperday;
	}

	public int getAnnualcalibration() {
		return annualcalibration;
	}

	public void setAnnualcalibration(int annualcalibration) {
		this.annualcalibration = annualcalibration;
	}

	public int getAnnualdeadtraveldaycost() {
		return annualdeadtraveldaycost;
	}

	public void setAnnualdeadtraveldaycost(int annualdeadtraveldaycost) {
		this.annualdeadtraveldaycost = annualdeadtraveldaycost;
	}

	public double getThreeyearfactor() {
		return threeyearfactor;
	}

	public void setThreeyearfactor(double threeyearfactor) {
		this.threeyearfactor = threeyearfactor;
	}

	public double getOneyearfactor() {
		return oneyearfactor;
	}

	public void setOneyearfactor(double oneyearfactor) {
		this.oneyearfactor = oneyearfactor;
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
	
}
