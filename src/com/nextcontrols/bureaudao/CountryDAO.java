package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.Country;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class CountryDAO implements ICountryDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static ICountryDAO instance;
	public static ICountryDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new CountryDAO();
		}
	}
	public static void setInstance(ICountryDAO ins) {
		instance=ins;
	}
	private CountryDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Country> getCountriesList() {
		List<Country> countriesList=new ArrayList<Country>();
		dbConnect();
		String query="SELECT * FROM [Country]";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				Country country=new Country(results.getString("country"), results.getString("full_name"), results.getString("currency"), 
						results.getDouble("conversionratefrompounds"), results.getDouble("freight"), results.getDouble("duty"));
				countriesList.add(country);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			stmnt=null;
			results=null;
		}
		return countriesList;
	}
	@Override
	public void addCountry(String pcountry, String pfullName, String pcurrency,
			double pconversionratefrompounds, double pfreight, double pduty) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [Country]([country],[full_name],[currency],[conversionratefrompounds],[freight],[duty])"+
           " VALUES(?,?,?,?,?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pcountry);
			prpedStatement.setString(2, pfullName);
			prpedStatement.setString(3, pcurrency);
			prpedStatement.setDouble(4, pconversionratefrompounds);
			prpedStatement.setDouble(5, pfreight);
			prpedStatement.setDouble(6, pduty);
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			prpedStatement=null;
		}						
	}
	@Override
	public void modifyCountry(String pcountry, String pfullName,
			String pcurrency, double pconversionratefrompounds, double pfreight, double pduty) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [Country] SET [full_name] = ?"+
					   ",[currency] = ?"+
					   ",[conversionratefrompounds] = ?"+
					   ",[freight] = ?"+
					   ",[duty] = ?"+
					   "WHERE [country]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pfullName);
			prpedStatement.setString(2, pcurrency);
			prpedStatement.setDouble(3, pconversionratefrompounds);
			prpedStatement.setDouble(4, pfreight);
			prpedStatement.setDouble(5, pduty);
			prpedStatement.setString(6, pcountry);
			
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			prpedStatement=null;
		}		
		
	}
	@Override
	public void deleteCountry(String pcountry) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [Country]"+
					   "WHERE [country]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pcountry);
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			prpedStatement=null;
		}
		
	}
	
	@Override
	public List<String> getCountryList() {
		List<String> countriesList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(country) FROM [Country] order by country asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				countriesList.add(results.getString("country"));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			stmnt=null;
			results=null;
		}
		return countriesList;	
	}
	
	@Override
	public List<String> getCurrencyList(){
		List<String> currenciesList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(currency) FROM [Country] order by currency asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				currenciesList.add(results.getString("currency"));
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				stmnt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			stmnt=null;
			results=null;
		}
		return currenciesList;	
	}
}
