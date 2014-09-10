package com.nextcontrols.bureaudomain;

import java.io.Serializable;

public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String companyName = "";
	private String companyRef = "";
	private double markup = 3.3;
	private double hwdiscount = 0;
	private double monitoringdiscount = 0;
	private Country country = new Country();
	private boolean multiyear_options;

	public Company() {

	}

	public Company(int pid, String pCompanyName, double pmarkup,
			double phwdiscount, double pmonitoringdiscount, Country pcounty,
			String pCompanyRef,boolean multiyear_options) {
		this.id = pid;
		this.companyName = pCompanyName;
		this.companyRef = pCompanyRef;
		this.markup = pmarkup;
		this.hwdiscount = phwdiscount;
		this.monitoringdiscount = pmonitoringdiscount;
		this.country = pcounty;
		this.multiyear_options=multiyear_options;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyRef() {
		return companyRef;
	}

	public void setCompanyRef(String companyRef) {
		this.companyRef = companyRef;
	}

	public double getMarkup() {
		return markup;
	}

	public void setMarkup(double markup) {
		this.markup = markup;
	}

	public double getHwdiscount() {
		return hwdiscount;
	}

	public void setHwdiscount(double hwdiscount) {
		this.hwdiscount = hwdiscount;
	}

	public double getMonitoringdiscount() {
		return monitoringdiscount;
	}

	public void setMonitoringdiscount(double monitoringdiscount) {
		this.monitoringdiscount = monitoringdiscount;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public boolean isMultiyear_options() {
		return multiyear_options;
	}

	public void setMultiyear_options(boolean multiyear_options) {
		this.multiyear_options = multiyear_options;
	}

	@Override
	public String toString() {
		return this.companyName;
	}
}
