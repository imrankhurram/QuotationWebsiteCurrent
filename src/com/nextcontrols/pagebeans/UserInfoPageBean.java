package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudao.UserDAO;
import com.nextcontrols.bureaudomain.User;

@ManagedBean(name="userInfo")
@SessionScoped
public class UserInfoPageBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private User user = new User();
	boolean shown= false;
	private static boolean wrongDetails=false;
	String nextCompanyName ="Tutela Monitoring Systems";
	String currTime;
	String currDate;
	SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMM yyyy HH:mm");
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void setWrongDetails(boolean WrongDetails) {
		wrongDetails = WrongDetails;
	}

	public boolean isWrongDetails() {
		return wrongDetails;
	}
	
	public String getCurrTime() {
		return this.now("HH:mm a");
	}

	public void setCurrTime(String currTime) {
		this.currTime = currTime;
	}

	public String getCurrDate() {
		return this.now("dd MMM yyyy");
	}

	public void setCurrDate(String currDate) {
		this.currDate = currDate;
	}

	public String getNextCompanyName() {
		//if(user.getCountry().compareToIgnoreCase("UK")==0)	
			return "Tutela Monitoring Systems";
		//else
		//	return "Tutela Monitoring LLC";
	}

	public void setNextCompanyName(String nextCompanyName) {
		this.nextCompanyName = nextCompanyName;
	}

	/**
	 * login function
	 * @return
	 */
	public String processLogin(){
		user = UserDAO.getInstance().correctPassword(this.user.getUsername(), this.user.getPassword());
		
		if (user!=null){
			wrongDetails=false;
			UserAuditDAO.getInstance().addUserAudit("LoggedIn", "User Logged In");
			return "HomePage?faces-redirect=true";
			
		}else{
			wrongDetails=true;
			user = new User();
			return "SystemLogin?faces-redirect=true";
		}
	 }
	public  String actionLogout (){
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		session.removeAttribute("userInfo");
		session.invalidate();
 	    return "SystemLogin?faces-redirect=true";
	}
	public String acceptTermsnConditions(){
		//System.out.println("Accept Terms");
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean user = (UserInfoPageBean)session.getAttribute("userInfo");
		user.getUser().setTermsnConditions(true);
		session.removeAttribute("userInfo");
		UserDAO.getInstance().acceptTermsnConidtions(user.getUser().getUserId()+"");
		session.setAttribute("userInfo", user);
		return "HomePage?faces-redirect=true";
	}
	
	public String now(String dateFormat) {
	    Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
	    return sdf.format(cal.getTime());

	  }
}
