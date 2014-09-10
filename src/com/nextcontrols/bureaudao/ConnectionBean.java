package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;

import com.nextcontrols.utility.ServiceProperties;

@SessionScoped
@ManagedBean(name="connection")
public class ConnectionBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn;
	private static ConnectionBean connBean;
	
	public ConnectionBean(){}
	
	public synchronized static ConnectionBean getInstance(){
		if (connBean==null){
			connBean=new ConnectionBean();
		}
		return connBean;
	}
	
	public Connection getDBConnection() throws SQLException{
		if (dbConn==null){
			dbConn=ServiceProperties.getInstance().getDatabaseConnection();
		}
		return dbConn;
	}
	
	
	public void closeConnections() throws SQLException
	{
		dbConn=null;
	} 
	
}
