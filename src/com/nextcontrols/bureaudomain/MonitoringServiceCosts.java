package com.nextcontrols.bureaudomain;




public class MonitoringServiceCosts {
	
	private int id;
	private int numberofSensors;
	private int oneYearlyCosts;

	public MonitoringServiceCosts() {
	}
	
	public MonitoringServiceCosts(int pid, int pnumberofsensors, int poneyearlycosts) {
		super();
		this.id = pid;
		this.numberofSensors = pnumberofsensors;
		this.oneYearlyCosts = poneyearlycosts;
	}

	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getNumberofSensors() {
		return numberofSensors;
	}

	public void setNumberofSensors(int numberofSensors) {
		this.numberofSensors = numberofSensors;
	}

	public int getOneYearlyCosts() {
		return oneYearlyCosts;
	}

	public void setOneYearlyCosts(int oneYearlyCosts) {
		this.oneYearlyCosts = oneYearlyCosts;
	}

	/////*********************************************////
	
	public int hashCode() { return id; }

    @Override 
    public boolean equals(Object o) {
        return (o instanceof MonitoringServiceCosts) && (this.id == ((MonitoringServiceCosts) o).id);
    }
	
}
