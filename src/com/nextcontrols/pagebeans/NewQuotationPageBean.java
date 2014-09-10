package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.PartsDAO;
import com.nextcontrols.bureaudao.QuotationDAO;
import com.nextcontrols.bureaudomain.NonStandardParts;
import com.nextcontrols.bureaudomain.Quotation;
import com.nextcontrols.bureaudomain.StandardParts;

@ManagedBean(name="newquotation11")
@SessionScoped
public class NewQuotationPageBean implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private Quotation quotation;
	private String currency;
	private int noOfsensors=0;
	private int currentPageCounter=0;
	///////////////////////////////////////////
	StandardParts selectedpart;
	/****************************************************selectPartsPage*************************************************/
	Map<Integer,List<StandardParts>> standardParts = new HashMap<Integer,List<StandardParts>>();
	private List<StandardParts> partst;  
	private List<NonStandardParts> partnst;  
   


	/****************************************************flags 4 pagination*************************************************/
    private	boolean NewQuotationPage;
    private boolean SelectPartsPage;	
    private boolean TotalnDiscountPage;
    private boolean DisplaynEditPage;
    
    /****************************************************totaldiscountPage VARIABLES*************************************************/
    private double standardmonitoringhardware=0;
    private double incubatorCO2rhhardware=0;
    private double cleanroomdpthardware=0;          // CLEAN ROOM DPT HARDWARE
    private double o2oxygensensinghardware=0;       //O2 OXYGEN SENSING HARDWARE
    private double nonstandardhardware=0;           //NON STANDARD HARDWARE
    private double agreeddiscounthardware=0;          //AGREED DISCOUNT  hardware
    private double totalhardwarecost=0;           //TOTAL HARDWARE COST
    private double sitenistsensorcalibration=0;     // SITE NIST SENSOR CALIBRATION
    private double siteinstallation=0;         //site INSTALLATION 
    private double installationtraveldays=0;         //INSTALLATION TRAVEL DAYS
    private double agreeddiscountsiteinstall=0;   //AGREED DISCOUNT
    private double totalinstallcost=0;  //TOTAL INSTALL COST
    private double factorynistsensorcalibration=0; //FACTORY NIST SENSOR CALIBRATION
    private double dqiqoqdocumentation=0; //DQ,IQ,OQ DOCUMENTATION
    private double websitedatabasesetup=0;//WEB SITE + DATABASE SETUP
    private double webhostedguidisplaypages=0;//WEB HOSTED GUI DISPLAY PAGES 
    private double agreeddiscountserviceoption=0;//AGREED DISCOUNT
    private double totaloptionscost=0;//TOTAL OPTIONS COST
    private double nistsensorreclibration1year=0; //NIST SENSOR RE-CALIBRATION 1 YEAR
    private double nistsensorreclibration3year=0; //NIST SENSOR RE-CALIBRATION 3 YEAR
    private double nistsensorreclibration5year=0; //NIST SENSOR RE-CALIBRATION 5 YEAR
    private double recordingonly1year=0; //RECORDING ONLY 1 YEAR
    private double recordingonly3year=0; //RECORDING ONLY 3 YEAR
    private double recordingonly5year=0; //RECORDING ONLY 5 YEAR
    private double alarmingrecording1year=0; //ALARMING + RECORDING 1 YEAR
    private double alarmingrecording3year=0; //ALARMING + RECORDING 3 YEAR
    private double alarmingrecording5year=0; //ALARMING + RECORDING 5 YEAR
    private double installdays=1;//INSTALL DAYS
    private double installtraveldays=1;//INSTALL TRAVEL DAYS
    private double calibrationdays=1;//CALIBRATION TRAVEL DAYS
    private double calibrationtraveldays=1;//CALIBRATION TRAVEL DAYS
    private double hardwarediscount=0;//HARDWARE DISCOUNT
    private double siteinstallationdiscount=0;//SITE INSTALLATION DISCOUNT
    private double serviceoptiondiscount=0;//SERVICE OPTION DISCOUNT
    private double monitoringdiscount=0;//MONITORING DISCOUNT
    private double discretionarydiscount=0;//DISCRETIONARY DISCOUNT
    private double totalcost=0;//TOTAL COST

	public NewQuotationPageBean() {
		this.creatQuotation();
	}
	
	public void clearNewQuotationPage(){
		quotation = new Quotation();
	}
	
	public void creatQuotation(){
		quotation = new Quotation();
		
		NewQuotationPage = false;
		SelectPartsPage = false;
		TotalnDiscountPage = false;
		DisplaynEditPage = false;
		
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		this.currency = userInfo.getUser().getCompany().getCountry().getCurrency();
		this.quotation.setQuotationref(userInfo.getUser().getFirstName().toUpperCase()+QuotationDAO.getInstance().getMaxQuotationID(userInfo.getUser().getUserId()));
		
	}
	
	public Quotation getQuotation() {
		return quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}
	
	public List<StandardParts> getPartst() {
		//System.out.println("parts " + this.currentPageCounter+";"+standardParts.get(this.currentPageCounter));
		/*for(int i =0;i<quotation.getNumberofsites();i++){
			System.out.println("checking " + standardParts.get(i).size());
		}*/
			//partst = standardParts.get(this.currentPageCounter);
		
		return partst;
	}

	public void setPartst(List<StandardParts> partst) {
		this.partst = partst;
	}

	/****************************************************NewQuotationsPage*************************************************/
	
	public String newQuotationPageNext(){
		NewQuotationPage = true;
		standardParts.clear();
		if(quotation.getIndividualquotepersite())
		{
			for(int i =0;i<quotation.getNumberofsites();i++){
				//System.out.println("loading " + i);
				standardParts.put(i, PartsDAO.getInstance().getStandardPartsAndAssembliesList());
			}
		}else{
			standardParts.put(0, PartsDAO.getInstance().getStandardPartsAndAssembliesList());
		}
		
		return "SelectPartsPage.xhtml?faces-redirect=true";
	}
	
	public String selectPartsPageNext(){
		NewQuotationPage = false;
		SelectPartsPage = true;
		standardParts.clear();
		this.currentPageCounter= currentPageCounter+1;
		
		if(quotation.getIndividualquotepersite())
		{
			if( this.currentPageCounter < quotation.getNumberofsites()){
				return "SelectPartsPage.xhtml?faces-redirect=true";
			}else{
				return "TotalnDiscountPage.xhtml?faces-redirect=true";
			}
		}else{
			return "TotalnDiscountPage.xhtml?faces-redirect=true";
		}
	}
	/*public String savePartialQuotation() {
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		this.quotation.setUserid(userInfo.getUser().getUserId());
		quotation.setId(QuotationDAO.getInstance().saveQuotation(this.quotation,NewQuotationPage));
		NewQuotationPage = true;

		return "SelectPartsPage.xhtml?faces-redirect=true";

	}*/
	


	/****************************************************flags for pagination*************************************************/
		
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

	
	
	/****************************************************selectPartsPage*************************************************/

	public String saveQuantity(){

	    standardmonitoringhardware=0;
        this.noOfsensors=0;
        this.cleanroomdpthardware=0;
        this.incubatorCO2rhhardware=0;
        this.o2oxygensensinghardware=0;
		
		//IQuotationDAO iq=new QuotationDAO();
	/*	iq.saveQuantitySP(this.quotation,partstb1,this.SelectPartsPage);
        iq.saveQuantitySP(this.quotation,partstb2,this.SelectPartsPage);
        iq.saveQuantitySP(this.quotation,partstb3,this.SelectPartsPage);*/
        QuotationDAO.getInstance().saveQuantityNonSP(this.quotation,partnst,this.SelectPartsPage);
    		     
    		StandardParts tempssensor;
			
			
	     for(int i=0;i<this.partst.size();i++)
	     { 
	    	 tempssensor=this.partst.get(i);
	    	 
	    	 
	    	 if(tempssensor.getPartType().equals("STANDARD MONITORING HARDWARE"))
	    	 { 
	    		//System.out.println(".............................: "+standardmonitoringhardware);
	    		 standardmonitoringhardware=standardmonitoringhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    	 }
	    	 
	    	 else if(tempssensor.getPartType().equals("INCUBATOR CO2 + RH HARDWARE"))
	    	 {
	    	//	 System.out.println(".............................: "+incubatorCO2rhhardware);
		    		 
	    		 incubatorCO2rhhardware=incubatorCO2rhhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 
	    	 else if(tempssensor.getPartType().equals("CLEAN ROOM DPT HARDWARE"))
	    	 {
	    	
	    		 
	   // 		 System.out.println(".............................: "+cleanroomdpthardware);
	    		 
	    		 cleanroomdpthardware=cleanroomdpthardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 else if(tempssensor.getPartType().equals("O2 OXYGEN SENSING HARDWARE"))
	    	 {
	    		 

	    		 o2oxygensensinghardware=o2oxygensensinghardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 else if(tempssensor.getPartType().equals("NON STANDARD HARDWARE"))
	    	 {
	    		 
	    		 nonstandardhardware=nonstandardhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }

	    	 if((this.currency.equals("$"))&&((tempssensor.getPartNumber().equals("SMRM3K3B")) || (tempssensor.getPartNumber().equals("SMTP3K3B"))||
	    			 (tempssensor.getPartNumber().equals("SMTP3K3A"))|| (tempssensor.getPartNumber().equals("SMTP3K3AL"))||
	    			 (tempssensor.getPartNumber().equals("M22EFR01M5/PC")) ||(tempssensor.getPartNumber().equals("M22EXR01M5/PC"))||
	    			 (tempssensor.getPartNumber().equals("M22BLU01M0/E1/PC"))|| (tempssensor.getPartNumber().equals("M02WS101/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS104/US"))|| (tempssensor.getPartNumber().equals("M02WS104/L15/US"))||
	    			 
	    			 (tempssensor.getPartNumber().equals("M02WS106/US"))|| (tempssensor.getPartNumber().equals("M02WS107/FR/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS107/FL/US"))|| (tempssensor.getPartNumber().equals("M02WS108/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS109/US"))|| (tempssensor.getPartNumber().equals("M02WS11US"))||
	    		
	    		
	    			 (tempssensor.getPartNumber().equals("M02WS111/US"))|| (tempssensor.getPartNumber().equals("M02WS113/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS113/L15/US"))|| (tempssensor.getPartNumber().equals("DPG701/MO2WS/108/US"))||
	    			 (tempssensor.getPartNumber().equals("CO2IC108"))))
	    	 {   
	    	     
	    	         
	    //		 System.out.println(".............................: "+noOfsensors);
	    		 
	    		 this.noOfsensors=(this.noOfsensors+(tempssensor.getQuantity()));
	    	 }
 
	    	 else if((this.currency.equals("£"))&&((tempssensor.getPartNumber().equals("SMRM3K3B")) || (tempssensor.getPartNumber().equals("SMTP3K3B"))||
	    			 (tempssensor.getPartNumber().equals("SMTP3K3A"))|| (tempssensor.getPartNumber().equals("SMTP3K3AL"))||
	    			 (tempssensor.getPartNumber().equals("M22EFR01M5/PC")) ||(tempssensor.getPartNumber().equals("M22EXR01M5/PC"))||
	    			 (tempssensor.getPartNumber().equals("M22BLU01M0/E1/PC"))|| (tempssensor.getPartNumber().equals("M02WS101"))||
	    			 (tempssensor.getPartNumber().equals("M02WS104"))|| (tempssensor.getPartNumber().equals("M02WS104/L15"))||
	    			 
	    			 (tempssensor.getPartNumber().equals("M02WS106"))|| (tempssensor.getPartNumber().equals("M02WS107/FR"))||
	    			 (tempssensor.getPartNumber().equals("M02WS107/FL"))|| (tempssensor.getPartNumber().equals("M02WS108"))||
	    			 (tempssensor.getPartNumber().equals("M02WS109"))|| (tempssensor.getPartNumber().equals("M02WS110"))||
	    		
	    		
	    			 (tempssensor.getPartNumber().equals("M02WS111"))|| (tempssensor.getPartNumber().equals("M02WS113"))||
	    			 (tempssensor.getPartNumber().equals("M02WS113/L15"))|| (tempssensor.getPartNumber().equals("DPG701/MO2WS/108"))||
	    			 (tempssensor.getPartNumber().equals("CO2IC108"))
	    		 ))
	    	 {   
	    	     
	    	         
	    	//	 System.out.println(".............................: "+noOfsensors);
	    		 
	    		 this.noOfsensors=(this.noOfsensors+(tempssensor.getQuantity()));
	    	 }
  
	     }
				
		 /*  for(int i=0;i<this.partstb2.size();i++)
	     { 
	    	 tempssensor=this.partstb2.get(i);
	    	 if(tempssensor.getPartType().equals("STANDARD MONITORING HARDWARE"))
	    	 { 
	    		 standardmonitoringhardware=standardmonitoringhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    	 }

	    	 else if(tempssensor.getPartType().equals("INCUBATOR CO2 + RH HARDWARE"))
	    	 {
	    		 
	    		 incubatorCO2rhhardware=incubatorCO2rhhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }		 
	    	 else if(tempssensor.getPartType().equals("CLEAN ROOM DPT HARDWARE"))
	    	 {
	    	
	    		 
	    		 
	    		 cleanroomdpthardware=cleanroomdpthardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 else if(tempssensor.getPartType().equals("O2 OXYGEN SENSING HARDWARE"))
	    	 {
	    		 

	    		 o2oxygensensinghardware=o2oxygensensinghardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 else if(tempssensor.getPartType().equals("NON STANDARD HARDWARE"))
	    	 {
	    		 
	    		 nonstandardhardware=nonstandardhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 if((this.currency.equals("$"))&&((tempssensor.getPartNumber().equals("SMRM3K3B")) || (tempssensor.getPartNumber().equals("SMTP3K3B"))||
	    			 (tempssensor.getPartNumber().equals("SMTP3K3A"))|| (tempssensor.getPartNumber().equals("SMTP3K3AL"))||
	    			 (tempssensor.getPartNumber().equals("M22EFR01M5/PC")) ||(tempssensor.getPartNumber().equals("M22EXR01M5/PC"))||
	    			 (tempssensor.getPartNumber().equals("M22BLU01M0/E1/PC"))|| (tempssensor.getPartNumber().equals("M02WS101/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS104/US"))|| (tempssensor.getPartNumber().equals("M02WS104/L15/US"))||
	    			 
	    			 (tempssensor.getPartNumber().equals("M02WS106/US"))|| (tempssensor.getPartNumber().equals("M02WS107/FR/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS107/FL/US"))|| (tempssensor.getPartNumber().equals("M02WS108/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS109/US"))|| (tempssensor.getPartNumber().equals("M02WS11US"))||
	    		
	    		
	    			 (tempssensor.getPartNumber().equals("M02WS111/US"))|| (tempssensor.getPartNumber().equals("M02WS113/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS113/L15/US"))|| (tempssensor.getPartNumber().equals("DPG701/MO2WS/108/US"))||
	    			 (tempssensor.getPartNumber().equals("CO2IC108"))
	    		 ))
	    	 {   
	    	     
	    	         
	    		// System.out.println(".............................: "+noOfsensors);
	    		 
	    		 this.noOfsensors=(this.noOfsensors+(tempssensor.getQuantity()));
	    	 }
 
	    	 else if((this.currency.equals("£"))&&((tempssensor.getPartNumber().equals("SMRM3K3B")) || (tempssensor.getPartNumber().equals("SMTP3K3B"))||
	    			 (tempssensor.getPartNumber().equals("SMTP3K3A"))|| (tempssensor.getPartNumber().equals("SMTP3K3AL"))||
	    			 (tempssensor.getPartNumber().equals("M22EFR01M5/PC")) ||(tempssensor.getPartNumber().equals("M22EXR01M5/PC"))||
	    			 (tempssensor.getPartNumber().equals("M22BLU01M0/E1/PC"))|| (tempssensor.getPartNumber().equals("M02WS101"))||
	    			 (tempssensor.getPartNumber().equals("M02WS104"))|| (tempssensor.getPartNumber().equals("M02WS104/L15"))||
	    			 
	    			 (tempssensor.getPartNumber().equals("M02WS106"))|| (tempssensor.getPartNumber().equals("M02WS107/FR"))||
	    			 (tempssensor.getPartNumber().equals("M02WS107/FL"))|| (tempssensor.getPartNumber().equals("M02WS108"))||
	    			 (tempssensor.getPartNumber().equals("M02WS109"))|| (tempssensor.getPartNumber().equals("M02WS110"))||
	    		
	    		
	    			 (tempssensor.getPartNumber().equals("M02WS111"))|| (tempssensor.getPartNumber().equals("M02WS113"))||
	    			 (tempssensor.getPartNumber().equals("M02WS113/L15"))|| (tempssensor.getPartNumber().equals("DPG701/MO2WS/108"))||
	    			 (tempssensor.getPartNumber().equals("CO2IC108"))
	    		 ))
	    	 {   
	    	     
//	    		 System.out.println(".............................: "+noOfsensors);
	    	         
	    		 
	    		 this.noOfsensors=(this.noOfsensors+(tempssensor.getQuantity()));
	    	 }


	     }
	     for(int i=0;i<this.partstb3.size();i++)
	     { 
	    	 tempssensor=this.partstb3.get(i);
	    	 if(tempssensor.getPartType().equals("STANDARD MONITORING HARDWARE"))
	    	 {   
	    		 standardmonitoringhardware=(standardmonitoringhardware+(tempssensor.getCost()*tempssensor.getQuantity()));
	    	 }

	    	 else if(tempssensor.getPartType().equals("INCUBATOR CO2 + RH HARDWARE"))
	    	 {
	    		 
	    		 incubatorCO2rhhardware=incubatorCO2rhhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }

	    	 else if(tempssensor.getPartType().equals("CLEAN ROOM DPT HARDWARE"))
	    	 {
	    	
	    		 
	    		 
	    		 cleanroomdpthardware=cleanroomdpthardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 else if(tempssensor.getPartType().equals("O2 OXYGEN SENSING HARDWARE"))
	    	 {
	    		 

	    		 o2oxygensensinghardware=o2oxygensensinghardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }
	    	 else if(tempssensor.getPartType().equals("NON STANDARD HARDWARE"))
	    	 {
	    		 
	    		 nonstandardhardware=nonstandardhardware+(tempssensor.getCost()*tempssensor.getQuantity());
	    		 
	    	 }

	    	 if((this.currency.equals("$"))&&((tempssensor.getPartNumber().equals("SMRM3K3B")) || (tempssensor.getPartNumber().equals("SMTP3K3B"))||
	    			 (tempssensor.getPartNumber().equals("SMTP3K3A"))|| (tempssensor.getPartNumber().equals("SMTP3K3AL"))||
	    			 (tempssensor.getPartNumber().equals("M22EFR01M5/PC")) ||(tempssensor.getPartNumber().equals("M22EXR01M5/PC"))||
	    			 (tempssensor.getPartNumber().equals("M22BLU01M0/E1/PC"))|| (tempssensor.getPartNumber().equals("M02WS101/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS104/US"))|| (tempssensor.getPartNumber().equals("M02WS104/L15/US"))||
	    			 
	    			 (tempssensor.getPartNumber().equals("M02WS106/US"))|| (tempssensor.getPartNumber().equals("M02WS107/FR/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS107/FL/US"))|| (tempssensor.getPartNumber().equals("M02WS108/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS109/US"))|| (tempssensor.getPartNumber().equals("M02WS11US"))||
	    		
	    		
	    			 (tempssensor.getPartNumber().equals("M02WS111/US"))|| (tempssensor.getPartNumber().equals("M02WS113/US"))||
	    			 (tempssensor.getPartNumber().equals("M02WS113/L15/US"))|| (tempssensor.getPartNumber().equals("DPG701/MO2WS/108/US"))||
	    			 (tempssensor.getPartNumber().equals("CO2IC108"))
	    		 ))
	    	 {   
	    	     
	    	         
	    		 
	    		 this.noOfsensors=(this.noOfsensors+(tempssensor.getQuantity()));
	    	 }
 
	    	 else if((this.currency.equals("£"))&&((tempssensor.getPartNumber().equals("SMRM3K3B")) || (tempssensor.getPartNumber().equals("SMTP3K3B"))||
	    			 (tempssensor.getPartNumber().equals("SMTP3K3A"))|| (tempssensor.getPartNumber().equals("SMTP3K3AL"))||
	    			 (tempssensor.getPartNumber().equals("M22EFR01M5/PC")) ||(tempssensor.getPartNumber().equals("M22EXR01M5/PC"))||
	    			 (tempssensor.getPartNumber().equals("M22BLU01M0/E1/PC"))|| (tempssensor.getPartNumber().equals("M02WS101"))||
	    			 (tempssensor.getPartNumber().equals("M02WS104"))|| (tempssensor.getPartNumber().equals("M02WS104/L15"))||
	    			 
	    			 (tempssensor.getPartNumber().equals("M02WS106"))|| (tempssensor.getPartNumber().equals("M02WS107/FR"))||
	    			 (tempssensor.getPartNumber().equals("M02WS107/FL"))|| (tempssensor.getPartNumber().equals("M02WS108"))||
	    			 (tempssensor.getPartNumber().equals("M02WS109"))|| (tempssensor.getPartNumber().equals("M02WS110"))||
	    		
	    		
	    			 (tempssensor.getPartNumber().equals("M02WS111"))|| (tempssensor.getPartNumber().equals("M02WS113"))||
	    			 (tempssensor.getPartNumber().equals("M02WS113/L15"))|| (tempssensor.getPartNumber().equals("DPG701/MO2WS/108"))||
	    			 (tempssensor.getPartNumber().equals("CO2IC108"))
	    		 ))
	    	 {   
	    	     
	    	         
	    		 
	    		 this.noOfsensors=(this.noOfsensors+(tempssensor.getQuantity()));
	    	 }

	     
	     }*/
	    NonStandardParts tempssensor1;    
	    for(int k=0;k<this.partnst.size();k++)
	    {
	    	tempssensor1=this.partnst.get(k);
	    	this.nonstandardhardware=this.nonstandardhardware+(tempssensor1.getCost()*tempssensor1.getQuantity());
	
	    }
	 	updateDiscountPage();
	 	SelectPartsPage=true;
 	    return "TotalnDiscountPage.xhtml?faces-redirect=true";
			
	 }

	public void updateDiscountPage()
	{
		
	    this.agreeddiscounthardware=(this.standardmonitoringhardware+this.incubatorCO2rhhardware+this.cleanroomdpthardware+this.o2oxygensensinghardware+this.nonstandardhardware)*this.hardwarediscount/100;
		this.totalhardwarecost=this.standardmonitoringhardware+this.incubatorCO2rhhardware+this.cleanroomdpthardware+this.o2oxygensensinghardware+this.nonstandardhardware-this.agreeddiscounthardware;
	    double a[]=QuotationDAO.getInstance().getYearlyServiceCostRecordingOnly(noOfsensors,this.currency);
	 	recordingonly1year=a[0]-(a[0]*this.monitoringdiscount/100); //RECORDING ONLY 1 YEAR
	 	recordingonly3year=a[1]-(a[1]*this.monitoringdiscount/100); //RECORDING ONLY 3 YEAR
	 	recordingonly5year=a[2]-(a[2]*this.monitoringdiscount/100); //RECORDING ONLY 5 YEAR
 	
	 	a=QuotationDAO.getInstance().getYearlyServiceCostMonitoringandRecording(noOfsensors,this.currency);

	 	alarmingrecording1year=a[0]-(a[0]*this.monitoringdiscount/100); //ALARMING + RECORDING 1 YEAR
	 	alarmingrecording3year=a[1]-(a[1]*this.monitoringdiscount/100); //ALARMING + RECORDING 3 YEAR
	 	alarmingrecording5year=a[2]-(a[2]*this.monitoringdiscount/100); //ALARMING + RECORDING 5 YEAR



	 	if(currency.equals("£"))
	 	{
	 	
	 		double D138=1,D139=1; 
	 		double res=575*D138+250*D139;
	 		
	 	nistsensorreclibration1year=res*1.18; //NIST SENSOR RE-CALIBRATION 1 YEAR
	 	nistsensorreclibration3year=res*1.18*0.9; //NIST SENSOR RE-CALIBRATION 3 YEAR
	 	nistsensorreclibration5year=res; //NIST SENSOR RE-CALIBRATION 5 YEAR
	 	}

	 	else if(currency.equals("$"))
	 	{
	 		double D138=1,D139=1; 
	 		double res=1500*D138+650*D139;
	 		
	 	nistsensorreclibration1year=res*1.18; //NIST SENSOR RE-CALIBRATION 1 YEAR
	 	nistsensorreclibration3year=res*1.18*0.9; //NIST SENSOR RE-CALIBRATION 3 YEAR
	 	nistsensorreclibration5year=res; //NIST SENSOR RE-CALIBRATION 5 YEAR
	 	}

	 	
	 	/***********************       SITE INSTALLATION        */
	 	
	 	
	 	 a=QuotationDAO.getInstance().getonsitesensorcalibration(noOfsensors, currency);

	 	 this.sitenistsensorcalibration=a[0]*this.calibrationtraveldays;	 	
	 	
	 	 a=QuotationDAO.getInstance().getsiteinstallation(noOfsensors, currency);

	 	 this.siteinstallation=a[0];	 	

	 	 a=QuotationDAO.getInstance().gettraveldays(noOfsensors, currency);
	 	 this.installationtraveldays=a[0]*this.installtraveldays;	 	
	 	 this.agreeddiscountsiteinstall=(this.sitenistsensorcalibration+this.siteinstallation+this.installationtraveldays)*this.siteinstallationdiscount/100;
	 	 this.totalinstallcost=this.sitenistsensorcalibration+this.siteinstallation+this.installationtraveldays-this.agreeddiscountsiteinstall;

	 	 /***********************       SERVICE OPTIONS  */

	 	 a=QuotationDAO.getInstance().getnistsensorcalibration(noOfsensors,currency);
	 	 this.factorynistsensorcalibration=Math.max(a[0], a[1]*noOfsensors);
	 		 	
	 	 a=QuotationDAO.getInstance().getDQIQOQDocuments(noOfsensors, currency);
	 	 this.dqiqoqdocumentation=Math.max(a[0], a[1]*noOfsensors);

	 	 a=QuotationDAO.getInstance().getWebsiteserverdatabase(noOfsensors, currency);
	 	 this.websitedatabasesetup=Math.max(a[0], a[1]*noOfsensors);

	 	 a=QuotationDAO.getInstance().getWebhostedGUI(noOfsensors, currency);
	 	 this.webhostedguidisplaypages=Math.max(a[0], a[1]*noOfsensors);
	 	 this.agreeddiscountserviceoption=(this.factorynistsensorcalibration+this.dqiqoqdocumentation+this.websitedatabasesetup+this.webhostedguidisplaypages)*this.serviceoptiondiscount/100;
	 	 this.totaloptionscost=this.factorynistsensorcalibration+this.dqiqoqdocumentation+this.websitedatabasesetup+this.webhostedguidisplaypages-this.agreeddiscountserviceoption;
	 	 double d=(this.totalhardwarecost+this.totalinstallcost+this.totaloptionscost)*this.discretionarydiscount/100;
	 	 this.totalcost=this.totalhardwarecost+this.totalinstallcost+this.totaloptionscost-d;
	}
		
	public String goBackTotalDiscount(){
          noOfsensors=0;
		  return "SelectPartsPage.xhtml?faces-redirect=true";
			
	}
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

	/****************************************************Total discount Page*************************************************/
	public String saveDiscount(){
		
		this.quotation.setHardwarediscount(this.hardwarediscount);
		this.quotation.setSiteinstallationdiscount(this.siteinstallationdiscount);
		QuotationDAO.getInstance().saveDiscountQuotation(this.quotation,this.TotalnDiscountPage);
        saveQuotationCoveringLetter();
	    TotalnDiscountPage=true;
		return "DisplaynEditPage.xhtml?faces-redirect=true";
	}

	public int getNoOfsensors() {
		return noOfsensors;
	}

	public void setNoOfsensors(int noOfsensors) {
		this.noOfsensors = noOfsensors;
	}
	
	public double getStandardmonitoringhardware() {
		return standardmonitoringhardware;
	}

	public void setStandardmonitoringhardware(double standardmonitoringhardware) {
		this.standardmonitoringhardware = standardmonitoringhardware;
	}
	public double getIncubatorCO2rhhardware() {
		return incubatorCO2rhhardware;
	}

	public void setIncubatorCO2rhhardware(double incubatorCO2rhhardware) {
		this.incubatorCO2rhhardware = incubatorCO2rhhardware;
	}

	public double getCleanroomdpthardware() {
		return cleanroomdpthardware;
	}

	public void setCleanroomdpthardware(double cleanroomdptharwar) {
		this.cleanroomdpthardware = cleanroomdptharwar;
	}

	public double getO2oxygensensinghardware() {
		return o2oxygensensinghardware;
	}

	public void setO2oxygensensinghardware(double o2oxygensensingharware) {
		this.o2oxygensensinghardware = o2oxygensensingharware;
	}

	public double getNonstandardhardware() {
		return nonstandardhardware;
	}

	public void setNonstandardhardware(double nonstandardhardware) {
		this.nonstandardhardware = nonstandardhardware;
	}

	public double getAgreeddiscounthardware() {
		return agreeddiscounthardware;
	}

	public void setAgreeddiscounthardware(double agreeddiscounthardware) {
		this.agreeddiscounthardware = agreeddiscounthardware;
	}

	public double getTotalhardwarecost() {
		return totalhardwarecost;
	}

	public void setTotalhardwarecost(double totalhardwarecost) {
		this.totalhardwarecost = totalhardwarecost;
	}

	public double getSitenistsensorcalibration() {
		return sitenistsensorcalibration;
	}

	public void setSitenistsensorcalibration(double sitenistsensorcalibration) {
		this.sitenistsensorcalibration = sitenistsensorcalibration;
	}
	public double getSiteinstallation() {
		return siteinstallation;
	}

	public void setSiteinstallation(double siteinstallation) {
		this.siteinstallation = siteinstallation;
	}

	public double getInstallationTraveldays() {
		return installationtraveldays;
	}

	public void setInstallationTraveldays(double traveldays) {
		this.installationtraveldays = traveldays;
	}

	public double getAgreeddiscountsiteinstall() {
		return agreeddiscountsiteinstall;
	}

	public void setAgreeddiscountsiteinstall(double agreeddiscountsiteinstall) {
		this.agreeddiscountsiteinstall = agreeddiscountsiteinstall;
	}

	public double getTotalinstallcost() {
		return totalinstallcost;
	}

	public void setTotalinstallcost(double totalinstallcost) {
		this.totalinstallcost = totalinstallcost;
	}

	public double getFactorynistsensorcalibration() {
		return factorynistsensorcalibration;
	}

	public void setFactorynistsensorcalibration(double factorynistsensorcalibration) {
		this.factorynistsensorcalibration = factorynistsensorcalibration;
	}

	public double getDqiqoqdocumentation() {
		return dqiqoqdocumentation;
	}

	public void setDqiqoqdocumentation(double dqiqoqdocumentation) {
		this.dqiqoqdocumentation = dqiqoqdocumentation;
	}

	public double getWebsitedatabasesetup() {
		return websitedatabasesetup;
	}

	public void setWebsitedatabasesetup(double websitedatabasesetup) {
		this.websitedatabasesetup = websitedatabasesetup;
	}

	public double getWebhostedguidisplaypages() {
		return webhostedguidisplaypages;
	}

	public void setWebhostedguidisplaypages(double webhostedguidisplaypages) {
		this.webhostedguidisplaypages = webhostedguidisplaypages;
	}

	public double getAgreeddiscountserviceoption() {
		return agreeddiscountserviceoption;
	}

	public void setAgreeddiscountserviceoption(double agreeddiscountserviceoption) {
		this.agreeddiscountserviceoption = agreeddiscountserviceoption;
	}

	public double getTotaloptionscost() {
		return totaloptionscost;
	}

	public void setTotaloptionscost(double totaloptionscost) {
		this.totaloptionscost = totaloptionscost;
	}

	public double getNistsensorreclibration1year() {
		return nistsensorreclibration1year;
	}

	public void setNistsensorreclibration1year(double nistsensorreclibration1year) {
		this.nistsensorreclibration1year = nistsensorreclibration1year;
	}

	public double getNistsensorreclibration3year() {
		return nistsensorreclibration3year;
	}

	public void setNistsensorreclibration3year(double nistsensorreclibration3year) {
		this.nistsensorreclibration3year = nistsensorreclibration3year;
	}

	public double getNistsensorreclibration5year() {
		return nistsensorreclibration5year;
	}

	public void setNistsensorreclibration5year(double nistsensorreclibration5year) {
		this.nistsensorreclibration5year = nistsensorreclibration5year;
	}

	public double getRecordingonly1year() {
		return recordingonly1year;
	}

	public void setRecordingonly1year(double recordingonly1year) {
		this.recordingonly1year = recordingonly1year;
	}

	public double getRecordingonly3year() {
		return recordingonly3year;
	}

	public void setRecordingonly3year(double recordingonly3year) {
		this.recordingonly3year = recordingonly3year;
	}

	public double getRecordingonly5year() {
		return recordingonly5year;
	}

	public void setRecordingonly5year(double recordingonly5year) {
		this.recordingonly5year = recordingonly5year;
	}

	public double getAlarmingrecording1year() {
		return alarmingrecording1year;
	}

	public void setAlarmingrecording1year(double alarmingrecording1year) {
		this.alarmingrecording1year = alarmingrecording1year;
	}

	public double getAlarmingrecording3year() {
		return alarmingrecording3year;
	}

	public void setAlarmingrecording3year(double alarmingrecording3year) {
		this.alarmingrecording3year = alarmingrecording3year;
	}

	public double getAlarmingrecording5year() {
		return alarmingrecording5year;
	}

	public void setAlarmingrecording5year(double alarmingrecording5year) {
		this.alarmingrecording5year = alarmingrecording5year;
	}


	public double getInstalldays() {
		return installdays;
	}

	public void setInstalldays(double installdays) {
		this.installdays = installdays;
	}
	public double getCalibrationdays() {
		return calibrationdays;
	}

	public void setCalibrationdays(double calibrationdays) {
		this.calibrationdays = calibrationdays;
	}

	public double getInstalltraveldays() {
		return installtraveldays;
	}

	public void setInstalltraveldays(double installtraveldays) {
		this.installtraveldays = installtraveldays;
	}

	public double getCalibrationtraveldays() {
		return calibrationtraveldays;
	}

	public void setCalibrationtraveldays(double calibrationtraveldays) {
		this.calibrationtraveldays = calibrationtraveldays;
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

	public double getMonitoringdiscount() {
		return monitoringdiscount;
	}

	public void setMonitoringdiscount(double monitoringdiscount) {
		this.monitoringdiscount = monitoringdiscount;
	}

	public double getDiscretionarydiscount() {
		return discretionarydiscount;
	}

	public void setDiscretionarydiscount(double discretionarydiscount) {
		this.discretionarydiscount = discretionarydiscount;
	}

	public double getTotalcost() {
		return totalcost;
	}

	public void setTotalcost(double totalcost) {
		this.totalcost = totalcost;
	}

	
	/**************************************************** DisplaynEditPage.jsf  *************************************************/
	
	public void recalculateCost(){
		
		//System.out.print("In recalculate function");
		updateDiscountPage();
		//System.out.print("Aft update");
		
		SelectPartsPage=true;
//		return "TotalnDiscountPage.xhtml?faces-redirect=true";

		
	}
	
	public void saveQuotationCoveringLetter(){
		String coveringletter=this.quotation.getCustomername()+"\n"+this.quotation.getAddress1()

		+"Dear "+this.quotation.getCustomername()+","+

		"Temperature monitoring system" 


		+"\n	Thank you for your interest in our Tutela remote monitoring and alarming service."

	+"\n	As requested we have prepared a quotation covering the installation of a new wireless temperature monitoring systems in your Edmond OK. facility.  We have allowed for factory UKAS calibration of all sensors and also included full IO/OQ documentation."

	+"\n	Our proposal covers:"

	+"\n	•	Our latest wireless based sensing technology minimising disruption in your facilities during installation."

	+"\n	•	1 – LCMU wireless receiver panel.  As an option the LCMU panel can be battery backed providing up to 3.5 hours of monitoring following a power outage.  The additional price of providing this would be £240"

	+"\n	•	1 – Wireless room temperature transmitters."

	+"\n	•	3 – Wireless -80°C freezer temperature transmitters."

    +"\n	•	1 – Wireless -4°C refrigerator temperature transmitter."

	+"\n	•	Additional remote monitoring and recording cost options."

	+"\n	•	A cost option to provide yearly system service and sensor re calibration."

	+"\n	•	Full local installation and on-going system support by our qualified installation team."

	+"\n	I hope that this proposal meets with your approval.  However, if you have any questions or require further clarification then please do not hesitate to contact us."

	+"\n	I hope that this quotation is acceptable to you and we look forward being of service to you and the American Tissue Services foundation both now and in the future."

	+"\n	Tim C Bartholomew"
	+"\n	Managing Director"

	+"\n 1. SYSTEM COST BREAKDOWN"

	+"\nBasic temperature monitoring system hardware:"+this.currency+this.standardmonitoringhardware 
	+"\n              Incubator humidity and CO2 hardware:"+this.currency+this.incubatorCO2rhhardware         
	+"\n         Clean room differential pressure hardware:"+this.currency+this.cleanroomdpthardware               
	+"\n                            Room ambient O2 hardware:"+this.currency+this.o2oxygensensinghardware
	+"\nFactory sensor calibration:"+this.currency+this.factorynistsensorcalibration  
	+"\nDQ, IQ, OQ documentation:"+this.currency+this.dqiqoqdocumentation 
	+"\nWeb-site setup:"+this.currency+this.websitedatabasesetup 
	+"\nWeb hosted sensor display graphics: "+this.currency+this.webhostedguidisplaypages  
    +"\n                                                         Sub Total:     "+this.currency+   
    (this.standardmonitoringhardware+this.incubatorCO2rhhardware+this.cleanroomdpthardware+this.o2oxygensensinghardware+this.factorynistsensorcalibration+this.dqiqoqdocumentation+this.websitedatabasesetup+this.webhostedguidisplaypages)  
    +"\n                                       Systems installation: "+this.currency+this.siteinstallation
    +"\n                              On site sensor calibration: "+this.currency+this.sitenistsensorcalibration
    +"\n                                                       Sub Total:    "+this.currency+(this.siteinstallation+this.sitenistsensorcalibration)
    +"\n                                        Total System cost:        "+this.currency+ (this.standardmonitoringhardware+this.incubatorCO2rhhardware+this.cleanroomdpthardware+this.o2oxygensensinghardware+this.factorynistsensorcalibration+this.dqiqoqdocumentation+this.websitedatabasesetup+this.webhostedguidisplaypages+this.siteinstallation+this.sitenistsensorcalibration)  
    +"\n                                                Less agreed discount:    "+this.currency+this.discretionarydiscount           
    +"\n                                                    Final system cost:        "+this.currency+this.totalcost 
    +"\n 2. REMOTE MONITORING COST OPTIONS"
    +"\n  Recording only service 1 year option:"+this.currency+this.recordingonly1year+"  	Per year"
    +"\n            3 year option:  "+this.currency+this.recordingonly3year+"	Per year"
    +"\n            5 year option:  "+this.currency+this.recordingonly5year+ 	"Per year"
    +"\n Recording and remote alarming service 1 year option: "+this.currency+this.alarmingrecording1year+" 	Per year"
    +"\n            3 year option:  "+this.currency+this.alarmingrecording3year+"  	Per year"
    +"\n            5 year option:  "+this.currency+this.alarmingrecording5year+"  	Per year"
    +"\n 3. ANNUAL SENSOR RE CALIBRATION SERVICE COST OPTIONS"
    +"\n  Recording only service 1 year option:  "+this.currency+this.nistsensorreclibration1year+"  	Per year"
    +"\n            3 year option: "+this.currency+this.nistsensorreclibration3year+"  	Per year"
    +"\n            5 year option:  "+this.currency+this.nistsensorreclibration5year+" 	Per year"
    +"\n 4. BATTERY BACKED WIRELESS PANEL (OPTION)"
    +"\n Wireless monitoring panel 3.5 Hour backup:   £240 	Per panel"
    +"\n Additional12 hours backup battery pack (15.5 hours total):   £420 	Per panel"
    +"\n 5. WIRELESS SIGNAL REPEATER (OPTION)"
    +"\n Standard wireless signal repeater: £186 	Each"
    +"\n Battery backed (12 hours) wireless signal repeater: £300 	Each"
    +"\n TERMS"
    +"\n 1.	Tutela systems are supplied and installed against our standard terms and conditions of sale and purchase."
    +"\n 2.	Remote recording and alarm monitoring services are subject to our standard Remote Monitoring and Alarm service agreement which must be signed prior to the alarm monitoring service commencing."  
    +"\n 3.	All prices exclude any applicable taxes and or duty."
    +"\n	4.	PAYMENT TERMS"
    +"\n		Stage payment invoices will be raised as follows:"
    +"\n a.	15% of project value on receipt of purchase order"
    +"\n b.	50% of project value following equipment delivery to site"
    +"\n c.	25% of project value on completion of installation"
    +"\n d.	10% of project value on completion of training and handover"
    +"\n Remote recording and or remote alarm monitoring services will be invoiced either annually or quarterly in advance of the service provision."
    +"\n Annual sensor re calibration services will be invoiced on completion of the work." 
    +"\n Payment of all invoices is required net 30 days following original date of invoice."
    +"\n 6.	Prices exclude delivery charges."
    +"\n 7.	Warranty on system hardware is 24 months following date of system handover on a return to manufacturer basis."
    +"\n	8.	The customer will be required to provide AC power sockets or fused electrical spurs for" 
    +"\n                       the following sensing equipment where applicable:"
    +"\n		Master wireless receiver panel"
    +"\n		Incubator humidity sensors"
    +"\n		Incubator CO2 sensors"
    +"\n		Clean room differential pressure sensors"
    +"\n		Room ambient O2 sensors";
	this.quotation.setQuotationcoveringletter(coveringletter);		
	}

	public void saveDisplayEdit(){
		//System.out.print("quotation save");
		QuotationDAO.getInstance().saveCoveringLetter(this.quotation);
	}
	public StandardParts getSelectedpart() {
		return selectedpart;
	}

	public void setSelectedpart(StandardParts selectedpart) {
		this.selectedpart = selectedpart;
	}
	

}
