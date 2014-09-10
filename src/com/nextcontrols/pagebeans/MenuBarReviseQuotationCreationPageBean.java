package com.nextcontrols.pagebeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "revisequotationcreationmenubar")
@SessionScoped
public class MenuBarReviseQuotationCreationPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int linkClicked;
	

	public MenuBarReviseQuotationCreationPageBean() {
		setLinkClicked(0);
		
	}
	public QuotationRevisionPageBean getQuotationBean() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		QuotationRevisionPageBean quotationBean = (QuotationRevisionPageBean) session
				.getAttribute("revisedquotation");
		return quotationBean;

	}
	public int getLinkClicked() {
		return linkClicked;
	}

	public void setLinkClicked(int linkClicked) {
		this.linkClicked = linkClicked;
	}

	public String actionHomePage() {
		setLinkClicked(1);
		/*getQuotationBean().saveQuotationFirstPage();*/
		return "HomePage?faces-redirect=true";
	}

	public String actionHomePartsPage() {
		getQuotationBean().saveQuotationPartsPage();
		return "HomePage?faces-redirect=true";
	}

	public String actionHomeSummariesPage() {
		return "HomePage?faces-redirect=true";
	}

	public String actionHomeDisplaynEditPage() {
		getQuotationBean().saveQuotationDisplaynEditpage();
		return "HomePage?faces-redirect=true";
	}
}
