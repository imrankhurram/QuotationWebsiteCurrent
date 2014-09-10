package com.nextcontrols.bureaudomain;

// default package
// Generated 09-Feb-2010 15:54:24 by Hibernate Tools 3.2.2.GA

import java.io.Serializable;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int userId;
	private String username;
	private String password = "";
	private String firstName = "";
	private String lastName = "";
	private String email = "";
	/*
	 * private String country=""; private String currency="";
	 */
	private String usertype = "";
	// private String company="";
	private double maxalloweddiscount;
	private boolean boolEnabled = true;
	private boolean termsnConditions = false;
	private String title = "";
	private String phoneNumber = "";

	Company company;

	public User() {
		company = new Company();
	}

	public User(int userId) {
		this.userId = userId;
		company = new Company();
	}

	public User(int pUserId, String pUsername, String pPassword,
			String pFirstName, String pLastName, String pEmail,
			double pMaxalloweddiscount, boolean pEnabled, String pUserType,
			Company pCompany, boolean pTermsnConditions, String title,
			String phoneNumber) {
		this.userId = pUserId;
		this.firstName = pFirstName;
		this.lastName = pLastName;
		this.email = pEmail;
		// this.country = pCountry;
		this.username = pUsername;
		this.password = pPassword;
		// this.currency =pCurrency;
		this.maxalloweddiscount = pMaxalloweddiscount;
		this.boolEnabled = pEnabled;
		this.usertype = pUserType;
		// this.company = pCompany;
		company = pCompany;
		this.termsnConditions = pTermsnConditions;
		if (!"".equals(title))
			this.title = title;
		else
			this.title = "";
		if (!"".equals(phoneNumber))
			this.phoneNumber = phoneNumber;
		else
			this.phoneNumber = "";
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/*
	 * public String getCountry() { return this.country; }
	 * 
	 * public void setCountry(String country) { this.country = country; }
	 */

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		/*
		 * //encrypting the password before saving it byte[] defaultBytes =
		 * password.getBytes(); StringBuffer hexString = new StringBuffer();
		 * try{ MessageDigest algorithm = MessageDigest.getInstance("MD5");
		 * algorithm.reset(); algorithm.update(defaultBytes); byte
		 * messageDigest[] = algorithm.digest(); for (int
		 * i=0;i<messageDigest.length;i++) { String hex =
		 * Integer.toHexString(0xFF & messageDigest[i]); if(hex.length()==1)
		 * hexString.append('0'); hexString.append(hex); } }catch (Exception
		 * e){e.printStackTrace();} this.password = hexString.toString();
		 */
		if (password.compareToIgnoreCase("") != 0) {
			this.password = password;
		}
	}

	/*
	 * public String getCurrency() { return currency; }
	 * 
	 * public void setCurrency(String currency) { this.currency = currency; }
	 */

	public double getMaxalloweddiscount() {
		return maxalloweddiscount;
	}

	public void setMaxalloweddiscount(double maxalloweddiscount) {
		this.maxalloweddiscount = maxalloweddiscount;
	}

	public void setBoolEnabled(boolean boolEnabled) {
		this.boolEnabled = boolEnabled;
	}

	public boolean isBoolEnabled() {
		return boolEnabled;
	}

	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public boolean isTermsnConditions() {
		return termsnConditions;
	}

	public void setTermsnConditions(boolean termsnConditions) {
		this.termsnConditions = termsnConditions;
	}

}
