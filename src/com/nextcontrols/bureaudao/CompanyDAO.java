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

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class CompanyDAO implements ICompanyDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static ICompanyDAO instance;
	public static ICompanyDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new CompanyDAO();
		}
	}
	public static void setInstance(ICompanyDAO ins) {
		instance=ins;
	}
	private CompanyDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<Company> getCompaniesList() {
		List<Company> companiesList=new ArrayList<Company>();
		dbConnect();
		String query="SELECT comp.[id],comp.[company_name],comp.[company_ref],coun.[freight],coun.[duty],comp.[markup],comp.[hwdiscount],comp.[monitoringdiscount] "+
					 ",comp.[country],comp.[multiyear_options],coun.[full_name],coun.[currency],coun.[conversionratefrompounds] "+
					 " FROM [Company] as comp"+
					 " Inner join [Country] as coun on comp.country = coun.country order by comp.[company_name] asc;";
		Statement stmnt=null;
		ResultSet results = null;
		
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				Country country = new Country(results.getString("country"),results.getString("full_name"),results.getString("currency"),
						results.getDouble("conversionratefrompounds"), results.getDouble("freight"), results.getDouble("duty"));
				Company company=new Company(results.getInt("id"),results.getString("company_name"), 
						results.getDouble("markup"),results.getDouble("hwdiscount"),results.getDouble("monitoringdiscount"),country,results.getString("company_ref"),
						results.getBoolean("multiyear_options"));
				companiesList.add(company);
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
		return companiesList;
	}
	
	public List<Company> getCompaniesList(String pCountry){
		List<Company> companiesList=new ArrayList<Company>();
		dbConnect();
		String query="SELECT comp.[id],comp.[company_name],comp.[company_ref],comp.[markup],comp.[hwdiscount],comp.[monitoringdiscount] "+
					 ",comp.[country],comp.[multiyear_options],coun.[freight],coun.[duty],coun.[full_name],coun.[currency],coun.[conversionratefrompounds] "+
					 " FROM [Company] as comp"+
					 " Inner join [Country] as coun on comp.country = coun.country where comp.country=? order by comp.[company_name] asc;";
		PreparedStatement prpedStatement=null;
		ResultSet results = null;
		//System.out.println(query + pCountry);
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pCountry);

			results=prpedStatement.executeQuery();
			while (results.next()){
				Country country = new Country(results.getString("country"),results.getString("full_name"),results.getString("currency"),
						results.getDouble("conversionratefrompounds"), results.getDouble("freight"), results.getDouble("duty"));
				Company company=new Company(results.getInt("id"),results.getString("company_name"), 
						results.getDouble("markup"),results.getDouble("hwdiscount"),results.getDouble("monitoringdiscount"),country,results.getString("company_ref"),
						results.getBoolean("multiyear_options"));
				companiesList.add(company);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			prpedStatement=null;
			results=null;
		}
		return companiesList;
	}
	@Override
	public void addCompany(String pcompanyName,
			double pmarkup, double pdiscountpercent,double pmonitoringdiscount, String pcountry, String pCompanyRef,boolean multiyear_options) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [Company]([company_name],[markup],[hwdiscount],[monitoringdiscount],[country],[company_ref],[multiyear_options])"+
           " VALUES(?,?,?,?,?,?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pcompanyName);
			prpedStatement.setDouble(2, pmarkup);
			prpedStatement.setDouble(3, pdiscountpercent);
			prpedStatement.setDouble(4, pmonitoringdiscount);
			prpedStatement.setString(5, pcountry);
			prpedStatement.setString(6, pCompanyRef);
			prpedStatement.setBoolean(7,multiyear_options);
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
	public void modifyCompany(int pID, String pcompanyName, double pmarkup, double pdiscountpercent,double pmonitoringdiscount,
			String pcountry, String pCompanyRef,boolean multiyear_options) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [Company] SET [company_name] = ?"+
					   ",[markup] = ?"+
					   ",[hwdiscount] = ?"+
					   ",[monitoringdiscount] = ?"+
					   ",[country] = ?"+
					   ",[company_ref] = ?"+
					   ",[multiyear_options] = ?"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pcompanyName);
			prpedStatement.setDouble(2, pmarkup);
			prpedStatement.setDouble(3, pdiscountpercent);
			prpedStatement.setDouble(4, pmonitoringdiscount);
			prpedStatement.setString(5, pcountry);
			prpedStatement.setString(6, pCompanyRef);
			prpedStatement.setBoolean(7, multiyear_options);
			prpedStatement.setInt(8, pID);
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
	public void deleteCompany(int pID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [Company]"+
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
}
