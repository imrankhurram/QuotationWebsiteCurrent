package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.QuantityCategory;


/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class QuantityCategoryDAO implements IQuantityCategoryDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IQuantityCategoryDAO instance;
	public static IQuantityCategoryDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new QuantityCategoryDAO();
		}
	}
	public static void setInstance(IQuantityCategoryDAO ins) {
		instance=ins;
	}
	private QuantityCategoryDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<QuantityCategory> getQuantityCategoryList() {
		List<QuantityCategory> quantityCategoriesList=new ArrayList<QuantityCategory>();
		dbConnect();
		String query="SELECT * FROM [Quantity_Categories]";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				QuantityCategory country=new QuantityCategory(results.getInt("id"), results.getString("name"), results.getString("description"));
				quantityCategoriesList.add(country);
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
		return quantityCategoriesList;
	}
	
	@Override
	public List<QuantityCategory> getQuantityCategoryListWithoutNuLL(){
		List<QuantityCategory> quantityCategoriesList=new ArrayList<QuantityCategory>();
		dbConnect();
		String query="SELECT * FROM [Quantity_Categories] where description != 'NULL'";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				QuantityCategory country=new QuantityCategory(results.getInt("id"), results.getString("name"), results.getString("description"));
				quantityCategoriesList.add(country);
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
		return quantityCategoriesList;
	}
	@Override
	public void addQuantityCategory(String pname, String pdescription) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [Quantity_Categories]([name],[description])"+
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
	public void modifyQuantityCategory(int pID, String pname, String pdescription) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [Quantity_Categories] SET [name] = ?"+
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
	public void deleteQuantityCategory(int pID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [Quantity_Categories]"+
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
	public List<String> getQuantityCategoryStringList() {
		List<String> quantityCategoryList=new ArrayList<String>();
		dbConnect();
//		String query="SELECT distinct(name) FROM [Quantity_Categories] order by name asc";
		String query="SELECT[name] FROM [Quantity_Categories] order by name *1 asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				quantityCategoryList.add(results.getString("name"));
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
//		System.out.println(quantityCategoryList + " in db");
		return quantityCategoryList;	
	}
}
