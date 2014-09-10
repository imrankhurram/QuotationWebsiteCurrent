package com.nextcontrols.pagebeans;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.Quotation;

@ManagedBean(name = "mainmenubar")
@SessionScoped
public class MenuBarMainPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int linkClicked;
	private static final String ERROR_MESSAGE = "Error: Could not open quotation, this quotation was not saved properly";

	public MenuBarMainPageBean() {
		linkClicked = 1;
	}

	public int getLinkClicked() {
		return linkClicked;
	}

	public void setLinkClicked(int linkClicked) {
		this.linkClicked = linkClicked;
	}

	public String actionHomePage() {
		setLinkClicked(1);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		LetterTemplatesPageBean letterBean = (LetterTemplatesPageBean) session
				.getAttribute("letterTemp");
		EmailTemplatePageBean emailBean = (EmailTemplatePageBean) session
				.getAttribute("emailTemp");
		if (letterBean != null) {
			session.removeAttribute("letterTemp");
		}
		if (emailBean != null) {
			session.removeAttribute("emailTemp");
		}
		return "HomePage?faces-redirect=true";
	}

	public String actionNewQuotationPage() {
		setLinkClicked(2);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		QuotationCreationPageBean quotationBean = (QuotationCreationPageBean) session
				.getAttribute("newquote");
		if (quotationBean != null) {
			session.removeAttribute("newquote");
			quotationBean.creatQuotation();
			session.setAttribute("newquote", quotationBean);
		}
		
		return "NewQuotationPage.xhtml?faces-redirect=true";
	}

	public String actionTotalnDiscountPage() {
		setLinkClicked(3);

		return "TotalnDiscountPage.xhtml?faces-redirect=true";
	}

	public String actionQuotationsPage() {
		setLinkClicked(3);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		QuotationsPageBean quotationsBean = (QuotationsPageBean) session
				.getAttribute("quotations");
		if (quotationsBean != null) {
			session.removeAttribute("quotations");
			quotationsBean.initializeDates();
			session.setAttribute("quotations", quotationsBean);
		}
		return "QuotationsPage.xhtml?faces-redirect=true";
	}

	public String actionUserAuditsPage() {
		setLinkClicked(4);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditsPageBean userauditBean = (UserAuditsPageBean) session
				.getAttribute("useraudit");
		if (userauditBean != null) {
			session.removeAttribute("useraudit");
			userauditBean.initializeDates();
			session.setAttribute("useraudit", userauditBean);
		}
		return "UserAuditsPage.xhtml?faces-redirect=true";
	}

	public String actionChangeQuotationPage() {
		setLinkClicked(4);
		return "";
	}

	public String actionMainUsersPage() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		if (userInfo.getUser().getUsertype().compareToIgnoreCase("User") != 0) {
			setLinkClicked(5);
			return "MainUsersPage.xhtml?faces-redirect=true";
		} else {
			setLinkClicked(5);
			return "";

		}
	}

	public String actionAddNewMainUserPage() {
		setLinkClicked(5);
		return "AddMainUserPage.xhtml?faces-redirect=true";
	}

	public String actionModifyMainUserPage() {
		setLinkClicked(5);
		return "ModifyMainUserPage.xhtml?faces-redirect=true";
	}

	public String actionUsersPage() {
		setLinkClicked(5);
		return "CountriesPage.xhtml?faces-redirect=true";
	}

	public String actionLogOutPage() {
		setLinkClicked(6);
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserAuditDAO.getInstance().addUserAudit("LoggedOut", "User Logged Out");
		session.removeAttribute("userInfo");
		session.invalidate();
		return "SystemLogin?faces-redirect=true";
	}

	public String actionReviseQuotationPage(Quotation quotation) {
//		if (quotation.isCompleted()
//				&& quotation.getPagecontents().split("_").length < 6) {
//			FacesMessage message = new FacesMessage(
//					FacesMessage.SEVERITY_ERROR, ERROR_MESSAGE, ERROR_MESSAGE);
//			FacesContext.getCurrentInstance().addMessage(null, message);
//			RequestContext context = RequestContext.getCurrentInstance();
//			context.update("frmquotationPage:valMsg");
//			return "";
//
//		} else {
		if(quotation==null){
			FacesMessage message = new FacesMessage(
			FacesMessage.SEVERITY_ERROR, "Please select a quotation first!", "Please select a quotation first!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext context = RequestContext.getCurrentInstance();
			context.update("frmquotationPage:valMsg");
			return "";
		}
			setLinkClicked(2);
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			QuotationRevisionPageBean quotationBean = (QuotationRevisionPageBean) session
					.getAttribute("revisedquotation");
			if (quotationBean != null) {
				session.removeAttribute("revisedquotation");
				quotationBean.creatQuotation();
				session.setAttribute("revisedquotation", quotationBean);
			}
			return "ReviseQuotationPage.xhtml?faces-redirect=true";
//		}

	}

	public String actionTemplatesPage() {
		setLinkClicked(7);
		return "LetterTemplatesPage.xhtml?faces-redirect=true";
	}

	public String actionEmailTemplatesPage() {
		setLinkClicked(8);
		return "EmailTemplatesPage.xhtml?faces-redirect=true";
	}
}
