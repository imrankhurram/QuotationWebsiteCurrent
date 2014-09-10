package com.nextcontrols.bureaudomain;

import java.io.Serializable;

public class EmailTemplate extends Template implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailTemplate(){
		
	}
	public EmailTemplate(String templateName, String body, int user_id){
		super(templateName, body, user_id);
	}
	public EmailTemplate(int templateId, String templateName, String body,
			String  userName) {
		super(templateId, templateName, body, userName);
	}
}
