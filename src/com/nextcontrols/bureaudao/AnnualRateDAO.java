package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.AnnualRate;


/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class AnnualRateDAO implements IAnnualRateDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IAnnualRateDAO instance;
	public static IAnnualRateDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new AnnualRateDAO();
		}
	}
	public static void setInstance(IAnnualRateDAO ins) {
		instance=ins;
	}
	private AnnualRateDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getCountryList() {
		List<String> countriesList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(country) FROM [CoareAnnualRates] order by country asc";
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
		String query="SELECT distinct(currency) FROM [CoareAnnualRates] order by currency asc";
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
	
	
	@Override
	public List<AnnualRate> getAnnualRatesList() {
		List<AnnualRate> annualRatesList=new ArrayList<AnnualRate>();
		dbConnect();
		String query="SELECT * FROM [CoareAnnualRates] order by country asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				AnnualRate annualrate=new AnnualRate(results.getInt("id"), results.getString("description"),results.getInt("costperday"), 
						results.getInt("annualcalibration"),results.getInt("annualdeadtraveldaycost"),
						results.getDouble("threeyearfactor"),results.getDouble("oneyearfactor"),
						results.getString("country"),results.getString("currency"));
				annualRatesList.add(annualrate);
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
		return annualRatesList;		
	}
	@Override
	public void addAnnualRate(String pdescription, int pcostperday, int pannualcalibration,
			int pannualdeadtraveldaycost, double pthreeyearfactor,
			double poneyearfactor, String pcountry, String pcurrency) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [CoareAnnualRates]([description],[costperday],[annualcalibration],[annualdeadtraveldaycost],[threeyearfactor],[oneyearfactor],[country],[currency])"+
           " VALUES(?,?,?,?,?,?,?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pdescription);
			prpedStatement.setInt(2, pcostperday);
			prpedStatement.setInt(3, pannualcalibration);
			prpedStatement.setInt(4, pannualdeadtraveldaycost);
			prpedStatement.setDouble(5, pthreeyearfactor);
			prpedStatement.setDouble(6, poneyearfactor);
			prpedStatement.setString(7, pcountry);
			prpedStatement.setString(8, pcurrency);
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
	public void modifyAnnualRate(int pid, String pdescription, int pcostperday,
			int pannualcalibration, int pannualdeadtraveldaycost,
			double pthreeyearfactor, double poneyearfactor, String pcountry,
			String pcurrency) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [CoareAnnualRates] SET [description] = ?"+
					   ",[costperday] = ?"+
					   ",[annualcalibration] = ?"+
					   ",[annualdeadtraveldaycost] = ?"+
					   ",[threeyearfactor] = ?"+
					   ",[oneyearfactor] = ?"+
					   ",[country] = ?"+
					   ",[currency] = ?"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pdescription);
			prpedStatement.setInt(2, pcostperday);
			prpedStatement.setInt(3, pannualcalibration);
			prpedStatement.setInt(4, pannualdeadtraveldaycost);
			prpedStatement.setDouble(5, pthreeyearfactor);
			prpedStatement.setDouble(6, poneyearfactor);
			prpedStatement.setString(7, pcountry);
			prpedStatement.setString(8, pcurrency);
			prpedStatement.setInt(9, pid);
			
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
	public void deleteAnnualRate(int pID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [CoareAnnualRates]"+
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
	public AnnualRate getAnnualRate(String pCountry){
		AnnualRate tempAnnualRate= null;
		dbConnect();
		String query="SELECT * FROM [CoareAnnualRates] where country like '%"+pCountry+"%'";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			//System.out.println(query);
			while (results.next()){
				tempAnnualRate=new AnnualRate(results.getInt("id"), results.getString("description"),results.getInt("costperday"), 
						results.getInt("annualcalibration"),results.getInt("annualdeadtraveldaycost"),
						results.getDouble("threeyearfactor"),results.getDouble("oneyearfactor"),
						results.getString("country"),results.getString("currency"));
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
		return tempAnnualRate;		
	}
	
	@Override
	public List <AnnualRate> getAnnualRatesForQCreation(String pCountry){
		List <AnnualRate> annualrates = new ArrayList<AnnualRate>();
		
		dbConnect();
		String query = "SELECT *"+
					   " FROM [CoareAnnualRates]"+
					   " where country like '"+pCountry+"' AND"+
					   " (description like '%Annual Calibration%'"+
					   " OR description like '%Dead Travel Day%'"+
					   " OR description like '%Default%'"+
					   " OR description like '%Temperature Mapping%')"+
					   " order by description asc";
	
		//System.out.println(query);
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
						
				annualrates.add(new AnnualRate(results.getInt("id"), results.getString("description"),results.getInt("costperday"), 
						results.getInt("annualcalibration"),results.getInt("annualdeadtraveldaycost"),
						results.getDouble("threeyearfactor"),results.getDouble("oneyearfactor"),
						results.getString("country"),results.getString("currency")));
				System.out.println("description: "+results.getString("description"));
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
		return annualrates;
	}
}
