package com.nextcontrols.pagebeans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean(name = "quotationcreationmenubar")
@SessionScoped
public class MenuBarQuotationCreationPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int linkClicked;

	/* private QuotationCreationPageBean quotationBean; */

	public MenuBarQuotationCreationPageBean() {
		setLinkClicked(0);

	}

	public int getLinkClicked() {
		return linkClicked;
	}

	public void setLinkClicked(int linkClicked) {
		this.linkClicked = linkClicked;
	}

	public QuotationCreationPageBean getQuotationBean() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		QuotationCreationPageBean quotationBean = (QuotationCreationPageBean) session
				.getAttribute("newquote");
		return quotationBean;

	}

	public String actionHomePage() {
		setLinkClicked(1);
		//System.out.println("action home page**********");
		/*getQuotationBean().saveQuotatonFirstPage();*/
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
