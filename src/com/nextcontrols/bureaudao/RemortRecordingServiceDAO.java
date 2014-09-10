package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.RemortRecordingService;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class RemortRecordingServiceDAO implements IRemortRecordingServiceDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IRemortRecordingServiceDAO instance;
	public static IRemortRecordingServiceDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new RemortRecordingServiceDAO();
		}
	}
	public static void setInstance(IRemortRecordingServiceDAO ins) {
		instance=ins;
	}
	private RemortRecordingServiceDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<RemortRecordingService> getRemortRecordingsList(String pCountry) {
		List<RemortRecordingService> remortRecordingServiceList=new ArrayList<RemortRecordingService>();
		dbConnect();
		String query="SELECT * FROM [RemoteRecordingService"+pCountry+"]";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				RemortRecordingService remortrecordingservice=new RemortRecordingService(results.getInt("id"), results.getInt("numberofsensors"), results.getInt("peryearOneYear"),
						results.getInt("permonthOneYear"), results.getInt("peryearThreeYear"),results.getInt("permonthThreeYear"),
						results.getInt("peryearFiveYear"),results.getInt("permonthFiveYear"));
				remortRecordingServiceList.add(remortrecordingservice);
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
		return remortRecordingServiceList;
	}
	@Override
	public void addRemortRecording(int pnumberofsensors, int pperyearOneYear,
			int ppermonthOneYear, int pperyearThreeYear,
			int ppermonthThreeYear, int pperyearFiveYear, int ppermonthFiveYear,String pCountry) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [RemoteRecordingService"+pCountry+"]([numberofsensors],[peryearOneYear],[permonthOneYear],[peryearThreeYear],[permonthThreeYear],[peryearFiveYear],[permonthFiveYear])"+
           " VALUES(?,?,?,?,?,?,?);";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, pnumberofsensors);
			prpedStatement.setInt(2, pperyearOneYear);
			prpedStatement.setInt(3, ppermonthOneYear);
			prpedStatement.setInt(4, pperyearThreeYear);
			prpedStatement.setInt(5, ppermonthThreeYear);
			prpedStatement.setInt(6, pperyearFiveYear);
			prpedStatement.setInt(7, ppermonthFiveYear);
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}		
	}
	@Override
	public void modifyRemortRecording(int pnumberofsensors,
			int pperyearOneYear, int ppermonthOneYear, int pperyearThreeYear,
			int ppermonthThreeYear, int pperyearFiveYear, int ppermonthFiveYear, int pID,String pCountry) {
		PreparedStatement prpedStatement=null;		
		String query = "UPDATE [RemoteRecordingService"+pCountry+"] SET [numberofsensors] = ?"+
					   ",[peryearOneYear] = ?"+
					   ",[permonthOneYear] = ?"+
					   ",[peryearThreeYear] = ?"+
					   ",[permonthThreeYear] = ?"+
					   ",[peryearFiveYear] = ?"+
					   ",[permonthFiveYear] = ?"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, pnumberofsensors);
			prpedStatement.setInt(2, pperyearOneYear);
			prpedStatement.setInt(3, ppermonthOneYear);
			prpedStatement.setInt(4, pperyearThreeYear);
			prpedStatement.setInt(5, ppermonthThreeYear);
			prpedStatement.setInt(6, pperyearFiveYear);
			prpedStatement.setInt(7, ppermonthFiveYear);
			prpedStatement.setInt(8, pID);
			
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}		
	}
	@Override
	public void deleteRemortRecording(int pID,String pCountry) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [RemoteRecordingService"+pCountry+"]"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, pID);
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}
		
	}
}
