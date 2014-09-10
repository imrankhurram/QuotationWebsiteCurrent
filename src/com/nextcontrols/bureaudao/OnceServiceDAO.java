package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.OnceService;
import com.nextcontrols.bureaudomain.Quotation;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class OnceServiceDAO implements IOnceServiceDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IOnceServiceDAO instance;
	public static IOnceServiceDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new OnceServiceDAO();
		}
	}
	public static void setInstance(IOnceServiceDAO ins) {
		instance=ins;
	}
	private OnceServiceDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	@Override
	public List<OnceService> getOnceServicesList() {
		List<OnceService> onceservicesList=new ArrayList<OnceService>();
		dbConnect();
		String query="SELECT * FROM [OnceServiceOption] order by country asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				OnceService onceservice=new OnceService(results.getInt("service_id"), results.getString("service_description"), 
						results.getDouble("minimum_charge"),results.getDouble("unit_cost")
						, results.getString("country"), results.getString("currency"), results.getString("service_type"));
				onceservicesList.add(onceservice);
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
		return onceservicesList;
	}
	
	@Override
	public void addOnceService(String pServiceDescription,
			double pminimumCharge, double punitCost, String pCountry, String pcurrency, String pServiceType) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [OnceServiceOption]([service_description],[unit_cost],[minimum_charge],[country],[currency],[service_type])"+
           " VALUES(?,?,?,?,?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pServiceDescription);
			prpedStatement.setDouble(2, punitCost);
			prpedStatement.setDouble(3, pminimumCharge);
			prpedStatement.setString(4, pCountry);
			prpedStatement.setString(5, pcurrency);
			prpedStatement.setString(6, pServiceType);
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
	public void modifyOnceService(int pID, String pServiceDescription,
			double pminimumCharge, double punitCost, String pCountry, String pcurrency, String pServiceType) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [OnceServiceOption] SET [service_description] = ?"+
					   ",[unit_cost] = ?"+
					   ",[minimum_charge] = ?"+
					   ",[country] = ?"+
					   ",[currency] = ?"+
					   ",[service_type] = ?"+
					   "WHERE [service_id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pServiceDescription);
			prpedStatement.setDouble(2, punitCost);
			prpedStatement.setDouble(3, pminimumCharge);
			prpedStatement.setString(4, pCountry);
			prpedStatement.setString(5, pcurrency);
			prpedStatement.setString(6, pServiceType);
			prpedStatement.setInt(7, pID);
			
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
	public void deleteOnceService(int pID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [OnceServiceOption]"+
					   "WHERE [service_id]= ?";
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
		String query="SELECT country FROM [OnceServiceOption] order by country asc";
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
		String query="SELECT currency FROM [OnceServiceOption] order by currency asc";
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
	
	public List<OnceService> getOnceServicesList(String pCountry, Quotation pQuotation ){
		/*DQ, IQ, OQ Protocol Documents" itemValue="DQ, IQ, OQ Protocol Documents" />    
		<f:selectItem itemLabel="Factory sensor calibration" itemValue="Factory sensor calibration" />  
		<f:selectItem itemLabel="Website server database setup" itemValue="Website server database setup" /> 
		<f:selectItem itemLabel="Web hosted GUI" 
		*/
		List<OnceService> onceservicesList=new ArrayList<OnceService>();
		dbConnect();
		String query="SELECT * FROM [OnceServiceOption] where country like '%"+pCountry+"%' order by service_description asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				OnceService onceservice=new OnceService(results.getInt("service_id"), results.getString("service_description"), 
						results.getDouble("minimum_charge"),results.getDouble("unit_cost")
						, results.getString("country"), results.getString("currency"), results.getString("service_type"));
				if(onceservice.getServiceType().compareToIgnoreCase("DQ, IQ, OQ Protocol Documents")==0 && pQuotation.getDqiqoqprotocoldocs()){
					onceservice.setShowCost(true);
				}
				if(onceservice.getServiceType().compareToIgnoreCase("Factory sensor calibration")==0 && pQuotation.getFactorysensorcalibration()){
					onceservice.setShowCost(true);
				}
				if(onceservice.getServiceType().compareToIgnoreCase("Website server database setup")==0 && pQuotation.getWebsitedbsetup()){
					onceservice.setShowCost(true);
				}
				if(onceservice.getServiceType().compareToIgnoreCase("Web hosted GUI")==0 && pQuotation.getWebhostedgui()){
					onceservice.setShowCost(true);
				}
				onceservicesList.add(onceservice);
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
		return onceservicesList;
	}
}
