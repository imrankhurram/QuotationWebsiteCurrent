package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.CountryDAO;
import com.nextcontrols.bureaudao.DayDAO;
import com.nextcontrols.bureaudao.OnceServiceDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.OnceService;

@ManagedBean(name="onceservices")
@SessionScoped
public class OnceServicesPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<OnceService> onceservicesList;
	private List<OnceService> filteredOnceservicesList;
	private OnceService selectedOnceService;
	private OnceService newOnceService;

	private Map<String,String> countries ;  
	private Map<String,String> currencies;  
	    
	public OnceServicesPageBean(){
		onceservicesList = new ArrayList<OnceService>();
		newOnceService= new OnceService();

		initializeOptions();
	}

	public List<OnceService> getOnceservicesList() {
		setOnceservicesList();
		return onceservicesList;
	}

	public void setOnceservicesList() {
		this.onceservicesList = OnceServiceDAO.getInstance().getOnceServicesList();
	}
	
	public List<OnceService> getFilteredOnceservicesList() {
		return filteredOnceservicesList;
	}

	public void setFilteredOnceservicesList(
			List<OnceService> filteredOnceservicesList) {
		this.filteredOnceservicesList = filteredOnceservicesList;
	}

	public OnceService getSelectedOnceService() {
		return selectedOnceService;
	}

	public void setSelectedOnceService(OnceService selectedOnceService) {
		this.selectedOnceService = selectedOnceService;
	}

	public OnceService getNewOnceService() {
		return newOnceService;
	}

	public void setNewOnceService(OnceService newOnceService) {
		this.newOnceService = newOnceService;
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
		for(String str : DayDAO.getInstance().getCountryList()){
			countries.put(str, str);
		}
		for(String str : DayDAO.getInstance().getCurrencyList()){
			currencies.put(str, str);
		}
		for(String str: CountryDAO.getInstance().getCountryList()){
			countries.put(str, str);
		}
		for(String str: CountryDAO.getInstance().getCurrencyList()){
			currencies.put(str, str);
		}
	}
	
	public void updateCountry(){
		//System.out.println("sfda"+selectedUser.getUsername());
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String addNewOnceService(){
		OnceServiceDAO.getInstance().addOnceService(this.newOnceService.getServiceDescription(),this.newOnceService.getMinimumCharge(),this.newOnceService.getUnitCost(),this.newOnceService.getCountry(),this.newOnceService.getCurrency(),this.newOnceService.getServiceType());
		UserAuditDAO.getInstance().addUserAudit("Add Once Service", "User added the once service : "+this.newOnceService.getServiceDescription());
		newOnceService=new OnceService();
		return "OnceServicesPage?faces-redirect=true";
	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyOnceService(){
		if(this.selectedOnceService!= null){
			OnceServiceDAO.getInstance().modifyOnceService(this.selectedOnceService.getId(),this.selectedOnceService.getServiceDescription(),this.selectedOnceService.getMinimumCharge(),this.selectedOnceService.getUnitCost(),
															this.selectedOnceService.getCountry(),this.selectedOnceService.getCurrency(),this.selectedOnceService.getServiceType());
			UserAuditDAO.getInstance().addUserAudit("Modify Once Service", "User modified the once service : "+this.selectedOnceService.getServiceDescription());
		}
		return "OnceServicesPage?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteOnceService(){
		if(this.selectedOnceService!= null){
			OnceServiceDAO.getInstance().deleteOnceService(this.selectedOnceService.getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Once Service", "User deleted the once service :"+this.selectedOnceService.getServiceDescription());
		}
		return "OnceServicesPage?faces-redirect=true";
	}
}
