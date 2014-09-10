package com.nextcontrols.pagebeans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.nextcontrols.bureaudao.RemortMonPRecordingServiceDAO;
import com.nextcontrols.bureaudao.RemortRecordingServiceDAO;
import com.nextcontrols.bureaudao.UserAuditDAO;
import com.nextcontrols.bureaudomain.RemortMonPRecordingService;
import com.nextcontrols.bureaudomain.RemortRecordingService;

@ManagedBean(name="remortrecordingservice")
@SessionScoped
public class RemortRecordingServicePageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<RemortRecordingService> remortRecordingServicesList;
	private RemortRecordingService selectedRemortRecordingService;
	private RemortRecordingService newRemortRecordingService;
	private List<RemortMonPRecordingService> remortMonPRecordingServicesList;
	private RemortMonPRecordingService selectedRemortMonPRecordingService;
	private RemortMonPRecordingService newRemortMonPRecordingService;

	public RemortRecordingServicePageBean(){
		remortRecordingServicesList = new ArrayList<RemortRecordingService>();
		remortMonPRecordingServicesList = new ArrayList<RemortMonPRecordingService>();
		newRemortRecordingService= new RemortRecordingService();
		newRemortMonPRecordingService = new RemortMonPRecordingService();
	}


	public List<RemortRecordingService> getRemortRecordingServicesList() {
		setRemortRecordingServicesList();
		return remortRecordingServicesList;
	}

	public void setRemortRecordingServicesList() {
		this.remortRecordingServicesList = RemortRecordingServiceDAO.getInstance().getRemortRecordingsList("UK");
	}

	public List<RemortMonPRecordingService> getRemortMonPRecordingServicesList() {
		setRemortMonPRecordingServicesList();
		return remortMonPRecordingServicesList;
	}


	public void setRemortMonPRecordingServicesList() {
		this.remortMonPRecordingServicesList = RemortMonPRecordingServiceDAO.getInstance().getRemortMonPRecordingsList("UK");
	}

	
	public RemortRecordingService getSelectedRemortRecordingService() {
		return selectedRemortRecordingService;
	}

	public void setSelectedRemortRecordingService(
			RemortRecordingService selectedRemortRecordingService) {
		this.selectedRemortRecordingService = selectedRemortRecordingService;
	}

	public RemortRecordingService getNewRemortRecordingService() {
		return newRemortRecordingService;
	}

	public void setNewRemortRecordingService(
			RemortRecordingService newRemortRecordingService) {
		this.newRemortRecordingService = newRemortRecordingService;
	}

	public RemortMonPRecordingService getSelectedRemortMonPRecordingService() {
		return selectedRemortMonPRecordingService;
	}


	public void setSelectedRemortMonPRecordingService(
			RemortMonPRecordingService selectedRemortMonPRecordingService) {
		this.selectedRemortMonPRecordingService = selectedRemortMonPRecordingService;
	}


	public RemortMonPRecordingService getNewRemortMonPRecordingService() {
		return newRemortMonPRecordingService;
	}


	public void setNewRemortMonPRecordingService(
			RemortMonPRecordingService newRemortMonPRecordingService) {
		this.newRemortMonPRecordingService = newRemortMonPRecordingService;
	}


	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String saveSheet(){
		return "RemoteRecordingServicePage?faces-redirect=true";
	}
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	/*public String addNewRemoteRecordingService(){
		RemortRecordingServiceDAO.getInstance().addRemortRecording(this.newRemortRecordingService.getNumberofsensors(), this.newRemortRecordingService.getPeryearOneYear(), this.newRemortRecordingService.getPermonthOneYear(), this.newRemortRecordingService.getPeryearThreeYear(), this.newRemortRecordingService.getPermonthThreeYear(), this.newRemortRecordingService.getPeryearFiveYear(), this.newRemortRecordingService.getPermonthFiveYear(), "UK");
		newRemortRecordingService=new RemortRecordingService();
		return "AddRemoteMonPRecordingPage?faces-redirect=true";
	}*/
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	/*public String modifyRemoteRecordingService(){
		RemortRecordingServiceDAO.getInstance().modifyRemortRecording(this.selectedRemortRecordingService.getNumberofsensors(), this.selectedRemortRecordingService.getPeryearOneYear(), this.selectedRemortRecordingService.getPermonthOneYear(), this.selectedRemortRecordingService.getPeryearThreeYear(), this.selectedRemortRecordingService.getPermonthThreeYear(), this.selectedRemortRecordingService.getPeryearFiveYear(), this.selectedRemortRecordingService.getPermonthFiveYear(),this.selectedRemortRecordingService.getId(), "UK");
		return "ModifyRemoteMonPRecordingPage?faces-redirect=true";
	}*/
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	/*public String deleteRemoteRecordingService(){
		RemortRecordingServiceDAO.getInstance().deleteRemortRecording(this.getSelectedRemortRecordingService().getId(),"UK");
		return "RemoteRecordingServicePage?faces-redirect=true";
	}*/
	
	
	
	
	/**
	 * adds a new user and inserts audit
	 * @return
	 */
	public String addNewRemoteMonPRecordingService(){
		RemortMonPRecordingServiceDAO.getInstance().addRemortMonPRecording(this.newRemortMonPRecordingService.getNumberofsensors(), this.newRemortMonPRecordingService.getPeryearOneYear(), this.newRemortMonPRecordingService.getPermonthOneYear(), this.newRemortMonPRecordingService.getPeryearThreeYear(), this.newRemortMonPRecordingService.getPermonthThreeYear(), this.newRemortMonPRecordingService.getPeryearFiveYear(), this.newRemortMonPRecordingService.getPermonthFiveYear());
		UserAuditDAO.getInstance().addUserAudit("Add Monitoring Service", "User added the Monitoring Service for sensor count : "+this.selectedRemortMonPRecordingService.getNumberofsensors());
		newRemortRecordingService=new RemortRecordingService();
		return "RemoteRecordingServicePage?faces-redirect=true";
	}
	/**
	 * modify a user and inserts audit
	 * @return
	 */
	public String modifyRemoteMonPRecordingService(){
		if(this.selectedRemortMonPRecordingService!= null){
			RemortMonPRecordingServiceDAO.getInstance().modifyRemortMonPRecording(this.selectedRemortMonPRecordingService.getNumberofsensors(), this.selectedRemortMonPRecordingService.getPeryearOneYear(), this.selectedRemortMonPRecordingService.getPermonthOneYear(), this.selectedRemortMonPRecordingService.getPeryearThreeYear(), this.selectedRemortMonPRecordingService.getPermonthThreeYear(), this.selectedRemortMonPRecordingService.getPeryearFiveYear(), this.selectedRemortMonPRecordingService.getPermonthFiveYear(),this.selectedRemortMonPRecordingService.getId());
			UserAuditDAO.getInstance().addUserAudit("Modify Monitoring Service", "User modified the Monitoring Service for sensor count : "+this.selectedRemortMonPRecordingService.getNumberofsensors());
		}
		return "RemoteRecordingServicePage?faces-redirect=true";
	}
	
	/**
	 * delete a user and inserts audit
	 * @return
	 */
	public String deleteRemoteMonPRecordingService(){
		if(this.selectedRemortMonPRecordingService!= null){
			RemortMonPRecordingServiceDAO.getInstance().deleteRemortMonPRecording(this.getSelectedRemortMonPRecordingService().getId());
			UserAuditDAO.getInstance().addUserAudit("Delete Monitoring Service", "User deleted the Monitoring Service for sensor count : "+this.selectedRemortMonPRecordingService.getNumberofsensors());
		}
		return "RemoteRecordingServicePage?faces-redirect=true";
	}
}
