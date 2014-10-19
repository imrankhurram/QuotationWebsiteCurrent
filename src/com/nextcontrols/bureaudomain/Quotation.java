package com.nextcontrols.bureaudomain;

// default package
// Generated 09-Feb-2010 15:54:24 by Hibernate Tools 3.2.2.GA

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.pagebeans.UserInfoPageBean;

public class Quotation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String quotationref;
	ExternalContext context = FacesContext.getCurrentInstance()
			.getExternalContext();
	HttpSession session = (HttpSession) context.getSession(false);
	UserInfoPageBean bean = (UserInfoPageBean) session.getAttribute("userInfo");
	private String customersalutation = "";
	private String projectname = "";
	private String customername = "";
	private String contactname = "";
	private String address1 = "";
	private String address2 = "";
	private String address3 = "";
	private String department = "";
	private String towncity = "";
	private String countrystate = "";
	private String zippincode = "";
	private String facilitytype = "";
	private String contacttelno = "";
	private String sitetelno = "";
	private String contactemail = "";
	private String siteemail = "";
	private String customerwebsite = "";
	private String pagecontents = "";
	private String emailedto = "";
	private Boolean onsitesensorcalibration;
	private Boolean factorysensorcalibration;
	private Boolean dqiqoqprotocoldocs;
	private Boolean websitedbsetup;
	private Boolean webhostedgui;
	private Boolean combinedrnamonitoring;
	private Boolean recordingonly;
	private Boolean yearlyrecalibrationservice;
	private Boolean financedhdoption = false;
	private Boolean individualquotepersite = false;
	private Boolean temperatureMapping=false;
	private int revisionnumber;
	private int numberofsites = 1;
	private double hardwarediscount;
	private double siteinstallationdiscount;
	private double serviceoptiondiscount;
	private double remotemonitoringdiscount;
	private Timestamp creationTime;
	private int assignedallowablediscount;
	private int installationtravelday;
	private User user;
	private int noOfsensors;
	public static int count;
	private String emailcc="";
	private String emailbcc="";
	private String emailsubject="";
	

	private boolean completed;
	private boolean accepted;

	private SimpleDateFormat createFormat = new SimpleDateFormat(
			"dd/MM/yyyy");
	private String creatTime;
	// private String quotationcoveringletter=
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
	// +
	// "(Customer Contact Name) <br/> (Customer Company Name) <br/> (Address Line 1),<br/> (Address Line 2) <br/> (Address Line 3), <br/> (Post Code) <br /><br/>"+
	// "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "
	// +
	// "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+
	// "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+
	// new SimpleDateFormat("dd/MM/yyyy").format(new
	// Date())+" <br /><br /> Dear (Name),</div>"+
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"+
	// "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"+
	// "&nbsp; &nbsp; &nbsp; &nbsp; "+
	// "<u><strong>" +
	// "Re (Project Name) remote wireless monitoring system</strong></u></div> "
	// +
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"+
	// "<br/>Thank you for your recent enquiry. I have pleasure in enclosing our quotation to carry"
	// +
	// " out the work fully detailed in the following pages.</div>"+
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
	// +
	// "<br/>This quotation covers supply, installation and commissioning of the new "+
	// "wireless based remote monitoring system in your facility. "+
	// "</div> "+
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"+
	// "<br/>This proposal consists of:<br/><br/></div> " +
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
	// +
	// "<ul><ul><ul><li>Wireless based sensing technology to minimise disruption of your facility during the installation phase.<br/></li>"
	// +
	// "<li>A master wireless alarm receiver panel with 7 hours battery standby operation.<br/></li>"+
	// "<li>All necessary wireless sensing transmitters.<br/></li>"+
	// "<li>Local and remote Ethernet communications setup.<br/></li>"+
	// "<li>24/7 remote alarm monitoring and recording service.<br/></li>"+
	// "<li>Annual on site sensor re-calibration service.<br/></li>"+
	// "<li>Full local system installation by our qualified installation team together with on-going remote telephone support.</li></ul></ul></ul>"+
	// "<br/> I hope that this proposal meets with your approval. However, if you have any questions or require further clarification then please do not hesitate to contact me.</div>"+
	// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><br/><br/>Yours truly,<br/><br/><br/><br/>"+this.bean.getUser().getFirstName()+
	// " "+ this.bean.getUser().getLastName()+
	// "<br/>(Sales executive title),&nbsp;"+
	// "<br/>(phone number)" +
	// "<br/>"+bean.getUser().getEmail()+"</div>";
	//
	private String quotationcoveringletter = "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
			+ "("
			+ getCustomername()
			+ ") <br/> (Customer Company Name) <br/> (Address Line 1),<br/> (Address Line 2) ,<br/> (city) ,<br/> (state) , <br/> (Post Code) <br /><br/>"
			+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "
			+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
			+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
			+ new SimpleDateFormat("dd/MM/yyyy").format(new Date())
			+ " <br /><br /> Dear (Name),</div>"
			+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
			+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
			+ "&nbsp; &nbsp; &nbsp; &nbsp; "
			+ "<u><strong>"
			+ "Re (Project Name) remote wireless monitoring system</strong></u></div> "
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
			+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><br/><br/>Yours truly,<br/><br/><br/><br/>"
			+ this.bean.getUser().getFirstName()
			+ " "
			+ this.bean.getUser().getLastName()
			+ "<br/>(Sales executive title),&nbsp;"
			+ "<br/>(phone number)"
			+ "<br/>" + bean.getUser().getEmail() + "</div>";

	private String emailBody = "";
	private boolean updateInstallation=false;
	private double remoteMonitoring;
	private double annualCalibration;
	private boolean disable_coveringletter=false;
	private boolean hardwareOnly=false;
	public Quotation() {
		count++;
	}

	public Quotation(int pid, String pcustomersalutation,String pqoutationref,
			String quotationcoveringletter, String pQuotationType,
			String projectname, String customername, String contactname,
			String address1, String address2, String ptowncity,
			String pcountrystate, String zippincode, String facilitytype,
			String contacttelno, String sitetelno, String contactemail,
			String siteemail, String customerwebsite,
			Boolean onsitesensorcalibration, Boolean factorysensorcalibration,
			Boolean dqiqoqprotocoldocs, Boolean websitedbsetup,
			Boolean webhostedgui, Boolean combinedrnamonitoring,
			Boolean recordingonly, Boolean yearlyrecalibrationservice,
			Boolean financedhdoption, Boolean individualquotepersite,
			int numberofsites, double hardwarediscount,
			double siteinstallationdiscount, double serviceoptiondiscount,
			int assignedallowablediscount, int installationtravelday,
			User pUser, Boolean pTemperatureMapping,
			double pRemotemonitoringdiscount, String pPageContents,
			Timestamp pCreationTime, String pEmailedTo, int pRevisionnumber,boolean isCompleted,boolean isAccepted,String emailCc,String emailBcc,String emailSubject) {
		this.id = pid;
		this.quotationref = pqoutationref;
		this.customersalutation = pcustomersalutation;
		this.projectname = projectname;
		this.customername = customername;
		this.contactname = contactname;
		this.address1 = address1;
		this.address2 = address2;
		this.towncity = ptowncity;
		this.countrystate = pcountrystate;
		this.zippincode = zippincode;
		this.facilitytype = facilitytype;
		this.contacttelno = contacttelno;
		this.sitetelno = sitetelno;
		this.contactemail = contactemail;
		this.siteemail = siteemail;
		this.customerwebsite = customerwebsite;
		this.onsitesensorcalibration = onsitesensorcalibration;
		this.factorysensorcalibration = factorysensorcalibration;
		this.dqiqoqprotocoldocs = dqiqoqprotocoldocs;
		this.websitedbsetup = websitedbsetup;
		this.webhostedgui = webhostedgui;
		this.combinedrnamonitoring = combinedrnamonitoring;
		this.recordingonly = recordingonly;
		this.yearlyrecalibrationservice = yearlyrecalibrationservice;
		this.financedhdoption = financedhdoption;
		this.individualquotepersite = individualquotepersite;
		this.numberofsites = numberofsites;
		this.hardwarediscount = hardwarediscount;
		this.siteinstallationdiscount = siteinstallationdiscount;
		this.serviceoptiondiscount = serviceoptiondiscount;
		this.assignedallowablediscount = assignedallowablediscount;
		this.installationtravelday = installationtravelday;
		this.user = pUser;
		this.temperatureMapping = pTemperatureMapping;
		this.remotemonitoringdiscount = pRemotemonitoringdiscount;
		this.pagecontents = pPageContents;
		this.creationTime = pCreationTime;
		this.setCreatTime(creationTime);
		this.emailedto = pEmailedTo;
		this.revisionnumber = pRevisionnumber;
		this.quotationcoveringletter = quotationcoveringletter;
		this.completed=isCompleted;
		this.accepted=isAccepted;
		this.emailcc=emailCc;
		this.emailbcc=emailBcc;
		this.emailsubject=emailSubject;
	}

	public boolean isUpdateInstallation() {
		return updateInstallation;
	}

	public void setUpdateInstallation(boolean updateInstallation) {
		this.updateInstallation = updateInstallation;
	}

	public double getRemoteMonitoring() {
		return remoteMonitoring;
	}

	public void setRemoteMonitoring(double remoteMonitoring) {
		this.remoteMonitoring = remoteMonitoring;
	}

	public double getAnnualCalibration() {
		return annualCalibration;
	}

	public void setAnnualCalibration(double annualCalibration) {
		this.annualCalibration = annualCalibration;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuotationref() {
		return quotationref;
	}

	public void setQuotationref(String qoutationref) {
		this.quotationref = qoutationref;
	}
	
	public String getCustomersalutation() {
		return customersalutation;
	}

	public void setCustomersalutation(String customersalutation) {
		this.customersalutation = customersalutation;
	}

	/*
	 * public String getQuotationType() { return quotationType; } public void
	 * setQuotationType(String quotationType) { this.quotationType =
	 * quotationType; }
	 */
	public String getQuotationcoveringletter() {
		return quotationcoveringletter;
	}

	public void setQuotationcoveringletter(String quotationcoveringletter) {
		this.quotationcoveringletter = quotationcoveringletter;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getContactname() {
		return contactname;
	}

	public void setContactname(String contactname) {
		this.contactname = contactname;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getTowncity() {
		return towncity;
	}

	public void setTowncity(String towncity) {
		this.towncity = towncity;
	}

	public String getCountrystate() {
		return countrystate;
	}

	public void setCountrystate(String countrystate) {
		this.countrystate = countrystate;
	}

	public String getZippincode() {
		return zippincode;
	}

	public void setZippincode(String zippincode) {
		this.zippincode = zippincode;
	}

	public String getFacilitytype() {
		return facilitytype;
	}

	public void setFacilitytype(String facilitytype) {
		this.facilitytype = facilitytype;
	}

	public String getContacttelno() {
		return contacttelno;
	}

	public void setContacttelno(String contacttelno) {
		this.contacttelno = contacttelno;
	}

	public String getSitetelno() {
		return sitetelno;
	}

	public void setSitetelno(String sitetelno) {
		this.sitetelno = sitetelno;
	}

	public String getContactemail() {
		return contactemail;
	}

	public void setContactemail(String contactemail) {
		this.contactemail = contactemail;
	}

	public String getSiteemail() {
		return siteemail;
	}

	public void setSiteemail(String siteemail) {
		this.siteemail = siteemail;
	}

	public String getCustomerwebsite() {
		return customerwebsite;
	}

	public void setCustomerwebsite(String customerwebsite) {
		this.customerwebsite = customerwebsite;
	}

	public int getNumberofsites() {
		return numberofsites;
	}

	public void setNumberofsites(int numberofsites) {
		this.numberofsites = numberofsites;
	}

	public double getHardwarediscount() {
		return hardwarediscount;
	}

	public void setHardwarediscount(double hardwarediscount) {
		this.hardwarediscount = hardwarediscount;
	}

	public double getSiteinstallationdiscount() {
		return siteinstallationdiscount;
	}

	public void setSiteinstallationdiscount(double siteinstallationdiscount) {
		this.siteinstallationdiscount = siteinstallationdiscount;
	}

	public double getServiceoptiondiscount() {
		return serviceoptiondiscount;
	}

	public void setServiceoptiondiscount(double serviceoptiondiscount) {
		this.serviceoptiondiscount = serviceoptiondiscount;
	}

	public int getAssignedallowablediscount() {
		return assignedallowablediscount;
	}

	public void setAssignedallowablediscount(int assignedallowablediscount) {
		this.assignedallowablediscount = assignedallowablediscount;
	}

	public double getRemotemonitoringdiscount() {
		return remotemonitoringdiscount;
	}

	public void setRemotemonitoringdiscount(double remotemonitoringdiscount) {
		this.remotemonitoringdiscount = remotemonitoringdiscount;
	}

	public int getInstallationtravelday() {
		return installationtravelday;
	}

	public void setInstallationtravelday(int installationtravelday) {
		this.installationtravelday = installationtravelday;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getOnsitesensorcalibration() {
		return onsitesensorcalibration;
	}

	public void setOnsitesensorcalibration(Boolean onsitesensorcalibration) {
		this.onsitesensorcalibration = onsitesensorcalibration;
	}

	public Boolean getFactorysensorcalibration() {
		return factorysensorcalibration;
	}

	public void setFactorysensorcalibration(Boolean factorysensorcalibration) {
		this.factorysensorcalibration = factorysensorcalibration;
	}

	public Boolean getDqiqoqprotocoldocs() {
		return dqiqoqprotocoldocs;
	}

	public void setDqiqoqprotocoldocs(Boolean dqiqoqprotocoldocs) {
		this.dqiqoqprotocoldocs = dqiqoqprotocoldocs;
	}

	public Boolean getWebsitedbsetup() {
		return websitedbsetup;
	}

	public void setWebsitedbsetup(Boolean websitedbsetup) {
		this.websitedbsetup = websitedbsetup;
	}

	public Boolean getWebhostedgui() {
		return webhostedgui;
	}

	public void setWebhostedgui(Boolean webhostedgui) {
		this.webhostedgui = webhostedgui;
	}

	public Boolean getCombinedrnamonitoring() {
		return combinedrnamonitoring;
	}

	public void setCombinedrnamonitoring(Boolean combinedrnamonitoring) {
		this.combinedrnamonitoring = combinedrnamonitoring;
	}

	public Boolean getRecordingonly() {
		return recordingonly;
	}

	public void setRecordingonly(Boolean recordingonly) {
		this.recordingonly = recordingonly;
	}

	public Boolean getYearlyrecalibrationservice() {
		return yearlyrecalibrationservice;
	}

	public void setYearlyrecalibrationservice(Boolean yearlyrecalibrationservice) {
		this.yearlyrecalibrationservice = yearlyrecalibrationservice;
	}

	public Boolean getFinancedhdoption() {
		return financedhdoption;
	}

	public void setFinancedhdoption(Boolean financedhdoption) {
		this.financedhdoption = financedhdoption;
	}

	public Boolean getIndividualquotepersite() {
		return individualquotepersite;
	}

	public void setIndividualquotepersite(Boolean individualquotepersite) {
		this.individualquotepersite = individualquotepersite;
	}

	public Boolean getTemperatureMapping() {
		return temperatureMapping;
	}

	public void setTemperatureMapping(Boolean temperatureMapping) {
		this.temperatureMapping = temperatureMapping;
	}

	public int getNoOfsensors() {
		return noOfsensors;
	}

	public void setNoOfsensors(int noOfsensors) {
		this.noOfsensors = noOfsensors;
	}

	public String getPagecontents() {
		return pagecontents;
	}

	public void setPagecontents(String pagecontents) {
		this.pagecontents = pagecontents;
	}

	public Timestamp getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Timestamp creationTime) {
		this.creationTime = creationTime;
	}

	public String getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Timestamp creatTime) {
		this.creatTime = createFormat.format(creatTime);
	}

	public String getEmailedto() {
		return emailedto;
	}

	public void setEmailedto(String emailedto) {
		this.emailedto = emailedto;
	}

	public int getRevisionnumber() {
		return revisionnumber;
	}

	public void setRevisionnumber(int revisionnumber) {
		this.revisionnumber = revisionnumber;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public String getEmailcc() {
		return emailcc;
	}

	public void setEmailcc(String emailcc) {
		this.emailcc = emailcc;
	}

	public String getEmailbcc() {
		return emailbcc;
	}

	public void setEmailbcc(String emailbcc) {
		this.emailbcc = emailbcc;
	}

	public String getEmailsubject() {
		return emailsubject;
	}

	public void setEmailsubject(String emailsubject) {
		this.emailsubject = emailsubject;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public boolean isDisable_coveringletter() {
		return disable_coveringletter;
	}

	public void setDisable_coveringletter(boolean disable_coveringletter) {
		this.disable_coveringletter = disable_coveringletter;
	}

	public boolean isHardwareOnly() {
		return hardwareOnly;
	}

	public void setHardwareOnly(boolean hardwareOnly) {
		this.hardwareOnly = hardwareOnly;
	}

}
