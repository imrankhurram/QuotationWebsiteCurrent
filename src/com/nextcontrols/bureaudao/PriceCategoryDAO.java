package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.PriceCategory;


/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class PriceCategoryDAO implements IPriceCategoryDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IPriceCategoryDAO instance;
	public static IPriceCategoryDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new PriceCategoryDAO();
		}
	}
	public static void setInstance(IPriceCategoryDAO ins) {
		instance=ins;
	}
	private PriceCategoryDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<PriceCategory> getPriceCategoryList() {
		List<PriceCategory> priceCategoriesList=new ArrayList<PriceCategory>();
		dbConnect();
		String query="SELECT * FROM [Price_Categories]";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				PriceCategory country=new PriceCategory(results.getInt("id"), results.getString("name"), results.getString("description"));
				priceCategoriesList.add(country);
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
		return priceCategoriesList;
	}
	
	@Override
	public List<PriceCategory> getPriceCategoryListWithoutNuLL() {
		List<PriceCategory> priceCategoriesList=new ArrayList<PriceCategory>();
		dbConnect();
		String query="SELECT * FROM [Price_Categories] where description != 'NULL'";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				PriceCategory country=new PriceCategory(results.getInt("id"), results.getString("name"), results.getString("description"));
				priceCategoriesList.add(country);
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
		return priceCategoriesList;
	}
	@Override
	public void addPriceCategory(String pname, String pdescription) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [Price_Categories]([name],[description])"+
           " VALUES(?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pname);
			prpedStatement.setString(2, pdescription);
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
	public void modifyPriceCategory(int pID, String pname, String pdescription) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [Price_Categories] SET [name] = ?"+
					   ",[description] = ?"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, pname);
			prpedStatement.setString(2, pdescription);
			prpedStatement.setInt(3, pID);
			
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
	public void deletePriceCategory(int pID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [Price_Categories]"+
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
	public List<String> getPriceCategoryStringList() {
		List<String> priceCategoryList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(name) FROM [Price_Categories] order by name asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				priceCategoryList.add(results.getString("name"));
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
		return priceCategoryList;	
	}
}
