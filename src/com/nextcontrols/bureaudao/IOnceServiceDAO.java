package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.OnceService;
import com.nextcontrols.bureaudomain.Quotation;

public interface IOnceServiceDAO {
	public List<OnceService> getOnceServicesList() ;
	public void addOnceService(String pServiceDescription, double pminimumCharge,
			 double punitCost, String pCountry, String pcurrency, String pServiceType);
	public void modifyOnceService(int pID, String pServiceDescription, double pminimumCharge,
			 double punitCost, String pCountry, String pcurrency, String pServiceType);
	public void deleteOnceService(int pID);
	public List<String>getCountryList();
	public List<String>getCurrencyList();
	public List<OnceService> getOnceServicesList(String pCountry, Quotation pQuotation) ;

}
