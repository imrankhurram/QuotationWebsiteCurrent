package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.AnnualRate;

public interface IAnnualRateDAO {
	public List<AnnualRate> getAnnualRatesList() ;
	public void addAnnualRate(String pdescription, int pcostperday, int pannualcalibration, int pannualdeadtraveldaycost,
			double pthreeyearfactor,double poneyearfactor, String pcountry, String pcurrency);
	public void modifyAnnualRate(int pid, String pdescription, int pcostperday, int pannualcalibration, int pannualdeadtraveldaycost,
			double pthreeyearfactor,double poneyearfactor, String pcountry, String pcurrency);
	public void deleteAnnualRate(int pID);
	public List<String>getCountryList();
	public List<String>getCurrencyList();
	public AnnualRate getAnnualRate(String pCountry) ;
	public List<AnnualRate> getAnnualRatesForQCreation(String pCountry) ;
}
