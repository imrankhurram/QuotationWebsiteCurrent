package com.nextcontrols.pagebeans;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.bureaudao.AnnualRateDAO;
import com.nextcontrols.bureaudao.DayDAO;
import com.nextcontrols.bureaudao.EmailTemplateDAO;
import com.nextcontrols.bureaudao.LetterTemplateDAO;
import com.nextcontrols.bureaudao.MonitoringServiceDAO;
import com.nextcontrols.bureaudao.OnceServiceDAO;
import com.nextcontrols.bureaudao.PartsDAO;
import com.nextcontrols.bureaudao.PriceCategoryDAO;
import com.nextcontrols.bureaudao.QuantityCategoryDAO;
import com.nextcontrols.bureaudao.QuotationDAO;
import com.nextcontrols.bureaudao.SettingsDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.AnnualRate;
import com.nextcontrols.bureaudomain.EmailTemplate;
import com.nextcontrols.bureaudomain.LetterTemplate;
import com.nextcontrols.bureaudomain.NonStandardParts;
import com.nextcontrols.bureaudomain.OnceService;
import com.nextcontrols.bureaudomain.PriceCategory;
import com.nextcontrols.bureaudomain.QuantityCategory;
import com.nextcontrols.bureaudomain.Quotation;
import com.nextcontrols.bureaudomain.StandardParts;
import com.nextcontrols.bureaudomain.Template;
import com.nextcontrols.utility.ValidateEmails;

@ManagedBean(name = "newquote")
@SessionScoped
public class QuotationCreationPageBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private Quotation quotation;
	private String currency;
	private int currentPageCounter = 0;
	StandardParts selectedpart;
	/**************************************************** selectPartsPage *************************************************/
	private List<List<StandardParts>> standardPartsCollection;
	private List<NonStandardParts> partnst;
	List<StandardParts> tempStParts;
	private List<PriceCategory> priceCategoriesList;
	private List<QuantityCategory> quantityCategoriesList;
	private List<OnceService> onceservicesList;
	private List<String> siteNames;
	private List<String> siteInstallation;
	private List<String> installationTravel;
	private List<String> siteCalibration;
	private List<String> calibartionTravel;
	private List<String> annualSiteCalibration;
	private List<String> annualDeadTravelDays;
	private List<StandardParts> selectedStandardPartsSummary;

	String tempSiteName = "";
	String tempSiteInstallation = "";
	String tempInstallationTravel = "";
	String tempSiteCalibration = "";
	String tempCalibartionTravel = "";
	String tempAnnualSiteCalibration = "";
	String tempAnnualDeadTravelDays = "";
	double mHardwareDiscount = 0;
	double mRemoteMonitoringDiscount = 0;
	double mSiteInstallationDiscount = 0;
	double mServiceOptionsDiscount = 0;
	boolean renderDiscounts = false;

	int totalSensorCount = 0;
	int totalsystembreakdown = 0;
	int onceServicestotal = 0;
	// int instaOnSiteCalib = 0;
	double onSiteCalib = 0;
	double onSiteInst = 0;
	private List<Integer> costsList;
	int alarmingnmonitoring1y = 0;
	int alarmingnmonitoring3y = 0;
	int alarmingnmonitoring5y = 0;
	int recording1y = 0;
	int recording3y = 0;
	int recording5y = 0;
	int annualnistcalibration1y = 0;
	int annualnistcalibration3y = 0;
	int annualnistcalibration5y = 0;
	int temperaturemapping1y = 0;
	int temperaturemapping3y = 0;
	int temperaturemapping5y = 0;

	String email1 = "";
	String email2 = "";
	String email3 = "";
	String email4 = "";
	String email5 = "";
	String email6 = "";
	String emailCc = "";
	String emailBcc = "";
	String emailSubject = "";

	DecimalFormat intFormatter = new DecimalFormat("00000");

	/**************************************************** flags 4 pagination *************************************************/
	private boolean NewQuotationPage;
	private boolean SelectPartsPage;
	private boolean TotalnDiscountPage;
	private boolean DisplaynEditPage;
	private boolean showSummary;

	/**************************************************** Error Flags *************************************************/
	private boolean errorFlag;

	/**************************************************** totaldiscountPage VARIABLES *************************************************/

	public void checkBoxListener(ValueChangeEvent event) {

	}

	/**************************************************** DisplaynEditPage VARIABLES *************************************************/
	private String templateName;
	private String selectedTemplate;
	private Map<String, Integer> templates;
	List<Template> templateFormats;
	boolean visited = false;
	// boolean saveLetterTemplate;

	private String selectedEmailTemplate;
	private Map<String, Integer> emailTemplates;
	List<Template> emailTemplatesFormats;
	private String option = "1";
	private boolean saveLetterTemp;
	private boolean saveEmailTemp;
	private String emailTempName;
	ExternalContext context = FacesContext.getCurrentInstance()
			.getExternalContext();
	HttpSession session = (HttpSession) context.getSession(false);
	UserInfoPageBean bean = (UserInfoPageBean) session.getAttribute("userInfo");
	private String quotationcoveringletter = "";
	private String quotationCoverUpperText = "";
	private String quotationCoverLowerText = "";
	private String summariesFirstPage = "";
	private String summariesSecondPage = "";
	private String summariesThirdPage = "";
	private String summariesFirstImage = "";
	private String summariesSecondImage = "";
	private boolean autoSaving = false;
	private boolean updateInstallation=false;
	private List<String> remoteMonitoring;
	private List<String> annualCalibration;
	String tempRemoteMonitoring = "";
	String tempAnnualCalibration = "";
//	private boolean notUpdateInstallation=false;
	/**************************************************** FacesMessage VARIABLES *************************************************/
	private static final String SUCCESS_MESSAGE = "Successfully saved the template format";
	private static final String FAILURE_MESSAGE = "Error: Was not able to save template format, try again";

	/********************************************** flag to save quotation on each step **********************************/
	private int savedQuotationId;

	public QuotationCreationPageBean() {
		this.creatQuotation();

	}

	public boolean isAutoSaving() {
		return autoSaving;
	}

	public void setAutoSaving(boolean autoSaving) {
		this.autoSaving = autoSaving;
	}

	public boolean isUpdateInstallation() {
		return updateInstallation;
	}

	public void setUpdateInstallation(boolean updateInstallation) {
		this.updateInstallation = updateInstallation;
	}

//	public boolean isNotUpdateInstallation() {
//		return notUpdateInstallation;
//	}
//
//	public void setNotUpdateInstallation(boolean notUpdateInstallation) {
//		this.notUpdateInstallation = notUpdateInstallation;
//	}

	public void showAutoSaving() {
		this.autoSaving = true;
	}

	public void clearNewQuotationPage() {
//		quotation = new Quotation();
		this.quotation.setCustomername("");
		this.quotation.setContactname("");
		this.quotation.setCustomersalutation("");
		this.quotation.setDepartment("");
		this.quotation.setAddress1("");
		this.quotation.setAddress2("");
		this.quotation.setAddress3("");
		this.quotation.setTowncity("");
		this.quotation.setCountrystate("");
		this.quotation.setZippincode("");
		this.quotation.setProjectname("");
		this.quotation.setContacttelno("");
		this.quotation.setContactemail("");
		this.quotation.setUpdateInstallation(false);
		this.quotation.setRecordingonly(false);
		this.quotation.setCombinedrnamonitoring(false);
		this.quotation.setTemperatureMapping(false);
		this.quotation.setOnsitesensorcalibration(false);
		this.quotation.setFactorysensorcalibration(false);
		this.quotation.setDqiqoqprotocoldocs(false);
		this.quotation.setWebsitedbsetup(false);
		this.quotation.setWebhostedgui(false);
		this.quotation.setYearlyrecalibrationservice(false);
		this.quotation.setFinancedhdoption(false);
		
	/*	ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		if (userInfo.getUser().getUsername().compareToIgnoreCase("akhurram@nextcontrols.com") != 0) {
			this.quotation.setRevisionnumber(QuotationDAO.getInstance()
					.getMaxQuotationID(userInfo.getUser().getUserId()));
			this.quotation.setQuotationref("QT"
					+ userInfo.getUser().getFirstName().toUpperCase().charAt(0)
					+ userInfo.getUser().getLastName().toUpperCase().charAt(0)
					+ userInfo.getUser().getCompany().getCompanyRef()
					+ intFormatter.format(this.quotation.getRevisionnumber()));
		} else {
			this.quotation.setRevisionnumber(QuotationDAO.getInstance()
					.getMaxQuotationID(userInfo.getUser().getUserId()));
			this.quotation.setQuotationref("TESTING");
		}*/
		RequestContext.getCurrentInstance().reset(":frmnewquotationPage"); 
	}

	public void creatQuotation() {
		quotation = new Quotation();
		standardPartsCollection = new ArrayList<List<StandardParts>>();
		siteNames = new ArrayList<String>();
		siteInstallation = new ArrayList<String>();
		installationTravel = new ArrayList<String>();
		siteCalibration = new ArrayList<String>();
		calibartionTravel = new ArrayList<String>();
		annualSiteCalibration = new ArrayList<String>();
		annualDeadTravelDays = new ArrayList<String>();
		remoteMonitoring=new ArrayList<String>();
		annualCalibration=new ArrayList<String>();

		totalSensorCount = 0;
		totalsystembreakdown = 0;
		onceServicestotal = 0;
		// instaOnSiteCalib = 0;
		onSiteCalib = 0;
		onSiteInst = 0;
		costsList = new ArrayList<Integer>();
		alarmingnmonitoring1y = 0;
		alarmingnmonitoring3y = 0;
		alarmingnmonitoring5y = 0;
		recording1y = 0;
		recording3y = 0;
		recording5y = 0;
		annualnistcalibration1y = 0;
		annualnistcalibration3y = 0;
		annualnistcalibration5y = 0;
		temperaturemapping1y = 0;
		temperaturemapping3y = 0;
		temperaturemapping5y = 0;

		NewQuotationPage = false;
		SelectPartsPage = false;
		TotalnDiscountPage = false;
		DisplaynEditPage = false;
		visited = false;
		updateInstallation=false;
//		notUpdateInstallation=false;
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		this.currency = userInfo.getUser().getCompany().getCountry()
				.getCurrency();
		if (userInfo.getUser().getUsername().compareToIgnoreCase("akhurram@nextcontrols.com") != 0) {
			this.quotation.setRevisionnumber(QuotationDAO.getInstance()
					.getMaxQuotationID(userInfo.getUser().getUserId()));
			this.quotation.setQuotationref("QT"
					+ userInfo.getUser().getFirstName().toUpperCase().charAt(0)
					+ userInfo.getUser().getLastName().toUpperCase().charAt(0)
					+ userInfo.getUser().getCompany().getCompanyRef()
					+ intFormatter.format(this.quotation.getRevisionnumber()));
		} else {
			this.quotation.setRevisionnumber(QuotationDAO.getInstance()
					.getMaxQuotationID(userInfo.getUser().getUserId()));
			this.quotation.setQuotationref("TESTING");
		}
		this.emailSubject = "Quotation Ref : "
				+ this.quotation.getQuotationref();
	}

	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public void updatenumberofsites() {

	}

	public List<StandardParts> getTempStParts() {
		return standardPartsCollection.get(this.currentPageCounter);// PartsDAO.getInstance().getStandardPartsAndAssembliesList();
	}

	public void setTempStParts(List<StandardParts> tempStParts) {
		 this.tempStParts = tempStParts;
	}

	public String getTempRemoteMonitoring() {
		return remoteMonitoring.get(this.currentPageCounter);
	}

	public void setTempRemoteMonitoring(String tempRemoteMonitoring) {
//		this.tempRemoteMonitoring = tempRemoteMonitoring;
//		System.out.println("temp remote monitoring"+tempRemoteMonitoring);
		remoteMonitoring.remove(this.currentPageCounter);
		remoteMonitoring.add(this.currentPageCounter,tempRemoteMonitoring);
	}

	public String getTempAnnualCalibration() {
		return annualCalibration.get(this.currentPageCounter);
	}

	public void setTempAnnualCalibration(String tempAnnualCalibration) {
//		this.tempAnnualCalibration = tempAnnualCalibration;
		annualCalibration.remove(this.currentPageCounter);
		annualCalibration.add(this.currentPageCounter, tempAnnualCalibration);
	}

	public String getTempSiteName() {
		return siteNames.get(this.currentPageCounter);
	}

	public void setTempSiteName(String tempSiteName) {
		siteNames.remove(this.currentPageCounter);
		siteNames.add(this.currentPageCounter, tempSiteName);
	}

	public String getTempSiteInstallation() {
		calculateInstallationTime();
//		System.out.println("new installation value: "+siteInstallation.get(this.currentPageCounter));
		return siteInstallation.get(this.currentPageCounter);
	}
	public void calculateInstallationTime(){
		double totalTime=0;
		double totalTimeInHours=0;
		double totalRoundedValue=0;
		double totalInstallationTime=0;
		float hours=SettingsDAO.getInstance().getHoursInDay();
		if(hours==0){
			hours=8;
		}
		float hoursInHalf=hours/2;
//		boolean anyPartSelected=false;
//		System.out.println("calculating");
		if(getTempStParts()!=null){
			for(StandardParts part:getTempStParts()){
				if(part.getQuoteQuantity()>0){
//					anyPartSelected=true;
					totalTime+=(part.getInstallationTime()*part.getQuoteQuantity());
				}
			}
//			System.out.println("calculated value: "+totalTime);
			totalTimeInHours=totalTime/60;
//			System.out.println("calculated value after divide: "+totalTimeInHours);
//			if(totalTimeInHours<1 && anyPartSelected)
//				totalTimeInHours=1;
			totalRoundedValue=hoursInHalf*Math.ceil(totalTimeInHours/ hoursInHalf);
//			System.out.println("calculated value after round: "+totalRoundedValue);
			totalInstallationTime=((double)totalRoundedValue)/hours;
//			System.out.println("calculated value after: "+totalInstallationTime);
		}
//		else
//			System.out.println("list is null");
		siteInstallation.remove(this.currentPageCounter);
		siteInstallation.add(this.currentPageCounter, ""+totalInstallationTime);
		
	}
//	int roundUp(int n, int m) {
//	    return n >= 0 ? ((n + m - 1) / m) * m : (n / m) * m;
//	}
	public void setTempSiteInstallation(String tempSiteInstallation) {
		siteInstallation.remove(this.currentPageCounter);
		siteInstallation.add(this.currentPageCounter, tempSiteInstallation);
	}

	public String getTempInstallationTravel() {
		return installationTravel.get(this.currentPageCounter);
	}

	public void setTempInstallationTravel(String tempInstallationTravel) {
		installationTravel.remove(this.currentPageCounter);
		installationTravel.add(this.currentPageCounter, tempInstallationTravel);
	}

	public String getTempSiteCalibration() {
		siteCalibration.remove(this.currentPageCounter);
		siteCalibration.add(this.currentPageCounter, ""+calculateCalibrationTime());
		return siteCalibration.get(this.currentPageCounter);
	}
	public double calculateCalibrationTime(){
		
		double totalTime=0;
		double totalTimeInHours=0;
		double totalRoundedValue=0;
		double totalCalibrationTime=0;
		boolean anyPartSelected=false;
		float hours=SettingsDAO.getInstance().getHoursInDay();
		if(hours==0){
			hours=8;
		}
		float hoursInHalf=hours/2;
		
		
//		System.out.println("calculating");
		if(getTempStParts()!=null){
			for(StandardParts part:getTempStParts()){
				if(part.getQuoteQuantity()>0){
					anyPartSelected=true;
					totalTime+=(part.getCalibrationTime()*part.getQuoteQuantity());
				}
			}
//			System.out.println("calculated value: "+totalTime);
			totalTimeInHours=totalTime/60;
//			System.out.println("calculated value after divide: "+totalTimeInHours);
//			if(totalTimeInHours<1 && anyPartSelected)
//				totalTimeInHours=1;
			totalRoundedValue=hoursInHalf*Math.ceil(totalTimeInHours/ hoursInHalf);
//			System.out.println("calculated value after round: "+totalRoundedValue);
			totalCalibrationTime=((double)totalRoundedValue)/hours;
//			System.out.println("calculated value after: "+totalCalibrationTime);
		}
		return totalCalibrationTime;
		
		
	}
	public void setTempSiteCalibration(String tempSiteCalibration) {
		siteCalibration.remove(this.currentPageCounter);
		siteCalibration.add(this.currentPageCounter, tempSiteCalibration);
	}

	public String getTempCalibartionTravel() {
		return calibartionTravel.get(this.currentPageCounter);
	}

	public void setTempCalibartionTravel(String tempCalibartionTravel) {
		calibartionTravel.remove(this.currentPageCounter);
		calibartionTravel.add(this.currentPageCounter, tempCalibartionTravel);
	}

	public String getTempAnnualSiteCalibration() {
		annualSiteCalibration.remove(this.currentPageCounter);
		annualSiteCalibration.add(this.currentPageCounter, ""+calculateCalibrationTime());
		return annualSiteCalibration.get(this.currentPageCounter);
	}

	public void setTempAnnualSiteCalibration(String tempAnnualSiteCalibration) {
		annualSiteCalibration.remove(this.currentPageCounter);
		annualSiteCalibration.add(this.currentPageCounter,
				tempAnnualSiteCalibration);
	}

	public String getTempAnnualDeadTravelDays() {
		return annualDeadTravelDays.get(this.currentPageCounter);
	}

	public void setTempAnnualDeadTravelDays(String tempAnnualDeadTravelDays) {
		annualDeadTravelDays.remove(this.currentPageCounter);
		annualDeadTravelDays.add(this.currentPageCounter,
				tempAnnualDeadTravelDays);
	}

	public double getmHardwareDiscount() {
		return mHardwareDiscount;
	}

	public void setmHardwareDiscount(double mHardwareDiscount) {
		this.mHardwareDiscount = mHardwareDiscount;
	}

	public double getmRemoteMonitoringDiscount() {
		return mRemoteMonitoringDiscount;
	}

	public void setmRemoteMonitoringDiscount(double mRemoteMonitoringDiscount) {
		this.mRemoteMonitoringDiscount = mRemoteMonitoringDiscount;
	}

	public double getmSiteInstallationDiscount() {
		return mSiteInstallationDiscount;
	}

	public void setmSiteInstallationDiscount(double mSiteInstallationDiscount) {
		this.mSiteInstallationDiscount = mSiteInstallationDiscount;
	}

	public double getmServiceOptionsDiscount() {
		return mServiceOptionsDiscount;
	}

	public void setmServiceOptionsDiscount(double mServiceOptionsDiscount) {
		this.mServiceOptionsDiscount = mServiceOptionsDiscount;
	}

	public List<PriceCategory> getPriceCategoriesList() {
		return priceCategoriesList;
	}

	public void setPriceCategoriesList(List<PriceCategory> priceCategoriesList) {
		this.priceCategoriesList = priceCategoriesList;
	}

	public List<QuantityCategory> getQuantityCategoriesList() {
		return quantityCategoriesList;
	}

	public void setQuantityCategoriesList(
			List<QuantityCategory> quantityCategoriesList) {
		this.quantityCategoriesList = quantityCategoriesList;
	}

	public List<OnceService> getOnceservicesList() {
		return onceservicesList;
	}

	public void setOnceservicesList(List<OnceService> onceservicesList) {
		this.onceservicesList = onceservicesList;
	}

	public int getTotalSensorCount() {
		return totalSensorCount;
	}

	public void setTotalSensorCount(int totalSensorCount) {
		this.totalSensorCount = totalSensorCount;
	}

	public int getTotalsystembreakdown() {
		return totalsystembreakdown;
	}

	public void setTotalsystembreakdown(int totalsystembreakdown) {
		this.totalsystembreakdown = totalsystembreakdown;
	}

	public int getOnceServicestotal() {
		return onceServicestotal;
	}

	public void setOnceServicestotal(int onceServicestotal) {
		this.onceServicestotal = onceServicestotal;
	}

	/*
	 * public int getInstaOnSiteCalib() { return instaOnSiteCalib; } public void
	 * setInstaOnSiteCalib(int instaOnSiteCalib) { this.instaOnSiteCalib =
	 * instaOnSiteCalib; }
	 */

	public List<Integer> getCostsList() {
		return costsList;
	}

	public double getOnSiteCalib() {
		return onSiteCalib;
	}

	public void setOnSiteCalib(double onSiteCalib) {
		this.onSiteCalib = onSiteCalib;
	}

	public double getOnSiteInst() {
		return onSiteInst;
	}

	public void setOnSiteInst(double onSiteInst) {
		this.onSiteInst = onSiteInst;
	}

	public void setCostsList(List<Integer> costsList) {
		this.costsList = costsList;
	}

	public int getAlarmingnmonitoring1y() {
		return alarmingnmonitoring1y;
	}

	public void setAlarmingnmonitoring1y(int alarmingnmonitoring1y) {
		this.alarmingnmonitoring1y = alarmingnmonitoring1y;
	}

	public int getAlarmingnmonitoring3y() {
		return alarmingnmonitoring3y;
	}

	public void setAlarmingnmonitoring3y(int alarmingnmonitoring3y) {
		this.alarmingnmonitoring3y = alarmingnmonitoring3y;
	}

	public int getAlarmingnmonitoring5y() {
		return alarmingnmonitoring5y;
	}

	public void setAlarmingnmonitoring5y(int alarmingnmonitoring5y) {
		this.alarmingnmonitoring5y = alarmingnmonitoring5y;
	}

	public int getRecording1y() {
		return recording1y;
	}

	public void setRecording1y(int recording1y) {
		this.recording1y = recording1y;
	}

	public int getRecording3y() {
		return recording3y;
	}

	public void setRecording3y(int recording3y) {
		this.recording3y = recording3y;
	}

	public int getRecording5y() {
		return recording5y;
	}

	public void setRecording5y(int recording5y) {
		this.recording5y = recording5y;
	}

	public int getAnnualnistcalibration1y() {
		return annualnistcalibration1y;
	}

	public void setAnnualnistcalibration1y(int annualnistcalibration1y) {
		this.annualnistcalibration1y = annualnistcalibration1y;
	}

	public int getAnnualnistcalibration3y() {
		return annualnistcalibration3y;
	}

	public void setAnnualnistcalibration3y(int annualnistcalibration3y) {
		this.annualnistcalibration3y = annualnistcalibration3y;
	}

	public int getAnnualnistcalibration5y() {
		return annualnistcalibration5y;
	}

	public void setAnnualnistcalibration5y(int annualnistcalibration5y) {
		this.annualnistcalibration5y = annualnistcalibration5y;
	}

	public int getTemperaturemapping1y() {
		return temperaturemapping1y;
	}

	public void setTemperaturemapping1y(int temperaturemapping1y) {
		this.temperaturemapping1y = temperaturemapping1y;
	}

	public int getTemperaturemapping3y() {
		return temperaturemapping3y;
	}

	public void setTemperaturemapping3y(int temperaturemapping3y) {
		this.temperaturemapping3y = temperaturemapping3y;
	}

	public int getTemperaturemapping5y() {
		return temperaturemapping5y;
	}

	public void setTemperaturemapping5y(int temperaturemapping5y) {
		this.temperaturemapping5y = temperaturemapping5y;
	}

	public String getEmail1() {
		return email1;
	}

	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	public String getEmail2() {
		return email2;
	}

	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	public String getEmail3() {
		return email3;
	}

	public void setEmail3(String email3) {
		this.email3 = email3;
	}

	public String getEmail4() {
		return email4;
	}

	public void setEmail4(String email4) {
		this.email4 = email4;
	}

	public String getEmail5() {
		return email5;
	}

	public void setEmail5(String email5) {
		this.email5 = email5;
	}

	public String getEmail6() {
		return email6;
	}

	public void setEmail6(String email6) {
		this.email6 = email6;
	}

	public List<StandardParts> getSelectedStandardPartsSummary() {
		return selectedStandardPartsSummary;
	}

	public void setSelectedStandardPartsSummary(
			List<StandardParts> selectedStandardPartsSummary) {
		//System.out.println("set called");
		this.selectedStandardPartsSummary = selectedStandardPartsSummary;
	}

	/**************************************************** NewQuotationsPage *************************************************/

	public String newQuotationPageNext() {
//		if(!updateInstallation && !notUpdateInstallation){
//			System.out.println("point1");
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//					"Please select existing installation or not!", "Please select existing installation or not!");
//			System.out.println("point2");
//			FacesContext.getCurrentInstance().addMessage(null, message);
//			System.out.println("point3"+FacesContext.getCurrentInstance().getMessageList().size());
//			RequestContext.getCurrentInstance().update(
//					":valMsgs");
//			System.out.println("point4");
//			return "NewQuotationPage.xhtml?faces-redirect=true";
////			return "";
//		}
		UserInfoPageBean userInfo;
		if (!NewQuotationPage) {
			NewQuotationPage = true;
			standardPartsCollection.clear();
			siteNames.clear();
			siteInstallation.clear();
			installationTravel.clear();
			siteCalibration.clear();
			calibartionTravel.clear();
			annualSiteCalibration.clear();
			annualDeadTravelDays.clear();
			remoteMonitoring.clear();
			annualCalibration.clear();
			totalSensorCount = 0;
			totalsystembreakdown = 0;
			onceServicestotal = 0;
			// instaOnSiteCalib = 0;
			onSiteCalib = 0;
			onSiteInst = 0;
			costsList = new ArrayList<Integer>();
			alarmingnmonitoring1y = 0;
			alarmingnmonitoring3y = 0;
			alarmingnmonitoring5y = 0;
			recording1y = 0;
			recording3y = 0;
			recording5y = 0;
			annualnistcalibration1y = 0;
			annualnistcalibration3y = 0;
			annualnistcalibration5y = 0;
			temperaturemapping1y = 0;
			temperaturemapping3y = 0;
			temperaturemapping5y = 0;
			savedQuotationId = 0;

			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			userInfo = (UserInfoPageBean) session.getAttribute("userInfo");
			if (quotation.getIndividualquotepersite()) {
				for (int i = 0; i < quotation.getNumberofsites(); i++) {
					standardPartsCollection.add(PartsDAO.getInstance()
							.getStandardPartsAndAssembliesList(
									userInfo.getUser().getCompany()
											.getCountry().getFreight(),
									userInfo.getUser().getCompany()
											.getCountry().getDuty(),
									userInfo.getUser().getCompany()
											.getCountry()
											.getConversionratefrompounds(),
									userInfo.getUser().getCompany()
											.getCountry().getCountry()));
					siteNames.add("Site " + i);
					siteInstallation.add("" + 0);
					installationTravel.add("" + 0);
					siteCalibration.add("" + 0);
					calibartionTravel.add("" + 0);
					annualSiteCalibration.add("" + 0);
					annualDeadTravelDays.add("" + 0);
					remoteMonitoring.add("" + 0);
					annualCalibration.add(""+0);
				}
			} else {
				standardPartsCollection.add(PartsDAO.getInstance()
						.getStandardPartsAndAssembliesList(
								userInfo.getUser().getCompany().getCountry()
										.getFreight(),
								userInfo.getUser().getCompany().getCountry()
										.getDuty(),
								userInfo.getUser().getCompany().getCountry()
										.getConversionratefrompounds(),
								userInfo.getUser().getCompany().getCountry()
										.getCountry()));
				siteNames.add("Site " + 0);
				siteInstallation.add("" + 0);
				installationTravel.add("" + 0);
				siteCalibration.add("" + 0);
				calibartionTravel.add("" + 0);
				annualSiteCalibration.add("" + 0);
				annualDeadTravelDays.add("" + 0);
				remoteMonitoring.add("" + 0);
				annualCalibration.add(""+0);
			}

		} else {
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			userInfo = (UserInfoPageBean) session.getAttribute("userInfo");
			if (!quotation.getIndividualquotepersite()) {
				// standardPartsCollection.clear();
				// siteNames.clear();
				// siteInstallation.clear();
				// installationTravel.clear();
				// siteCalibration.clear();
				// calibartionTravel.clear();
				// standardPartsCollection.add(PartsDAO.getInstance().getStandardPartsAndAssembliesList(userInfo.getUser().getCompany().getCountry().getFreight(),
				// userInfo.getUser().getCompany().getCountry().getDuty(),userInfo.getUser().getCompany().getCountry().getConversionratefrompounds(),userInfo.getUser().getCompany().getCountry().getCountry()));
				// siteNames.add("Site "+0);
				// siteInstallation.add(""+0);
				// installationTravel.add(""+0);
				// siteCalibration.add(""+0);
				// calibartionTravel.add(""+0);
			} else {
				if (quotation.getNumberofsites() >= standardPartsCollection
						.size()) {
					for (int i = standardPartsCollection.size(); i < quotation
							.getNumberofsites(); i++) {
						standardPartsCollection.add(PartsDAO.getInstance()
								.getStandardPartsAndAssembliesList(
										userInfo.getUser().getCompany()
												.getCountry().getFreight(),
										userInfo.getUser().getCompany()
												.getCountry().getDuty(),
										userInfo.getUser().getCompany()
												.getCountry()
												.getConversionratefrompounds(),
										userInfo.getUser().getCompany()
												.getCountry().getCountry()));
						siteNames.add("Site " + i);
						siteInstallation.add("" + 0);
						installationTravel.add("" + 0);
						siteCalibration.add("" + 0);
						calibartionTravel.add("" + 0);
						annualSiteCalibration.add("" + 0);
						annualDeadTravelDays.add("" + 0);
						remoteMonitoring.add("" + 0);
						annualCalibration.add(""+0);
					}
				}
			}
		}
		String email = this.quotation.getContactemail();
		PDFExporterFromHTMLPageBean pdfExporter = new PDFExporterFromHTMLPageBean();
		this.quotation.setUser(userInfo.getUser());
//		this.quotation.setUpdateInstallation(this.updateInstallation);
		savedQuotationId = QuotationDAO.getInstance().saveUpdateQuote(
				savedQuotationId, this.getQuotation(),
				pdfExporter.getContents(), email, Boolean.FALSE);
//		this.setEmail1(this.quotation.getContactemail());
	
		return "SelectPartsPage.xhtml?faces-redirect=true";
	}

	public void saveQuotatonFirstPage() {
		String email = this.quotation.getContactemail();
		PDFExporterFromHTMLPageBean pdfExporter = new PDFExporterFromHTMLPageBean();

		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");

		this.quotation.setUser(userInfo.getUser());
		this.savedQuotationId = QuotationDAO.getInstance().saveUpdateQuote(
				this.savedQuotationId, this.getQuotation(),
				pdfExporter.getContents(), email, Boolean.FALSE);

	}

	public String selectPartsPageNext() {
		// NewQuotationPage = false;
		// SelectPartsPage = true;
		this.currentPageCounter = currentPageCounter + 1;
		if (quotation.getIndividualquotepersite()) {
			if (this.currentPageCounter < quotation.getNumberofsites()) {
				return "SelectPartsPage.xhtml?faces-redirect=true";
			} else {
				return "TotalnDiscountPage.xhtml?faces-redirect=true";
			}
		} else {
			return "TotalnDiscountPage.xhtml?faces-redirect=true";
		}
	}

	public String selectPartsPageBack() {
		//System.out.println("selectback " + this.currentPageCounter);
		if (this.currentPageCounter > 0) {
			this.currentPageCounter = currentPageCounter - 1;
			return "SelectPartsPage.xhtml?faces-redirect=true";
		} else {
			return "NewQuotationPage.xhtml?faces-redirect=true";
		}

	}

	public void saveQuotationPartsPage() {
		this.quotation.setRemoteMonitoring(Double.valueOf(remoteMonitoring.get(this.currentPageCounter)));
		this.quotation.setAnnualCalibration(Double.valueOf(annualCalibration.get(this.currentPageCounter)));
		String email = this.quotation.getContactemail();
		PDFExporterFromHTMLPageBean pdfExporter = new PDFExporterFromHTMLPageBean();
		QuotationDAO.getInstance().saveUpdateQuote(savedQuotationId,
				this.getQuotation(), pdfExporter.getContents(), email,
				Boolean.FALSE);
		
		QuotationDAO.getInstance().saveParts(savedQuotationId,
				standardPartsCollection, siteNames, siteInstallation,
				installationTravel, siteCalibration, calibartionTravel,
				annualSiteCalibration, annualDeadTravelDays);
	}

	public String tosummariesPage() {
		this.quotation.setRemoteMonitoring(Double.valueOf(remoteMonitoring.get(this.currentPageCounter)));
		this.quotation.setAnnualCalibration(Double.valueOf(annualCalibration.get(this.currentPageCounter)));
		String email = this.quotation.getContactemail();
		PDFExporterFromHTMLPageBean pdfExporter = new PDFExporterFromHTMLPageBean();
		QuotationDAO.getInstance().saveUpdateQuote(savedQuotationId,
				this.getQuotation(), pdfExporter.getContents(), email,
				Boolean.FALSE);
		
		
		
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		QuotationDAO.getInstance().saveParts(savedQuotationId,
				standardPartsCollection, siteNames, siteInstallation,
				installationTravel, siteCalibration, calibartionTravel,
				annualSiteCalibration, annualDeadTravelDays);

		this.quantityCategoriesList = QuantityCategoryDAO.getInstance()
				.getQuantityCategoryListWithoutNuLL();
		this.priceCategoriesList = PriceCategoryDAO.getInstance()
				.getPriceCategoryListWithoutNuLL();
		/*for (PriceCategory cat : this.priceCategoriesList) {
			System.out.println(cat.getDescription());
		}*/
		this.onceservicesList = OnceServiceDAO.getInstance()
				.getOnceServicesList(
						userInfo.getUser().getCompany().getCountry()
								.getCountry(), quotation);

		totalSensorCount = 0;
		totalsystembreakdown = 0;
		onceServicestotal = 0;
		// onSiteCalib = 0;
		// onSiteInst = 0;
		// instaOnSiteCalib = 0;
		costsList = new ArrayList<Integer>();
		alarmingnmonitoring1y = 0;
		alarmingnmonitoring3y = 0;
		alarmingnmonitoring5y = 0;
		recording1y = 0;
		recording3y = 0;
		recording5y = 0;
		annualnistcalibration1y = 0;
		annualnistcalibration3y = 0;
		annualnistcalibration5y = 0;
		temperaturemapping1y = 0;
		temperaturemapping3y = 0;
		temperaturemapping5y = 0;

		for (int i = 0; i < standardPartsCollection.size(); i++) {
			List<StandardParts> tempStList = standardPartsCollection.get(0);
			for (StandardParts st : tempStList) {
				for (int count = 0; count < this.quantityCategoriesList.size(); count++) {
					if (/*
						 * st.getPartType().compareToIgnoreCase("assembly")==0
						 * &&
						 */st.getQuoteQuantity() > 0
							&& this.quantityCategoriesList.get(count).getName()
									.compareToIgnoreCase("NULL") != 0
							&& this.quantityCategoriesList
									.get(count)
									.getName()
									.compareToIgnoreCase(
											st.getQuantitycategory()) == 0) {
						this.quantityCategoriesList.get(count).addQuantitytype(
								st.getQuoteQuantity(), st.getQuantityfactor());
						if (st.getQuantitycategory().compareToIgnoreCase("13") != 0) {
							totalSensorCount += st.getQuoteQuantity()
									* st.getQuantityfactor();
						}
					}
				}
				for (int count = 0; count < this.priceCategoriesList.size(); count++) {
					if (/*
						 * st.getPartType().compareToIgnoreCase("assembly")==0
						 * &&
						 */st.getQuoteQuantity() > 0
							&& this.priceCategoriesList.get(count).getName()
									.compareToIgnoreCase("NULL") != 0
							&& this.priceCategoriesList.get(count).getName()
									.compareToIgnoreCase(st.getPricecategory()) == 0) {
						this.priceCategoriesList.get(count).addQuantitytype(
								st.getQuoteQuantity(), st.getListPrice());
						totalsystembreakdown += st.getQuoteQuantity()
								* st.getListPrice();
					}
				}
				/*
				 * if(st.getPartType().compareToIgnoreCase("NonStandard Part")==0
				 * ){ this.priceCategoriesList.get(count).addQuantitytype(st.
				 * getQuoteQuantity() , st.getListPrice());
				 * 
				 * }
				 */
			}
		}

		for (int i = 0; i < onceservicesList.size(); i++) {
			if (onceservicesList.get(i).isShowCost()) {
				if ((onceservicesList.get(i).getUnitCost() * totalSensorCount) < onceservicesList
						.get(i).getMinimumCharge()) {
					if (totalSensorCount == 0) {
						onceServicestotal += 0;
					} else {
						onceServicestotal += onceservicesList.get(i)
								.getMinimumCharge();
					}
				} else {
					onceServicestotal += onceservicesList.get(i).getUnitCost()
							* totalSensorCount;
				}
			}
		}
		List<StandardParts> partsList = this.standardPartsCollection.get(0);
		this.selectedStandardPartsSummary = new ArrayList<StandardParts>();

		for (StandardParts part : partsList) {
			if (part.getQuoteQuantity() > 0) {
				this.selectedStandardPartsSummary.add(part);
			}
		}
		Map<String, Integer> rates = DayDAO.getInstance().getRates(
				userInfo.getUser().getCompany().getCountry().getCountry());
		onSiteCalib = 0;
		onSiteInst = 0;
		for (String key : rates.keySet()) {
			if (key.compareToIgnoreCase("Calibration Travel") == 0
					&& quotation.getOnsitesensorcalibration()) {
				for (String str : calibartionTravel) {
					onSiteCalib += Double.parseDouble(str) * rates.get(key);
//					System.out.println("calibration travel days: "+str);
//					System.out.println("calibration travel rate: "+rates.get(key));
//					System.out.println("calibration travel cost: "+onSiteCalib);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
				}
			} else if (key.compareToIgnoreCase("Sensor Calibration") == 0
					&& quotation.getOnsitesensorcalibration()) {
				double totalTime=0;
				double totalTimeInHours=0;
				double totalRoundedValue=0;
				double totalCalibrationTime=0;
//				
				if(selectedStandardPartsSummary!=null){
//					System.out.println("calculating");	
					for(StandardParts part:selectedStandardPartsSummary){
//						System.out.println("calculating2");
						if(part.getQuoteQuantity()>0){
//							System.out.println("calibration time: "+part.getCalibrationTime());
//							System.out.println("quote quantitiy: "+part.getQuoteQuantity());
							totalTime+=(part.getCalibrationTime()*part.getQuoteQuantity());
						}
					}
//					System.out.println("calculated value: "+totalTime);
					totalTimeInHours=totalTime/60;
//					System.out.println("calculated value after divide: "+totalTimeInHours);
//					if(totalTimeInHours<1 && anyPartSelected)
//						totalTimeInHours=1;
					totalRoundedValue=4*Math.ceil(totalTimeInHours/ 4);
//					System.out.println("calculated value after round: "+totalRoundedValue);
					totalCalibrationTime=((double)totalRoundedValue)/8;
//					System.out.println("calculated value after: "+totalCalibrationTime);
				}
//				for (String str : siteCalibration) {
//					onSiteCalib += Double.parseDouble(str) * rates.get(key);
					onSiteCalib += totalCalibrationTime* rates.get(key);
//					System.out.println("calibration days: "+str);
//					System.out.println("calibration rate: "+rates.get(key));
//					System.out.println("calibration cost: "+onSiteCalib);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
//				}
			} else if (key.compareToIgnoreCase("Installation Travel") == 0) {
				for (String str : installationTravel) {
					onSiteInst += Double.parseDouble(str) * rates.get(key);
//					System.out.println("installation travel days: "+str);
//					System.out.println("installation travel rate: "+rates.get(key));
//					System.out.println("installation  travel cost: "+onSiteInst);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
				}
			} else if (key.compareToIgnoreCase("System Installation") == 0) {
				double totalTime=0;
				double totalTimeInHours=0;
				double totalRoundedValue=0;
				double totalInstallationTime=0;
//				
				if(selectedStandardPartsSummary!=null){
//					System.out.println("calculating");	
					for(StandardParts part:selectedStandardPartsSummary){
//						System.out.println("calculating2");
						if(part.getQuoteQuantity()>0){
//							System.out.println("installation time: "+part.getInstallationTime());
//							System.out.println("quote quantitiy: "+part.getQuoteQuantity());
							totalTime+=(part.getInstallationTime()*part.getQuoteQuantity());
						}
					}
//					System.out.println("calculated value: "+totalTime);
					totalTimeInHours=totalTime/60;
//					System.out.println("calculated value after divide: "+totalTimeInHours);
//					if(totalTimeInHours<1 && anyPartSelected)
//						totalTimeInHours=1;
					totalRoundedValue=4*Math.ceil(totalTimeInHours/ 4);
//					System.out.println("calculated value after round: "+totalRoundedValue);
					totalInstallationTime=((double)totalRoundedValue)/8;
//					System.out.println("calculated value after: "+totalInstallationTime);
				}

				
				
//				for (String str : siteInstallation) {

					
//					onSiteInst += Double.parseDouble(str) * rates.get(key);
				onSiteInst += (totalInstallationTime * rates.get(key));
//					System.out.println("installation days: "+totalInstallationTime);
//					System.out.println("installation rate: "+rates.get(key));
//					System.out.println("installation cost: "+onSiteInst);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
//				}
			}
		}

		costsList = MonitoringServiceDAO.getInstance()
				.getMonitoringServicesList(
						userInfo.getUser().getCompany().getCountry()
								.getCountry(), totalSensorCount);
		if (costsList != null && costsList.size() > 0) {
			alarmingnmonitoring1y = costsList.get(0);
			alarmingnmonitoring3y = costsList.get(1);
			alarmingnmonitoring5y = costsList.get(2);
			recording1y = costsList.get(3);
			recording3y = costsList.get(4);
			recording5y = costsList.get(5);
		}
		int tempAnnualSiteCalibrationCost = 0;
		List<AnnualRate> annualrates = AnnualRateDAO.getInstance()
				.getAnnualRatesForQCreation(
						userInfo.getUser().getCompany().getCountry()
								.getCountry());
		AnnualRate tempAnnualCalibration = null;
		AnnualRate tempAnnualdeadtravelday = null;
		AnnualRate tempAnnualTemperatureMapping = null;
		AnnualRate tempDefaultAnnualCalibration = null;
		AnnualRate tempDefaultAnnualDeadTravelDay = null;
		AnnualRate tempDefaultAnnualTemperatureMapping = null;
		for (AnnualRate temp : annualrates) {
			if (temp.getDescription().compareToIgnoreCase(
					"Default Annual Calibration") == 0) {
				tempDefaultAnnualCalibration = temp;
			} else if (temp.getDescription().compareToIgnoreCase(
					"Annual Calibration") == 0) {
				tempAnnualCalibration = temp;
				//System.out.println("temp anuual");
			} else if (temp.getDescription().compareToIgnoreCase(
					"Default Dead Travel") == 0) {
				tempDefaultAnnualDeadTravelDay = temp;
			} else if (temp.getDescription().compareToIgnoreCase("Dead Travel") == 0) {
				tempAnnualdeadtravelday = temp;
			} else if (temp.getDescription().compareToIgnoreCase(
					"Default Temperature Mapping") == 0) {
				tempDefaultAnnualTemperatureMapping = temp;
			} else if (temp.getDescription().compareToIgnoreCase(
					"Temperature Mapping") == 0) {
				tempAnnualTemperatureMapping = temp;
			}
		}
		for (String str : annualSiteCalibration) {
			if (tempAnnualCalibration != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempAnnualCalibration.getCostperday();
				//System.out.println("temp anul site calib: "+str+"::"+tempAnnualCalibration.getCostperday());
			} else if (tempDefaultAnnualCalibration != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempDefaultAnnualCalibration.getCostperday();
				//System.out.println("default temp anul site calib: "+str+"::"+tempDefaultAnnualCalibration.getCostperday());
			}/*
			 * else{ tempAnnualSiteCalibrationCost += Integer.parseInt(str)*
			 * 1500; }
			 */
		}
		for (String str : annualDeadTravelDays) {
			if (tempAnnualdeadtravelday != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempAnnualdeadtravelday.getCostperday();
				//System.out.println("anual dead travel days: "+str+"::"+tempAnnualdeadtravelday.getCostperday());

			} else if (tempDefaultAnnualDeadTravelDay != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempDefaultAnnualDeadTravelDay.getCostperday();
				//System.out.println("default anual dead travel days: "+str+"::"+tempDefaultAnnualDeadTravelDay.getCostperday());
			}/*
			 * else{ tempAnnualSiteCalibrationCost += Integer.parseInt(str)*
			 * 600; }
			 */
		}

		annualnistcalibration5y = tempAnnualSiteCalibrationCost;
		if (tempAnnualCalibration != null) {
			annualnistcalibration3y = (int) (tempAnnualSiteCalibrationCost * tempAnnualCalibration
					.getThreeyearfactor());
			annualnistcalibration1y = (int) (tempAnnualSiteCalibrationCost * tempAnnualCalibration
					.getOneyearfactor());
			//System.out.println("anual rate: "+tempAnnualSiteCalibrationCost+"::"+tempAnnualCalibration.getOneyearfactor());
		} else if (tempDefaultAnnualCalibration != null) {
			annualnistcalibration3y = (int) (tempAnnualSiteCalibrationCost * tempDefaultAnnualCalibration
					.getThreeyearfactor());
			annualnistcalibration1y = (int) (tempAnnualSiteCalibrationCost * tempDefaultAnnualCalibration
					.getOneyearfactor());
			//System.out.println("anual rate: "+tempAnnualSiteCalibrationCost+"::"+tempDefaultAnnualCalibration.getOneyearfactor());

		}/*
		 * else{ annualnistcalibration3y =(int) (tempAnnualSiteCalibrationCost *
		 * 1.1); annualnistcalibration1y =(int) (tempAnnualSiteCalibrationCost *
		 * 1.5); }
		 */
		//System.out.println("ans: "+annualnistcalibration1y);
		if (tempAnnualTemperatureMapping != null) {
			temperaturemapping5y = (int) tempAnnualTemperatureMapping
					.getCostperday();
			temperaturemapping3y = (int) (tempAnnualTemperatureMapping
					.getCostperday() * tempAnnualTemperatureMapping
					.getThreeyearfactor());
			temperaturemapping1y = (int) (tempAnnualTemperatureMapping
					.getCostperday() * tempAnnualTemperatureMapping
					.getOneyearfactor());
		} else if (tempDefaultAnnualTemperatureMapping != null) {
			temperaturemapping5y = (int) tempDefaultAnnualTemperatureMapping
					.getCostperday();
			temperaturemapping3y = (int) (tempDefaultAnnualTemperatureMapping
					.getCostperday() * tempDefaultAnnualTemperatureMapping
					.getThreeyearfactor());
			temperaturemapping1y = (int) (tempDefaultAnnualTemperatureMapping
					.getCostperday() * tempDefaultAnnualTemperatureMapping
					.getOneyearfactor());
		} else if (totalSensorCount == 0) {
			temperaturemapping5y = 0;
			temperaturemapping3y = 0;
			temperaturemapping1y = 0;
		}/*
		 * else { temperaturemapping5y = 3000; temperaturemapping3y =2000;
		 * temperaturemapping1y =1000; }
		 */


		initializeSummariesText();
		return "SummariesPage.xhtml?faces-redirect=true";
	}

	private void initializeSummariesText() {
		summariesFirstPage = "<div><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>YOUR SYSTEM CONFIGURATION </p></div>"
				+ "<table  border='0' width='80%' style='padding-left:30px'>"
				+ "<tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: center;'>ITEM</th>"
				+ "<th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: right;'>QTY</th></tr>";
		summariesFirstPage += "<tr><td >&nbsp;</td><td style='text-align: right;'>&nbsp;</td></tr>";
		for (QuantityCategory qc : this.getQuantityCategoriesList()) {
			summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
					+ qc.getDescription()
					+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ qc.getTempquantity() + "</td></tr>";
		}
		summariesFirstPage += "<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: center;text-decoration:underline;'>TOTAL SENSOR COUNT : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ this.getTotalSensorCount() + "</td></tr>";
		summariesFirstPage += "</table><br/>";

		summariesFirstPage += "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>YOUR CHOSEN SERVICE OPTIONS </p>"
				+ "<table  border='0' width='80%' style='padding-left:30px'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: center;'>ITEM</th>"
				+ "<th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: right;'>CHOICE</th></tr>";
		summariesFirstPage += "<tr><td >&nbsp;</td><td style='text-align: right;'>&nbsp;</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Remote Ethernet Communications Link</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Yes</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>On-Site System Installation</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Yes</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>On-Site NIST Probe Calibration</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (this.getQuotation().getOnsitesensorcalibration() ? "Yes"
						: "No") + "</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Factory NIST Probe Calibration</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (this.getQuotation().getFactorysensorcalibration() ? "Yes"
						: "No") + "</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Remote Alarm Monitoring Service</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (this.getQuotation().getCombinedrnamonitoring() ? "Yes"
						: "No") + "</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Remote Electronic Recording Service</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ ((this.getQuotation().getRecordingonly() || this
						.getQuotation().getCombinedrnamonitoring()) ? "Yes"
						: "No") + "</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Web Hosted Sensor Display Graphics (Web GUI)</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (this.getQuotation().getWebhostedgui() ? "Yes" : "No")
				+ "</td></tr>";
		summariesFirstPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>DQ, IQ and OQ, QC Record Documentation</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (this.getQuotation().getDqiqoqprotocoldocs() ? "Yes" : "No")
				+ "</td></tr>";
		/*
		 * summariesFirstPage+=
		 * "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Refrigerator Temperature Mapping service</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
		 * +(this.getQuotation().getTemperatureMapping() ? "Yes" :
		 * "No")+"</td></tr>";
		 */
		summariesFirstPage += "</table><br/><br/><br/><br/>";
		// ////////////////
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		DecimalFormat formater = (DecimalFormat) NumberFormat
				.getCurrencyInstance(Locale.US);
		DecimalFormatSymbols symbols = formater.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formater.setDecimalFormatSymbols(symbols);
		formater.setMaximumFractionDigits(0);
		String costHeading="SYSTEM COST BREAKDOWN ";
		if(this.quotation.isUpdateInstallation()){
			costHeading="SYSTEM UPGRADE COST";
		}
//		<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
		summariesSecondPage = "<div >"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>"+costHeading+"</p></div>"
				+ "<table  border='0' width='80%' style='padding-left:30px'>"
				+ "<tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 36px; text-decoration:underline;color:#41918A;text-align: left;'>HARDWARE</th>"
				+ "<th >&nbsp;</th></tr>";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("US") == 0) {
				
			
			summariesSecondPage+= "</table><br/>";
			summariesSecondPage+="<table border='1' cellpadding='3' style='border-collapse: collapse;margin-left:50px;'><th>ITEM</th><th>PART NO.</th><th>UNIT COST</th><th>QTY</th><th>TOTAL COST</th>";
			int itemCounter=1;
			for (StandardParts st : standardPartsCollection.get(0)) {
				if(st.getQuoteQuantity()!=0){
				summariesSecondPage+="<tr><td>"+itemCounter+"</td>"
						+ "<td>"+st.getPartNumber()+"</td>"
						+ "<td>"+userInfo.getUser().getCompany().getCountry().getCurrency()+formater.format(st.getListPrice())+"</td>"
						+ "<td>"+st.getQuoteQuantity()+"</td>"
						+ "<td>"+userInfo.getUser().getCompany().getCountry().getCurrency()+formater.format(st.getListPrice()*st.getQuoteQuantity())+"</td>"
						+ "</tr>";
				itemCounter++;
				}
			}
			
		
			// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
			if (this.getmHardwareDiscount() != 0) {
				// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getTotalsystembreakdown())+"</td></tr>";
				summariesSecondPage += "<tr><td></td><td></td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td></td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(this.getTotalsystembreakdown()
								* this.getmHardwareDiscount() / 100) + "</td></tr>";
			}
		
			summariesSecondPage += "<tr><td></td><td></td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td></td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry().getCurrency()
					+ formater.format((this.getTotalsystembreakdown() - (this
							.getTotalsystembreakdown()
							* this.getmHardwareDiscount() / 100))) + "</td></tr>";
			summariesSecondPage+="</table>";
		}else{
			for (PriceCategory pc : this.getPriceCategoriesList()) {
				summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
						+ pc.getDescription()
						+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(pc.getTempquantity()) + "</td></tr>";
			}
			// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
			if (this.getmHardwareDiscount() != 0) {
				// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getTotalsystembreakdown())+"</td></tr>";
				summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(this.getTotalsystembreakdown()
								* this.getmHardwareDiscount() / 100) + "</td></tr>";
			}
			summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry().getCurrency()
					+ formater.format((this.getTotalsystembreakdown() - (this
							.getTotalsystembreakdown()
							* this.getmHardwareDiscount() / 100))) + "</td></tr>";
			summariesSecondPage += "</table><br/>";
		}
		

		summariesSecondPage += "<table  border='0' width='80%' style='padding-left:30px'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: left;'>SERVICE</th>"
				+ "<th >&nbsp;</th></tr>";

		for (OnceService os : this.getOnceservicesList()) {
			if (os.isShowCost()) {
				if ((os.getUnitCost() * this.getTotalSensorCount()) > os
						.getMinimumCharge()) {
					summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
							+ os.getServiceDescription()
							+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
							+ userInfo.getUser().getCompany().getCountry()
									.getCurrency()
							+ formater.format(os.getUnitCost()
									* this.getTotalSensorCount())
							+ "</td></tr>";
				} else {
					if (this.getTotalSensorCount() == 0) {
						summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
								+ os.getServiceDescription()
								+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
								+ userInfo.getUser().getCompany().getCountry()
										.getCurrency()
								+ formater.format(0)
								+ "</td></tr>";
					} else {
						summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
								+ os.getServiceDescription()
								+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
								+ userInfo.getUser().getCompany().getCountry()
										.getCurrency()
								+ formater.format(os.getMinimumCharge())
								+ "</td></tr>";
					}
				}
			} else {
				// pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"+os.getServiceDescription()+"</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(0)+"</td></tr>";
			}

		}
		// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		if (this.getmServiceOptionsDiscount() != 0) {
			// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getOnceServicestotal())+"</td></tr>";
			summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(this.getOnceServicestotal()
							* this.getmServiceOptionsDiscount() / 100)
					+ "</td></tr>";
		}
		summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater.format((this.getOnceServicestotal() - (this
						.getOnceServicestotal()
						* this.getmServiceOptionsDiscount() / 100)))
				+ "</td></tr>";
		summariesSecondPage += "</table>";

		summariesSecondPage += "<div align='left'><table  border='0' width='80%' style='padding-left:30px'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: left;'>INSTALLATION</th>"
				+ "<th >&nbsp;</th></tr>";

		summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>On Site System Installation</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater.format(this.getOnSiteInst()) + "</td></tr>";
		if (this.getQuotation().getOnsitesensorcalibration()) {
			summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>On Site System Calibration</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(this.getOnSiteCalib()) + "</td></tr>";
		}
		// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		if (this.getmSiteInstallationDiscount() != 0) {
			// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+(quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib())+"</td></tr>";
			summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(((this.getOnSiteInst() + this
							.getOnSiteCalib())
							* this.getmSiteInstallationDiscount() / 100))
					+ "</td></tr>";
		}
		summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater.format(((this.getOnSiteInst() + this
						.getOnSiteCalib()) - ((this.getOnSiteInst() + this
						.getOnSiteCalib())
						* this.getmSiteInstallationDiscount() / 100)))
				+ "</td></tr>";
		summariesSecondPage += "<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		summariesSecondPage += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;font-weight: bold;'>TOTAL PROJECT PRICE ";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			summariesSecondPage += "(Ex Vat) ";
		}
		summariesSecondPage += ": </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater
						.format((this.getTotalsystembreakdown() - (this
								.getTotalsystembreakdown()
								* this.getmHardwareDiscount() / 100))
								+ (this.getOnceServicestotal() - (this
										.getOnceServicestotal()
										* this.getmServiceOptionsDiscount() / 100))
								+ ((this.getOnSiteInst() + this
										.getOnSiteCalib()) - ((this
										.getOnSiteInst() + this
										.getOnSiteCalib())
										* this.getmSiteInstallationDiscount() / 100)))
				+ "</td></tr>";

		summariesSecondPage += "</table></div>";

		String VATLabel = "";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			VATLabel = " (Ex Vat)";
		}

		if ((this.getQuotation().getCombinedrnamonitoring() && !this.getQuotation().isUpdateInstallation())
				|| this.getQuotation().getRecordingonly()
				|| this.getQuotation().getYearlyrecalibrationservice()
				|| this.getQuotation().getTemperatureMapping()) {
			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>RECURRING SERVICE OPTIONS </p>";
		}

		if (this.getQuotation().getCombinedrnamonitoring() && !this.getQuotation().isUpdateInstallation()) {
			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>ALARM MONITORING AND RECORDING SERVICE</p>";

			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			summariesThirdPage += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format((this.alarmingnmonitoring1y - (this
							.getAlarmingnmonitoring1y()
							* this.getmRemoteMonitoringDiscount() / 100)))
					+ " Per year" + VATLabel + "<br/>";
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				summariesThirdPage += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format((this.alarmingnmonitoring3y - (this
								.getAlarmingnmonitoring3y()
								* this.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel + "<br/>";
				summariesThirdPage += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format((this.alarmingnmonitoring5y - (this
								.getAlarmingnmonitoring5y()
								* this.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel;
			}
			summariesThirdPage += "</p>";
		}

		if (this.getQuotation().getRecordingonly()) {
			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>RECORDING ONLY SERVICE</p>";

			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			summariesThirdPage += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format((this.recording1y - (this.recording1y
							* this.getmRemoteMonitoringDiscount() / 100)))
					+ " Per year" + VATLabel + "<br/>";
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				summariesThirdPage += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format((this.recording3y - (this.recording3y
								* this.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel + "<br/>";
				summariesThirdPage += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format((this.recording5y - (this.recording5y
								* this.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel;
			}
			summariesThirdPage += "</p>";
		}
		if (this.getQuotation().getYearlyrecalibrationservice() && !this.getQuotation().isUpdateInstallation()) {
			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>NPL RE-CALIBRATION AND MAINTENANCE SERVICE</p>";

			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			summariesThirdPage += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(this.annualnistcalibration1y)
					+ " Per year" + VATLabel + "<br/>";
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				summariesThirdPage += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(this.annualnistcalibration3y)
						+ " Per year" + VATLabel + "<br/>";
				summariesThirdPage += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(this.annualnistcalibration5y)
						+ " Per year" + VATLabel;
			}
			summariesThirdPage += "</p>";
		}
		if (this.getQuotation().getTemperatureMapping()) {
			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>REFRIGERATOR TEMPERATURE MAPPING SERVICE</p>";

			summariesThirdPage += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			summariesThirdPage += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(this.temperaturemapping1y) + " Per year"
					+ VATLabel + "<br/>";
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				summariesThirdPage += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(this.temperaturemapping3y)
						+ " Per year" + VATLabel + "<br/>";
				summariesThirdPage += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(this.temperaturemapping5y)
						+ " Per year" + VATLabel;
			}
			summariesThirdPage += "</p>";
		}

		if (this.getQuotation().isUpdateInstallation()) {
			summariesThirdPage += "<div align='left'><table  border='0' width='80%' style='padding-top:10px;padding-left:30px'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: left;'>ADDITIONAL RECURRING SERVICE COSTS</th>"
					+ "<th >&nbsp;</th></tr>";
	
			summariesThirdPage += "<tr><td style='padding-top:10px;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Addition to existing remote monitoring service: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ userInfo.getUser().getCompany().getCountry().getCurrency()
					+ formater.format((Double.valueOf(this.getTempRemoteMonitoring())* this.getTotalSensorCount())) + " Per Year</td></tr>";
			summariesThirdPage += "<tr><td style='padding-top:10px;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Addition to existing annual calibration service: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format((Double.valueOf(this.getTempAnnualCalibration())* this.getTotalSensorCount())) + " Per Year</td></tr>";
		
			summariesThirdPage += "</table></div>";
		}
		
		summariesSecondPage += summariesThirdPage;
		summariesThirdPage = "";
		
		summariesFirstImage = "./images/"
				+"Bio Tech And Life Sciences4.png";
		summariesSecondImage = "./images/"
				+ "Bio Tech And Life Sciences5.png";

	}

	public String getQuotationCoverUpperText() {
		return quotationCoverUpperText;
	}

	public void setQuotationCoverUpperText(String quotationCoverUpperText) {
		this.quotationCoverUpperText = quotationCoverUpperText;
	}

	public String getQuotationCoverLowerText() {
		return quotationCoverLowerText;
	}

	public void setQuotationCoverLowerText(String quotationCoverLowerText) {
		this.quotationCoverLowerText = quotationCoverLowerText;
	}

	public String summariesPageNext() {
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) context.getSession(false);
		UserInfoPageBean bean = (UserInfoPageBean) session
				.getAttribute("userInfo");
		/* this.setEmail1(bean.getUser().getUsername()); */
		this.setEmailCc(bean.getUser().getUsername());
		if (this.templateFormats == null) {
			this.templateFormats = LetterTemplateDAO.getInstance()
					.getUsersSavedTemplates(bean.getUser().getUserId());
			if (this.templateFormats != null && this.templateFormats.size() > 0) {
				this.templates = new HashMap<String, Integer>();
				for (Template template : this.templateFormats) {
					this.templates.put(template.getTemplateName(),
							template.getTemplateId());
				}
			}
		}
		if (this.emailTemplatesFormats == null) {
			this.emailTemplatesFormats = EmailTemplateDAO.getInstance()
					.getUsersSavedTemplates(bean.getUser().getUserId());
			if (this.emailTemplatesFormats != null
					&& this.emailTemplatesFormats.size() > 0) {
				this.emailTemplates = new HashMap<>();
				for (Template template : this.emailTemplatesFormats) {
					this.emailTemplates.put(template.getTemplateName(),
							template.getTemplateId());
				}
			}
		}
		if (quotation != null && !visited) {
			this.visited = true;
			//String customerSalutation =(this.quotation.getCustomersalutation() != null) ? this.quotation
			//		.getCustomersalutation() : "";
			String contactName = (this.quotation.getContactname() != null) ? this.quotation
					.getContactname() : "";
			String customerName = (this.quotation.getCustomername() != null) ? this.quotation
					.getCustomername() : "";
			String address1 = (this.quotation.getAddress1() != null) ? this.quotation
					.getAddress1() : "";
			String address2 = (this.quotation.getAddress2() != null) ? this.quotation
					.getAddress2() : "";
			String address3 = (this.quotation.getAddress3() != null) ? this.quotation
					.getAddress3() : "";
			String department = (this.quotation.getDepartment() != null) ? this.quotation
					.getDepartment() : "";
			String city = (this.getQuotation().getTowncity() != null) ? this
					.getQuotation().getTowncity() : "";
			String state = (this.quotation.getCountrystate() != null) ? this.quotation
					.getCountrystate() : "";
			String postCode = (this.quotation.getZippincode() != null) ? this.quotation
					.getZippincode() : "";

			this.quotationCoverUpperText = "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
					+ ""
					+ contactName
					+ "<br/>";
					if(!department.isEmpty()){
						this.quotationCoverUpperText = quotationCoverUpperText+  department	+ "<br/> ";	
					}	
					this.quotationCoverUpperText = quotationCoverUpperText
							+ customerName
							+ "<br/> "+ address1
					+ "<br/> ";
					if(!address2.isEmpty()){
						this.quotationCoverUpperText = quotationCoverUpperText+  address2	+ "<br/> ";	
					}
					if(!address3.isEmpty()){
						this.quotationCoverUpperText = quotationCoverUpperText+  address3	+ "<br/> ";	
					}
			this.quotationCoverUpperText = quotationCoverUpperText
					+ city
					+ "<br/> "
					+ state
					+ "<br/> "
					+ postCode
					+ " <br /><br/>"
					+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "
					+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
					+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
					+ new SimpleDateFormat("dd/MM/yyyy").format(new Date())
					+ " <br /><br /> Dear "
					+this.quotation.getCustomersalutation()
					//+ this.quotation.getContactname()
					+ ",</div>"
					;
			this.quotationcoveringletter += "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
					+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
					+ "&nbsp; &nbsp; &nbsp; &nbsp; "
					+ "<u><strong>"
					+ "Re "
					+ this.quotation.getProjectname()
					+ " remote wireless monitoring system</strong></u></div> "
					+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
					+ "<br/>Thank you for your recent enquiry. I have pleasure in enclosing our quotation to carry"
					+ " out the work fully detailed in the following pages.</div>"
					+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
					+ "<br/>This quotation covers supply, installation and commissioning of the new "
					+ "wireless based remote monitoring system in your facility. "
					+ "</div> "
					+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
					+ "<br/>This proposal consists of:<br/><br/></div> "
					+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
					+ "<ul><ul><ul><li>Wireless based sensing technology to minimise disruption of your facility during the installation phase.<br/></li>"
					+ "<li>A master wireless alarm receiver panel with 7 hours battery standby operation.<br/></li>"
					+ "<li>All necessary wireless sensing transmitters.<br/></li>"
					+ "<li>Local and remote Ethernet communications setup.<br/></li>"
					+ "<li>24/7 remote alarm monitoring and recording service.<br/></li>"
					+ "<li>Annual on site sensor re-calibration service.<br/></li>"
					+ "<li>Full local system installation by our qualified installation team together with on-going remote telephone support.</li></ul></ul></ul>"
					+ "<br/> I hope that this proposal meets with your approval. However, if you have any questions or require further clarification then please do not hesitate to contact me.</div>"
					+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><br/><br/>Yours truly,<br/><br/><br/><br/></div>";

			this.quotationCoverLowerText = "<div>"
					+ this.bean.getUser().getFirstName() + " "
					+ this.bean.getUser().getLastName() + "<br/>"
					+ this.bean.getUser().getTitle() + ",&nbsp;" + "<br/>"
					+ this.bean.getUser().getPhoneNumber() + "<br/>"
					+ bean.getUser().getUsername() + "</div>";
			this.quotation
					.setQuotationcoveringletter(this.quotationcoveringletter);

		}
		return "DisplaynEditPage.xhtml?faces-redirect=true";
	}

	public String summariesPageBack() {
		this.showSummary = false;
		return "SelectPartsPage.xhtml?faces-redirect=true";
	}

	public String addNonStandardParts() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		StandardParts stTemp = new StandardParts(0, "Part 00", "Non Standard ",
				"NonStandard Part", 0.0, 3.3, 0, userInfo.getUser()
						.getCompany().getCountry().getCountry(),
				new ArrayList<StandardParts>(), "NonStandard Part", "D", "14");
		List<Double> factors = new ArrayList<Double>();
		factors.add(userInfo.getUser().getCompany().getCountry()
				.getConversionratefrompounds());
		factors.add(userInfo.getUser().getCompany().getCountry().getFreight());
		factors.add(userInfo.getUser().getCompany().getCountry().getDuty());
		stTemp.setFactors(factors);
		standardPartsCollection.get(this.currentPageCounter).add(0, stTemp);
		return "SelectPartsPage.xhtml?faces-redirect=true";
	}

	public String saveAndSend() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		QuotationCreationPageBean quotationBean = (QuotationCreationPageBean) session
				.getAttribute("newquote");
		// UserInfoPageBean userInfo =
		// (UserInfoPageBean)session.getAttribute("userInfo");
		this.quotation.setHardwarediscount(mHardwareDiscount);
		this.quotation.setSiteinstallationdiscount(mSiteInstallationDiscount);
		this.quotation.setServiceoptiondiscount(mServiceOptionsDiscount);
		this.quotation.setRemotemonitoringdiscount(mRemoteMonitoringDiscount);
		String email = "";// userInfo.getUser().getEmail();
		if (email1.compareToIgnoreCase("") != 0) {
			email += email1;
		}
		if (email2.compareToIgnoreCase("") != 0) {
			email += "," + email2;
		}
		if (email3.compareToIgnoreCase("") != 0) {
			email += "," + email3;
		}
		if (email4.compareToIgnoreCase("") != 0) {
			email += "," + email4;
		}
		if (email5.compareToIgnoreCase("") != 0) {
			email += "," + email5;
		}
		if (email6.compareToIgnoreCase("") != 0) {
			email += "," + email6;
		}
		// this.quotation.setRevisionnumber(0);
		PDFExporterPageBean pdfExporter = new PDFExporterPageBean();
		pdfExporter.writeToFile(validateEmails(email.split(",")), quotationBean
				.getQuotation().getQuotationref());
		QuotationDAO.getInstance().saveQuotation(quotationBean.getQuotation(),
				standardPartsCollection, siteNames, siteInstallation,
				installationTravel, siteCalibration, calibartionTravel,
				annualSiteCalibration, annualDeadTravelDays, "", email);
		return "ConfirmationPage.xhtml?faces-redirect=true";

	}

	public void saveQuotationDisplaynEditpage() {
		String email = this.quotation.getContactemail();
		PDFExporterFromHTMLPageBean pdfExporter = new PDFExporterFromHTMLPageBean();

		QuotationDAO.getInstance().saveUpdateQuote(savedQuotationId,
				this.getQuotation(), pdfExporter.getContents(), email,
				Boolean.FALSE);
		QuotationDAO.getInstance().saveParts(savedQuotationId,
				standardPartsCollection, siteNames, siteInstallation,
				installationTravel, siteCalibration, calibartionTravel,
				annualSiteCalibration, annualDeadTravelDays);

	}

	public String saveAndSendtest() {

		this.quotation.setQuotationcoveringletter(this.quotationcoveringletter);
		// if(!saveLetterTemplate){
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		// QuotationCreationPageBean quotationBean =
		// (QuotationCreationPageBean)session.getAttribute("newquote");
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		//System.out.println("**************************user Info: " + userInfo);
		//System.out.println("*************************user: "
		//		+ userInfo.getUser());
		this.quotation.setUser(userInfo.getUser());
		this.quotation.setHardwarediscount(mHardwareDiscount);
		this.quotation.setSiteinstallationdiscount(mSiteInstallationDiscount);
		this.quotation.setServiceoptiondiscount(mServiceOptionsDiscount);
		this.quotation.setRemotemonitoringdiscount(mRemoteMonitoringDiscount);
		
		String email = "";// userInfo.getUser().getEmail();
		if (email1.compareToIgnoreCase("") != 0) {
			email += email1;
		}
		/*
		 * if (email2.compareToIgnoreCase("") != 0) { email += "," + email2; }
		 * if (email3.compareToIgnoreCase("") != 0) { email += "," + email3; }
		 * if (email4.compareToIgnoreCase("") != 0) { email += "," + email4; }
		 * if (email5.compareToIgnoreCase("") != 0) { email += "," + email5; }
		 * if (email6.compareToIgnoreCase("") != 0) { email += "," + email6; }
		 */
		// email +=","+userInfo.getUser().getEmail();
		// this.quotation.setRevisionnumber(0);
		try {
			PDFExporterFromHTMLPageBean pdfExporter = new PDFExporterFromHTMLPageBean();
			String emailBody = "";
			emailBody = this.quotation.getEmailBody();
			/*
			 * String cc=""; if(ValidateEmails.validate(emailCc)) cc=emailCc;
			 * String bcc=""; if(ValidateEmails.validate(emailBcc))
			 * bcc=emailBcc;
			 */
			//System.out.println("to email: "+email);
			pdfExporter.writeToFileNEmail(validateEmails(email.split(",")),
					validateEmails(this.emailCc.split(",")),
					validateEmails(this.emailBcc.split(",")),
					this.emailSubject, this.getQuotation().getQuotationref(),
					userInfo.getUser().getUsername(), emailBody);
			// QuotationDAO.getInstance().saveQuotation(this.getQuotation(),
			// standardPartsCollection,
			// siteNames, siteInstallation, installationTravel, siteCalibration,
			// calibartionTravel, annualSiteCalibration, annualDeadTravelDays,
			// pdfExporter.getContents(),email);
			this.getQuotation().setEmailcc(this.emailCc);
			this.getQuotation().setEmailbcc(this.emailBcc);
			this.getQuotation().setEmailsubject(this.emailSubject);

			QuotationDAO.getInstance().saveUpdateQuote(savedQuotationId,
					this.getQuotation(), pdfExporter.getContents(), email,
					Boolean.TRUE);
			QuotationDAO.getInstance().saveParts(savedQuotationId,
					standardPartsCollection, siteNames, siteInstallation,
					installationTravel, siteCalibration, calibartionTravel,
					annualSiteCalibration, annualDeadTravelDays);
		} catch (RuntimeException ex) { // If a Runtime exception occurs, This
										// will notify the user about the error
			this.errorFlag = true;
			StringWriter writer = new StringWriter();
			PrintWriter pw = new PrintWriter(writer);
			ex.printStackTrace(pw);
			UserAuditDAO.getInstance().addUserAudit("Error", writer.toString());
		}
		if (errorFlag) {
			errorFlag = false;
			this.destroy();
			return "ErrorPage.xhtml?faces-redirect=true";
		} else {
			this.destroy();
			return "ConfirmationPage.xhtml?faces-redirect=true";
		}
		// }
		// else {
		// FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
		// "Please Enter the name to save this letter format",
		// "Please Enter the name to save this letter format");
		// FacesContext.getCurrentInstance().addMessage(null, message);
		// this.saveLetterTemplate=false;
		// return "";
		// }
	}

	private String validateEmails(String[] pEmailAddress) {
		String temp = "";
		// String []to = {ServiceProperties.getInstance().TO};

		for (int i = 0; i < pEmailAddress.length; i++) {
			if (ValidateEmails.validate(pEmailAddress[i])) {
				temp = temp + pEmailAddress[i] + ",";
			} else {
				// try {
				// saveMessageDetails(getAlarmID(mMessage),mSiteCode,mUserName,"Unable to Deliver :"
				// +mMessage + ": To :"+pEmailAddress[i],"ERROR",false);
				// sendEmail("Unable to Deliver :" +mMessage +
				// ": To :"+pEmailAddress[i] ,mForm,"DELIVERY FAILURE ",to);
				// } catch (MessagingException e) {e.printStackTrace();}
			}
		}
		// String []address = temp.split(",");
		return temp;
	}

	/**************************************************** flags for pagination *************************************************/

	public int getCurrentPageCounter() {
		return currentPageCounter;
	}

	public void setCurrentPageCounter(int currentPageCounter) {
		this.currentPageCounter = currentPageCounter;
	}

	public boolean isNewQuotationPage() {
		return NewQuotationPage;
	}

	public void setNewQuotationPage(boolean newQuotationPage) {
		NewQuotationPage = newQuotationPage;
	}

	public boolean isSelectPartsPage() {
		return SelectPartsPage;
	}

	public void setSelectPartsPage(boolean selectPartsPage) {
		SelectPartsPage = selectPartsPage;
	}

	public boolean isTotalnDiscountPage() {
		return TotalnDiscountPage;
	}

	public void setTotalnDiscountPage(boolean totalnDiscountPage) {
		TotalnDiscountPage = totalnDiscountPage;
	}

	public boolean isDisplaynEditPage() {
		return DisplaynEditPage;
	}

	public void setDisplaynEditPage(boolean displaynEditPage) {
		DisplaynEditPage = displaynEditPage;
	}

	/**************************************************** selectPartsPage *************************************************/

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public List<NonStandardParts> getPartnst() {
		return partnst;
	}

	public void setPartnst(List<NonStandardParts> partnst) {
		this.partnst = partnst;
	}

	/**************************************************** Total discount Page *************************************************/

	public void saveDisplayEdit() {
		//System.out.print("quotation save");
		QuotationDAO.getInstance().saveCoveringLetter(this.quotation);
	}

	public StandardParts getSelectedpart() {
		return selectedpart;
	}

	public void setSelectedpart(StandardParts selectedpart) {
		this.selectedpart = selectedpart;
	}

	/**************************************************** DisplaynEditPage *************************************************/

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getSelectedTemplate() {
		return selectedTemplate;
	}

	public void setSelectedTemplate(String selectedTemplate) {
		this.selectedTemplate = selectedTemplate;
	}

	public Map<String, Integer> getTemplates() {
		return templates;
	}

	public void setTemplates(Map<String, Integer> templates) {
		this.templates = templates;
	}

	public String getSelectedEmailTemplate() {
		return selectedEmailTemplate;
	}

	public void setSelectedEmailTemplate(String selectedEmailTemplate) {
		this.selectedEmailTemplate = selectedEmailTemplate;
	}

	public Map<String, Integer> getEmailTemplates() {
		return emailTemplates;
	}

	public void setEmailTemplates(Map<String, Integer> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}

	public String getOption() {
		return option;
	}

	public void setOption(String option) {
		this.option = option;
	}

	public boolean isSaveLetterTemp() {
		return saveLetterTemp;
	}

	public void setSaveLetterTemp(boolean saveLetterTemp) {
		this.saveLetterTemp = saveLetterTemp;
	}

	public boolean isSaveEmailTemp() {
		return saveEmailTemp;
	}

	public void setSaveEmailTemp(boolean saveEmailTemp) {
		this.saveEmailTemp = saveEmailTemp;
	}

	public String getEmailTempName() {
		return emailTempName;
	}

	public void setEmailTempName(String emailTempName) {
		this.emailTempName = emailTempName;
	}

	public String getQuotationcoveringletter() {
		return quotationcoveringletter;
	}

	public void setQuotationcoveringletter(String quotationcoveringletter) {
		this.quotationcoveringletter = quotationcoveringletter;
	}

	public String getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}

	public String getEmailBcc() {
		return emailBcc;
	}

	public void setEmailBcc(String emailBcc) {
		this.emailBcc = emailBcc;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getSummariesFirstPage() {
		return summariesFirstPage;
	}

	public void setSummariesFirstPage(String summariesFirstPage) {
		this.summariesFirstPage = summariesFirstPage;
	}

	public String getSummariesSecondPage() {
		return summariesSecondPage;
	}

	public void setSummariesSecondPage(String summariesSecondPage) {
		this.summariesSecondPage = summariesSecondPage;
	}

	public String getSummariesThirdPage() {
		return summariesThirdPage;
	}

	public void setSummariesThirdPage(String summariesThirdPage) {
		this.summariesThirdPage = summariesThirdPage;
	}

	public String getSummariesFirstImage() {
		return summariesFirstImage;
	}

	public void setSummariesFirstImage(String summariesFirstImage) {
		this.summariesFirstImage = summariesFirstImage;
	}

	public String getSummariesSecondImage() {
		return summariesSecondImage;
	}

	public void setSummariesSecondImage(String summariesSecondImage) {
		this.summariesSecondImage = summariesSecondImage;
	}

	/*
	 * Save this template for the user
	 */
	public void saveTemplate() {
		this.quotation.setQuotationcoveringletter(this.quotationcoveringletter);
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		UserInfoPageBean bean = (UserInfoPageBean) sessionMap.get("userInfo");
		FacesMessage message = null;
		if (this.saveLetterTemp) {
			if (LetterTemplateDAO.getInstance().saveTemplate(
					new LetterTemplate(this.templateName, this.quotation
							.getQuotationcoveringletter(), bean.getUser()
							.getUserId())) > 0) {
				UserAuditDAO.getInstance()
						.addUserAudit("Saved Letter Template",
								"Saved Letter Template Format");
				this.templateName = null;
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						SUCCESS_MESSAGE, SUCCESS_MESSAGE);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update(
						"frmdisplayeditPage:valMsg");
			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						FAILURE_MESSAGE, FAILURE_MESSAGE);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update(
						"frmdisplayeditPage:valMsg");
			}
		}

		if (this.saveEmailTemp) {
			if (EmailTemplateDAO.getInstance().saveTemplate(
					new EmailTemplate(this.emailTempName, this.quotation
							.getEmailBody(), bean.getUser().getUserId())) > 0) {
				UserAuditDAO.getInstance().addUserAudit("Saved Email Template",
						"Saved Email Template Format");
				this.emailTempName = null;
				message = new FacesMessage(FacesMessage.SEVERITY_INFO,
						SUCCESS_MESSAGE, SUCCESS_MESSAGE);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update(
						"frmdisplayeditPage:valMsg");

			} else {
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						FAILURE_MESSAGE, FAILURE_MESSAGE);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext.getCurrentInstance().update(
						"frmdisplayeditPage:valMsg");
			}
		}
	}

	public String updateTemplate(String template) {
		if (!"".equals(template)) {
			for (Template tmp : this.templateFormats) {
				if (tmp.getTemplateId() == Integer.parseInt(template)) {
					this.quotation.setQuotationcoveringletter(tmp.getBody());
					this.setQuotationcoveringletter(tmp.getBody());
					break;
				}
			}
		}
		return "";
	}

	public String updateEmailTemplate(String template) {
		if (!"".equals(template)) {
			for (Template temp : this.emailTemplatesFormats) {
				if (temp.getTemplateId() == Integer.parseInt(template)) {
					this.quotation.setEmailBody(temp.getBody());
					break;
				}
			}
		}
		return "";
	}

	public String updateTemplateSelection() {
		return this.updateTemplate(this.selectedTemplate);
	}

	public String updateEmailTemplateSelection() {
		return this.updateEmailTemplate(this.selectedEmailTemplate);
	}

	public void revertSelection(ActionEvent event) {
		if ("2".equals(this.option)) {
			this.selectedEmailTemplate = "";
		}
	}

	public void saveLetterTemplate() {
		this.option = "1";
		this.saveLetterTemp = true;
		this.saveEmailTemp = false;
		//System.out.println("template name:::::" + templateName);
		saveTemplate();
		getLatestLetterTemplates();
	}

	public void saveEmailTemplate() {
		this.option = "2";
		this.saveEmailTemp = true;
		this.saveLetterTemp = false;
		//System.out.println("template name:::::" + emailTempName);
		saveTemplate();
		getLatestEmailTemplates();
	}

	public void getLatestLetterTemplates() {
		this.templateFormats = LetterTemplateDAO.getInstance()
				.getUsersSavedTemplates(bean.getUser().getUserId());
		if (this.templateFormats != null && this.templateFormats.size() > 0) {
			this.templates = new HashMap<String, Integer>();
			for (Template template : this.templateFormats) {
				this.templates.put(template.getTemplateName(),
						template.getTemplateId());
			}
		}
	}

	public void getLatestEmailTemplates() {
		this.emailTemplatesFormats = EmailTemplateDAO.getInstance()
				.getUsersSavedTemplates(bean.getUser().getUserId());
		if (this.emailTemplatesFormats != null
				&& this.emailTemplatesFormats.size() > 0) {
			this.emailTemplates = new HashMap<>();
			for (Template template : this.emailTemplatesFormats) {
				this.emailTemplates.put(template.getTemplateName(),
						template.getTemplateId());
			}
		}
	}

	@PreDestroy
	public void destroy() {
		this.quotation = null;
		this.templateFormats = null;
		this.selectedTemplate = null;
		this.templates = null;
		this.templateName = null;
		// this.saveLetterTemplate=false;
		this.selectedEmailTemplate = null;
		this.emailTemplatesFormats = null;
		this.emailTemplates = null;
		this.option = "1";
		this.saveLetterTemp = false;
		this.saveEmailTemp = false;
		this.emailTempName = null;
		Map<String, Object> session = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		PDFExporterFromHTMLPageBean bean = (PDFExporterFromHTMLPageBean) session
				.get("pdfhtmlExp");
		//System.out.println(bean);
		if (bean != null)
			System.out.println(session.remove(bean));
		this.email1 = "";
		this.email2 = "";
		this.email3 = "";
		this.email4 = "";
		this.email5 = "";
		this.email6 = "";

	}
}
