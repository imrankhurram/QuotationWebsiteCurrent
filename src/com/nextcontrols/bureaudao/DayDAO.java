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

public class DayDAO implements IDayDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IDayDAO instance;
	public static IDayDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new DayDAO();
		}
	}
	public static void setInstance(IDayDAO ins) {
		instance=ins;
	}
	private DayDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Day> getDaysList() {
		List<Day> daysList=new ArrayList<Day>();
		dbConnect();
		String query="SELECT * FROM [CoreDays] order by country asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				Day day=new Day(results.getInt("id"), results.getString("name"), results.getInt("costperday"),results.getString("country"),results.getString("currency"));
				daysList.add(day);
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
		return daysList;
	}
	@Override
	public void addDay(String pName,int pCostPerDay, String pCountry, String pcurrency) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [CoreDays]([name],[costperday],[country],[currency])"+
           " VALUES(?,?,?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pName);
			prpedStatement.setInt(2, pCostPerDay);
			prpedStatement.setString(3, pCountry);
			prpedStatement.setString(4, pcurrency);
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
	public void modifyDay(int pID, String pName,int pCostPerDay, String pCountry, String pcurrency) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [CoreDays] SET [name] = ?"+
					   ",[costperday] = ?"+
					   ",[country] = ?"+
					   ",[currency] = ?"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pName);
			prpedStatement.setInt(2, pCostPerDay);
			prpedStatement.setString(3, pCountry);
			prpedStatement.setString(4, pcurrency);
			prpedStatement.setInt(5, pID);
			
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
	public void deleteDay(int pID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [CoreDays]"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, pID);
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
		String query="SELECT distinct(country) FROM [CoreDays] order by country asc";
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
	public List<String> getCurrencyList() {
		List<String> currenciesList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(currency) FROM [CoreDays] order by currency asc";
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
	
	public Map<String, Integer> getRates(String pCountry) {
		Map<String, Integer> rates = new HashMap<String, Integer>();
		
		dbConnect();
		String query = "SELECT [name],[costperday]"+
					   " FROM [CoreDays]"+
					   " where country like '"+pCountry+"' AND"+
					   " (name like '%Calibration Travel%'"+
					   " OR name like '%Installation Travel%'"+
					   " OR name like '%Sensor Calibration%'"+
					   " OR name like '%System Installation%')"+
					   " order by name asc";
	
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				rates.put(results.getString("name"),(int)(results.getFloat("costperday")));
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
		return rates;
	}
}
