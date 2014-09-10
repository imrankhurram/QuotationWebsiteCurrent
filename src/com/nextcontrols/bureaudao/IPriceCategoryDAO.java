package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.PriceCategory;


public interface IPriceCategoryDAO {
	public List<PriceCategory> getPriceCategoryList() ;
	public List<PriceCategory> getPriceCategoryListWithoutNuLL() ;
	public void addPriceCategory(String pname, String pdescription);
	public void modifyPriceCategory(int pID, String pname, String pdescription);
	public void deletePriceCategory(int pID);
	public List<String> getPriceCategoryStringList();
}
