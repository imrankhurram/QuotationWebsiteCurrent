package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.UserAudit;

@ManagedBean(name = "useraudit")
@SessionScoped
public class UserAuditsPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<UserAudit> userauditsList;
	private List<UserAudit> filteredUserAuditsList;
	private UserAudit selectedUserAudit;
	private Date dateFrom;
	private Date dateTo;

	public UserAuditsPageBean() {
		userauditsList = new ArrayList<UserAudit>();
		initializeDates();
	}

	public List<UserAudit> getUserauditsList() {
		setUserauditsList();
		return userauditsList;
	}

	public void setUserauditsList() {
		/*
		 * ExternalContext ectx =
		 * FacesContext.getCurrentInstance().getExternalContext(); HttpSession
		 * session = (HttpSession)ectx.getSession(false); UserInfoPageBean
		 * userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		 * this.userauditsList =
		 * UserAuditDAO.getInstance().getUserAuditsList(userInfo
		 * .getUser().getUserId
		 * (),userInfo.getUser().getCompany().getId(),userInfo
		 * .getUser().getUsertype(),dateFrom,dateTo);
		 */}

	public UserAudit getSelectedUserAudit() {
		return selectedUserAudit;
	}

	public void setSelectedUserAudit(UserAudit selectedUserAudit) {
		this.selectedUserAudit = selectedUserAudit;
	}

	public List<UserAudit> getFilteredUserAuditsList() {
		return filteredUserAuditsList;
	}

	public void setFilteredUserAuditsList(List<UserAudit> filteredUserAuditsList) {
		this.filteredUserAuditsList = filteredUserAuditsList;
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

	public void initializeDates() {
		// if ((dateTo==null)||(dateFrom==null)){
		Calendar to = Calendar.getInstance();
		to.add(Calendar.DATE, 1);
		dateTo = to.getTime();
		Calendar before = Calendar.getInstance();
		before.add(Calendar.DATE, -29);
		dateFrom = before.getTime();
		// }
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		this.userauditsList = UserAuditDAO.getInstance().getUserAuditsList(
				userInfo.getUser().getUserId(),
				userInfo.getUser().getCompany().getId(),
				userInfo.getUser().getUsertype(), dateFrom, dateTo);

	}

	public String updateDataTable() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		this.userauditsList = UserAuditDAO.getInstance().getUserAuditsList(
				userInfo.getUser().getUserId(),
				userInfo.getUser().getCompany().getId(),
				userInfo.getUser().getUsertype(), dateFrom, dateTo);
		return "UserAuditsPage.xhtml?faces-redirect=true";
	}
}
