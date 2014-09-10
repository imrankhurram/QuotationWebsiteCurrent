package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

import com.nextcontrols.bureaudao.CompanyDAO;
import com.nextcontrols.bureaudao.PartsDAO;
import com.nextcontrols.bureaudomain.Company;
import com.nextcontrols.bureaudomain.StandardParts;

@ManagedBean(name="viewcompanyparts")
@SessionScoped
public class ViewCompanyPartsPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<SelectItem> companiesDisplayList;
	private List<Company> companiesList;
	private String selectedCompany;
	private Company selectedCompanyObj;
	private List<StandardParts> standarpartsList;
	private List<StandardParts> filteredStandarpartsList;
	private String currency;
	

	
	public ViewCompanyPartsPageBean(){
		companiesDisplayList=new ArrayList<SelectItem> ();
		companiesList = new ArrayList<Company>();
		standarpartsList = new ArrayList<StandardParts>();
		
		initializeOptions();
	}
	
	public List<SelectItem> getCompaniesDisplayList() {
		return companiesDisplayList;
	}

	public void setCompaniesDisplayList(List<SelectItem> companiesDisplayList) {
		this.companiesDisplayList = companiesDisplayList;
	}

	public List<Company> getCompaniesList() {
		return companiesList;
	}

	public void setCompaniesList(List<Company> companiesList) {
		this.companiesList = companiesList;
	}

	public String getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Company getSelectedCompanyObj() {
		return selectedCompanyObj;
	}

	public void setSelectedCompanyObj(Company selectedCompanyObj) {
		this.selectedCompanyObj = selectedCompanyObj;
	}

	public List<StandardParts> getStandarpartsList() {
		return standarpartsList;
	}

	public void setStandarpartsList(List<StandardParts> standarpartsList) {
		this.standarpartsList = standarpartsList;
	}

	public List<StandardParts> getFilteredStandarpartsList() {
		return filteredStandarpartsList;
	}

	public void setFilteredStandarpartsList(
			List<StandardParts> filteredStandarpartsList) {
		this.filteredStandarpartsList = filteredStandarpartsList;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void initializeOptions(){
		this.companiesList=CompanyDAO.getInstance().getCompaniesList();
		List<Double> factors= new ArrayList <Double>();
		factors.add(this.companiesList.get(0).getCountry().getConversionratefrompounds());
		factors.add(this.companiesList.get(0).getCountry().getFreight());
		factors.add(this.companiesList.get(0).getCountry().getDuty());
		selectedCompanyObj = this.companiesList.get(0);
		selectedCompany = this.companiesList.get(0).getId()+"";
		currency = this.companiesList.get(0).getCountry().getCurrency();
		this.standarpartsList=PartsDAO.getInstance().getStandardPartsAndAssembliesList(selectedCompanyObj.getCountry().getCountry());
		for(int i = 0 ; i<standarpartsList.size();i++){
			standarpartsList.get(i).setFactors(factors);
		}
		companiesDisplayList.clear();
		for (int i=0;i<=companiesList.size()-1;i++){
			companiesDisplayList.add(new SelectItem(companiesList.get(i).getId(),companiesList.get(i).getCompanyName()));
		}
		//currency = "£";
	}
	
	public void handleCountryChange() { 
		/*this.companiesDisplayList.clear();
		this.companiesList=CompanyDAO.getInstance().getCompaniesList(selectedCountry);
		for (int i=0;i<=companiesList.size()-1;i++){
			companiesDisplayList.add(new SelectItem(companiesList.get(i).getId(),companiesList.get(i).getCompanyName()));
		}*/
    }  
	
	public void handleCompanyChange() { 
		List<Double> factors= new ArrayList <Double>();
		if(selectedCompany != null){
			for(Company comp : companiesList){
				if(comp.getId() == Integer.parseInt(selectedCompany)){
					selectedCompanyObj = comp;
					this.standarpartsList=PartsDAO.getInstance().getStandardPartsAndAssembliesList(selectedCompanyObj.getCountry().getCountry());
					factors.add(selectedCompanyObj.getCountry().getConversionratefrompounds());
					factors.add(selectedCompanyObj.getCountry().getDuty());
					factors.add(selectedCompanyObj.getCountry().getFreight());
					currency = selectedCompanyObj.getCountry().getCurrency();
					break;
				}
			}
			for(int i = 0 ; i<standarpartsList.size();i++){
				standarpartsList.get(i).setFactors(factors);
			}
		}
	}
	
	public void saveMarkups(){
		/*System.out.println("Start");
		for(StandardParts st :standarpartsList){
			System.out.println(st.getPartNumber() +" : "+st.getMarkup());
		}
		System.out.println("Finish");*/
		PartsDAO.getInstance().saveMarkups(standarpartsList);
	}
	
	public void recalculate(){
		//this.standarpartsList=PartsDAO.getInstance().getStandardPartsAndAssembliesList(selectedCompanyObj.getCountry().getCountry());
		System.out.println("Recalculating Cost");
	}
}
