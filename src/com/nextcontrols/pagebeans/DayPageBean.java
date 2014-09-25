package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.CountryDAO;
import com.nextcontrols.bureaudao.DayDAO;
import com.nextcontrols.bureaudao.SettingsDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.Day;

@ManagedBean(name = "days")
@SessionScoped
public class DayPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Day> daysList;
	private List<Day> filteredDaysList;

	private Day selectedday;
	private Day newday;
	private Map<String, String> countries;
	private Map<String, String> currencies;
	private Float hoursInDay;

	public DayPageBean() {
		daysList = new ArrayList<Day>();
		newday = new Day();

		initializeOptions();
	}

	public List<Day> getDaysList() {
		setDaysList();
		return daysList;
	}

	public void setDaysList() {
		this.daysList = DayDAO.getInstance().getDaysList();
	}

	public List<Day> getFilteredDaysList() {
		return filteredDaysList;
	}

	public void setFilteredDaysList(List<Day> filteredDaysList) {
		this.filteredDaysList = filteredDaysList;
	}

	public Day getSelectedday() {
		return selectedday;
	}

	public void setSelectedday(Day selectedday) {
		this.selectedday = selectedday;
	}

	public Day getNewday() {
		return newday;
	}

	public void setNewday(Day newday) {
		this.newday = newday;
	}

	public Map<String, String> getCountries() {
		return countries;
	}

	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}

	public Map<String, String> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(Map<String, String> currencies) {
		this.currencies = currencies;
	}

	public void initializeOptions() {
		hoursInDay = SettingsDAO.getInstance().getHoursInDay();
		countries = new HashMap<String, String>();
		currencies = new HashMap<String, String>();
		for (String str : DayDAO.getInstance().getCountryList()) {
			countries.put(str, str);
		}
		for (String str : DayDAO.getInstance().getCurrencyList()) {
			currencies.put(str, str);
		}
		for (String str : CountryDAO.getInstance().getCountryList()) {
			countries.put(str, str);
		}
		for (String str : CountryDAO.getInstance().getCurrencyList()) {
			currencies.put(str, str);
		}
	}

	/**
	 * adds a new user and inserts audit
	 * 
	 * @return
	 */
	public String saveSheet() {
		return "DaysPage?faces-redirect=true";
	}

	/**
	 * adds a new user and inserts audit
	 * 
	 * @return
	 */
	public String addNewDay() {
		filteredDaysList = null;
		DayDAO.getInstance().addDay(this.newday.getName(),
				this.newday.getCostperday(), this.newday.getCountry(),
				this.newday.getCurrency());
		UserAuditDAO.getInstance().addUserAudit("Add Rate",
				"User added the rate : " + this.newday.getName());
		newday = new Day();
		return "DaysPage?faces-redirect=true";
	}

	/**
	 * modify a user and inserts audit
	 * 
	 * @return
	 */
	public String modifyDay() {
		filteredDaysList = null;
		if (this.selectedday != null) {
			DayDAO.getInstance().modifyDay(this.selectedday.getId(),
					this.selectedday.getName(),
					this.selectedday.getCostperday(),
					this.selectedday.getCountry(),
					this.selectedday.getCurrency());
			UserAuditDAO.getInstance().addUserAudit("Modify Rate",
					"User modified the rate : " + this.selectedday.getName());
		}
		return "DaysPage?faces-redirect=true";
	}

	/**
	 * delete a user and inserts audit
	 * 
	 * @return
	 */
	public String deleteDay() {
		filteredDaysList = null;
		if (this.selectedday != null) {
			DayDAO.getInstance().deleteDay(this.getSelectedday().getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Rate",
					"User deleted the rate : " + this.selectedday.getName());
		}
		return "DaysPage?faces-redirect=true";
	}

	public void saveHoursInDay() {
		System.out.println("this.hoursInDay:" +this.hoursInDay);
		SettingsDAO.getInstance().saveHoursInDay(this.hoursInDay);
	}

	public Float getHoursInDay() {
		return hoursInDay;
	}

	public void setHoursInDay(Float hoursInDay) {
		this.hoursInDay = hoursInDay;
	}



}
