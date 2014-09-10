package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.EmailTemplate;
import com.nextcontrols.bureaudomain.LetterTemplate;
import com.nextcontrols.bureaudomain.Template;

public class EmailTemplateDAO implements ITemplateDAO, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static EmailTemplateDAO instance; 
	private Connection dbConn ;
	/*
	 * 
	 */
	public static void setInstance(EmailTemplateDAO inst){
		instance = inst;
	}
	public static EmailTemplateDAO getInstance(){
		if(instance!=null){
			return instance;
		}
		else {
			return new EmailTemplateDAO();
		}
	}
	/*
	 * Private constructor for this DAO
	 */
	private EmailTemplateDAO(){	
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
		int LAST_INSERTED_ID =0; 
		String query="INSERT INTO [email_Template_Format] (templateName, body, user_id) VALUES(?,?,?)";
		this.dbConnect();
		try(PreparedStatement stmnt = this.dbConn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)){
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
			this.dbConn = null; 
		}
		return LAST_INSERTED_ID;
	}

	@Override
	public List<Template> getUsersSavedTemplates(int user_id) {
		// TODO Auto-generated method stub
		List<Template> templates = null; 
		String query="SELECT template.templateId, template.templateName, template.body, usr.user_name "
				     + "FROM [email_Template_Format] AS template INNER JOIN [user] AS usr ON template.user_id = usr.user_id  "
				     + "WHERE usr.user_id = ?;";
		
		dbConnect();
		try(PreparedStatement stmnt = this.dbConn.prepareStatement(query);){
			stmnt.setInt(1, user_id);
			try( ResultSet rs = stmnt.executeQuery()) {
				if(rs!=null){
					templates = new ArrayList<Template>();
					while(rs.next()){
						templates.add(new EmailTemplate(rs.getInt("templateId"), rs.getString("templateName"), rs.getString("body"),rs.getString("user_name")));
					}
				}	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.dbConn = null; 
		}
		return templates;
	}

	@Override
	public List<Template> getAllSavedTemplates() {
		// TODO Auto-generated method stub
		List<Template> templates = null; 
		String query="SELECT * FROM [email_Template_Format] AS template "
				+ "INNER JOIN [user] AS usr ON template.user_id = usr.user_id ORDER BY template.templateId DESC";
		this.dbConnect();
		try(PreparedStatement stmnt = this.dbConn.prepareStatement(query);){
			try (ResultSet rs = stmnt.executeQuery()){
				if(rs!=null){
					templates = new ArrayList<Template>();
					while(rs.next()){	
						templates.add(new EmailTemplate(rs.getInt("templateId"), rs.getString("templateName"), rs.getString("body"),rs.getString("user_name")));
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			this.dbConn = null; 
		}
		return templates;
	}

	@Override
	public boolean deleteTemplate(Template template) {
		// TODO Auto-generated method stub
		String query="DELETE FROM [email_Template_Format] WHERE templateId = ?";
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
		String query="UPDATE [email_Template_Format] SET templateName= ? , body = ? WHERE templateId = ?";
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
		return success;	}

}
