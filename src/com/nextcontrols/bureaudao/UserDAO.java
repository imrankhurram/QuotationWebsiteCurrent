package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.Company;
import com.nextcontrols.bureaudomain.Country;
import com.nextcontrols.bureaudomain.User;
import com.nextcontrols.utility.Encryption;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class UserDAO implements IUserDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn = null;
	private static IUserDAO instance;

	public static IUserDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new UserDAO();
		}
	}

	public static void setInstance(IUserDAO ins) {
		instance = ins;
	}

	private UserDAO() {

	}

	private void dbConnect() {
		try {
			dbConn = ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Override public List<User> getUserList(int pcompanyID) { List<User>
	 * userList=new ArrayList<User>(); dbConnect();
	 * 
	 * String query="SELECT * FROM [user] as usr"+
	 * " inner join [Company] as company on usr.company_id = company.id"
	 * ; if(pcompanyID>0){ query+= " where usr.company_id ="+pcompanyID; }
	 * Statement stmnt=null; ResultSet results = null; try{
	 * stmnt=dbConn.createStatement(); results=stmnt.executeQuery(query); while
	 * (results.next()){ Company company=new
	 * Company(results.getInt("id"),results.getString("company_name"),
	 * results.getDouble("freight"), results.getDouble("duty"),
	 * results.getDouble("markup"),results.getDouble("discountpercent"),null);
	 * 
	 * User user=new User(results.getInt("user_id"),
	 * results.getString("user_name"), results.getString("password"),
	 * results.getString("first_name"),
	 * results.getString("last_name"),results.getString("email"),
	 * results.getDouble
	 * ("max_alloweddiscount"),results.getBoolean("enabled"),results
	 * .getString("user_type"),company); userList.add(user); } }catch
	 * (SQLException e){ e.printStackTrace(); }finally{ try { results.close();
	 * stmnt.close(); } catch (SQLException e) { e.printStackTrace(); }
	 * dbConn=null; stmnt=null; results=null; } return userList; }
	 */

	@Override
	public List<User> getUserList(int pcompanyID, boolean pallUsers) {
		List<User> userList = new ArrayList<User>();
		dbConnect();

		String query = "SELECT * FROM [user] as usr"
				+ " inner join [Company] as company on usr.company_id = company.id"
				+ " inner join [Country] as cout on company.country = cout.country";
		if (pcompanyID > 0) {
			query += " where usr.company_id =" + pcompanyID;
		}
		if (!pallUsers) {
			query += " and (usr.user_type='User' or usr.user_type='Supervisor') ";

		}

		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				Country country = new Country(results.getString("country"),
						results.getString("full_name"),
						results.getString("currency"),
						results.getDouble("conversionratefrompounds"),
						results.getDouble("freight"), results.getDouble("duty"));
				Company company = new Company(results.getInt("id"),
						results.getString("company_name"),
						results.getDouble("markup"),
						results.getDouble("hwdiscount"),
						results.getDouble("monitoringdiscount"), country,
						results.getString("company_ref"),
						results.getBoolean("multiyear_options"));

				User user = new User(results.getInt("user_id"),
						results.getString("user_name"), "",
						results.getString("first_name"),
						results.getString("last_name"),
						results.getString("email"),
						results.getDouble("max_alloweddiscount"),
						results.getBoolean("enabled"),
						results.getString("user_type"), company,
						results.getBoolean("termsnconditions"),
						results.getString("title"),
						results.getString("phone_number"));
				userList.add(user);
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
		return userList;
	}

	@Override
	public User correctPassword(String username, String password) {
		PreparedStatement login = null;
		String query = "SELECT * FROM [user] as usr"
				+ " inner join [Company] as company on usr.company_id = company.id "
				+ "inner join [Country] as country on company.country = country.country WHERE [password]=? and [user_name]=? and [enabled]= 1";
		dbConnect();
		try {
			login = dbConn.prepareStatement(query);
			login.setString(1,
					Encryption.getInstance().encryptPassword(password));
			login.setString(2, username);
			ResultSet results = login.executeQuery();
			if (results.next()) {
				Country country = new Country(results.getString("country"),
						results.getString("full_name"),
						results.getString("currency"),
						results.getDouble("conversionratefrompounds"),
						results.getDouble("freight"), results.getDouble("duty"));

				Company company = new Company(results.getInt("id"),
						results.getString("company_name"),
						results.getDouble("markup"),
						results.getDouble("hwdiscount"),
						results.getDouble("monitoringdiscount"), country,
						results.getString("company_ref"),
						results.getBoolean("multiyear_options"));

				User user = new User(results.getInt("user_id"),
						results.getString("user_name"),
						results.getString("password"),
						results.getString("first_name"),
						results.getString("last_name"),
						results.getString("email"),
						results.getDouble("max_alloweddiscount"),
						results.getBoolean("enabled"),
						results.getString("user_type"), company,
						results.getBoolean("termsnconditions"),
						results.getString("title"),
						results.getString("phone_number"));
				return user;
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void addUser(String pUserName, String pPassword, String pFirstName,
			String pLastName, String pEmail, double pMaxAllowedDiscount,
			boolean pEnabled, String pUsertype, int pCompany, String title,
			String phoneNumber) {
		PreparedStatement prpedStatement = null;
		String query = "INSERT INTO [user] ([user_name],[password],[first_name],[last_name]"
				+ ",[email],[max_alloweddiscount],[enabled],[user_type],[company_id],[title],[phone_number]) VALUES(?,?,?,?,?,?,?,?,?,?,?);";
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setString(1, pUserName);
			prpedStatement.setString(2, Encryption.getInstance()
					.encryptPassword(pPassword));
			prpedStatement.setString(3, pFirstName);
			prpedStatement.setString(4, pLastName);
			prpedStatement.setString(5, pEmail);
			prpedStatement.setDouble(6, pMaxAllowedDiscount);
			prpedStatement.setBoolean(7, pEnabled);
			prpedStatement.setString(8, pUsertype);
			prpedStatement.setInt(9, pCompany);
			prpedStatement.setString(10, title);
			prpedStatement.setString(11, phoneNumber);
			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modifyUser(String pUserID, String pPassword, String pFirstName,
			String pLastName, String pEmail, double pMaxAllowedDiscount,
			boolean pEnabled, String pUsertype, int pCompany, String title,
			String phone) {
		PreparedStatement prpedStatement = null;
		String query = "UPDATE [user] SET [password] = ?"
				+ ",[first_name] = ?" + ",[last_name] = ?" + ",[email] = ?"
				+ ",[max_alloweddiscount] = ?" + ",[enabled] = ?"
				+ ",[user_type] = ?" + ",[company_id] = ?" + ",[title] = ?"
				+ ",[phone_number] = ?" + "WHERE [user_id]= ?";
		if (pPassword.compareToIgnoreCase("") == 0) {
			query = "UPDATE [user] SET [first_name] = ?"
					+ ",[last_name] = ?" + ",[email] = ?"
					+ ",[max_alloweddiscount] = ?" + ",[enabled] = ?"
					+ ",[user_type] = ?" + ",[company_id] = ?" + ",[title] = ?"
					+ ",[phone_number] = ?" + "WHERE [user_id]= ?";
		}
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			if (pPassword.compareToIgnoreCase("") != 0) {
				prpedStatement.setString(1, Encryption.getInstance()
						.encryptPassword(pPassword));
				prpedStatement.setString(2, pFirstName);
				prpedStatement.setString(3, pLastName);
				prpedStatement.setString(4, pEmail);
				prpedStatement.setDouble(5, pMaxAllowedDiscount);
				prpedStatement.setBoolean(6, pEnabled);
				prpedStatement.setString(7, pUsertype);
				prpedStatement.setInt(8, pCompany);
				prpedStatement.setString(9, title);
				prpedStatement.setString(10, phone);
				prpedStatement.setString(11, pUserID);
			} else {
				prpedStatement.setString(1, pFirstName);
				prpedStatement.setString(2, pLastName);
				prpedStatement.setString(3, pEmail);
				prpedStatement.setDouble(4, pMaxAllowedDiscount);
				prpedStatement.setBoolean(5, pEnabled);
				prpedStatement.setString(6, pUsertype);
				prpedStatement.setInt(7, pCompany);
				prpedStatement.setString(8, title);
				prpedStatement.setString(9, phone);
				prpedStatement.setString(10, pUserID);
			}

			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void deleteUser(String pUserID) {
		PreparedStatement prpedStatement = null;
		String query = "DELETE FROM [user]"
				+ "WHERE [user_id]= ?";
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setString(1, pUserID);
			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void acceptTermsnConidtions(String pUserID) {
		PreparedStatement prpedStatement = null;
		String query = "UPDATE [user] SET [termsnconditions] = ?"
				+ "WHERE [user_id]= ?";
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setBoolean(1, true);
			prpedStatement.setString(2, pUserID);
			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
