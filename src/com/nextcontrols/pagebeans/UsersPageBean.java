package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudao.CompanyDAO;
import com.nextcontrols.bureaudao.UserDAO;
import com.nextcontrols.bureaudomain.Company;
import com.nextcontrols.bureaudomain.User;

@ManagedBean(name="users")
@SessionScoped
public class UsersPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<User> userList;
	private List<User> filteredUserList;
	private User selectedUser;
	private User newUser;

	private List<SelectItem> companiesDisplayList;
	private List<Company> companiesList;
	private String newCompany ;
	private String selectedCompany;
	
	public UsersPageBean(){
		userList = new ArrayList<User>();
		newUser= new User();
		companiesDisplayList=new ArrayList<SelectItem> ();
		companiesList = new ArrayList<Company>();
		this.companiesList=CompanyDAO.getInstance().getCompaniesList();
		for (int i=0;i<=this.companiesList.size()-1;i++){
			companiesDisplayList.add(new SelectItem(companiesList.get(i).getId(),companiesList.get(i).getCompanyName()));
		}
	}

	public void setUserList() {
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(userInfo.getUser().getUsertype().compareToIgnoreCase("Master")==0
				||userInfo.getUser().getUsertype().compareToIgnoreCase("Admin")==0){
			this.userList = UserDAO.getInstance().getUserList(-1,true);
		}else{
			this.userList = UserDAO.getInstance().getUserList(userInfo.getUser().getCompany().getId(),true);
		}
	}

	public List<User> getUserList() {
		setUserList();
		return userList;
	}
	
	public List<User> getFilteredUserList() {
		return filteredUserList;
	}

	public void setFilteredUserList(List<User> filteredUserList) {
		this.filteredUserList = filteredUserList;
	}

	public void setSelectedUser(User SelectedUser) {
		selectedUser = SelectedUser;
		if(selectedUser!=null){
			selectedCompany = selectedUser.getCompany().getId()+"";
		}
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

	public List<SelectItem> getCompaniesDisplayList() {
		return companiesDisplayList;
	}

	public void setCompaniesDisplayList(List<SelectItem> companiesDisplayList) {
		this.companiesDisplayList = companiesDisplayList;
	}

	public List<Company> getCompaniesList() {
		return companiesList;
	}

	public void setCompaniesList(List<Company> companiesList) {
		this.companiesList = companiesList;
	}

	public String getNewCompany() {
		return newCompany;
	}

	public void setNewCompany(String newCompany) {
		this.newCompany = newCompany;
	}

	public String getSelectedCompany() {
		return selectedCompany;
	}

	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String addNewUser(){
		//System.out.println(newCompany);
		if(newCompany!=null && newCompany.compareToIgnoreCase("")!=0){
			UserDAO.getInstance().addUser(this.newUser.getUsername(), this.newUser.getPassword(), this.newUser.getFirstName(),
					this.newUser.getLastName(), this.newUser.getUsername(), 
					this.newUser.getMaxalloweddiscount(),this.newUser.isBoolEnabled(),this.newUser.getUsertype(),Integer.parseInt(newCompany),this.newUser.getTitle(),this.newUser.getPhoneNumber());
			newUser=new User();
			return "UsersPage?faces-redirect=true";
		}
		else
			return "UsersPage?faces-redirect=true";

	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyUser(){
		if(this.selectedUser!= null){
			UserDAO.getInstance().modifyUser(this.selectedUser.getUserId()+"", this.selectedUser.getPassword(), this.selectedUser.getFirstName(),
				this.selectedUser.getLastName(), this.selectedUser.getUsername(), this.selectedUser.getMaxalloweddiscount(),this.selectedUser.isBoolEnabled(),this.selectedUser.getUsertype(),Integer.parseInt(selectedCompany),this.selectedUser.getTitle(),this.selectedUser.getPhoneNumber());
		}
		return "UsersPage?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteUser(){
		if(this.selectedUser!= null){
			UserDAO.getInstance().deleteUser(this.selectedUser.getUserId()+"");
		}
		return "UsersPage?faces-redirect=true";
	}
}
