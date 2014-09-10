package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.MonitoringService;
import com.nextcontrols.bureaudomain.MonitoringServiceCosts;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class MonitoringServiceDAO implements IMonitoringServiceDAO,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn = null;
	private static IMonitoringServiceDAO instance;

	public static IMonitoringServiceDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new MonitoringServiceDAO();
		}
	}

	public static void setInstance(IMonitoringServiceDAO ins) {
		instance = ins;
	}

	private MonitoringServiceDAO() {

	}

	private void dbConnect() {
		try {
			dbConn = ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<MonitoringService> getMonitoringServicesList() {
		List<MonitoringService> remortMonitoringServiceList = new ArrayList<MonitoringService>();
		dbConnect();
		String query = "SELECT * FROM [CoreMonitoringService]";

		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				MonitoringService remortmonitoringservice = new MonitoringService(
						results.getInt("id"),
						results.getDouble("threeyeardisc"),
						results.getDouble("fiveyeardisc"),
						results.getDouble("factor2"),
						results.getString("country"),
						results.getString("currency"),
						results.getString("description"),
						results.getInt("stepvalue1"),
						results.getInt("stepvalue2"),
						results.getInt("stepvalue3"),
						results.getInt("stepvalue4"),
						results.getInt("stepvalue5"));
				remortMonitoringServiceList.add(remortmonitoringservice);
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
		return remortMonitoringServiceList;

	}

	@Override
	public void addMonitoringService(double pthreeyeardisc,
			double pfiveyeardisc, double pfactor2, String pcountry,
			String pcurrency, String pDescription, int pStepValue1,
			int pStepValue2, int pStepValue3, int pStepValue4, int pStepValue5) {
		PreparedStatement prpedStatement = null;
		String query = "INSERT INTO [CoreMonitoringService]([threeyeardisc],[fiveyeardisc],[factor2],[country],[currency],[description],[stepvalue1],[stepvalue2],[stepvalue3],[stepvalue4],[stepvalue5])"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?);";
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setDouble(1, pthreeyeardisc);
			prpedStatement.setDouble(2, pfiveyeardisc);
			prpedStatement.setDouble(3, pfactor2);
			prpedStatement.setString(4, pcountry);
			prpedStatement.setString(5, pcurrency);
			prpedStatement.setString(6, pDescription);
			prpedStatement.setInt(7, pStepValue1);
			prpedStatement.setInt(8, pStepValue2);
			prpedStatement.setInt(9, pStepValue3);
			prpedStatement.setInt(10, pStepValue4);
			prpedStatement.setInt(11, pStepValue5);
			prpedStatement.execute();

			query = "INSERT INTO [CoreMonitoringServiceCosts] ([numberofsensors],[peryearOneYear],[coremonitoringserviceid])";
					for(int i=1;i<245;i++){
						query+= " Select "+i+",0,@@IDENTITY"
								+ " Union ALL";
					}
					query+=" Select 246,0,@@IDENTITY";
//					+ " Select 8,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 16,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 24,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 32,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 40,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 48,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 56,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 64,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 72,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 80,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 88,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 96,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 104,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 112,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 120,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 128,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 136,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 144,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 152,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 160,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 168,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 176,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 184,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 192,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 200,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 208,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 216,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 224,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 232,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 240,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 248,0,@@IDENTITY"
//					+ " Union ALL"
//					+ " Select 256,0,@@IDENTITY";
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modifyMonitoringService(double pthreeyeardisc,
			double pfiveyeardisc, double pfactor2, String pcountry,
			String pcurrency, int pID, String pDescription, int pStepValue1,
			int pStepValue2, int pStepValue3, int pStepValue4, int pStepValue5) {
		PreparedStatement prpedStatement = null;

		String query = "UPDATE [CoreMonitoringService] SET [threeyeardisc] = ?"
				+ ",[fiveyeardisc] = ?"
				+ ",[factor2] = ?"
				+ ",[country] = ?"
				+ ",[currency] = ?"
				+ ",[description] = ?"
				+ ",[stepvalue1] = ?"
				+ ",[stepvalue2] = ?"
				+ ",[stepvalue3] = ?" + ",[stepvalue4] = ?"+",[stepvalue5] = ?" + "WHERE [id]= ?";

		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setDouble(1, pthreeyeardisc);
			prpedStatement.setDouble(2, pfiveyeardisc);
			prpedStatement.setDouble(3, pfactor2);
			prpedStatement.setString(4, pcountry);
			prpedStatement.setString(5, pcurrency);
			prpedStatement.setString(6, pDescription);
			prpedStatement.setInt(7, pStepValue1);
			prpedStatement.setInt(8, pStepValue2);
			prpedStatement.setInt(9, pStepValue3);
			prpedStatement.setInt(10, pStepValue4);
			prpedStatement.setInt(11, pStepValue5);
			prpedStatement.setInt(12, pID);

			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void deleteMonitoringService(int pID) {
		PreparedStatement prpedStatement = null;
		String query = "DELETE FROM [CoreMonitoringService]"
				+ "WHERE [id]= ?";
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setInt(1, pID);
			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<String> getCountryList() {
		List<String> countriesList = new ArrayList<String>();
		dbConnect();
		String query = "SELECT distinct(country) FROM [CoreMonitoringService] order by country asc";
		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				countriesList.add(results.getString("country"));
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
		return countriesList;
	}

	@Override
	public List<String> getCurrencyList() {
		List<String> currenciesList = new ArrayList<String>();
		dbConnect();
		String query = "SELECT distinct(currency) FROM [CoreMonitoringService] order by currency asc";
		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				currenciesList.add(results.getString("currency"));
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
		return currenciesList;
	}

	/*
	 * @Override public List<MonitoringServiceCosts>
	 * getMonitoringServiceCosts(int pID,int pStepValue1, int pStepValue2, int
	 * pStepValue3, int pStepValue4) { dbConnect(); String query =
	 * "SELECT * FROM [CoreMonitoringServiceCosts] where coremonitoringserviceid ="
	 * +pID+" order by numberofsensors asc"; List<MonitoringServiceCosts>
	 * remortMonitoringServiceCostsList=new ArrayList<MonitoringServiceCosts>();
	 * 
	 * Statement stmnt=null; ResultSet results = null; try{
	 * stmnt=dbConn.createStatement(); results=stmnt.executeQuery(query); while
	 * (results.next()){ MonitoringServiceCosts remortmonitoringservicecost=new
	 * MonitoringServiceCosts(results.getInt("id"),
	 * results.getInt("numberofsensors"), results.getInt("peryearOneYear"));
	 * remortMonitoringServiceCostsList.add(remortmonitoringservicecost); }
	 * }catch (SQLException e){ e.printStackTrace(); }finally{ try {
	 * results.close(); stmnt.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } dbConn=null; stmnt=null; results=null; } return
	 * remortMonitoringServiceCostsList;
	 * 
	 * 
	 * }
	 */
	@Override
	public List<MonitoringServiceCosts> getMonitoringServiceCosts(int pID,
			int pStepValue1, int pStepValue2, int pStepValue3, int pStepValue4,int pStepValue5) {
		dbConnect();
		String query = "SELECT * FROM [CoreMonitoringServiceCosts] where coremonitoringserviceid ="
				+ pID + " order by numberofsensors asc";
		List<MonitoringServiceCosts> remortMonitoringServiceCostsList = new ArrayList<MonitoringServiceCosts>();

		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			int tempCost = 0;
			MonitoringServiceCosts remortmonitoringservicecost;
			while (results.next()) {
//				tempCost = results.getInt("peryearOneYear");
				if (results.getInt("numberofsensors")== 1) {
					tempCost = results.getInt("peryearOneYear");
					remortmonitoringservicecost = new MonitoringServiceCosts(
							results.getInt("id"),
							results.getInt("numberofsensors"),tempCost);
				} 
				else if (results.getInt("numberofsensors")> 1 
						&& results.getInt("numberofsensors")< 8) {//1-7 step 1
					tempCost = tempCost + pStepValue1;
					remortmonitoringservicecost = new MonitoringServiceCosts(
							results.getInt("id"),
							results.getInt("numberofsensors"),tempCost);
				} else 
					if (results.getInt("numberofsensors") ==8)// 8 – 64 step 2
					{
						tempCost = results.getInt("peryearOneYear");
						remortmonitoringservicecost = new MonitoringServiceCosts(
								results.getInt("id"),
								results.getInt("numberofsensors"), tempCost);
					}else {
					if (results.getInt("numberofsensors") > 8
							&& results.getInt("numberofsensors") < 65)// 8 – 64 step 2
					{
						tempCost = tempCost + pStepValue2;
						remortmonitoringservicecost = new MonitoringServiceCosts(
								results.getInt("id"),
								results.getInt("numberofsensors"), tempCost);
					} else if (results.getInt("numberofsensors") >= 65
							&& results.getInt("numberofsensors") < 129)// 65 – 128 step 3
					{
						tempCost = tempCost + pStepValue3;
						remortmonitoringservicecost = new MonitoringServiceCosts(
								results.getInt("id"),
								results.getInt("numberofsensors"), tempCost);
					} else if (results.getInt("numberofsensors") >= 129
							&& results.getInt("numberofsensors") < 193)// 129 –192 step4
					{
						tempCost = tempCost + pStepValue4;
						remortmonitoringservicecost = new MonitoringServiceCosts(
								results.getInt("id"),
								results.getInt("numberofsensors"), tempCost);
					} else {//step 5
						tempCost = tempCost + pStepValue5;
						remortmonitoringservicecost = new MonitoringServiceCosts(
								results.getInt("id"),
								results.getInt("numberofsensors"), tempCost);
					}
				}
				remortMonitoringServiceCostsList
						.add(remortmonitoringservicecost);
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
		return remortMonitoringServiceCostsList;

	}

	@Override
	public void updateMonitoringServiceCosts(
			MonitoringService pmonitoringService) {
		PreparedStatement prpedStatement = null;
		dbConnect();

		try {
			String query = "update [CoreMonitoringServiceCosts]"
					+ " set [peryearOneYear] =" + " case";
			for (MonitoringServiceCosts cost : pmonitoringService
					.getMonitoringCostsList()) {
				query += " when id =" + cost.getId()
						+ " AND coremonitoringserviceid="
						+ pmonitoringService.getId() + " then "
						+ cost.getOneYearlyCosts();
			}
			query += " else peryearOneYear";
			query += " end";
//			 System.out.println(query);
			prpedStatement = dbConn.prepareStatement(query);

			prpedStatement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn = null;
			prpedStatement = null;

		}
	}

	@Override
	public List<Integer> getMonitoringServicesList(String pCountry,
			int pNumberOfSensors) {
		List<Integer> costsList = new ArrayList<Integer>();
		if (pNumberOfSensors == 0) {
			costsList.add(0);
			costsList.add(0);
			costsList.add(0);
			costsList.add(0);
			costsList.add(0);
			costsList.add(0);
			return costsList;
		}
		if (pNumberOfSensors >= 256) {
			pNumberOfSensors = 255;
		}
		dbConnect();
		String query = "SELECT top 1 cmsc.[peryearOneYear] as peryearOneYearMonRecording,"
				+ " (cmsc.[peryearOneYear] - (cmsc.[peryearOneYear]*cms.[threeyeardisc]/100)) as peryearThreeYearMonRecording,"
				+ " (cmsc.[peryearOneYear] - (cmsc.[peryearOneYear]*cms.[fiveyeardisc]/100)) as peryearFiveYearMonRecording,"
				+ " (cmsc.[peryearOneYear]*cms.[factor2]) as peryearRecordingOnly,"
				+ " (cmsc.[peryearOneYear]*cms.[factor2] - (cmsc.[peryearOneYear]*cms.[threeyeardisc]/100)) as peryearThreeYearRecordingOnly,"
				+ " (cmsc.[peryearOneYear]*cms.[factor2] - (cmsc.[peryearOneYear]*cms.[fiveyeardisc]/100)) as peryearFiveYearRecordingOnly"
				+ " FROM [CoreMonitoringService] as cms"
				+ " INNER JOIN [CoreMonitoringServiceCosts] as cmsc ON cms.id = cmsc.coremonitoringserviceid"
				+ " where cms.country = '"
				+ pCountry
				+ "'and cmsc.[numberofsensors] > "
				+ pNumberOfSensors
				+ " order by cmsc.[numberofsensors] asc ";
		Statement stmnt = null;
		ResultSet results = null;
		// System.out.println(query);
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {
				costsList.add((int) (results
						.getFloat("peryearOneYearMonRecording")));
				costsList.add((int) (results
						.getFloat("peryearThreeYearMonRecording")));
				costsList.add((int) (results
						.getFloat("peryearFiveYearMonRecording")));
				costsList.add((int) (results.getFloat("peryearRecordingOnly")));
				costsList.add((int) (results
						.getFloat("peryearThreeYearRecordingOnly")));
				costsList.add((int) (results
						.getFloat("peryearFiveYearRecordingOnly")));
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
		return costsList;
		//
	}
}
