package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.Country;

public interface ICountryDAO {
	public List<Country> getCountriesList() ;
	public void addCountry(String pcountry, String pfullName, String pcurrency,
			 double pconversionratefrompounds, double pfreight, double pduty);
	public void modifyCountry(String pcountry, String pfullName, String pcurrency,
			 double pconversionratefrompounds, double pfreight, double pduty);
	public void deleteCountry(String pcountry);
	public List<String> getCountryList();
	public List<String> getCurrencyList();
}
