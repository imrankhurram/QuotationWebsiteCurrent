package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.CountryDAO;
import com.nextcontrols.bureaudao.MonitoringServiceDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.MonitoringService;

@ManagedBean(name="monitoringservices")
@SessionScoped
public class MonitoringServicesPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<MonitoringService> remotemonitoringservicesList;
	private List<MonitoringService> filteredRemotemonitoringservicesList;
	private MonitoringService selectedremotemonitoringService;
	private MonitoringService newremotemonitoringService;

	private Map<String,String> countries ;  
	private Map<String,String> currencies;  
	private int numberOfSensor;
	private int oneYearOption;
	private double salesDiscount;
	String option = "1";
	public MonitoringServicesPageBean(){
		remotemonitoringservicesList = new ArrayList<MonitoringService>();
		newremotemonitoringService= new MonitoringService();
		numberOfSensor = 4;
		salesDiscount = 0.0;

		initializeOptions();
	}
	
	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public int getNumberOfSensor() {
		return numberOfSensor;
	}

	public void setNumberOfSensor(int numberOfSensor) {
		this.numberOfSensor = numberOfSensor;
	}

	public int getOneYearOption() {
		return oneYearOption;
	}

	public void setOneYearOption(int oneYearOption) {
		this.oneYearOption = oneYearOption;
	}

	public double getSalesDiscount() {
		return salesDiscount;
	}

	public void setSalesDiscount(double salesDiscount) {
		this.salesDiscount = salesDiscount;
	}

	public List<MonitoringService> getRemotemonitoringservicesList() {
		setRemotemonitoringservicesList();
		return remotemonitoringservicesList;
	}

	public void setRemotemonitoringservicesList() {
		this.remotemonitoringservicesList = MonitoringServiceDAO.getInstance().getMonitoringServicesList();
		
	}

	public List<MonitoringService> getFilteredRemotemonitoringservicesList() {
		return filteredRemotemonitoringservicesList;
	}

	public void setFilteredRemotemonitoringservicesList(
			List<MonitoringService> filteredRemotemonitoringservicesList) {
		this.filteredRemotemonitoringservicesList = filteredRemotemonitoringservicesList;
	}

	public MonitoringService getSelectedremotemonitoringService() {
		return selectedremotemonitoringService;
	}

	public void setSelectedremotemonitoringService(
			MonitoringService selectedremotemonitoringService) {
		this.selectedremotemonitoringService = selectedremotemonitoringService;
	}

	public MonitoringService getNewremotemonitoringService() {
		return newremotemonitoringService;
	}

	public void setNewremotemonitoringService(
			MonitoringService newremotemonitoringService) {
		this.newremotemonitoringService = newremotemonitoringService;
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
		for(String str : MonitoringServiceDAO.getInstance().getCountryList()){
			countries.put(str, str);
		}
		for(String str : MonitoringServiceDAO.getInstance().getCurrencyList()){
			currencies.put(str, str);
		}
		for(String str: CountryDAO.getInstance().getCountryList()){
			countries.put(str, str);
		}
		for(String str: CountryDAO.getInstance().getCurrencyList()){
			currencies.put(str, str);
		}
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String addNewMonitoringService(){
		MonitoringServiceDAO.getInstance().addMonitoringService(this.newremotemonitoringService.getThreeyeardisc(),this.newremotemonitoringService.getFiveyeardisc(),this.newremotemonitoringService.getFactor2(),this.newremotemonitoringService.getCountry(),this.newremotemonitoringService.getCurrency(),this.newremotemonitoringService.getDescription(),this.newremotemonitoringService.getStepvalue1(),this.newremotemonitoringService.getStepvalue2(),this.newremotemonitoringService.getStepvalue3(),this.newremotemonitoringService.getStepvalue4(),this.newremotemonitoringService.getStepvalue5());
		UserAuditDAO.getInstance().addUserAudit("Add Monitoring Service", "User added the monitoring service : "+this.newremotemonitoringService.getCountry());
		newremotemonitoringService=new MonitoringService();
		return "MonitoringServicesPage?faces-redirect=true";
	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyMonitoringService(){
		if(this.selectedremotemonitoringService!= null){
			MonitoringServiceDAO.getInstance().modifyMonitoringService(this.selectedremotemonitoringService.getThreeyeardisc(),this.selectedremotemonitoringService.getFiveyeardisc(),this.selectedremotemonitoringService.getFactor2(),this.selectedremotemonitoringService.getCountry(),this.selectedremotemonitoringService.getCurrency(),this.selectedremotemonitoringService.getId(),this.selectedremotemonitoringService.getDescription(),this.selectedremotemonitoringService.getStepvalue1(),this.selectedremotemonitoringService.getStepvalue2(),this.selectedremotemonitoringService.getStepvalue3(),this.selectedremotemonitoringService.getStepvalue4(),this.selectedremotemonitoringService.getStepvalue5());
			UserAuditDAO.getInstance().addUserAudit("Modify Monitoring Service", "User modified the monitoring service : "+this.selectedremotemonitoringService.getCountry());
		}
		return "MonitoringServicesPage?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteMonitoringService(){
		if(this.selectedremotemonitoringService!= null){
			MonitoringServiceDAO.getInstance().deleteMonitoringService(this.selectedremotemonitoringService.getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Monitoring Service", "User deleted the monitoring service :"+this.selectedremotemonitoringService.getCountry());
		}
		return "MonitoringServicesPage?faces-redirect=true";
	}
	
	public String openMonitoringList(){
		if(this.selectedremotemonitoringService!= null){
			selectedremotemonitoringService.removeMonitoringServiceCosts();
			selectedremotemonitoringService.addMonitoringServiceCosts(MonitoringServiceDAO.getInstance().getMonitoringServiceCosts(selectedremotemonitoringService.getId(), selectedremotemonitoringService.getStepvalue1(),selectedremotemonitoringService.getStepvalue2(),selectedremotemonitoringService.getStepvalue3(),selectedremotemonitoringService.getStepvalue4(),selectedremotemonitoringService.getStepvalue5()));
			oneYearOption = selectedremotemonitoringService.getMonitoringCostsList().get(0).getOneYearlyCosts();
			return "MonitoringServicesCostsPage?faces-redirect=true";
		}
		return "MonitoringServicesPage?faces-redirect=true";
	}
	
	public void deleteCost(){
		//todo
	}
	public String saveCosts(){
		if(this.selectedremotemonitoringService!= null){
			MonitoringServiceDAO.getInstance().updateMonitoringServiceCosts(this.selectedremotemonitoringService);
			//
			selectedremotemonitoringService.removeMonitoringServiceCosts();
			selectedremotemonitoringService.addMonitoringServiceCosts(MonitoringServiceDAO.getInstance().getMonitoringServiceCosts(selectedremotemonitoringService.getId(), selectedremotemonitoringService.getStepvalue1(),selectedremotemonitoringService.getStepvalue2(),selectedremotemonitoringService.getStepvalue3(),selectedremotemonitoringService.getStepvalue4(),selectedremotemonitoringService.getStepvalue5()));
			oneYearOption = selectedremotemonitoringService.getMonitoringCostsList().get(0).getOneYearlyCosts();
			MonitoringServiceDAO.getInstance().updateMonitoringServiceCosts(this.selectedremotemonitoringService);

			//
		}
		return "MonitoringServicesCostsPage?faces-redirect=true";
	}
	
	public void lookupTable(){
		if(this.selectedremotemonitoringService!= null){
			if(numberOfSensor <= this.selectedremotemonitoringService.getMonitoringCostsList().get(0).getNumberofSensors()){
				oneYearOption = this.selectedremotemonitoringService.getMonitoringCostsList().get(0).getOneYearlyCosts();
			}else{
				for(int i=1 ; i<this.selectedremotemonitoringService.getMonitoringCostsList().size();i++){
					if(numberOfSensor>this.selectedremotemonitoringService.getMonitoringCostsList().get(i-1).getNumberofSensors()
						&& numberOfSensor<=this.selectedremotemonitoringService.getMonitoringCostsList().get(i).getNumberofSensors()){
						oneYearOption = this.selectedremotemonitoringService.getMonitoringCostsList().get(i).getOneYearlyCosts();
					}
				}
			}
		}
	}
}
