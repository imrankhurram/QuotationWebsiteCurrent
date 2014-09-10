package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.nextcontrols.bureaudomain.User;
import com.nextcontrols.bureaudomain.UserAudit;
import com.nextcontrols.pagebeans.UserInfoPageBean;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class UserAuditDAO implements IUserAuditDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn = null;
	private static IUserAuditDAO instance;

	public static IUserAuditDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new UserAuditDAO();
		}
	}

	public static void setInstance(IUserAuditDAO ins) {
		instance = ins;
	}

	private UserAuditDAO() {

	}

	private void dbConnect() {
		try {
			dbConn = ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<UserAudit> getUserAuditsList(int pUserID, int pCompanyID,
			String pUserType, Date pDateFrom, Date pDateTo) {
		List<UserAudit> userAuditsList = new ArrayList<UserAudit>();
		dbConnect();
		String query = "SELECT * FROM [UserAudit] as useraudit "
				+ " Inner Join [user] as us on useraudit.user_id = us.user_id "
				+ " where useraudit.action_type !='Error' AND (useraudit.audit_date >= '"
				+ new java.sql.Timestamp(pDateFrom.getTime())
				+ "' AND useraudit.audit_date <= '"
				+ new java.sql.Timestamp(pDateTo.getTime())
				+ "') "
				+ " order by useraudit.audit_date desc";
		if (pUserType.compareToIgnoreCase("Admin") == 0
				|| pUserType.compareToIgnoreCase("Master") == 0) {
			query = "SELECT * FROM [UserAudit] as useraudit "
					+ " Inner Join [user] as us on useraudit.user_id = us.user_id "
					+ " where useraudit.action_type !='Error' AND (useraudit.audit_date >= '"
					+ new java.sql.Timestamp(pDateFrom.getTime())
					+ "' AND useraudit.audit_date <= '"
					+ new java.sql.Timestamp(pDateTo.getTime())
					+ "') "
					+ " order by useraudit.audit_date desc";
		} else if (pUserType.compareToIgnoreCase("Supervisor") == 0) {
			query = "SELECT * FROM [UserAudit] as useraudit "
					+ " Inner join [user] as puser on useraudit.user_id = puser.user_id"
					+ " where useraudit.action_type !='Error' AND (useraudit.audit_date >= '"
					+ new java.sql.Timestamp(pDateFrom.getTime())
					+ "' AND useraudit.audit_date <= '"
					+ new java.sql.Timestamp(pDateTo.getTime())
					+ "') "
					+ " AND puser.company_id = "
					+ pCompanyID
					+ " order by useraudit.audit_date desc";
		} else {
			query = "SELECT * FROM [UserAudit] as useraudit "
					+ " Inner join [user] as puser on useraudit.user_id = puser.user_id"
					+ " where useraudit.action_type !='Error' AND (useraudit.audit_date >= '"
					+ new java.sql.Timestamp(pDateFrom.getTime())
					+ "' AND useraudit.audit_date <= '"
					+ new java.sql.Timestamp(pDateTo.getTime())
					+ "') "
					+ " AND puser.user_id = "
					+ pUserID
					+ " order by useraudit.audit_date desc";
		}
		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {

				User user = new User(results.getInt("user_id"),
						results.getString("user_name"),
						results.getString("password"),
						results.getString("first_name"),
						results.getString("last_name"),
						results.getString("email"),
						results.getDouble("max_alloweddiscount"),
						results.getBoolean("enabled"),
						results.getString("user_type"), null,
						results.getBoolean("termsnconditions"),
						results.getString("title"),
						results.getString("phone_number"));

				UserAudit useraudit = new UserAudit(results.getInt("id"),
						results.getTimestamp("audit_date"),
						results.getString("action_type"),
						results.getString("action_description"), user);
				userAuditsList.add(useraudit);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn = null;
			stmnt = null;
			results = null;
		}
		return userAuditsList;

	}

	@Override
	public List<UserAudit> getUserAuditsList() {
		return null;
	}

	@Override
	public void addUserAudit(String pactionType, String pactionDescription) {
		ExternalContext ectx = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) ectx.getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean) session
				.getAttribute("userInfo");
		PreparedStatement prpedStatement = null;
		String query = "INSERT INTO [UserAudit]([user_id],[audit_date],[action_type],[action_description])"
				+ " VALUES(?,?,?,?);";
		dbConnect();
		// System.out.println(userInfo.getUser().getUserId()+" : "+pactionType+" : "+pactionDescription);
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setInt(1, userInfo.getUser().getUserId());
			prpedStatement.setTimestamp(2, new Timestamp(Calendar.getInstance()
					.getTime().getTime()));
			prpedStatement.setString(3, pactionType);
			prpedStatement.setString(4, pactionDescription);
			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn = null;
			prpedStatement = null;
		}

	}

	@Override
	public void modifyUserAudit(String pactionType, String pactionDescription) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteUserAudit(int pauditID) {
		// TODO Auto-generated method stub

	}

}
