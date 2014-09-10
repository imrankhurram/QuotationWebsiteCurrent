package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.Company;

public interface ICompanyDAO {
	public List<Company> getCompaniesList();

	public List<Company> getCompaniesList(String pCountry);

	public void addCompany(String pcompanyName, double pmarkup,
			double pdiscountpercent, double pmonitoringdiscount,
			String pcountry, String pCompanyRef, boolean multiyear_options);

	public void modifyCompany(int pID, String pcompanyName, double pmarkup,
			double pdiscountpercent, double pmonitoringdiscount,
			String pcountry, String pCompanyRef, boolean multiyear_options);

	public void deleteCompany(int pID);

}
