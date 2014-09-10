package com.nextcontrols.bureaudao;

import java.util.List;

import com.nextcontrols.bureaudomain.User;

public interface IUserDAO {
	public User correctPassword(String pUserName, String pPassword);

	// public List<User> getUserList(int pcompanyID) ;
	public List<User> getUserList(int pcompanyID, boolean pallUsers);

	public void addUser(String pUserName, String pPassword, String pFirstName,
			String pLastName, String pEmail, double pMaxAllowedDiscount,
			boolean pEnabled, String pUsertype, int pCompany, String title,
			String phoneNumber);

	public void modifyUser(String pUserID, String pPassword, String pFirstName,
			String pLastName, String pEmail, double pMaxAllowedDiscount,
			boolean pEnabled, String pUsertype, int pCompany, String title,
			String phone);

	public void deleteUser(String pUserID);

	public void acceptTermsnConidtions(String pUserID);
}
