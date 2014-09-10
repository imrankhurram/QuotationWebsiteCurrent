package com.nextcontrols.bureaudomain;



public class NonStandardParts {

	
	private int id;
	private String partNumber;
	private String partDescription;
	private double cost;
	private int quantity;
	
	public NonStandardParts() {

	}
	
	public NonStandardParts(int id, String partNumber, String partDescription,
			 double cost,int quantity) {
		this.id = id;
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.cost = cost;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPartNumber() {
		return partNumber;
	}
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	public String getPartDescription() {
		return partDescription;
	}
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	
}
