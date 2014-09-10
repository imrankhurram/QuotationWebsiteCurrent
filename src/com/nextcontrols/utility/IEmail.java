package com.nextcontrols.utility;

import java.io.Serializable;

import javax.mail.MessagingException;


public interface IEmail extends Serializable{
	
	public void sendEmail ()throws MessagingException ;
	
}
