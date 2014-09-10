package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.StandardParts;

public interface IPartsDAO {
	public List<StandardParts> getStandardPartsAndAssembliesList();
	public List<StandardParts> getStandardPartsAndAssembliesList(String pcountry);
	public List<StandardParts> getStandardPartsAndAssembliesList(double frieght, double duty, double conversionratefrompounds,String pcountry);
	public List<StandardParts> getStandardPartsAndAssembliesListWithQuoteQuantity(double frieght, double duty, double conversionratefrompounds,String pcountry, int pquotationID);
	public List<StandardParts> getStandardPartsAndAssembliesListWithNonzeroQuantity(double frieght, double duty, double conversionratefrompounds,String pcountry, int pquotationID);
	public List<String> getDaysForQuotation( int pquotationID);
	public List<StandardParts> getStandardAssembliesList();
	public List<StandardParts> getStandardPartsList() ;
	public List<StandardParts> getStandardPartsList(int assemblyID) ;
	public void addStandardParts(String partNumber, String partDescription,String partType, double cost, double markup, String pCountry, String pPartTag, String pPriceCat, String pQuantityCat,int pInstallationTime, int pCalibrationTime,int listPriority);
	public void modifyStandardParts(int pPartID, String partNumber, String partDescription,String partType, double cost, double markup , String pCountry, String pPartTag, String pPriceCat, String pQuantityCat,int pInstallationTime, int pCalibrationTime,int listPriority);
	public void deleteStandardParts(int pPartID);
	public void saveSubParts(int passemblyID, List<StandardParts> pSubpartsList);
	public void saveMarkups(List<StandardParts> pPartsList);
	public void saveCategories(List<StandardParts> pPartsList);
	public void saveAllParts(List<StandardParts> pPartsList);
	public void saveCategory(StandardParts part);
	public List<String>getCountryList();
	public List<String>getPartsTagList();
}
