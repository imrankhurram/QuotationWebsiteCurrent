package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.QuantityCategory;


public interface IQuantityCategoryDAO {
	public List<QuantityCategory> getQuantityCategoryList() ;
	public List<QuantityCategory> getQuantityCategoryListWithoutNuLL() ;
	public void addQuantityCategory(String pname, String pdescription);
	public void modifyQuantityCategory(int pID, String pname, String pdescription);
	public void deleteQuantityCategory(int pID);
	public List<String> getQuantityCategoryStringList();
}
