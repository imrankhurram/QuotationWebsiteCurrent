package com.nextcontrols.bureaudomain;

import java.util.ArrayList;
import java.util.List;



public class MonitoringService {
	
	private int id;
	private String description;
	private double threeyeardisc;
	private double fiveyeardisc;
	private double factor2;
	private int stepvalue1;
	private int stepvalue2;
	private int stepvalue3;
	private int stepvalue4;
	private int stepvalue5;
	private String country;
	private String currency;
	private List<MonitoringServiceCosts> monitoringCostsList;
	    		  
	public MonitoringService() {
		monitoringCostsList = new ArrayList<MonitoringServiceCosts>();

	}
	
	public MonitoringService(int pid,double pthreeyeardisc,double pfiveyeardisc,double pfactor2,String pcountry,String pcurrency,String pDescription, int pStepValue1, int pStepValue2, int pStepValue3, int pStepValue4,int pStepValue5) {
		super();
		this.id = pid;
		this.threeyeardisc = pthreeyeardisc;
		this.fiveyeardisc = pfiveyeardisc;
		this.factor2 = pfactor2;
		this.country = pcountry;
		this.currency = pcurrency;
		this.description = pDescription;
		this.stepvalue1 = pStepValue1;
		this.stepvalue2 = pStepValue2;
		this.stepvalue3 = pStepValue3;
		this.stepvalue4 = pStepValue4;
		this.stepvalue5 = pStepValue5;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public double getThreeyeardisc() {
		return threeyeardisc;
	}

	public void setThreeyeardisc(double threeyeardisc) {
		this.threeyeardisc = threeyeardisc;
	}

	public double getFiveyeardisc() {
		return fiveyeardisc;
	}

	public void setFiveyeardisc(double fiveyeardisc) {
		this.fiveyeardisc = fiveyeardisc;
	}

	public double getFactor2() {
		return factor2;
	}

	public void setFactor2(double factor2) {
		this.factor2 = factor2;
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
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStepvalue1() {return stepvalue1;}
	public void setStepvalue1(int stepvalue1) {this.stepvalue1 = stepvalue1;}

	public int getStepvalue2() {return stepvalue2;}
	public void setStepvalue2(int stepvalue2) {this.stepvalue2 = stepvalue2;}

	public int getStepvalue3() {return stepvalue3;}
	public void setStepvalue3(int stepvalue3) {this.stepvalue3 = stepvalue3;}

	public int getStepvalue4() {return stepvalue4;}
	public void setStepvalue4(int stepvalue4) {this.stepvalue4 = stepvalue4;}

	public int getStepvalue5() {return stepvalue5;}

	public void setStepvalue5(int stepvalue5) {this.stepvalue5 = stepvalue5;}

	public List<MonitoringServiceCosts> getMonitoringCostsList() {
		return monitoringCostsList;
	}

	public void setMonitoringCostsList(
			List<MonitoringServiceCosts> monitoringCostsList) {
		this.monitoringCostsList = monitoringCostsList;
	}

	/////*********************************************////
	public void addMonitoringServiceCost(MonitoringServiceCosts pMonitoringServiceCost){
		this.monitoringCostsList.add(pMonitoringServiceCost);
	}
	
	public void addMonitoringServiceCosts(List<MonitoringServiceCosts> pMonitoringServiceCost){
		this.monitoringCostsList.addAll(pMonitoringServiceCost);
	}
	
	public void removeMonitoringServiceCosts(){
		this.monitoringCostsList = new ArrayList<MonitoringServiceCosts>();
	}
	
}
