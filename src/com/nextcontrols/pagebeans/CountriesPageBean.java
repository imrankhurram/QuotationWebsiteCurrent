package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.CountryDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.Country;

@ManagedBean(name="countries")
@SessionScoped
public class CountriesPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Country> countryList;
	private List<Country> filteredCountryList;
	private Country selectedCountry;
	private Country newCountry;

	public CountriesPageBean(){
		countryList = new ArrayList<Country>();
		newCountry= new Country();

	}

	public List<Country> getCountryList() {
		setCountryList();
		return countryList;
	}

	public void setCountryList() {
		this.countryList = CountryDAO.getInstance().getCountriesList();
	}
	
	public List<Country> getFilteredCountryList() {
		return filteredCountryList;
	}

	public void setFilteredCountryList(List<Country> filteredCountryList) {
		this.filteredCountryList = filteredCountryList;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public Country getNewCountry() {
		return newCountry;
	}

	public void setNewCountry(Country newCountry) {
		this.newCountry = newCountry;
	}

	public void updateCountry(){
		//System.out.println("sfda"+selectedUser.getUsername());
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String addNewCountry(){
		CountryDAO.getInstance().addCountry(this.newCountry.getCountry(), this.newCountry.getFullName(), this.newCountry.getCurrency(), this.newCountry.getConversionratefrompounds(),
											this.newCountry.getFreight(),this.newCountry.getDuty());
		UserAuditDAO.getInstance().addUserAudit("Add Country", "User added the country : "+this.newCountry.getCountry());
		newCountry=new Country();
		return "CountriesPage?faces-redirect=true";
	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyCountry(){
		if(this.selectedCountry!= null){
			CountryDAO.getInstance().modifyCountry(this.selectedCountry.getCountry(), this.selectedCountry.getFullName(), this.selectedCountry.getCurrency(), this.selectedCountry.getConversionratefrompounds(),
												   this.selectedCountry.getFreight(),this.selectedCountry.getDuty());
			UserAuditDAO.getInstance().addUserAudit("Modify Country", "User modified the country : "+this.newCountry.getCountry());
		}
		return "CountriesPage?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteCountry(){
		if(this.selectedCountry!= null){
			CountryDAO.getInstance().deleteCountry(this.selectedCountry.getCountry());
			UserAuditDAO.getInstance().addUserAudit("Delete Country", "User deleted the country :"+this.selectedCountry.getCountry());
		}
		return "CountriesPage?faces-redirect=true";
	}
	
}
