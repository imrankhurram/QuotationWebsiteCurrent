package com.nextcontrols.bureaudomain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;



public class UserAudit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private Timestamp auditDate;
	private String actionType;
	private String actionDescription;
	private User user;
	
	private SimpleDateFormat createFormat=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String creatTime;
	
	public UserAudit() {

	}
	
	public UserAudit(int pid,Timestamp pauditDate, String pactionType, String pactionDescription, User puser) {
		this.id = pid;
		this.auditDate = pauditDate;
		this.actionType = pactionType;
		this.actionDescription = pactionDescription;
		this.user = puser;
		this.setCreatTime(pauditDate);
	}
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Timestamp auditDate) {
		this.auditDate = auditDate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionDescription() {
		return actionDescription;
	}

	public void setActionDescription(String actionDescription) {
		this.actionDescription = actionDescription;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCreatTime() {return creatTime;}
	public void setCreatTime(Timestamp creatTime) {this.creatTime=createFormat.format(creatTime);}
	
	@Override
	  public String toString() {
	    return this.actionDescription;
	  }
	
}
