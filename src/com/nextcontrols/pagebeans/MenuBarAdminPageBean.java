package com.nextcontrols.pagebeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name="adminmenubar")
@SessionScoped
public class MenuBarAdminPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int linkClicked;
	public MenuBarAdminPageBean(){
		linkClicked=2;
	}

	public int getLinkClicked() {
		return linkClicked;
	}

	public void setLinkClicked(int linkClicked) {
		this.linkClicked = linkClicked;
	}

	public  String actionHomePage (){
		setLinkClicked(1);
		//System.out.println("home page called..............");
		return "HomePage?faces-redirect=true";
	}

	public  String actionCountriesPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			setLinkClicked(2);
			return "CountriesPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionAddCountryPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(2);
			return "AddCountryPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyCountryPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		CountriesPageBean countriesPageBean = (CountriesPageBean)session.getAttribute("countries");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(countriesPageBean.getSelectedCountry()!=null){
				setLinkClicked(2);
				return "ModifyCountryPage.xhtml?faces-redirect=true";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	public  String actionCompaniesPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(3);
			return "CompaniesPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionAddCompanyPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			CompaniesPageBean companiesbean = (CompaniesPageBean)session.getAttribute("companies");
			session.removeAttribute("companies");
			companiesbean.initializeOptions();
			session.setAttribute("companies", companiesbean);		
			setLinkClicked(3);
			return "AddCompanyPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyCompanyPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		CompaniesPageBean companiesbean = (CompaniesPageBean)session.getAttribute("companies");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(companiesbean.getSelectedCompany()!=null){
				session.removeAttribute("companies");
				companiesbean.initializeOptions();
				session.setAttribute("companies", companiesbean);		
				setLinkClicked(3);
				return "ModifyCompanyPage.xhtml?faces-redirect=true";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	public  String actionUsersPage (){
		setLinkClicked(4);
		return "UsersPage.xhtml?faces-redirect=true";
	}
	
	public  String actionAddNewUserPage (){
		setLinkClicked(4);
		return "AddUserPage.xhtml?faces-redirect=true";
	}
	public  String actionModifyUserPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UsersPageBean userPageBean = (UsersPageBean)session.getAttribute("users");
		if(userPageBean.getSelectedUser()!=null){
			setLinkClicked(4);
			return "ModifyUserPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	
	public  String actionViewCompanyPartsPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		ViewCompanyPartsPageBean viewcompanyPartsBean = (ViewCompanyPartsPageBean)session.getAttribute("viewcompanyparts");
				
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(viewcompanyPartsBean !=null){
				session.removeAttribute("viewcompanyparts");
				viewcompanyPartsBean.initializeOptions();
				session.setAttribute("viewcompanyparts", viewcompanyPartsBean);
			}
			setLinkClicked(5);
			return "ViewCompanyPartsPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}

	public String actionEditAllPartsPage(){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
				
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(5);
			return "ModifyAllPartsPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
		
	}
	public  String actionSystemAnalysisPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		SystemAnalysisPageBean systemAnalysisPageBean = (SystemAnalysisPageBean)session.getAttribute("systemanalysis");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			if(systemAnalysisPageBean!=null){
				session.removeAttribute("systemanalysis");
				systemAnalysisPageBean.initializeOptions();
				session.setAttribute("systemanalysis", systemAnalysisPageBean);
			}
			setLinkClicked(5);
			return "SystemAnalysisPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	
	public  String actionPartsPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		PartsPageBean partsPageBean = (PartsPageBean)session.getAttribute("parts");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			if(partsPageBean!=null){
				session.removeAttribute("parts");
				session.setAttribute("parts", new PartsPageBean());
			}
			setLinkClicked(5);
			return "PartsPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionAddPartsPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(5);
			return "AddPartPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyPartsPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		PartsPageBean partsPageBean = (PartsPageBean)session.getAttribute("parts");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(partsPageBean.getSelectedStandardPart()!=null){
				setLinkClicked(5);
				return "ModifyPartPage.xhtml?faces-redirect=true";
			}else{
				return "";
			}	
		}else{
			return "";
		}
	}
	
	public  String actionAddPriceCategoryPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(5);
			return "AddPriceCategoryPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyPriceCategoryPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(5);
			return "ModifyPriceCategoryPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	
	public  String actionAddQuantityCategoryPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(5);
			return "AddQuantityCategoryPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyQuantityCategoryPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(5);
			return "ModifyQuantityCategoryPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	/*public  String actionRemoteRecordingServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			setLinkClicked(6);
			return "RemoteRecordingServicePage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionAddRemoteRecordingServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(6);
			return "AddRemortMonPRecordingPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyRemoteRecordingServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			setLinkClicked(6);
			return "ModifyRemortMonPRecordingPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}*/
	public  String actionMonitoringServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		MonitoringServicesPageBean monitoringServicesPageBean = (MonitoringServicesPageBean)session.getAttribute("monitoringservices");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			if(monitoringServicesPageBean!=null ){
				session.removeAttribute("monitoringservices");
				session.setAttribute("monitoringservices", new MonitoringServicesPageBean());
			}
				setLinkClicked(6);
				return "MonitoringServicesPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionAddMonitoringServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			MonitoringServicesPageBean monitoringbean = (MonitoringServicesPageBean)session.getAttribute("monitoringservices");
			session.removeAttribute("monitoringservices");
			monitoringbean.initializeOptions();
			session.setAttribute("monitoringservices", monitoringbean);
			setLinkClicked(6);
			return "AddMonitoringServicePage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyMonitoringServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		MonitoringServicesPageBean monitoringServicesPageBean = (MonitoringServicesPageBean)session.getAttribute("monitoringservices");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(monitoringServicesPageBean!=null&& monitoringServicesPageBean.getSelectedremotemonitoringService()!=null){
				session.removeAttribute("monitoringservices");
				monitoringServicesPageBean.initializeOptions();
				session.setAttribute("monitoringservices", monitoringServicesPageBean);
				setLinkClicked(6);
				return "ModifyMonitoringServicePage.xhtml?faces-redirect=true";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	public  String actionOnceServicesPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			setLinkClicked(7);
			return "OnceServicesPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
			
	}
	public  String actionAddOnceServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			OnceServicesPageBean onceservicebean = (OnceServicesPageBean)session.getAttribute("onceservices");
			session.removeAttribute("onceservices");
			onceservicebean.initializeOptions();
			session.setAttribute("onceservices", onceservicebean);
			setLinkClicked(7);
			return "AddOnceServicePage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyOnceServicePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		OnceServicesPageBean onceservicebean = (OnceServicesPageBean)session.getAttribute("onceservices");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(onceservicebean.getSelectedOnceService()!=null){
				session.removeAttribute("onceservices");
				onceservicebean.initializeOptions();
				session.setAttribute("onceservices", onceservicebean);
				setLinkClicked(7);
				return "ModifyOnceServicePage.xhtml?faces-redirect=true";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	public  String actionInstallDaysPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0
			||userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){	
			setLinkClicked(8);
			return "DaysPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
			
	}
	public  String actionAddInstallDayPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			DayPageBean daypagebean = (DayPageBean)session.getAttribute("days");
			session.removeAttribute("days");
			daypagebean.initializeOptions();
			session.setAttribute("days", daypagebean);
			setLinkClicked(8);
			return "AddDayPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyInstallDayPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		DayPageBean daypagebean = (DayPageBean)session.getAttribute("days");

		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			if(daypagebean.getSelectedday()!=null){
				session.removeAttribute("days");
				daypagebean.initializeOptions();
				session.setAttribute("days", daypagebean);
				setLinkClicked(8);
				return "ModifyDayPage.xhtml?faces-redirect=true";
			}else{
				return "";
			}
		}else{
			return "";
		}
	}
	
	public  String actionAnnualRatesPage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			//AnnualRatePageBean annualratebean = (AnnualRatePageBean)session.getAttribute("annualrates");
			//session.removeAttribute("annualrates");
			//annualratebean.initializeOptions();
			//session.setAttribute("annualrates", annualratebean);
			setLinkClicked(8);
			return "AnnualRatesPage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	
	public  String actionAddAnnualRatePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			AnnualRatePageBean annualratebean = (AnnualRatePageBean)session.getAttribute("annualrates");
			session.removeAttribute("annualrates");
			annualratebean.initializeOptions();
			session.setAttribute("annualrates", annualratebean);
			setLinkClicked(8);
			return "AddAnnualRatePage.xhtml?faces-redirect=true";
		}else{
			return "";
		}
	}
	public  String actionModifyAnnualRatePage (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		AnnualRatePageBean annualratebean = (AnnualRatePageBean)session.getAttribute("annualrates");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0){
			
			if(annualratebean.getSelectedannualRate() != null){
				session.removeAttribute("annualrates");
				annualratebean.initializeOptions();
				session.setAttribute("annualrates", annualratebean);
				setLinkClicked(8);
				return "ModifyAnnualRatePage.xhtml?faces-redirect=true";
			}else{
				return "";
			}
			
		}else{
			return "";
		}
	}
	public String actionProceedToQuote() {
		setLinkClicked(0);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		QuotationCreationPageBean quotationBean = (QuotationCreationPageBean) session
				.getAttribute("newquote");
		return quotationBean.newQuotationPageNext();
	}
}
