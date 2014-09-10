package com.nextcontrols.bureaudomain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



public class StandardParts {
	
	private int id;
	private String partNumber;
	private String partDescription;
	String truncatedpartDescription;
	String truncatedpartDescriptionsubparts;
	String truncatedpartDescriptionnewquotation;
	private String partType;
	private String partTag;
	private double cost;
	private double recalculatedcost;
	private double listPrice;
	private double listPrice2;//used only in modify parts page 
	private int quantity;
	private int quoteQuantity;
	private String country;
	private String pricecategory;
	private String quantitycategory;
	private int quantityfactor;
	private int installationTime;
	private int calibrationTime;
	private int listPriority;
	private List<StandardParts> subpartsList;
	private List<Double> factors;
	private double markup;
	double grossMargin;
	DecimalFormat twoDForm = new DecimalFormat("#");
	public StandardParts() {
		partNumber="";
		partDescription ="";
		partType="";
		partTag="";
		cost=0;
		recalculatedcost=0;
		listPrice =0;
		listPrice2 =0;
		grossMargin=0;
		quantity=0;
		quoteQuantity =0;
		installationTime=0;
		calibrationTime=0;
		markup = 3.3;
		country="N/A";
		pricecategory = "";
		quantitycategory = "";
		factors=new ArrayList<Double>();
		subpartsList = new ArrayList<StandardParts>();
		listPriority=0;
	}
	
	public StandardParts(int id, String partNumber, String partDescription,
			String partType, double cost, double markup,
			int quantity, String pCountry, List<StandardParts>psubpartsList, String pPartTag, String pPriceCat, String pQuantityCat) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.partType = partType;
		this.cost=cost;
		this.markup = markup;
		this.quantity = quantity;
		this.country = pCountry;
		this.subpartsList = psubpartsList;
		this.factors = new ArrayList <Double>();
		this.partTag = pPartTag;
		this.pricecategory = pPriceCat;
		this.quantitycategory = pQuantityCat;
	}

	public StandardParts(int id, String partNumber, String partDescription,
			String partType, double cost, double markup,
			int quantity, String pCountry, List<StandardParts>psubpartsList, String pPartTag, String pPriceCat, String pQuantityCat, int pquantityfactor,
			int pInstallationTime,int pCalibrationTime,int listPriority) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.partType = partType;
		this.cost=cost;
		this.markup = markup;
		this.quantity = quantity;
		this.country = pCountry;
		this.subpartsList = psubpartsList;
		this.factors = new ArrayList <Double>();
		this.partTag = pPartTag;
		this.pricecategory = pPriceCat;
		this.quantitycategory = pQuantityCat;
		this.quantityfactor = pquantityfactor;
		this.installationTime = pInstallationTime;
		this.calibrationTime = pCalibrationTime;
		this.listPriority=listPriority;
	}
	
	public StandardParts(int id, String partNumber, String partDescription,
			String partType, double cost, double markup,
			int quantity, String pCountry, List<StandardParts>psubpartsList, String pPartTag, String pPriceCat, String pQuantityCat, 
			int pquantityfactor, int pquotequantity,int pInstallationTime,int pCalibrationTime,int listPriority) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.partType = partType;
		this.cost=cost;
		this.markup = markup;
		this.quantity = quantity;
		this.country = pCountry;
		this.subpartsList = psubpartsList;
		this.factors = new ArrayList <Double>();
		this.partTag = pPartTag;
		this.pricecategory = pPriceCat;
		this.quantitycategory = pQuantityCat;
		this.quantityfactor = pquantityfactor;
		this.quoteQuantity = pquotequantity;
		this.installationTime = pInstallationTime;
		this.calibrationTime = pCalibrationTime;
		this.listPriority=listPriority;
	}

	public StandardParts(int id, String partNumber, String partDescription,
			String partType, double cost, double markup, String pCountry, String pPartTag) {
		super();
		this.id = id;
		this.partNumber = partNumber;
		this.partDescription = partDescription;
		this.partType = partType;
		this.cost=cost;
		this.markup = markup;
		this.country = pCountry;
		this.factors = new ArrayList <Double>();
		this.partTag = pPartTag;

	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPartNumber() {
		return partNumber;
	}
	
	public void setPartNumber(String partNumber) {
		this.partNumber = partNumber;
	}
	
	public String getPartDescription() {
		return partDescription;
	}
	
	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}
	
	public String getTruncatedpartDescription() {
		if(partDescription != null && partDescription.length()> 20)
			return partDescription.substring(0, 20);
		else
			return partDescription;	
		//return truncatedpartDescription;
	}

	public void setTruncatedpartDescription(String truncatedpartDescription) {
		this.truncatedpartDescription = truncatedpartDescription;
	}
	
	public String getTruncatedpartDescriptionsubparts() {
		if(partDescription != null && partDescription.length()> 35)
			return partDescription.substring(0, 35);
		else
			return partDescription;	
	}

	public void setTruncatedpartDescriptionsubparts(
			String truncatedpartDescriptionsubparts) {
		this.truncatedpartDescriptionsubparts = truncatedpartDescriptionsubparts;
	}

	public String getTruncatedpartDescriptionnewquotation() {
		if(partDescription != null && partDescription.length()> 30)
			return partDescription.substring(0, 30);
		else
			return partDescription;	
	}

	public void setTruncatedpartDescriptionnewquotation(
			String truncatedpartDescriptionnewquotation) {
		this.truncatedpartDescriptionnewquotation = truncatedpartDescriptionnewquotation;
	}

	public String getPartTag() {
		return partTag;
	}

	public void setPartTag(String partTag) {
		this.partTag = partTag;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuoteQuantity() {
		return quoteQuantity;
	}

	public void setQuoteQuantity(int quoteQuantity) {
		this.quoteQuantity = quoteQuantity;
	}

	public String getPartType() {
		return partType;
	}

	public void setPartType(String partType) {
		this.partType = partType;
	}

	public double getCost() {
		DecimalFormat twoDForm = new DecimalFormat("#");
		
		if(this.partType.compareToIgnoreCase("Assembly")==0){
			double totalcost = 0;
			for(StandardParts stp : subpartsList){
				totalcost += (stp.getCost()*stp.getQuantity());
			}
//			if(this.cost!=0)
//				if(this.cost!=totalcost){
//					totalcost=this.cost;
//				}
			return Double.valueOf(twoDForm.format(totalcost));
		}else{
			return Double.valueOf(twoDForm.format(this.cost));
		}
	}

	public void setCost(double cost) {
//		System.out.println("set cost called: "+cost);
		this.cost = cost;
	}
	
	
	public double getRecalculatedcost() {
		//DecimalFormat twoDForm = new DecimalFormat("#");
		
		if(this.partType.compareToIgnoreCase("Assembly")==0){
			recalculatedcost = 0;
			for(StandardParts stp : subpartsList){
				recalculatedcost += (stp.getCost()*stp.getQuantity());
			}
//			if(this.cost!=0)
//				if(this.cost!=recalculatedcost){
//					recalculatedcost=this.cost;
//				}
			if(factors.size()>0){
				return Double.valueOf(twoDForm.format(applyFactors(recalculatedcost)));
			}else{
				
				return Double.valueOf(twoDForm.format(recalculatedcost));
			}
		}else{
			if(factors.size()>0){
				return Double.valueOf(twoDForm.format(applyFactors(this.cost)));
			}else{
				return Double.valueOf(twoDForm.format(this.cost));
			}
		}
	}
	public void setRecalculatedcost(double recalculatedcost) {
		this.recalculatedcost = recalculatedcost;
	}

	public double getListPrice() {
		if(getPartType().compareToIgnoreCase("NonStandard Part")!=0){
			listPrice = getRecalculatedcost() * getMarkup();
			return  Double.valueOf(twoDForm.format(listPrice));
		}else{
			return  Double.valueOf(twoDForm.format(listPrice));
		}
		
	}

	public void setListPrice(double listPrice) {
		this.listPrice = listPrice;
	}

	public void setListPrice2(double listPrice2) {
		this.listPrice2 = listPrice2;
	}

	public double getGrossMargin() {
		

		if(this.partType.compareToIgnoreCase("Assembly")==0){
			 double mrecalculatedcost = 0;
			for(StandardParts stp : subpartsList){
				mrecalculatedcost += (stp.getCost()*stp.getQuantity());
			}
			if(factors.size()>0){

				//System.out.println(this.partNumber +" : "+ mrecalculatedcost +" : "+applyFactorsForGrossMargin(mrecalculatedcost));
				return Double.valueOf(twoDForm.format(applyFactorsForGrossMargin(mrecalculatedcost)));
			}else{
				
				return Double.valueOf(twoDForm.format(mrecalculatedcost));
			}
		}else{
			if(factors.size()>0){
				//System.out.println(this.partNumber +" <:> "+ cost +" : ");
				return Double.valueOf(twoDForm.format(applyFactorsForGrossMargin(cost)));
			}else{
				return Double.valueOf(twoDForm.format(cost));
			}
		}		//return ((getRecalculatedcost() - getRecalculatedcost()* (100/this.markup))/getRecalculatedcost())*100;
		//return Double.valueOf(new DecimalFormat("#").format((this.cost *this.markup/100)*( this.factors.get(0))));
	}

	public void setGrossMargin(double grossMargin) {
		this.grossMargin = grossMargin;
	}

	public double getMarkup() {
		return markup;
	}

	public void setMarkup(double markup) {
//		System.out.println("set markup called: "+markup);
		this.markup = markup;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<StandardParts> getSubpartsList() {
		return subpartsList;
	}

	public void setSubpartsList(List<StandardParts> subpartsList) {
		this.subpartsList = subpartsList;
	}
	
	public List<Double> getFactors() {
		return factors;
	}

	public void setFactors(List<Double> factors) {
		this.factors = factors;
	}

	public String getPricecategory() {
		return pricecategory;
	}

	public void setPricecategory(String pricecategory) {
		this.pricecategory = pricecategory;
	}

	public String getQuantitycategory() {
		return quantitycategory;
	}

	public void setQuantitycategory(String quantitycategory) {
		this.quantitycategory = quantitycategory;
	}

	public int getQuantityfactor() {
		return quantityfactor;
	}

	public void setQuantityfactor(int quantityfactor) {
		this.quantityfactor = quantityfactor;
	}

	public int getInstallationTime() {
		return installationTime;
	}

	public void setInstallationTime(int installationTime) {
//	System.out.println("set installation time called: "+installationTime);
		this.installationTime = installationTime;
	}
	public int getCalibrationTime() {
		return calibrationTime;
	}

	public void setCalibrationTime(int calibrationTime) {
//		System.out.println("set calibration called: "+calibrationTime);
		this.calibrationTime = calibrationTime;
	}

	/////*********************************************////
	

	

	public int getListPriority() {
		return listPriority;
	}

	public void setListPriority(int listPriority) {
		this.listPriority = listPriority;
	}

	public int hashCode() { return id; }

    @Override 
    public boolean equals(Object o) {
        return (o instanceof StandardParts) && (this.id == ((StandardParts) o).id);
    }
	/////*********************************************////
	public void addSubPart(StandardParts pStandardPart){
		this.subpartsList.add(pStandardPart);
	}
	
	private double applyFactors(double pCost){
		double tempCost = pCost;
		for(int i=factors.size()-1 ; i>=0; i--){
			if(i>0){
				tempCost += pCost * (factors.get(i)/100);
			}else{
				tempCost = tempCost * factors.get(i);
			}
		}
		
		//pCost = pCost + tempCost;
		//return pCost;
		return tempCost;
	}
	
	private double applyFactorsForGrossMargin(double pCost){
		if(pCost == 0)
			return 0;
		
		double tempCost=0;
		for(int i=factors.size()-1 ; i>=0; i--){
			if(i>0){
				pCost = pCost + pCost * (factors.get(i)/100);
			}else{
				pCost = pCost*factors.get(i);//the fast factor is currency conversion.
			}
		}
		//System.out.println("<"+pCost);
		tempCost = pCost;
		pCost = pCost * (this.markup);
		//System.out.println("<<"+pCost);
		pCost = ((pCost - tempCost)/pCost)*100;
		//System.out.println("<<<"+pCost);
		return pCost;
	}
}
