package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.AnnualRateDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.AnnualRate;

@ManagedBean(name="annualrates")
@SessionScoped
public class AnnualRatePageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<AnnualRate> annualRatesList;
	private List<AnnualRate> filteredAnnualRatesList;
	private AnnualRate selectedannualRate;
	private AnnualRate newannualRate;
    private Map<String,String> countries ;  
    private Map<String,String> currencies;  

	public AnnualRatePageBean(){
		annualRatesList = new ArrayList<AnnualRate>();
		newannualRate= new AnnualRate();
		
		initializeOptions();
	}

	public List<AnnualRate> getAnnualRatesList() {
		setAnnualRatesList();
		return annualRatesList;
	}


	public void setAnnualRatesList() {
		this.annualRatesList = AnnualRateDAO.getInstance().getAnnualRatesList();
	}

	public List<AnnualRate> getFilteredAnnualRatesList() {
		return filteredAnnualRatesList;
	}

	public void setFilteredAnnualRatesList(List<AnnualRate> filteredAnnualRatesList) {
		this.filteredAnnualRatesList = filteredAnnualRatesList;
	}

	public AnnualRate getSelectedannualRate() {
		return selectedannualRate;
	}


	public void setSelectedannualRate(AnnualRate selectedannualRate) {
		this.selectedannualRate = selectedannualRate;
	}


	public AnnualRate getNewannualRate() {
		return newannualRate;
	}


	public void setNewannualRate(AnnualRate newannualRate) {
		this.newannualRate = newannualRate;
	}
	
	public Map<String, String> getCountries() {
		return countries;
	}

	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}

	public Map<String, String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, String> currencies) {
		this.currencies = currencies;
	}

	public void initializeOptions(){
		countries = new HashMap<String, String>();  
	    currencies = new HashMap<String, String>(); 
		for(String str : AnnualRateDAO.getInstance().getCountryList()){
			countries.put(str, str);
		}
		for(String str : AnnualRateDAO.getInstance().getCurrencyList()){
			currencies.put(str, str);
		}
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
		public String addAnnualRate(){
		AnnualRateDAO.getInstance().addAnnualRate(this.newannualRate.getDescription(),this.newannualRate.getCostperday(), this.newannualRate.getAnnualcalibration(),this.newannualRate.getAnnualdeadtraveldaycost(),this.newannualRate.getThreeyearfactor(),this.newannualRate.getOneyearfactor(),this.newannualRate.getCountry(),this.newannualRate.getCurrency());
		UserAuditDAO.getInstance().addUserAudit("Add Annual Rate", "User added the annual rate : "+this.newannualRate.getDescription());
		newannualRate=new AnnualRate();
		return "AnnualRatesPage.xhtml?faces-redirect=true";
	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyAnnualRate(){
		if(this.selectedannualRate!= null){
			AnnualRateDAO.getInstance().modifyAnnualRate(this.selectedannualRate.getId(),this.selectedannualRate.getDescription(),this.selectedannualRate.getCostperday(), this.selectedannualRate.getAnnualcalibration(),this.selectedannualRate.getAnnualdeadtraveldaycost(),this.selectedannualRate.getThreeyearfactor(),this.selectedannualRate.getOneyearfactor(),this.selectedannualRate.getCountry(),this.selectedannualRate.getCurrency());	
			UserAuditDAO.getInstance().addUserAudit("Modify Annual Rate", "User modified the annual rate : "+this.selectedannualRate.getDescription());
		}
		return "AnnualRatesPage.xhtml?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteAnnualRate(){
		if(this.selectedannualRate!= null){
			AnnualRateDAO.getInstance().deleteAnnualRate(this.selectedannualRate.getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Annual Rate", "User deleted the annual rate : "+this.selectedannualRate.getDescription());
		}
		return "AnnualRatesPage.xhtml?faces-redirect=true";
	}
}
