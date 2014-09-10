package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.RemortMonPRecordingService;


/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class RemortMonPRecordingServiceDAO implements IRemortMonPRecordingServiceDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IRemortMonPRecordingServiceDAO instance;
	public static IRemortMonPRecordingServiceDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new RemortMonPRecordingServiceDAO();
		}
	}
	public static void setInstance(IRemortMonPRecordingServiceDAO ins) {
		instance=ins;
	}
	private RemortMonPRecordingServiceDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	@Override
	public List<RemortMonPRecordingService> getRemortMonPRecordingsList(
			String pCountry) {
		List<RemortMonPRecordingService> remortRecordingServiceList=new ArrayList<RemortMonPRecordingService>();
		dbConnect();
		//String query="SELECT * FROM [RemoteMonPRecordingService"+pCountry+"]";
		String query="SELECT * FROM [CoreRemoteMonitoringService] order by numberofsensors asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				RemortMonPRecordingService remortrecordingservice=new RemortMonPRecordingService(results.getInt("id"), results.getInt("numberofsensors"), results.getInt("peryearOneYear"),
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
	public void addRemortMonPRecording(int pnumberofsensors,
			int pperyearOneYear, int ppermonthOneYear, int pperyearThreeYear,
			int ppermonthThreeYear, int pperyearFiveYear,
			int ppermonthFiveYear) {
		PreparedStatement prpedStatement=null;		
		//String query = "INSERT INTO [RemoteMonPRecordingService"+pCountry+"]([numberofsensors],[peryearOneYear],[permonthOneYear],[peryearThreeYear],[permonthThreeYear],[peryearFiveYear],[permonthFiveYear])"+
        //   " VALUES(?,?,?,?,?,?,?);";
		String query = "INSERT INTO [CoreRemoteMonitoringService]([numberofsensors],[peryearOneYear],[permonthOneYear],[peryearThreeYear],[permonthThreeYear],[peryearFiveYear],[permonthFiveYear])"+
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
	public void modifyRemortMonPRecording(int pnumberofsensors,
			int pperyearOneYear, int ppermonthOneYear, int pperyearThreeYear,
			int ppermonthThreeYear, int pperyearFiveYear,
			int ppermonthFiveYear, int pID) {
		PreparedStatement prpedStatement=null;		
		/*String query = "UPDATE [RemoteMonPRecordingService"+pCountry+"] SET [numberofsensors] = ?"+
					   ",[peryearOneYear] = ?"+
					   ",[permonthOneYear] = ?"+
					   ",[peryearThreeYear] = ?"+
					   ",[permonthThreeYear] = ?"+
					   ",[peryearFiveYear] = ?"+
					   ",[permonthFiveYear] = ?"+
					   "WHERE [id]= ?";*/
		String query = "UPDATE [CoreRemoteMonitoringService] SET [numberofsensors] = ?"+
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
	public void deleteRemortMonPRecording(int pID) {
		PreparedStatement prpedStatement=null;		
		/*String query = "DELETE FROM [RemoteMonPRecordingService"+pCountry+"]"+
					   "WHERE [id]= ?";*/
		String query = "DELETE FROM [CoreRemoteMonitoringService]"+
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
