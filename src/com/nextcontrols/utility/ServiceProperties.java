package com.nextcontrols.utility;

	import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
@ManagedBean
@ApplicationScoped
	public class ServiceProperties {
		
		public String CONNECTIONDB;
		public String HOST;
		public String FROM;
		public String TO;
		public String DATAFILEPATH;
		private Properties properties;
		public static ServiceProperties sProps;
		public  ServiceProperties()  {
			loadProperties();
		}

		public synchronized static ServiceProperties getInstance() {
			if (sProps == null) {
				sProps = new ServiceProperties();
			}
			return sProps;
		}

		
		public void loadProperties()  {
			try {
				loadProperties("/Service.properties");
			} catch (Exception e) {
				e.printStackTrace();
			}	
			}
		
		
		public void loadProperties(String pPath) throws Exception {
			InputStream stream = ServiceProperties.class
					.getResourceAsStream(pPath);
		    properties = new Properties();
			properties.load(stream);
			CONNECTIONDB = properties.getProperty("QuotationWeb.conString");
			HOST =  properties.getProperty("QuotationWeb.Email.host");
			FROM =  properties.getProperty("QuotationWeb.Email.from");
			TO =  properties.getProperty("QuotationWeb.Email.to");
			DATAFILEPATH =  properties.getProperty("QuotationWeb.Email.dataFilePath");
			stream.close();	
		}
		
		public void loadCallProperties(String path) throws Exception{
			InputStream stream = ServiceProperties.class
			.getResourceAsStream(path);
			properties = new Properties();
			properties.load(stream);
			stream.close();	
		}
		

		public Properties getProperties(){
			properties.put("conString",CONNECTIONDB);
			properties.put("mail.smtp.host", HOST);
			properties.put("from",FROM);
			properties.put("to",TO);
			properties.put("dataFilePath",DATAFILEPATH);
			return properties;
		}
		
		public Connection getDatabaseConnection() throws SQLException{
			try {
//				return DriverManager.getConnection("jdbc:jtds:sqlserver://Imran-PC\\SQLExpress:1433/QuotationDb;user=test1;password=test");
				//System.out.println(CONNECTIONDB);
				return DriverManager.getConnection(CONNECTIONDB);
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException() ;
			}
		}
	
		static {
			try {
				Class.forName("net.sourceforge.jtds.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
}

