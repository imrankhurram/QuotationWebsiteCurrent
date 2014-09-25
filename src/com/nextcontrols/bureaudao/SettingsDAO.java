package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nextcontrols.bureaudomain.Day;
import com.nextcontrols.bureaudomain.Quotation;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class SettingsDAO implements ISettingsDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn = null;
	private static ISettingsDAO instance;

	public static ISettingsDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new SettingsDAO();
		}
	}

	public static void setInstance(ISettingsDAO ins) {
		instance = ins;
	}

	private SettingsDAO() {

	}

	private void dbConnect() {
		try {
			dbConn = ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Float getHoursInDay() {
		float hours = 0;
		dbConnect();
		String query = "SELECT * FROM [Settings]";
		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				hours = results.getFloat("hours_in_day");
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
		return hours;
	}

	@Override
	public void saveHoursInDay(float hours) {
		dbConnect();
		boolean isExist = false;
		String query = "SELECT * FROM [Settings]";
		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				isExist = true;
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
		PreparedStatement prpedStatement = null;
		dbConnect();
		if (isExist) {
			query = "UPDATE [Settings] SET [hours_in_day] = ?";
		} else {
			query = "INSERT INTO [Settings] ([hours_in_day]) VALUES (?)";
		}
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setFloat(1, hours);
			
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

}
