package com.nextcontrols.utility;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Email implements IEmail, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String from;

	private ArrayList<String> fileNames;

	private String text;

	private String subject;

	private String[] recipients;
	private String[] s_arr;

	public Email() {

	}
	public Email(String pSubject, String pTextBody,
			ArrayList<String> pFileName, String[] pTo, String pFrom)
			throws MessagingException {
		recipients = pTo;
		from = pFrom;
		//text = pTextBody + "\r\n\r\n" + getDisclaimer();
		s_arr = pTextBody.split("\n");
		StringBuilder sb = new StringBuilder();
		sb.append("<html><body><table border='1'>");
		for(int i=0; i<s_arr.length; i++) {
			sb.append("<tr><td>");
			sb.append(s_arr[i]);
			sb.append("</td></tr>");
		}
		sb.append("</table><br/>");
		text = sb.toString();
		text = text+ "\r\n\r\n" + "\r\n\r\n"+ getDisclaimer();
		sb.append("</body></html>");
		subject = pSubject;
		fileNames = pFileName;
	} 

	public void sendEmail() throws MessagingException {
		Message msg;
		try {
			ServiceProperties.getInstance().loadProperties();

			Properties props = ServiceProperties.getInstance()
					.getProperties();
			Session session = Session.getInstance(props);
			//System.out.println(props.getProperty("QuotationWeb.conString"));
			msg = new MimeMessage(session);
			Transport.send(setAttributes(msg));
		} catch (Exception e) {
			e.printStackTrace();
			throw new MessagingException("Error: Unable to  Send EmailBean");
		}

	}
	
	private Message setAttributes(Message pMessage) throws AddressException,
			MessagingException {
		// From
		for (int i = 0; recipients.length > i; i++) {
			System.out.println(recipients[i]);
		}
		pMessage.setFrom(new InternetAddress(from));
		// To
		Address[] addresses = new Address[recipients.length];
		if (recipients.length >= 1) {
			for (int i = 0; recipients.length > i; i++) {
				addresses[i] = new InternetAddress(recipients[i]);
				System.out.println(addresses[i]);
			}
		}
		pMessage.setRecipients(Message.RecipientType.TO, addresses);
		// Set Date
		pMessage.setSentDate(new Date());
		// Set Subject
		if (subject == null)
			throw new MessagingException();
		else
			// System.out.println(subject);
			pMessage.setSubject(subject);
		// Set message parts
		BodyPart mbp = new MimeBodyPart();
		MimeMultipart multi = new MimeMultipart();
		// Set Text
		/*if (text != null) {
			mbp.setText(text);
			multi.addBodyPart(mbp);
		}*/
		// Set Text
		if (text != null) {
			mbp.setContent(text, "text/html");
			multi.addBodyPart(mbp);
		}

		
		// Attach file
		if(fileNames!=null){
		for (String fileName : fileNames) {
			mbp = new MimeBodyPart();
			if (fileName != null) {
				FileDataSource fds = new FileDataSource(fileName);
				mbp.setDataHandler(new DataHandler(fds));
				mbp.setFileName(fds.getName());
				multi.addBodyPart(mbp);
			}
		}
		}
		// Apply attributes
		pMessage.setContent(multi);
		// System.out.println("EmailBean ..."+pMessage.getAllRecipients());
		return pMessage;

	}

	private String getDisclaimer() {

		String tDisclaimer = "";
		InputStream stream = Email.class.getResourceAsStream("/footer.txt");

		try {
			byte[] disclaimer = new byte[1500];
			while (!(-1 == (stream.read()))) {

				stream.read(disclaimer);
			}
			for (int i = 0; i <= disclaimer.length - 1; i++) {

				tDisclaimer = (tDisclaimer + (char) disclaimer[i]);

			}
		} catch (IOException e1) {
			System.out.println("Reading disclaimer file failed");

		}
		return tDisclaimer;

	}

}
