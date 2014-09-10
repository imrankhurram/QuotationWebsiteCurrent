package com.nextcontrols.bureaudao;

import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.UserAudit;

public interface IUserAuditDAO {
	public List<UserAudit> getUserAuditsList() ;
	public List<UserAudit> getUserAuditsList(int pUserID, int pCompanyID, String pUserType, Date pDateFrom, Date pDateTo);
	public void addUserAudit( String pactionType, String pactionDescription);
	public void modifyUserAudit( String pactionType, String pactionDescription);
	public void deleteUserAudit(int pauditID);
}
