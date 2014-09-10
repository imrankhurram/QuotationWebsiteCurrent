package com.nextcontrols.bureaudomain;

import java.io.Serializable;

public class LetterTemplate extends Template implements Serializable {
	
//	private int templateId; 
//	private String templateName; 
//	private String body ;
//	private int user_id;
//	private String userName;
	public LetterTemplate(){
		
	}
	
	public LetterTemplate(String templateName, String body, int user_id) {
		super(templateName, body, user_id);
//		this.templateName = templateName;
//		this.body = body;
//		this.user_id =user_id;
	}
	
	public LetterTemplate(int templateId, String templateName, String body,
			String  userName) {
		super(templateId, templateName, body, userName);
//		this.templateId = templateId;
//		this.templateName = templateName;
//		this.body = body;
//		this.userName = userName;
	}
//
//	public int getTemplateId() {
//		return templateId;
//	}
//
//	public void setTemplateId(int templateId) {
//		this.templateId = templateId;
//	}
//
//	public String getTemplateName() {
//		return templateName;
//	}
//
//	public void setTemplateName(String templateName) {
//		this.templateName = templateName;
//	}
//
//	public String getBody() {
//		return body;
//	}
//
//	public void setBody(String body) {
//		this.body = body;
//	}
//
//	public int getUser_id() {
//		return user_id;
//	}
//
//	public void setUser_id(int user_id) {
//		this.user_id = user_id;
//	}
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}


}
