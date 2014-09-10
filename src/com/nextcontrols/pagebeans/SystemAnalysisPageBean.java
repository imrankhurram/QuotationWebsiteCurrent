package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.data.FilterEvent;

import com.nextcontrols.bureaudao.PartsDAO;
import com.nextcontrols.bureaudao.PriceCategoryDAO;
import com.nextcontrols.bureaudao.QuantityCategoryDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.PriceCategory;
import com.nextcontrols.bureaudomain.QuantityCategory;
import com.nextcontrols.bureaudomain.StandardParts;
@ManagedBean(name="systemanalysis")
@SessionScoped
public class SystemAnalysisPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<StandardParts> standarpartsList;
	private StandardParts selectedStandardPart;
	private StandardParts newStandardPart;
	
	private Map<String,String> priceCategoriesOptions ;
	private Map<String,String> quantityCategoriesOptions ;
	
	private List<PriceCategory> priceCategoriesList;
	private PriceCategory selectedpriceCategory;
	private PriceCategory newpriceCategory;
	
	private List<QuantityCategory> quantityCategoriesList;
	private QuantityCategory selectedquantityCategory;
	private QuantityCategory newquantityCategory;
	private List<StandardParts> filteredStarpartsList;
	
	
	private String selectedPriceCat ;
	private String selQuantitycategory;
	private int currQuantityfactor;
	private String currPartNumber; 
	
	public SystemAnalysisPageBean(){
		standarpartsList = new ArrayList<StandardParts>();
		newStandardPart= new StandardParts();
		
		priceCategoriesList = new ArrayList<PriceCategory>();
		newpriceCategory= new PriceCategory();
		
		quantityCategoriesList = new ArrayList<QuantityCategory>();
		newquantityCategory= new QuantityCategory();
		initializeOptions();
		
	}

	public List<StandardParts> getStandarpartsList() {
		setStandarpartsList();
		return standarpartsList;
	}

	public void setStandarpartsList() {
		//this.standarpartsList=PartsDAO.getInstance().getStandardAssembliesList();
	}
	
	public StandardParts getSelectedStandardPart() {
		return selectedStandardPart;
	}

	public void setSelectedStandardPart(StandardParts selectedStandardPart) {
		this.selectedStandardPart = selectedStandardPart;
	}

	public StandardParts getNewStandardPart() {
		return newStandardPart;
	}

	public void setNewStandardPart(StandardParts newStandardPart) {
		this.newStandardPart = newStandardPart;
	}

	public List<PriceCategory> getPriceCategoriesList() {
		setPriceCategoriesList();
		return priceCategoriesList;
	}

	public void setPriceCategoriesList() {
		this.priceCategoriesList = PriceCategoryDAO.getInstance().getPriceCategoryList();
		//this.priceCategoriesList = priceCategoriesList;
	}

	public PriceCategory getSelectedpriceCategory() {
		return selectedpriceCategory;
	}

	public void setSelectedpriceCategory(PriceCategory selectedpriceCategory) {
		this.selectedpriceCategory = selectedpriceCategory;
	}

	public PriceCategory getNewpriceCategory() {
		return newpriceCategory;
	}

	public void setNewpriceCategory(PriceCategory newpriceCategory) {
		this.newpriceCategory = newpriceCategory;
	}

	public List<QuantityCategory> getQuantityCategoriesList() {
		setQuantityCategoriesList();
		return quantityCategoriesList;
	}

	public void setQuantityCategoriesList() {
		this.quantityCategoriesList = QuantityCategoryDAO.getInstance().getQuantityCategoryList();
		//this.quantityCategoriesList = quantityCategoriesList;
	}

	public QuantityCategory getSelectedquantityCategory() {
		return selectedquantityCategory;
	}

	public void setSelectedquantityCategory(
			QuantityCategory selectedquantityCategory) {
		this.selectedquantityCategory = selectedquantityCategory;
	}

	public QuantityCategory getNewquantityCategory() {
		return newquantityCategory;
	}

	public void setNewquantityCategory(QuantityCategory newquantityCategory) {
		this.newquantityCategory = newquantityCategory;
	}

	public Map<String, String> getPriceCategoriesOptions() {
		return priceCategoriesOptions;
	}

	public void setPriceCategoriesOptions(Map<String, String> priceCategoriesOptions) {
		this.priceCategoriesOptions = priceCategoriesOptions;
	}

	public Map<String, String> getQuantityCategoriesOptions() {
		return quantityCategoriesOptions;
	}

	public void setQuantityCategoriesOptions(
			Map<String, String> quantityCategoriesOptions) {
		this.quantityCategoriesOptions = quantityCategoriesOptions;
	}
	

	public List<StandardParts> getFilteredStarpartsList() {
		return filteredStarpartsList;
	}

	public void setFilteredStarpartsList(List<StandardParts> filteredStarpartsList) {
		this.filteredStarpartsList = filteredStarpartsList;
	}

	public void initializeOptions(){
		priceCategoriesOptions = new TreeMap<String, String>();  
	 	for(String str : PriceCategoryDAO.getInstance().getPriceCategoryStringList()){
	 		priceCategoriesOptions.put(str, str);
		}
	 	quantityCategoriesOptions = new LinkedHashMap<String, String>();  
	 	for(String str : QuantityCategoryDAO.getInstance().getQuantityCategoryStringList()){
	 		quantityCategoriesOptions.put(str, str);
		}
	 	this.standarpartsList=PartsDAO.getInstance().getStandardAssembliesList();
	 	this.filteredStarpartsList = null;
	}

	public void saveCategories(){
		//System.out.println("Start");
/*		for(StandardParts st :standarpartsList){
			System.out.println(st.getPartNumber() +" : "+st.getPricecategory()+" : "+ st.getQuantitycategory());
		}
*/		//System.out.println("Finish");
		PartsDAO.getInstance().saveCategories(standarpartsList);
	}
	
	/**
	 * adds and inserts audit
	 * @return
	 */
	public String addNewPriceCategory(){
		PriceCategoryDAO.getInstance().addPriceCategory(this.newpriceCategory.getName(), this.newpriceCategory.getDescription());
		UserAuditDAO.getInstance().addUserAudit("Add Price Category", "User added the price category :"+this.newpriceCategory.getName()+":"+this.newpriceCategory.getDescription());
		newpriceCategory=new PriceCategory();
		return "SystemAnalysisPage?faces-redirect=true";
	}
	/**
	 * modify and inserts audit
	 * @return
	 */
	public String modifyPriceCategory(){
	if(this.getSelectedpriceCategory()!= null){
			PriceCategoryDAO.getInstance().modifyPriceCategory(this.getSelectedpriceCategory().getId(), this.getSelectedpriceCategory().getName(), this.getSelectedpriceCategory().getDescription());
			UserAuditDAO.getInstance().addUserAudit("Modify Price Category", "User modified the price category :"+this.getSelectedpriceCategory().getName()+":"+this.getSelectedpriceCategory().getDescription());
		}
		return "SystemAnalysisPage?faces-redirect=true";
	}
	
	/**
	 * delete and inserts audit
	 * @return
	 */
	public String deletePriceCategory(){
		if(this.getSelectedpriceCategory()!= null){
			PriceCategoryDAO.getInstance().deletePriceCategory(this.getSelectedpriceCategory().getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Price Category", "User deleted the price category :"+this.getSelectedpriceCategory().getName()+":"+this.getSelectedpriceCategory().getDescription());
		}
		return "SystemAnalysisPage?faces-redirect=true";
	}
	
	
	
	
	/**
	 * adds and inserts audit
	 * @return
	 */
	public String addNewQuantityCategory(){
		QuantityCategoryDAO.getInstance().addQuantityCategory(this.newquantityCategory.getName(), this.newquantityCategory.getDescription());
		UserAuditDAO.getInstance().addUserAudit("Add Quantity Category", "User added the quantity category :"+this.newquantityCategory.getName()+":"+this.newquantityCategory.getDescription());
		newquantityCategory=new QuantityCategory();
		return "SystemAnalysisPage?faces-redirect=true";
	}
	/**
	 * modify and inserts audit
	 * @return
	 */
	public String modifyQuantityCategory(){
	if(this.getSelectedquantityCategory()!= null){
			QuantityCategoryDAO.getInstance().modifyQuantityCategory(this.getSelectedquantityCategory().getId(), this.getSelectedquantityCategory().getName(), this.getSelectedquantityCategory().getDescription());
			UserAuditDAO.getInstance().addUserAudit("Modify Quantity Category", "User modified the quantity category :"+this.getSelectedquantityCategory().getName()+":"+this.getSelectedquantityCategory().getDescription());
		}
		return "SystemAnalysisPage?faces-redirect=true";
	}
	
	/**
	 * delete and inserts audit
	 * @return
	 */
	public String deleteQuantityCategory(){
		if(this.getSelectedquantityCategory()!= null){
			QuantityCategoryDAO.getInstance().deleteQuantityCategory(this.getSelectedquantityCategory().getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Quantity Category", "User deleted the quantity category :"+this.getSelectedquantityCategory().getName()+":"+this.getSelectedquantityCategory().getDescription());
		}
		return "SystemAnalysisPage?faces-redirect=true";
	}
	/*
	 * Selects the current row to modify
	 * on double click
	 */
	public void prepareAssemblyModification(StandardParts part){
//		System.out.println(part.getPricecategory() + " " + part.getQuantitycategory());
//		System.out.println(this.quantityCategoriesOptions);
		this.currPartNumber = part.getPartNumber();
		this.selectedPriceCat = part.getPricecategory();
		this.selQuantitycategory = part.getQuantitycategory();
		this.currQuantityfactor = part.getQuantityfactor();
		RequestContext.getCurrentInstance().update("frm");
	}
	
	/*
	 * Modifies Assembly
	 */
	public void ModifyAssembly(ActionEvent event) {
//		System.out.println(this.selectedStandardPart.getPricecategory() + " " + this.selectedStandardPart.getQuantitycategory());
		this.selectedStandardPart.setPricecategory(this.selectedPriceCat);
		this.selectedStandardPart.setQuantitycategory(this.selQuantitycategory);
		this.selectedStandardPart.setQuantityfactor(this.currQuantityfactor);
		PartsDAO.getInstance().saveCategory(this.selectedStandardPart);
//		System.out.println(this.selectedStandardPart.getPricecategory() + " " + this.selectedStandardPart.getQuantitycategory());
	}

	public String getSelectedPriceCat() {
		return selectedPriceCat;
	}

	public void setSelectedPriceCat(String selectedPriceCat) {
		this.selectedPriceCat = selectedPriceCat;
	}

	public String getSelQuantitycategory() {
		return selQuantitycategory;
	}

	public void setSelQuantitycategory(String selQuantitycategory) {
		this.selQuantitycategory = selQuantitycategory;
	}

	public int getCurrQuantityfactor() {
		return currQuantityfactor;
	}

	public void setCurrQuantityfactor(int currQuantityfactor) {
		this.currQuantityfactor = currQuantityfactor;
	}

	public String getCurrPartNumber() {
		return currPartNumber;
	}

	public void setCurrPartNumber(String currPartNumber) {
		this.currPartNumber = currPartNumber;
	}
	
	
}
