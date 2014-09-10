package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.nextcontrols.bureaudomain.LetterTemplate;
import com.nextcontrols.bureaudomain.Template;

public class LetterTemplateDAO implements ITemplateDAO, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static LetterTemplateDAO instance; 
	private Connection dbConn ;
	/*
	 * 
	 */
	public static void setInstance(LetterTemplateDAO inst){
		instance = inst;
	}
	public static LetterTemplateDAO getInstance(){
		if(instance!=null){
			return instance;
		}
		else {
			return new LetterTemplateDAO();
		}
	}
	/*
	 * Private constructor for this DAO
	 */
	private LetterTemplateDAO(){	
	}

	private void dbConnect(){
		try {
			dbConn = ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public int saveTemplate(Template template) {
		// TODO Auto-generated method stub
		dbConnect();
		PreparedStatement stmnt = null; 
		int LAST_INSERTED_ID=0;
		String query= "INSERT INTO [LetterTemplate] (templateName, body, user_id) VALUES(?,?,?)";
		try {
			stmnt = dbConn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
			stmnt.setString(1, template.getTemplateName());
			stmnt.setString(2, template.getBody());
			stmnt.setInt(3, template.getUser_id());
			stmnt.executeUpdate();
			try(ResultSet rs = stmnt.getGeneratedKeys();) {
				if(rs!=null && rs.next()){
					LAST_INSERTED_ID = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(stmnt!=null){
				try {
					stmnt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			stmnt=null;
			dbConn = null; 
		}
		return LAST_INSERTED_ID;
	}
	@Override
	public List<Template> getUsersSavedTemplates(int user_id) {
		// TODO Auto-generated method stub
		List<Template> templates = null; 
		String query="SELECT template.templateId, template.templateName, template.body, usr.user_name "
				     + "FROM [LetterTemplate] AS template INNER JOIN [user] AS usr ON template.user_id = usr.user_id  "
				     + "WHERE usr.user_id = ?;";
		dbConnect();
		PreparedStatement stmnt = null; 
		ResultSet rs = null; 
		try {
			stmnt = dbConn.prepareStatement(query);
			stmnt.setInt(1, user_id);
			rs = stmnt.executeQuery();
			if(rs!=null){
				templates = new ArrayList<Template>();
				while(rs.next()){
					templates.add(new LetterTemplate(rs.getInt("templateId"), rs.getString("templateName"), rs.getString("body"),rs.getString("user_name")));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(stmnt!=null){
					try {
						stmnt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			rs=null; stmnt=null; dbConn=null;
		}

		return templates;
	}
	@Override
	public List<Template> getAllSavedTemplates() {
		// TODO Auto-generated method stub
		List<Template> templates = null; 
		String query=  "SELECT template.templateId, template.templateName, template.body, usr.user_name "
				     + "FROM [LetterTemplate] AS template INNER JOIN [user] AS usr ON template.user_id = usr.user_id";
		dbConnect();
		PreparedStatement stmnt = null; 
		ResultSet rs = null; 
		try {
			stmnt = dbConn.prepareStatement(query);
			rs = stmnt.executeQuery();
			if(rs!=null){
				templates = new ArrayList<Template>();
				while(rs.next()){
					templates.add(new LetterTemplate(rs.getInt("templateId"), rs.getString("templateName"), rs.getString("body"),rs.getString("user_name")));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(stmnt!=null){
					try {
						stmnt.close();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			rs=null; stmnt=null; dbConn=null;
		}
		return templates;	
	}
	@Override
	public boolean deleteTemplate(Template template) {
		// TODO Auto-generated method stub
		String query="DELETE FROM [LetterTemplate] WHERE templateId = ?";
		boolean success = false; 
		this.dbConnect(); 
		try(PreparedStatement stmnt = dbConn.prepareStatement(query) ){
			stmnt.setInt(1, template.getTemplateId());
			success = stmnt.executeUpdate()>0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.dbConn = null; 
		}
		return success;
	}
	@Override
	public boolean modifyTemplate(Template template) {
		// TODO Auto-generated method stub
		boolean success=false;
		String query="UPDATE [LetterTemplate] SET templateName= ? , body = ? WHERE templateId = ?";
		this.dbConnect();
		try(PreparedStatement stmnt = this.dbConn.prepareStatement(query)){
			stmnt.setString(1, template.getTemplateName());
			stmnt.setString(2, template.getBody());
			stmnt.setInt(3, template.getTemplateId());
			success = stmnt.executeUpdate()>0;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.dbConn = null; 
		}
		return success;
	}
}
