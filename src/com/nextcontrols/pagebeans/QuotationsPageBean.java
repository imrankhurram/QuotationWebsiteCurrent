package com.nextcontrols.pagebeans;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.itextpdf.text.DocumentException;
import com.nextcontrols.bureaudao.AnnualRateDAO;
import com.nextcontrols.bureaudao.DayDAO;
import com.nextcontrols.bureaudao.MonitoringServiceDAO;
import com.nextcontrols.bureaudao.OnceServiceDAO;
import com.nextcontrols.bureaudao.PartsDAO;
import com.nextcontrols.bureaudao.PriceCategoryDAO;
import com.nextcontrols.bureaudao.QuantityCategoryDAO;
import com.nextcontrols.bureaudao.QuotationDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.AnnualRate;
import com.nextcontrols.bureaudomain.OnceService;
import com.nextcontrols.bureaudomain.PriceCategory;
import com.nextcontrols.bureaudomain.QuantityCategory;
import com.nextcontrols.bureaudomain.Quotation;
import com.nextcontrols.bureaudomain.StandardParts;

@ManagedBean(name = "quotations")
@SessionScoped
public class QuotationsPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Quotation> quotationsList;
	private List<Quotation> filteredquotationsList;
	private Quotation selectedQuotation;
	private Date dateFrom;
	private Date dateTo;
	private static final String ERROR_MESSAGE = "Error: Could not open quotation, this quotation was not saved properly";
	private static final String SUCCESS_MESSAGE = "Successfully deleted a quotation ";
	private static final String FAILURE_MESSAGE = "Please select a quotation to delete";

	private static final String SUCCESS_ACCEPTED = "Successfully accepted a quotation ";
	private static final String FAILURE_ACCEPTED = "Please select a quotation to accept";

	private boolean showCompletedQuotations;
	private List<StandardParts> selectedQuoteParts;
	List<StandardParts> tempSelectedQuoteParts;
	private boolean showParts = false;
	private int totalHardwareCost;
	private double totalHardwareDiscount;
	private int totalServiceCost;
	private double totalServiceDiscount;
	private int alarmingnmonitoring1y;
	private double alarmingnmonitoring1yDiscount;
	private double installationCost;
	private double installationDiscount;
	private double calibrationCost;
	private double calibrationDiscount;
	

	// private boolean uploadPO;

	// private Part poFile;

	public QuotationsPageBean() {
		showCompletedQuotations = true;
		quotationsList = new ArrayList<Quotation>();
		initializeDates();

		/*
		 * if ((dateTo==null)||(dateFrom==null)){
		 * dateTo=Calendar.getInstance().getTime(); Calendar
		 * before=Calendar.getInstance(); before.add(Calendar.DATE, -7);
		 * dateFrom=before.getTime(); }
		 */
	}

	public List<Quotation> getQuotationsList() {
		setQuotationsList();
		return quotationsList;
	}

	public void setQuotationsList() {
		/*
		 * ExternalContext ectx =
		 * FacesContext.getCurrentInstance().getExternalContext(); HttpSession
		 * session = (HttpSession)ectx.getSession(false); UserInfoPageBean
		 * userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		 * 
		 * this.quotationsList =
		 * QuotationDAO.getInstance().getQuotations(userInfo
		 * .getUser().getUserId(
		 * ),userInfo.getUser().getCompany().getId(),userInfo
		 * .getUser().getUsertype(),dateFrom,dateTo);
		 */}

	public Quotation getSelectedQuotation() {
		return selectedQuotation;
	}

	public void setSelectedQuotation(Quotation selectedQuotation) {
		this.selectedQuotation = selectedQuotation;
	}

	public List<StandardParts> getSelectedQuoteParts() {
		return selectedQuoteParts;
	}

	public void setSelectedQuoteParts(List<StandardParts> selectedQuoteParts) {
		this.selectedQuoteParts = selectedQuoteParts;
	}

	public List<StandardParts> getTempSelectedQuoteParts() {
		return tempSelectedQuoteParts;
	}

	public void setTempSelectedQuoteParts(List<StandardParts> tempSelectedQuoteParts) {
		this.tempSelectedQuoteParts = tempSelectedQuoteParts;
	}

	public boolean isShowParts() {
		return showParts;
	}

	public void setShowParts(boolean showParts) {
		this.showParts = showParts;
	}

	public String showParts(Quotation selectedQuotation) {
		this.selectedQuotation = selectedQuotation;
		this.totalHardwareCost=0;
		this.totalHardwareDiscount=0;
		this.totalServiceCost=0;
		this.totalServiceDiscount=0;
		this.alarmingnmonitoring1y=0;
		this.installationCost=0;
		this.installationDiscount=0;
		this.calibrationCost=0;
		this.calibrationDiscount=0;
		int totalSensorCount=0;
		
		List<QuantityCategory> quantityCategoriesList = QuantityCategoryDAO.getInstance()
				.getQuantityCategoryListWithoutNuLL();
		DecimalFormat formater = (DecimalFormat) NumberFormat
				.getCurrencyInstance(Locale.US);
		DecimalFormatSymbols symbols = formater.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formater.setDecimalFormatSymbols(symbols);
		formater.setMaximumFractionDigits(0);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		if(null!=selectedQuoteParts){
			selectedQuoteParts.clear();
		}
		if (!showParts) {
			selectedQuoteParts = PartsDAO.getInstance()
					.getStandardPartsAndAssembliesListWithNonzeroQuantity(
							userInfo.getUser().getCompany().getCountry()
									.getFreight(),
							userInfo.getUser().getCompany().getCountry()
									.getDuty(),
							userInfo.getUser().getCompany().getCountry()
									.getConversionratefrompounds(),
							userInfo.getUser().getCompany().getCountry()
									.getCountry(), selectedQuotation.getId());
			//calculating total hardware price
			List<PriceCategory> priceCategoriesList = PriceCategoryDAO.getInstance()
					.getPriceCategoryListWithoutNuLL();
			for (StandardParts st : selectedQuoteParts) {
				for (int count = 0; count < quantityCategoriesList.size(); count++) {
					if (/*
						 * st.getPartType().compareToIgnoreCase("assembly")==0
						 * &&
						 */st.getQuoteQuantity() > 0
							&& quantityCategoriesList.get(count).getName()
									.compareToIgnoreCase("NULL") != 0
							&& quantityCategoriesList
									.get(count)
									.getName()
									.compareToIgnoreCase(
											st.getQuantitycategory()) == 0) {
						quantityCategoriesList.get(count).addQuantitytype(
								st.getQuoteQuantity(), st.getQuantityfactor());
						if (st.getQuantitycategory().compareToIgnoreCase("13") != 0) {
							totalSensorCount += st.getQuoteQuantity()
									* st.getQuantityfactor();
						}
					}
				}
				for (int count = 0; count < priceCategoriesList.size(); count++) {
					if (/*
						 * st.getPartType().compareToIgnoreCase("assembly")==0
						 * &&
						 */st.getQuoteQuantity() > 0
							&& priceCategoriesList.get(count).getName()
									.compareToIgnoreCase("NULL") != 0
							&& priceCategoriesList.get(count).getName()
									.compareToIgnoreCase(st.getPricecategory()) == 0) {
						priceCategoriesList.get(count).addQuantitytype(
								st.getQuoteQuantity(), st.getListPrice());
						totalHardwareCost += st.getQuoteQuantity()
								* st.getListPrice();
					}
				}
			}
			/*List<OnceService> onceservicesList = OnceServiceDAO.getInstance()
					.getOnceServicesList(
							userInfo.getUser().getCompany().getCountry()
									.getCountry(), selectedQuotation);
			for (int i = 0; i < onceservicesList.size(); i++) {
				if (onceservicesList.get(i).isShowCost()) {
					if ((onceservicesList.get(i).getUnitCost() * totalSensorCount) < onceservicesList
							.get(i).getMinimumCharge()) {
						if (totalSensorCount == 0) {
							totalServiceCost += 0;
						} else {
							totalServiceCost += onceservicesList.get(i)
									.getMinimumCharge();
						}
					} else {
						totalServiceCost += onceservicesList.get(i).getUnitCost()
								* totalSensorCount;
					}
				}
			}*/
						
			
		} else {
			tempSelectedQuoteParts=new ArrayList<StandardParts>();
			 selectedQuoteParts = PartsDAO.getInstance()
					.getStandardPartsAndAssembliesListWithNonzeroQuantity(
							userInfo.getUser().getCompany().getCountry()
									.getFreight(),
							userInfo.getUser().getCompany().getCountry()
									.getDuty(),
							userInfo.getUser().getCompany().getCountry()
									.getConversionratefrompounds(),
							userInfo.getUser().getCompany().getCountry()
									.getCountry(), selectedQuotation.getId());
			 for(int i=0; i<selectedQuoteParts.size(); i++) {
				 if(selectedQuoteParts.get(i).getPartType().compareToIgnoreCase("Assembly")==0){
					 List<StandardParts> subParts=selectedQuoteParts.get(i).getSubpartsList();
						for(int j=0;j<subParts.size();j++){
							StandardParts subPart=subParts.get(j);
							subPart.setQuoteQuantity(subParts.get(j).getQuantity()*selectedQuoteParts.get(i).getQuoteQuantity());
							tempSelectedQuoteParts.add(subPart);
						}
					}else{
						tempSelectedQuoteParts.add(selectedQuoteParts.get(i));
					}
			}
			 //calculating total hardware price
			 List<PriceCategory> priceCategoriesList = PriceCategoryDAO.getInstance()
						.getPriceCategoryListWithoutNuLL();
				for (StandardParts st : tempSelectedQuoteParts) {
					for (int count = 0; count < quantityCategoriesList.size(); count++) {
						if (/*
							 * st.getPartType().compareToIgnoreCase("assembly")==0
							 * &&
							 */st.getQuoteQuantity() > 0
								&& quantityCategoriesList.get(count).getName()
										.compareToIgnoreCase("NULL") != 0
								&& quantityCategoriesList
										.get(count)
										.getName()
										.compareToIgnoreCase(
												st.getQuantitycategory()) == 0) {
							quantityCategoriesList.get(count).addQuantitytype(
									st.getQuoteQuantity(), st.getQuantityfactor());
							if (st.getQuantitycategory().compareToIgnoreCase("13") != 0) {
								totalSensorCount += st.getQuoteQuantity()
										* st.getQuantityfactor();
							}
						}
					}
					for (int count = 0; count < priceCategoriesList.size(); count++) {
						if (/*
							 * st.getPartType().compareToIgnoreCase("assembly")==0
							 * &&
							 */st.getQuoteQuantity() > 0
								&& priceCategoriesList.get(count).getName()
										.compareToIgnoreCase("NULL") != 0
								&& priceCategoriesList.get(count).getName()
										.compareToIgnoreCase(st.getPricecategory()) == 0) {
							priceCategoriesList.get(count).addQuantitytype(
									st.getQuoteQuantity(), st.getListPrice());
							totalHardwareCost += st.getQuoteQuantity()
									* st.getListPrice();
						}
					}
				}
				
		}
		List<OnceService> onceservicesList = OnceServiceDAO.getInstance()
				.getOnceServicesList(
						userInfo.getUser().getCompany().getCountry()
								.getCountry(), selectedQuotation);
		for (int i = 0; i < onceservicesList.size(); i++) {
			if (onceservicesList.get(i).isShowCost()) {
				if ((onceservicesList.get(i).getUnitCost() * totalSensorCount) < onceservicesList
						.get(i).getMinimumCharge()) {
					if (totalSensorCount == 0) {
						totalServiceCost += 0;
					} else {
						totalServiceCost += onceservicesList.get(i)
								.getMinimumCharge();
					}
				} else {
					totalServiceCost += onceservicesList.get(i).getUnitCost()
							* totalSensorCount;
				}
			}
		}
		List<Integer> costsList = MonitoringServiceDAO.getInstance()
				.getMonitoringServicesList(
						userInfo.getUser().getCompany().getCountry()
								.getCountry(), totalSensorCount);
		if (costsList != null && costsList.size() > 0) {
			alarmingnmonitoring1y = costsList.get(0);
		}
		List<List<String>> costs= QuotationDAO.getInstance()
				.getPartsCost(selectedQuotation.getId());
//		
//		this.installationCost= costs[0]+costs[1];
//		this.installationDiscount=this.installationCost*this.selectedQuotation.getSiteinstallationdiscount()/100;
//		
//		this.calibrationCost=costs[2]+costs[3];
		List<String> daysList = PartsDAO.getInstance()
				.getDaysForQuotation(this.selectedQuotation.getId());
		List<String> siteInstallation=new ArrayList<>();
		List<String> installationTravel=new ArrayList<>();
		List<String> siteCalibration=new ArrayList<>();
		List<String> calibartionTravel=new ArrayList<>();
		
		List<String> annualSiteCalibration=new ArrayList<>();
		List<String> annualDeadTravelDays=new ArrayList<>();
//		for(String str:daysList){
//			System.out.println("days list: "+str);
//			
//		}
		if (daysList.size() > 0) {
//			siteNames.add(daysList.get(0));
			siteInstallation.add(daysList.get(1));
			installationTravel.add(daysList.get(2));
			siteCalibration.add(daysList.get(3));
			calibartionTravel.add(daysList.get(4));
			annualSiteCalibration.add(daysList.get(5));
			annualDeadTravelDays.add(daysList.get(6));
		} else {
//			siteNames.add("Site ");
			siteInstallation.add("" + 0);
			installationTravel.add("" + 0);
			siteCalibration.add("" + 0);
			calibartionTravel.add("" + 0);
			annualSiteCalibration.add("" + 0);
			annualDeadTravelDays.add("" + 0);
		}
		Map<String, Integer> rates = DayDAO.getInstance().getRates(
				userInfo.getUser().getCompany().getCountry().getCountry());
		double onSiteCalib = 0;
		double onSiteInst = 0;

		for (String key : rates.keySet()) {
			if (key.compareToIgnoreCase("Calibration Travel") == 0
					&& selectedQuotation.getOnsitesensorcalibration()) {
				for (String str : calibartionTravel) {//costs.get(3)
					onSiteCalib += Double.parseDouble(str) * rates.get(key);
//					System.out.println("calibration travel days: "+str);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
				}
			} else if (key.compareToIgnoreCase("Sensor Calibration") == 0
					&& selectedQuotation.getOnsitesensorcalibration()) {
				for (String str : siteCalibration) {//costs.get(2)
					onSiteCalib += Double.parseDouble(str) * rates.get(key);
//					System.out.println("calibration days: "+str);
//					System.out.println("calibration rate: "+rates.get(key));
//					System.out.println("calibration cost: "+onSiteCalib);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
				}
			} else if (key.compareToIgnoreCase("Installation Travel") == 0) {
				for (String str : installationTravel) {//costs.get(1)
					onSiteInst += Double.parseDouble(str) * rates.get(key);
//					System.out.println("installation travel days: "+str);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
				}
			} else if (key.compareToIgnoreCase("System Installation") == 0) {
				for (String str : siteInstallation) {//costs.get(0)
					onSiteInst += Double.parseDouble(str) * rates.get(key);
//					System.out.println("installation days: "+str);
//					System.out.println("installation rate: "+rates.get(key));
//					System.out.println("installation cost: "+onSiteInst);
					// instaOnSiteCalib += Integer.parseInt(str)*
					// rates.get(key);
				}
			}
		}
//		System.out.println("installation cost final: "+onSiteInst+onSiteCalib);
		this.installationCost=onSiteInst+onSiteCalib;
		this.installationDiscount=this.installationCost*this.selectedQuotation.getSiteinstallationdiscount()/100;
		
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
		int tempAnnualSiteCalibrationCost = 0;
		for (String str : annualSiteCalibration) {
			if (tempAnnualCalibration != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempAnnualCalibration.getCostperday();
			} else if (tempDefaultAnnualCalibration != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempDefaultAnnualCalibration.getCostperday();
			}/*
			 * else{ tempAnnualSiteCalibrationCost += Integer.parseInt(str)*
			 * 1500; }
			 */
		}
		for (String str : annualDeadTravelDays) {
			if (tempAnnualdeadtravelday != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempAnnualdeadtravelday.getCostperday();
			} else if (tempDefaultAnnualDeadTravelDay != null) {
				tempAnnualSiteCalibrationCost += Double.parseDouble(str)
						* tempDefaultAnnualDeadTravelDay.getCostperday();
			}/*
			 * else{ tempAnnualSiteCalibrationCost += Integer.parseInt(str)*
			 * 600; }
			 */
		}
		int annualnistcalibration1y = 0;
		if (tempAnnualCalibration != null) {
			annualnistcalibration1y = (int) (tempAnnualSiteCalibrationCost * tempAnnualCalibration
					.getOneyearfactor());
		} else if (tempDefaultAnnualCalibration != null) {
			annualnistcalibration1y = (int) (tempAnnualSiteCalibrationCost * tempDefaultAnnualCalibration
					.getOneyearfactor());
		}
		this.calibrationCost=annualnistcalibration1y;
		this.calibrationDiscount=0;//not calculating yet
		this.alarmingnmonitoring1yDiscount=this.alarmingnmonitoring1y*this.selectedQuotation.getRemotemonitoringdiscount()/100;
		this.totalServiceDiscount=this.totalServiceCost * this.selectedQuotation.getServiceoptiondiscount() / 100;
		this.totalHardwareDiscount=this.totalHardwareCost * this.selectedQuotation.getHardwarediscount()/ 100;

		return "ShowParts.xhtml?faces-redirect=true";

	}

	public double getInstallationCost() {
		return installationCost;
	}

	public void setInstallationCost(double installationCost) {
		this.installationCost = installationCost;
	}

	public double getInstallationDiscount() {
		return installationDiscount;
	}

	public void setInstallationDiscount(double installationDiscount) {
		this.installationDiscount = installationDiscount;
	}

	public double getCalibrationCost() {
		return calibrationCost;
	}

	public void setCalibrationCost(double calibrationCost) {
		this.calibrationCost = calibrationCost;
	}

	public double getCalibrationDiscount() {
		return calibrationDiscount;
	}

	public void setCalibrationDiscount(double calibrationDiscount) {
		this.calibrationDiscount = calibrationDiscount;
	}

	public int getAlarmingnmonitoring1y() {
		return alarmingnmonitoring1y;
	}

	public void setAlarmingnmonitoring1y(int alarmingnmonitoring1y) {
		this.alarmingnmonitoring1y = alarmingnmonitoring1y;
	}

	public double getAlarmingnmonitoring1yDiscount() {
		return alarmingnmonitoring1yDiscount;
	}

	public void setAlarmingnmonitoring1yDiscount(
			double alarmingnmonitoring1yDiscount) {
		this.alarmingnmonitoring1yDiscount = alarmingnmonitoring1yDiscount;
	}

	public int getTotalServiceCost() {
		return totalServiceCost;
	}

	public void setTotalServiceCost(int totalServiceCost) {
		this.totalServiceCost = totalServiceCost;
	}

	public double getTotalServiceDiscount() {
		return totalServiceDiscount;
	}

	public void setTotalServiceDiscount(double totalServiceDiscount) {
		this.totalServiceDiscount = totalServiceDiscount;
	}

	public double getTotalHardwareDiscount() {
		return totalHardwareDiscount;
	}

	public void setTotalHardwareDiscount(double totalHardwareDiscount) {
		this.totalHardwareDiscount = totalHardwareDiscount;
	}

	public int getTotalHardwareCost() {
		return totalHardwareCost;
	}

	public void setTotalHardwareCost(int totalHardwareCost) {
		this.totalHardwareCost = totalHardwareCost;
	}

	public String showParts(boolean showParts) {
		this.showParts = showParts;
		return this.showParts(selectedQuotation);
	}

	public List<Quotation> getFilteredquotationsList() {
		return filteredquotationsList;
	}

	public void setFilteredquotationsList(List<Quotation> filteredquotationsList) {
		this.filteredquotationsList = filteredquotationsList;
	}

	public void setDateFrom(Date DateFrom) {
		dateFrom = DateFrom;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateTo(Date DateTo) {
		dateTo = DateTo;
	}

	public Date getDateTo() {
		return dateTo;
	}

	// public boolean isUploadPO() {
	// return uploadPO;
	// }
	//
	// public void setUploadPO(boolean uploadPO) {
	// this.uploadPO = uploadPO;
	// }

	public void initializeDates() {
		// if ((dateTo==null)||(dateFrom==null)){
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DATE, 1);
		dateTo = to.getTime();
		Calendar before = Calendar.getInstance();
		before.add(Calendar.DATE, -29);
		dateFrom = before.getTime();
		// }
		showCompletedQuotations = true;
		// uploadPO = false;
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");

		this.quotationsList = QuotationDAO.getInstance().getQuotations(
				userInfo.getUser().getUserId(),
				userInfo.getUser().getCompany().getId(),
				userInfo.getUser().getUsertype(), dateFrom, dateTo,
				showCompletedQuotations);

	}

	public String updateDataTable() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");

		this.quotationsList = QuotationDAO.getInstance().getQuotations(
				userInfo.getUser().getUserId(),
				userInfo.getUser().getCompany().getId(),
				userInfo.getUser().getUsertype(), dateFrom, dateTo,
				showCompletedQuotations);

		RequestContext.getCurrentInstance().update("frmquotationPage");
		return "QuotationsPage.xhtml?faces-redirect=true";
	}

	public void view() {
		System.out.println("pdf downloading");
		
		PDFViewFromHTMLPageBean pdfview = new PDFViewFromHTMLPageBean();
		try {
			
			pdfview.export(selectedQuotation);
		
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RuntimeException ex) { // If a Runtime exception occurs, notify
										// the user with error message
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, ERROR_MESSAGE, ERROR_MESSAGE);
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmquotationPage:valMsg");
			ex.printStackTrace();
		}
	}

	public String validateQuotation(ActionEvent event) {
		if (this.selectedQuotation.getPagecontents().split("_").length < 6) {
			FacesMessage message = new FacesMessage(
					FacesMessage.SEVERITY_ERROR, ERROR_MESSAGE, ERROR_MESSAGE);
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmquotationPage:valMsg");
			return "QuotationsPage.xhtml?faces-redirect=true";
		}
		return "";
	}

	/*
	 * 
	 * Deletes a selected quotation
	 */
	public void deleteQuotation(ActionEvent event) {
		FacesMessage message = null;
		if (this.selectedQuotation != null) {
			System.out.println("selected quotation for del: "+this.selectedQuotation.getId());
			int success = QuotationDAO.getInstance().deleteQuotation(
					this.selectedQuotation);
			System.out.println("succeess: "+success);
			if (success > 0) {
				UserAuditDAO.getInstance().addUserAudit(
						"Delete Quotation",
						"User deleted the quotation - "
								+ this.selectedQuotation.getQuotationref());
				this.quotationsList.remove(this.selectedQuotation);
				this.filteredquotationsList = null;
				this.selectedQuotation = null;
				message = new FacesMessage(SUCCESS_MESSAGE, SUCCESS_MESSAGE);
				FacesContext.getCurrentInstance().addMessage(null, message);
				RequestContext context = RequestContext.getCurrentInstance();
				context.update("frmquotationPage:valMsg");
			}
		} else {
			message = new FacesMessage(FAILURE_MESSAGE, FAILURE_MESSAGE);
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmquotationPage:valMsg");
		}
	}

	// public void acceptQuotation() {
	// FacesMessage message = null;
	// if (this.selectedQuotation != null) {
	// this.selectedQuotation.setAccepted(true);
	// int success = QuotationDAO.getInstance().updateQuotation(
	// this.selectedQuotation);
	// if (success > 0) {
	// this.filteredquotationsList = null;
	// this.selectedQuotation = null;
	// message = new FacesMessage(SUCCESS_ACCEPTED, SUCCESS_ACCEPTED);
	// FacesContext.getCurrentInstance().addMessage(null, message);
	// RequestContext context = RequestContext.getCurrentInstance();
	// context.update("frmquotationPage:valMsg");
	// }
	// } else {
	// message = new FacesMessage(FAILURE_ACCEPTED, FAILURE_ACCEPTED);
	// FacesContext.getCurrentInstance().addMessage(null, message);
	// RequestContext context = RequestContext.getCurrentInstance();
	// context.update("frmquotationPage:valMsg");
	// }
	//
	// }

	public boolean isShowCompletedQuotations() {
		return showCompletedQuotations;
	}

	public void setShowCompletedQuotations(boolean showCompletedQuotations) {
		this.showCompletedQuotations = showCompletedQuotations;
	}

	// public String uploadPO() {
	// try {
	// System.out.println("::::file name generated: " + poFile.getName());
	// InputStream inputStream = poFile.getInputStream();
	// FileOutputStream outputStream = new
	// FileOutputStream(getFilename(poFile));
	// byte[] buffer = new byte[4096];
	// int bytesRead = 0;
	// while (true) {
	// bytesRead = inputStream.read(buffer);
	// if (bytesRead > 0) {
	// outputStream.write(buffer, 0, bytesRead);
	// } else {
	// break;
	// }
	//
	// }
	// outputStream.close();
	// inputStream.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return "success";
	// }

	// private static String getFilename(Part part) {
	// for (String cd : part.getHeader("content-disposition").split(";")) {
	// if (cd.trim().startsWith("filename")) {
	// String filename = cd.substring(cd.indexOf('=') + 1).trim()
	// .replace("\"", "");
	// return filename.substring(filename.lastIndexOf('/') + 1)
	// .substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
	// }
	// }
	// return null;
	// }
}
