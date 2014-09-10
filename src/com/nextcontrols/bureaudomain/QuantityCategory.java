package com.nextcontrols.bureaudomain;

import java.io.Serializable;



public class QuantityCategory implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name="";
	private String description="";
	private int tempquantity =0;
	public QuantityCategory() {

	}
	
	public QuantityCategory(int pID, String pName, String pDescription) {
		this.id = pID;
		this.name = pName;
		this.description = pDescription;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTempquantity() {
		return tempquantity;
	}

	public void setTempquantity(int tempquantity) {
		this.tempquantity = tempquantity;
	}

	@Override
	public String toString() {
	    return this.name;
	}
	
	public void addQuantitytype(int pquantity, int pquantityfactor){
		tempquantity += pquantity*pquantityfactor;
	}
	
}
