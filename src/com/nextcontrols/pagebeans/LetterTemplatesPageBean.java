package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.nextcontrols.bureaudao.LetterTemplateDAO;
import com.nextcontrols.bureaudomain.LetterTemplate;
import com.nextcontrols.bureaudomain.Template;

@SessionScoped
@ManagedBean(name = "letterTemp")
public class LetterTemplatesPageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Template> letterTemplates;
	private Template selectedLetterTemplate;
	private List<Template> filteredLetterTemplates;
	private Template newLetterTemplate;
	private String quotationCoverUpperText;
	private String quotationCoverLowerText;

	/************************************** Faces Message Variables **********************************/
	private static final String FAILURE_MESSAGE = "Please select a template to ";
	private static final String SUCCESS_MESSAGE = "Successfully deleted the selected template";

	/************************************** No-args Constructor **********************************/
	public LetterTemplatesPageBean() {

	}

	/************************************** Bean initialization method **********************************/
	@PostConstruct
	public void init() {
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
				.getExternalContext().getSessionMap();
		UserInfoPageBean bean = (UserInfoPageBean) sessionMap.get("userInfo");
		if (bean.getUser().getUsertype().equalsIgnoreCase("User")) {
			this.letterTemplates = LetterTemplateDAO.getInstance()
					.getUsersSavedTemplates(bean.getUser().getUserId());
		} else {
			this.letterTemplates = LetterTemplateDAO.getInstance()
					.getAllSavedTemplates();
		}
		this.quotationCoverUpperText = "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ ""
				+ "(Customer Contact Name)"
				+ " <br/>"
				+ "(Customer Company Name)"
				+ " <br/> "
				+ "(Address Line 1)"
				+ ",<br/> "
				+ "(Address Line 2)"
				+ " , <br/> "
				+ "(City)"
				+ " , <br/> "
				+ "(State)"
				+ " , <br/> "
				+ "(Post Code)"
				+ " <br /><br/>"
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
				+ "(Date)"
				+ " <br /><br /> Dear "
				+ "(Name)"
				+ ",</div>"
				+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
				+ "&nbsp; &nbsp; &nbsp; &nbsp; "
				+ "<u><strong>"
				+ "Re "
				+ "(Project Name)"
				+ " remote wireless monitoring system</strong></u></div> ";

		this.quotationCoverLowerText = "<div>(Sales executive name)"
				+ "<br/>(Sales executive title),&nbsp;" + "<br/>(Sales executive phone number)"
				+ "<br/>(Sales executive email)</div>";
	}

	/************************************** Bean Destruction method **********************************/
	@PreDestroy
	public void destroy() {
		this.letterTemplates = null;
		this.selectedLetterTemplate = null;
		this.filteredLetterTemplates = null;
		this.newLetterTemplate = null;
	}

	/************************************** Action and Event Listeners **********************************/
	public void selectLetterTemp(SelectEvent event) {
		this.selectedLetterTemplate = (LetterTemplate) event.getObject();
	}

	public String addNewLetterFormatPreProcess() {
		if (this.newLetterTemplate == null) {
			this.newLetterTemplate = new LetterTemplate();
		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.put("letterTemp", this);
		return "AddLetterTemplatePage.xhtml?faces-redirect=true";
	}

	public String addNewLetterFormat() {
		if (this.newLetterTemplate != null) {
			Map<String, Object> sessionMap = FacesContext.getCurrentInstance()
					.getExternalContext().getSessionMap();
			UserInfoPageBean bean = (UserInfoPageBean) sessionMap
					.get("userInfo");
			this.newLetterTemplate.setUser_id(bean.getUser().getUserId());
			this.newLetterTemplate.setUserName(bean.getUser().getUsername());
			int success = LetterTemplateDAO.getInstance().saveTemplate(
					this.newLetterTemplate);
			if (success > 0) {
				this.newLetterTemplate.setTemplateId(success);
				this.letterTemplates.add(this.newLetterTemplate);
				newLetterTemplate = null;
			}
		}
		return "LetterTemplatesPage.xhtml?faces-redirect=true";
	}

	public void delLetterFormatPreProcess(ActionEvent event) {
		FacesMessage message = null;
		if (this.selectedLetterTemplate == null) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					FAILURE_MESSAGE + "delete", FAILURE_MESSAGE + "delete");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().addCallbackParam("isValid",
					false);
			RequestContext.getCurrentInstance().update(
					"letterTemplateFrm:valMsg");
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("isValid",
					true);
		}
	}

	public void deleteLetterTemplate(ActionEvent event) {
		if (this.selectedLetterTemplate != null) {
			boolean success = LetterTemplateDAO.getInstance().deleteTemplate(
					this.selectedLetterTemplate);
			if (success) {
				this.letterTemplates.remove(this.selectedLetterTemplate);
				this.selectedLetterTemplate = null;
				if (this.filteredLetterTemplates != null) {
					this.filteredLetterTemplates = null;
				}
				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO,
								SUCCESS_MESSAGE, SUCCESS_MESSAGE));
				RequestContext.getCurrentInstance().update(
						"letterTemplateFrm:valMsg");
			}
		}
	}

	public String modLetterFormatPreProcess() {
		FacesMessage message = null;

		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.put("letterTemp", this);
		if (this.selectedLetterTemplate == null) {
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					FAILURE_MESSAGE + "modify", FAILURE_MESSAGE + "modify");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update(
					"letterTemplateFrm:valMsg");
			return null;
		} else {

			return "ModifyLetterTemplatePage.xhtml?faces-redirect=true";
		}
	}

	public String modLetterFormat() {
		if (this.selectedLetterTemplate != null) {
			boolean success = LetterTemplateDAO.getInstance().modifyTemplate(
					this.selectedLetterTemplate);
			if (success)
				return "LetterTemplatesPage.xhtml?faces-redirect=true";
		}
		return null;
	}

	/************************************** Accessors and Mutators **********************************/
	public List<Template> getLetterTemplates() {
		return letterTemplates;
	}

	public void setLetterTemplates(List<Template> letterTemplates) {
		this.letterTemplates = letterTemplates;
	}

	public Template getSelectedLetterTemplate() {
		return selectedLetterTemplate;
	}

	public void setSelectedLetterTemplate(Template selectedLetterTemplate) {
		this.selectedLetterTemplate = selectedLetterTemplate;
	}

	public List<Template> getFilteredLetterTemplates() {
		return filteredLetterTemplates;
	}

	public void setFilteredLetterTemplates(
			List<Template> filteredLetterTemplates) {
		this.filteredLetterTemplates = filteredLetterTemplates;
	}

	public Template getNewLetterTemplate() {
		return newLetterTemplate;
	}

	public void setNewLetterTemplate(Template newLetterTemplate) {
		this.newLetterTemplate = newLetterTemplate;
	}

	public String getQuotationCoverUpperText() {
		return quotationCoverUpperText;
	}

	public void setQuotationCoverUpperText(String quotationCoverUpperText) {
		this.quotationCoverUpperText = quotationCoverUpperText;
	}

	public String getQuotationCoverLowerText() {
		return quotationCoverLowerText;
	}

	public void setQuotationCoverLowerText(String quotationCoverLowerText) {
		this.quotationCoverLowerText = quotationCoverLowerText;
	}

}
