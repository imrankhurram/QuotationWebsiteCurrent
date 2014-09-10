package com.nextcontrols.bureaudao;

import java.util.List;
import java.util.Map;

import com.nextcontrols.bureaudomain.Day;

public interface IDayDAO {
	public List<Day> getDaysList() ;
	public void addDay(String pName,int pCostPerDay, String pCountry, String pcurrency);
	public void modifyDay(int pID, String pName,int pCostPerDay, String pCountry, String pcurrency);
	public void deleteDay(int pID);
	public List<String>getCountryList();
	public List<String>getCurrencyList();
	public Map<String, Integer> getRates(String pCountry) ;
}
