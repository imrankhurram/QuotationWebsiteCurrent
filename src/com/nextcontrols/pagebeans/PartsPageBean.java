package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.CountryDAO;
import com.nextcontrols.bureaudao.PartsDAO;
import com.nextcontrols.bureaudao.PriceCategoryDAO;
import com.nextcontrols.bureaudao.QuantityCategoryDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.StandardParts;

@ManagedBean(name="parts")
@SessionScoped
public class PartsPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<StandardParts> standarpartsList;
	private List<StandardParts> filteredStandarpartsList;
	private List<StandardParts> subpartsList;
	private StandardParts selectedStandardPart;
	private StandardParts newStandardPart;
	private Map<String, String> countries;
	private Map<String, String> parttags;
	private Map<String, String> pricecat;
	private Map<String, String> quantitycat;
	private SelectItem[] typeFilter;

	public PartsPageBean() {
		standarpartsList = new ArrayList<StandardParts>();
		subpartsList = new ArrayList<StandardParts>();
		newStandardPart = new StandardParts();
		// standarpartsList=PartsDAO.getInstance().getStandardPartsAndAssembliesList();
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		newStandardPart.setMarkup(userInfo.getUser().getCompany().getMarkup());
		typeFilter = new SelectItem[3];
		typeFilter[0] = new SelectItem("", "All");
		typeFilter[1] = new SelectItem("ASSEMBLY", "ASSEMBLY");
		typeFilter[2] = new SelectItem("PART", "PART");
		initializeOptions();
		initializeStandardparts();

	}
	public void initializeStandardparts(){
		this.standarpartsList = PartsDAO.getInstance()
				.getStandardPartsAndAssembliesList();
	}
	public List<StandardParts> getStandarpartsList() {
//		if (this.standarpartsList.isEmpty()){
//			
//		}
		return this.standarpartsList;
	}

	public void setStandarpartsList(List<StandardParts> standarpartsList) {
		System.out.println("set standard parts list called");
		this.standarpartsList = standarpartsList;
	}

	public List<StandardParts> getFilteredStandarpartsList() {
		return filteredStandarpartsList;
	}

	public void setFilteredStandarpartsList(
			List<StandardParts> filteredStandarpartsList) {
		this.filteredStandarpartsList = filteredStandarpartsList;
	}

	public List<StandardParts> getSubpartsList() {
		return subpartsList;
	}

	public void setSubpartsList(List<StandardParts> subpartsList) {
		this.subpartsList = subpartsList;
	}

	public StandardParts getSelectedStandardPart() {
		return selectedStandardPart;
	}

	public void setSelectedStandardPart(StandardParts selectedStandardPart) {
		this.selectedStandardPart = selectedStandardPart;
	}

	public StandardParts getNewStandardPart() {
		return newStandardPart;
	}

	public void setNewStandardPart(StandardParts newStandardPart) {
		this.newStandardPart = newStandardPart;
	}

	public Map<String, String> getCountries() {
		return countries;
	}

	public void setCountries(Map<String, String> countries) {
		this.countries = countries;
	}

	public Map<String, String> getPricecat() {
		return pricecat;
	}

	public void setPricecat(Map<String, String> pricecat) {
		this.pricecat = pricecat;
	}

	public Map<String, String> getQuantitycat() {
		return quantitycat;
	}

	public void setQuantitycat(Map<String, String> quantitycat) {
		this.quantitycat = quantitycat;
	}

	public Map<String, String> getParttags() {
		return parttags;
	}

	public void setParttags(Map<String, String> parttags) {
		this.parttags = parttags;
	}

	public SelectItem[] getTypeFilter() {
		return typeFilter;
	}

	public void setTypeFilter(SelectItem[] typeFilter) {
		this.typeFilter = typeFilter;
	}

	public void initializeOptions() {
		countries = new HashMap<String, String>();
		for (String str : PartsDAO.getInstance().getCountryList()) {
			countries.put(str, str);
		}
		for (String str : CountryDAO.getInstance().getCountryList()) {
			countries.put(str, str);
		}
		parttags = new HashMap<String, String>();
		for (String str : PartsDAO.getInstance().getPartsTagList()) {
			parttags.put(str, str);
		}

		pricecat = new HashMap<String, String>();
		for (String str : PriceCategoryDAO.getInstance()
				.getPriceCategoryStringList()) {
			pricecat.put(str, str);
		}
		quantitycat = new HashMap<String, String>();
		for (String str : QuantityCategoryDAO.getInstance()
				.getQuantityCategoryStringList()) {
			quantitycat.put(str, str);
		}
	}

	public void loadSubParts() {
		this.subpartsList = PartsDAO.getInstance().getStandardPartsList(
				this.selectedStandardPart.getId());
	}

	/**
	 * adds a new user and inserts audit
	 * 
	 * @return
	 */
	public String addNewPart() {
		PartsDAO.getInstance().addStandardParts(
				this.newStandardPart.getPartNumber(),
				this.newStandardPart.getPartDescription(),
				this.newStandardPart.getPartType(),
				this.newStandardPart.getCost(),
				this.newStandardPart.getMarkup(),
				this.newStandardPart.getCountry(),
				this.newStandardPart.getPartTag(),
				this.newStandardPart.getPricecategory(),
				this.newStandardPart.getQuantitycategory(),
				this.newStandardPart.getInstallationTime(),
				this.newStandardPart.getCalibrationTime(),
				this.newStandardPart.getListPriority());
		UserAuditDAO.getInstance().addUserAudit("Add Part",
				"User added the part :" + this.newStandardPart.getPartNumber());
		newStandardPart = new StandardParts();
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		newStandardPart.setMarkup(userInfo.getUser().getCompany().getMarkup());
		return "PartsPage?faces-redirect=true";
	}

	/**
	 * modify a user and inserts audit
	 * 
	 * @return
	 */
	public String modifyPart() {
		if (this.getSelectedStandardPart() != null) {
			System.out.println("cost cahnged: "+this.selectedStandardPart.getCost());
			PartsDAO.getInstance().modifyStandardParts(
					this.selectedStandardPart.getId(),
					this.selectedStandardPart.getPartNumber(),
					this.selectedStandardPart.getPartDescription(),
					this.selectedStandardPart.getPartType(),
					this.selectedStandardPart.getCost(),
					this.selectedStandardPart.getMarkup(),
					this.selectedStandardPart.getCountry(),
					this.selectedStandardPart.getPartTag(),
					this.selectedStandardPart.getPricecategory(),
					this.selectedStandardPart.getQuantitycategory(),
					this.selectedStandardPart.getInstallationTime(),
					this.selectedStandardPart.getCalibrationTime(),
					this.selectedStandardPart.getListPriority());
			UserAuditDAO.getInstance().addUserAudit(
					"Modify Part",
					"User modified the part :"
							+ this.getSelectedStandardPart().getPartNumber());
		}
		return "PartsPage?faces-redirect=true";
	}

	/**
	 * delete a user and inserts audit
	 * 
	 * @return
	 */
	public String deletePart() {
		if (this.getSelectedStandardPart() != null) {
			PartsDAO.getInstance().deleteStandardParts(
					this.getSelectedStandardPart().getId());
			UserAuditDAO.getInstance().addUserAudit(
					"Delete Part",
					"User deleted the part :"
							+ this.getSelectedStandardPart().getPartNumber());
		}
		return "PartsPage?faces-redirect=true";
	}

	/**
	 * save sub parts
	 * 
	 * @return
	 */
	public void saveSubparts() {
		// System.out.println("Save subparts");
		if (this.selectedStandardPart != null) {
			PartsDAO.getInstance().saveSubParts(
					this.selectedStandardPart.getId(), subpartsList);
			UserAuditDAO.getInstance().addUserAudit(
					"Modified Part",
					"User added/modified subparts for the part :"
							+ this.getSelectedStandardPart().getPartNumber());
		}
		subpartsList = new ArrayList<StandardParts>();
	}

	public String modifyAllParts() {
//		System.out.println("Start");
//		for (StandardParts st : standarpartsList) {
//			System.out.println(st.getPartNumber() + " : "
//					+ st.getInstallationTime() + " : " + st.getMarkup()+" : "+st.getListPriority());
//		}
//		System.out.println("Finish");
		PartsDAO.getInstance().saveAllParts(standarpartsList);
		return "PartsPage?faces-redirect=true";
	}
}
