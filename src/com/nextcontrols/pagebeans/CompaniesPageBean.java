package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.CompanyDAO;
import com.nextcontrols.bureaudao.CountryDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.Company;

@ManagedBean(name="companies")
@SessionScoped
public class CompaniesPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Company> companyList;
	private List<Company> filteredCompanyList;
	private Company selectedCompany;
	private Company newCompany;
	private Map<String,String> countries ;  

	public CompaniesPageBean(){
		companyList = new ArrayList<Company>();
		newCompany= new Company();
		initializeOptions();

	}

	public List<Company> getCompanyList() {
		setCompanyList();
		return companyList;
	}

	public void setCompanyList() {
		this.companyList = CompanyDAO.getInstance().getCompaniesList();
	}
	
	public List<Company> getFilteredCompanyList() {
		return filteredCompanyList;
	}

	public void setFilteredCompanyList(List<Company> filteredCompanyList) {
		this.filteredCompanyList = filteredCompanyList;
	}

	public Company getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(Company selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public Company getNewCompany() {
		return newCompany;
	}

	public void setNewCompany(Company newCompany) {
		this.newCompany = newCompany;
	}

	public Map<String, String> getCountries() {
		return countries;
	}

	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}
	
	public void initializeOptions(){
		countries = new HashMap<String, String>();  
	 	for(String str : CountryDAO.getInstance().getCountryList()){
			countries.put(str, str);
		}
	}

	public void updateCompany(){
		//System.out.println("sfda"+selectedUser.getUsername());
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String addNewCompany(){
		CompanyDAO.getInstance().addCompany(this.newCompany.getCompanyName(), this.newCompany.getMarkup(),this.newCompany.getHwdiscount(),this.newCompany.getMonitoringdiscount(),this.newCompany.getCountry().getCountry(),this.newCompany.getCompanyRef(),this.newCompany.isMultiyear_options());
		UserAuditDAO.getInstance().addUserAudit("Add Company", "User added the company :"+this.newCompany.getCompanyName());
		newCompany=new Company();
		return "CompaniesPage?faces-redirect=true";
	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyCompany(){
		if(this.selectedCompany!= null){
			CompanyDAO.getInstance().modifyCompany(this.selectedCompany.getId(),this.selectedCompany.getCompanyName(),this.selectedCompany.getMarkup(),this.selectedCompany.getHwdiscount(),this.selectedCompany.getMonitoringdiscount(),this.selectedCompany.getCountry().getCountry(),this.selectedCompany.getCompanyRef(),this.selectedCompany.isMultiyear_options());
			UserAuditDAO.getInstance().addUserAudit("Delete Company", "User modified the company :"+this.selectedCompany.getCompanyName());
		}
		return "CompaniesPage?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteCompany(){
		if(this.selectedCompany!= null){
			CompanyDAO.getInstance().deleteCompany(this.selectedCompany.getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Company", "User deleted the company :"+this.selectedCompany.getCompanyName());
		}
		return "CompaniesPage?faces-redirect=true";
	}
}
