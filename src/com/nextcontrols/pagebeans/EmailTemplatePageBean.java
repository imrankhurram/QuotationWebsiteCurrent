package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import com.nextcontrols.bureaudao.EmailTemplateDAO;
import com.nextcontrols.bureaudomain.EmailTemplate;
import com.nextcontrols.bureaudomain.Template;

@SessionScoped
@ManagedBean(name="emailTemp")

public class EmailTemplatePageBean  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Template> emailTemplates ;
	private Template selectedEmailTemplate;
	private List<Template> filteredEmailTemplates;
	private Template newEmailTemplate;
	
	/**************************************Faces Message Variables**********************************/
	private static final String FAILURE_MESSAGE ="Please select a template to ";
	private static final String SUCCESS_MESSAGE = "Successfully deleted the selected template";
	/**************************************No-args Constructor**********************************/
	public EmailTemplatePageBean(){
		
	}
	/**************************************Bean initialization method**********************************/
	@PostConstruct
	public void init(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		UserInfoPageBean bean = (UserInfoPageBean) sessionMap.get("userInfo");
		if(bean.getUser().getUsertype().equalsIgnoreCase("User")){
			this.emailTemplates = EmailTemplateDAO.getInstance().getUsersSavedTemplates(bean.getUser().getUserId());
		}
		else {
			this.emailTemplates = EmailTemplateDAO.getInstance().getAllSavedTemplates();
		}
	}
	/**************************************Bean Destruction method**********************************/	
	@PreDestroy
	public void destroy(){
		this.emailTemplates =null; 
		this.selectedEmailTemplate = null; 
		this.filteredEmailTemplates= null;
		this.newEmailTemplate = null;
	}

	/**************************************Action and Event Listeners**********************************/
	public void selectEmailTemp(SelectEvent event) {
		this.selectedEmailTemplate = (EmailTemplate) event.getObject();
	}
	public String addNewEmailFormatPreProcess() {
		if(this.newEmailTemplate==null){
			this.newEmailTemplate = new EmailTemplate();
		}
//		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("EmailTemp", this);
		return "AddEmailTemplatePage.xhtml?faces-redirect=true";
	}
	public String addNewEmailFormat() {
		if(this.newEmailTemplate!=null){
			Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			UserInfoPageBean bean = (UserInfoPageBean) sessionMap.get("userInfo");
			this.newEmailTemplate.setUser_id(bean.getUser().getUserId());
			this.newEmailTemplate.setUserName(bean.getUser().getUsername());
			int success = EmailTemplateDAO.getInstance().saveTemplate(this.newEmailTemplate);	
			if(success>0){
				this.newEmailTemplate.setTemplateId(success);
				this.emailTemplates.add(this.newEmailTemplate);
				newEmailTemplate = null; 
			}
		}
		return "EmailTemplatesPage.xhtml?faces-redirect=true";
	}
	public void delEmailFormatPreProcess(ActionEvent event) {
		FacesMessage message = null; 
		if(this.selectedEmailTemplate==null){
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE_MESSAGE + "delete", FAILURE_MESSAGE + "delete");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().addCallbackParam("isValid", false);
			RequestContext.getCurrentInstance().update("emailTemplateFrm:valMsg");
		}
		else {
			RequestContext.getCurrentInstance().addCallbackParam("isValid", true);
		}
	}
	public void deleteEmailTemplate(ActionEvent event){
		if(this.selectedEmailTemplate!=null){
			boolean success = EmailTemplateDAO.getInstance().deleteTemplate(this.selectedEmailTemplate);
			if(success){
				this.emailTemplates.remove(this.selectedEmailTemplate);
				this.selectedEmailTemplate = null; 
				if(this.filteredEmailTemplates!=null){
					this.filteredEmailTemplates = null; 
				}
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, SUCCESS_MESSAGE, SUCCESS_MESSAGE));
				RequestContext.getCurrentInstance().update("emailTemplateFrm:valMsg");
			}
		}
	}
	public String modEmailFormatPreProcess() {
		FacesMessage message = null; 
		if(this.selectedEmailTemplate==null){
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, FAILURE_MESSAGE+ "modify", FAILURE_MESSAGE+ "modify");
			FacesContext.getCurrentInstance().addMessage(null, message);
			RequestContext.getCurrentInstance().update("emailTemplateFrm:valMsg");
			return null;
		}
		else { 
			
			return "ModifyEmailTemplatePage.xhtml?faces-redirect=true";
		}
	}
	public String modEmailFormat() {
		if(this.selectedEmailTemplate!=null){
			boolean success = EmailTemplateDAO.getInstance().modifyTemplate(this.selectedEmailTemplate);
			if(success)
				return "EmailTemplatesPage.xhtml?faces-redirect=true";
		}
		return null;
	}

	/**************************************Accessors and Mutators**********************************/

	public List<Template> getEmailTemplates() {
		return emailTemplates;
	}
	public void setEmailTemplates(List<Template> emailTemplates) {
		this.emailTemplates = emailTemplates;
	}
	public Template getSelectedEmailTemplate() {
		return selectedEmailTemplate;
	}
	public void setSelectedEmailTemplate(Template selectedEmailTemplate) {
		this.selectedEmailTemplate = selectedEmailTemplate;
	}
	public List<Template> getFilteredEmailTemplates() {
		return filteredEmailTemplates;
	}
	public void setFilteredEmailTemplates(List<Template> filteredEmailTemplates) {
		this.filteredEmailTemplates = filteredEmailTemplates;
	}
	public Template getNewEmailTemplate() {
		return newEmailTemplate;
	}
	public void setNewEmailTemplate(Template newEmailTemplate) {
		this.newEmailTemplate = newEmailTemplate;
	}

}
