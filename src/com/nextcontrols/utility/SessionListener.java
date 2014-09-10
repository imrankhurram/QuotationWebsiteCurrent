package com.nextcontrols.utility;

import java.sql.SQLException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.nextcontrols.bureaudao.ConnectionBean;

public class SessionListener implements HttpSessionListener{

	//public void SessionListener(){}
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		System.out.println("SESSION IS CREATED");
		//ConnectionBean.getInstance().openConnections();
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		try {
			ConnectionBean.getInstance().closeConnections();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try{
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		session.removeAttribute("user");
		session.invalidate();
		}catch(Exception e){
			
		}finally{
			System.gc();
			System.out.println("SESSION IS DESTROYED");	
		}
	}
}
