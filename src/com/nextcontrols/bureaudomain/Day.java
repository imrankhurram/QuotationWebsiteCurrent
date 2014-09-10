package com.nextcontrols.bureaudomain;



public class Day {
	
	private int id;
	private String name;
	private int costperday;
	private String country;
	private String currency;
	public Day() {

	}
	
	public Day(int pid, String pname, int pcostperday, String pcountry, String pcurrency) {
		super();
		this.id = pid;
		this.name = pname;
		this.costperday = pcostperday;
		this.country = pcountry;
		this.currency = pcurrency;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCostperday() {
		return costperday;
	}

	public void setCostperday(int costperday) {
		this.costperday = costperday;
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
