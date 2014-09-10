package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.UserDAO;
import com.nextcontrols.bureaudomain.User;

@ManagedBean(name = "mainusers")
@SessionScoped
public class MainUsersPageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> userList;
	private User selectedUser;
	private User newUser;

	public MainUsersPageBean() {
		userList = new ArrayList<User>();
		newUser = new User();

	}

	public void setUserList() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		this.userList = UserDAO.getInstance().getUserList(
				userInfo.getUser().getCompany().getId(), false);
	}

	public List<User> getUserList() {
		setUserList();
		return userList;
	}

	public void setSelectedUser(User SelectedUser) {
		selectedUser = SelectedUser;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public User getNewUser() {
		return newUser;
	}

	public void setNewUser(User newUser) {
		this.newUser = newUser;
	}

	/**
	 * adds a new user and inserts audit
	 * 
	 * @return
	 */
	public String addNewUser() {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		UserDAO.getInstance().addUser(this.newUser.getUsername(),
				this.newUser.getPassword(), this.newUser.getFirstName(),
				this.newUser.getLastName(), this.newUser.getEmail(),
				this.newUser.getMaxalloweddiscount(),
				this.newUser.isBoolEnabled(), this.newUser.getUsertype(),
				userInfo.getUser().getCompany().getId(),
				this.newUser.getTitle(), this.newUser.getPhoneNumber());
		newUser = new User();
		return "MainUsersPage?faces-redirect=true";
	}

	/**
	 * modify a user and inserts audit
	 * 
	 * @return
	 */
	public String modifyUser() {
		if (this.selectedUser != null) {
			ExternalContext ectx = FacesContext.getCurrentInstance()
					.getExternalContext();
			HttpSession session = (HttpSession) ectx.getSession(false);
			UserInfoPageBean userInfo = (UserInfoPageBean) session
					.getAttribute("userInfo");
			UserDAO.getInstance().modifyUser(
					this.selectedUser.getUserId() + "",
					this.selectedUser.getPassword(),
					this.selectedUser.getFirstName(),
					this.selectedUser.getLastName(),
					this.selectedUser.getEmail(),
					this.selectedUser.getMaxalloweddiscount(),
					this.selectedUser.isBoolEnabled(),
					this.selectedUser.getUsertype(),
					userInfo.getUser().getCompany().getId(),
					this.selectedUser.getTitle(),
					this.selectedUser.getPhoneNumber());
		}
		return "MainUsersPage?faces-redirect=true";
	}

	/**
	 * delete a user and inserts audit
	 * 
	 * @return
	 */
	public String deleteUser() {
		if (this.selectedUser != null) {
			UserDAO.getInstance()
					.deleteUser(this.selectedUser.getUserId() + "");
		}
		return "MainUsersPage?faces-redirect=true";
	}
}
