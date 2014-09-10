package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.nextcontrols.bureaudomain.StandardParts;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class PartsDAO implements IPartsDAO, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn=null;
	private static IPartsDAO instance;
	public static IPartsDAO getInstance() {
		if(instance!=null) {
			return instance;
		}
		else {
			return new PartsDAO();
		}
	}
	public static void setInstance(IPartsDAO ins) {
		instance=ins;
	}
	private PartsDAO() {
		
	}

	private void dbConnect(){
		try {
			dbConn=ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public List<StandardParts> getStandardPartsAndAssembliesList(String pcountry){
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		String query="SELECT (csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor]"+
						",csp.[installation_time],csp.[calibration_time],csp.[list_priority]"+
						",csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" where csp.[country] like '%"+pcountry+"%'"+
						" order by csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
						//" order by acsp.assembly_id desc,csp.[part_number] asc";
		Statement stmnt=null;
		ResultSet results = null;
		//System.out.println(query);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),results.getString("part_type"), results.getDouble("unit_cost")
							, results.getDouble("markup"), results.getInt("quantity"), results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat")
							,results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
					
						if(results.getInt("part_id")!=0){
							part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
								"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat")));
							
						}
					partsList.add(part);

				}else{
					partsList.remove(part);
					part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		return partsList;
	}
	
	@Override
	public List<StandardParts> getStandardPartsAndAssembliesList() {
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		String query="SELECT (csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor],csp.[installation_time],csp.[calibration_time],csp.[list_priority]"+
						" ,csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost ,csp2.[part_number] as subpart_number,csp2.[part_description] as subpart_description"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" order by csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
						//" order by acsp.assembly_id desc,csp.[part_number] asc";
		Statement stmnt=null;
		ResultSet results = null;
		
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					//System.out.println("assembly ID "+results.getInt("assembly_id")+"Part ID"+ results.getInt("part_id")+" : "+results.getDouble("part_cost"));
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),results.getString("part_type"), results.getDouble("unit_cost")
							, results.getDouble("markup"), results.getInt("quantity"), results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat")
							,results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
						if(results.getInt("part_id")!=0){
							part.addSubPart(new StandardParts(results.getInt("part_id"), results.getString("subpart_number"), results.getString("subpart_description"),
								"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat")));
						}
					partsList.add(part);

				}else{
					//System.out.println("Part ID"+ results.getInt("part_id")+" : "+results.getDouble("part_cost"));

					partsList.remove(part);
					part.addSubPart(new StandardParts(results.getInt("part_id"), results.getString("subpart_number"), results.getString("subpart_description"),
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),
							results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		return partsList;
	}	
	
	public List<StandardParts> getStandardAssembliesList(){
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		String query="SELECT (csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag]"+
						" ,csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor]"+
						",csp.[installation_time],csp.[calibration_time],csp.[list_priority]"+
						" FROM [CoreStandardParts] as csp"+
						//" where csp.[part_type] = 'Assembly'"+
						//" order by csp.[part_number] asc";
						" order by csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
		Statement stmnt=null;
		ResultSet results = null;
		
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			//int assemblyID=0;
			while (results.next()){
				//if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),
							results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), 
							0, results.getString("country"),new ArrayList<StandardParts>(),"", results.getString("pricecat"), 
							results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
					//	if(results.getInt("part_id")!=0){
					//		part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
					//			"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type")));
					//	}
					partsList.add(part);

				//}else{
					//partsList.remove(part);
				//	part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
				//			"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type")));
				//	partsList.add(part);
				//}
					
				//assemblyID = results.getInt("assembly_id");
						
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
		return partsList;
	}
	
	public List<StandardParts> getStandardPartsAndAssembliesList(double frieght, double duty, double conversionratefrompounds,String pcountry ){
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		
		String query="SELECT (csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor],csp.[installation_time],csp.[calibration_time],csp.[list_priority]"+
						" ,csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" where csp.[country] like '%"+pcountry+"%' AND (csp.[list_priority] IS NOT NULL AND csp.[list_priority]>0)"+
						" order by csp.[list_priority] asc,csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
//		System.out.println(query);
		Statement stmnt=null; 
		ResultSet results = null;
		List<Double> factors= new ArrayList <Double>();
		factors.add(conversionratefrompounds);
		factors.add(frieght);
		factors.add(duty);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),
							results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getInt("quantity"), 
							results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat"),
							results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
						if(results.getInt("part_id")!=0){
							part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
								"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
						}
					part.setFactors(factors);
					partsList.add(part);

				}else{
					partsList.remove(part);
					part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		dbConnect();
		
		 query="SELECT (csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor],csp.[installation_time],csp.[calibration_time],csp.[list_priority]"+
						" ,csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" where csp.[country] like '%"+pcountry+"%' AND (csp.[list_priority] IS NULL OR csp.[list_priority]<1)"+
						" order by csp.[list_priority] asc,csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
//		System.out.println(query);
		

		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),
							results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getInt("quantity"), 
							results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat"),
							results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
						if(results.getInt("part_id")!=0){
							part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
								"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
						}
					part.setFactors(factors);
					partsList.add(part);

				}else{
					partsList.remove(part);
					part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		return partsList;
	}
	
	public List<StandardParts> getStandardPartsAndAssembliesListWithQuoteQuantity(double frieght, double duty, double conversionratefrompounds,String pcountry, int pquotationID)
	{
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		///
		String query="SELECT ISNULL(csp.quantity,0)  as quotequantity,(csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[unit_cost]"+
				" FROM [Quotation_NonStandardPart] as csp"+
				" where csp.quotation_id = "+pquotationID+
				" order by csp.[part_number] asc";
		Statement stmnt=null; 
		ResultSet results = null;
		StandardParts stTemp = null;//new StandardParts(0, "Part 00", "Non Standard ", "NonStandard Part", 0.0, 3.3, 0, pcountry, new ArrayList<StandardParts>(), "NonStandard Part","D","14");
		List<Double> factors= new ArrayList <Double>();
		factors.add(1.0);
		factors.add(0.0);
		factors.add(0.0);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				stTemp=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"), "NonStandard Part", results.getDouble("unit_cost"), 1.0, 0, pcountry, new ArrayList<StandardParts>(), "NonStandard Part","D","14");
				stTemp.setFactors(factors);
				stTemp.setQuoteQuantity(results.getInt("quotequantity"));
				stTemp.setListPrice(results.getDouble("unit_cost"));
				partsList.add(stTemp);

			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		///
		query="SELECT ISNULL(qsp.quantity,0)  as quotequantity,(csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor]"+
						",csp.[installation_time],csp.[calibration_time] , csp.[list_priority] ,csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" LEFT JOIN [QUOTATION_STANDARDPART] AS qsp on csp.id= qsp.id and qsp.quotation_id = "+pquotationID+
						" where csp.[country] like '%"+pcountry+"%' AND (csp.[list_priority] IS NOT NULL AND csp.[list_priority]>0)"+
						" order by csp.[list_priority] asc,csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
//		System.out.println(query);
		stmnt=null; 
		results = null;
		factors= new ArrayList <Double>();
		factors.add(conversionratefrompounds);
		factors.add(frieght);
		factors.add(duty);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),
							results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getInt("quantity"), results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity")
							,results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
						if(results.getInt("part_id")!=0){
							part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
								"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
						}
					
					part.setFactors(factors);
					partsList.add(part);

				}else{
					partsList.remove(part);
					part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		/////////////////////////
		///
		query="SELECT ISNULL(qsp.quantity,0)  as quotequantity,(csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor]"+
						",csp.[installation_time],csp.[calibration_time] , csp.[list_priority] ,csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" LEFT JOIN [QUOTATION_STANDARDPART] AS qsp on csp.id= qsp.id and qsp.quotation_id = "+pquotationID+
						" where csp.[country] like '%"+pcountry+"%' AND (csp.[list_priority] IS NULL OR csp.[list_priority]<1)"+
						" order by csp.[list_priority] asc,csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
//		System.out.println(query);
		dbConnect();
		factors= new ArrayList <Double>();
		factors.add(conversionratefrompounds);
		factors.add(frieght);
		factors.add(duty);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),
							results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getInt("quantity"), results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity")
							,results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
						if(results.getInt("part_id")!=0){
							part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
								"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
						}
					
					part.setFactors(factors);
					partsList.add(part);

				}else{
					partsList.remove(part);
					part.addSubPart(new StandardParts(results.getInt("part_id"), "", "",
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		//////////////////////////////
		return partsList;	
	}
	public List<StandardParts> getStandardPartsAndAssembliesListWithNonzeroQuantity(double frieght, double duty, double conversionratefrompounds,String pcountry, int pquotationID)
	{
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		///
		String query="SELECT ISNULL(csp.quantity,0)  as quotequantity,(csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[unit_cost]"+
				" FROM [Quotation_NonStandardPart] as csp"+
				" where csp.quotation_id = "+pquotationID+
				" order by csp.[part_number] asc";
		Statement stmnt=null; 
		ResultSet results = null;
		Statement subPartsStmnt=null; 
		ResultSet subPartsResults = null;
		StandardParts stTemp = null;//new StandardParts(0, "Part 00", "Non Standard ", "NonStandard Part", 0.0, 3.3, 0, pcountry, new ArrayList<StandardParts>(), "NonStandard Part","D","14");
		List<Double> factors= new ArrayList <Double>();
		factors.add(1.0);
		factors.add(0.0);
		factors.add(0.0);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				stTemp=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"), "NonStandard Part", results.getDouble("unit_cost"), 1.0, 0, pcountry, new ArrayList<StandardParts>(), "NonStandard Part","D","14");
				stTemp.setFactors(factors);
				stTemp.setQuoteQuantity(results.getInt("quotequantity"));
				stTemp.setListPrice(results.getDouble("unit_cost"));
				if(stTemp.getQuoteQuantity()>0)
				partsList.add(stTemp);

			}
		}catch (SQLException e){
			e.printStackTrace();
		}
		///
		query="SELECT ISNULL(qsp.quantity,0)  as quotequantity,(csp.[id]) as assembly_id,csp.[part_number],csp.[part_description],csp.[part_tag],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor]"+
						" ,csp.[installation_time],csp.[calibration_time],csp.[list_priority],csp.[part_type],csp.[unit_cost],csp.[markup],csp.[country],acsp.[part_id],acsp.[quantity],csp2.[unit_cost] as part_cost"+
						" FROM [CoreStandardParts] as csp"+
						" LEFT JOIN [AssemblyCoreStandardParts] as acsp on csp.id = acsp.assembly_id"+
						" LEFT JOIN [CoreStandardParts] as csp2 on acsp.part_id = csp2.id"+
						" LEFT JOIN [QUOTATION_STANDARDPART] AS qsp on csp.id= qsp.id and qsp.quotation_id = "+pquotationID+
						//" where csp.[country] like '%"+pcountry+"%'"+
						" order by csp.[part_type] asc,csp.[pricecat] asc,csp.[part_number] asc";
		//System.out.println(query);
		stmnt=null; 
		results = null;
		factors= new ArrayList <Double>();
		factors.add(conversionratefrompounds);
		factors.add(frieght);
		factors.add(duty);
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			StandardParts part = null;
			int assemblyID=0;
			while (results.next()){
				if(assemblyID != results.getInt("assembly_id")){
					part=new StandardParts(results.getInt("assembly_id"), results.getString("part_number"), results.getString("part_description"),
							results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getInt("quantity"), results.getString("country"),new ArrayList<StandardParts>(),results.getString("part_tag"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
						if(results.getInt("part_id")!=0){
							String subPartQuery="SELECT csp.[part_number],csp.[part_description],csp.[installation_time],csp.[calibration_time],csp.[list_priority] FROM [CoreStandardParts] as csp where csp.[id]="+results.getInt("part_id");
							subPartsStmnt=dbConn.createStatement();
							subPartsResults=subPartsStmnt.executeQuery(subPartQuery);
							while (subPartsResults.next()){
								
								part.addSubPart(new StandardParts(results.getInt("part_id"), subPartsResults.getString("part_number"), subPartsResults.getString("part_description"),
									"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
							}
							subPartsStmnt=null;
							subPartsResults=null;
						}
					
					part.setFactors(factors);
					if(part.getQuoteQuantity()>0)
					partsList.add(part);
				

				}else{
					partsList.remove(part);
					String subPartQuery="SELECT csp.[part_number],csp.[part_description],csp.[installation_time],csp.[calibration_time],csp.[list_priority] FROM [CoreStandardParts] as csp where csp.[id]="+results.getInt("part_id");
					subPartsStmnt=dbConn.createStatement();
					subPartsResults=subPartsStmnt.executeQuery(subPartQuery);
					while (subPartsResults.next()){
					part.addSubPart(new StandardParts(results.getInt("part_id"),  subPartsResults.getString("part_number"), subPartsResults.getString("part_description"),
							"", results.getDouble("part_cost"), results.getDouble("markup"), results.getInt("quantity"), "",new ArrayList<StandardParts>(),results.getString("part_type"),results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("quotequantity"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority")));
					}
					if(part.getQuoteQuantity()>0)
					partsList.add(part);
				}
					
				assemblyID = results.getInt("assembly_id");
						
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
		return partsList;	
	}
	public List<String> getDaysForQuotation( int pquotationID)
	{
		List<String> daysList=new ArrayList<String>();
		dbConnect();
		String query="SELECT TOP 1 [site_name],[site_installation_days],[installation_travel_days],[site_calibration_days]"+
					",[calibration_travel_days],[annual_nist_calibration_days],[annual_travel_days]"+
					" FROM [Quotation_standardpart] where quotation_id =  "+pquotationID;
		Statement stmnt=null;
		ResultSet results = null;
		
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				daysList.add(results.getString("site_name"));
				daysList.add(results.getString("site_installation_days"));
				daysList.add(results.getString("installation_travel_days"));
				daysList.add(results.getString("site_calibration_days"));
				daysList.add(results.getString("calibration_travel_days"));
				daysList.add(results.getString("annual_nist_calibration_days"));
				daysList.add(results.getString("annual_travel_days"));
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
		return daysList;
		
	}
	@Override
	public List<StandardParts> getStandardPartsList() {
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		String query="SELECT * FROM [CoreStandardParts] order by [pricecat] asc,[part_number] asc";
		Statement stmnt=null;
		ResultSet results = null;
		
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				StandardParts part=new StandardParts(results.getInt("id"), results.getString("part_number"), results.getString("part_description"),
						results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getString("country"),results.getString("part_tag"));
				part.setListPriority(results.getInt("list_priority"));
				partsList.add(part);
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
		return partsList;

	}
	
	@Override
	public List<StandardParts> getStandardPartsList(int assemblyID) {
		List<StandardParts> partsList=new ArrayList<StandardParts>();
		dbConnect();
		String query="SELECT acsp.[quantity],csp.[id],csp.[part_number],csp.[part_description],csp.[part_type],csp.[part_tag],"+
					 "csp.[unit_cost],csp.[markup],csp.[country],csp.[pricecat],csp.[quantitycat],csp.[quantityfactor],csp.[installation_time],csp.[calibration_time],csp.[list_priority]"+
					 " from [AssemblyCoreStandardParts] as acsp"+
					 " RIGHT JOIN [CoreStandardParts] as csp on acsp.part_id = csp.id and acsp.[assembly_id] = ?"+
					 " where csp.[part_type] = 'PART'"+
					 " order by pricecat asc ,csp.part_number asc;";
		PreparedStatement prpedStatement=null;
		ResultSet results = null;
		
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, assemblyID);
			results=prpedStatement.executeQuery();
			while (results.next()){
				StandardParts part=new StandardParts(results.getInt("id"), results.getString("part_number"), results.getString("part_description"),
						results.getString("part_type"), results.getDouble("unit_cost"), results.getDouble("markup"), results.getInt("quantity"), results.getString("country"),null,results.getString("part_tag")
						,results.getString("pricecat"),results.getString("quantitycat"),results.getInt("quantityfactor"),results.getInt("installation_time"),results.getInt("calibration_time"),results.getInt("list_priority"));
				partsList.add(part);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			try {
				results.close();
				prpedStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			dbConn=null;
			prpedStatement=null;
			results=null;
		}
		return partsList;
	}
	
	@Override
	public void addStandardParts(String partNumber, String partDescription,
			String partType, double cost, double markup, String pCountry, String pPartTag, String pPriceCat, String pQuantityCat,
			int pInstallationTime, int pCalibrationTime,int listPriority) {
		PreparedStatement prpedStatement=null;		
		String query = "INSERT INTO [CoreStandardParts] ([part_number],[part_description],[part_type],[unit_cost],[markup],[country],[part_tag],[pricecat],[quantitycat],[installation_time],[calibration_time],[list_priority])"+
           " VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
		dbConnect();
		try{
			
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, partNumber);
			prpedStatement.setString(2, partDescription);
			prpedStatement.setString(3, partType);
			prpedStatement.setDouble(4, cost);
			prpedStatement.setDouble(5, markup);
			prpedStatement.setString(6, pCountry);
			prpedStatement.setString(7, pPartTag);
			prpedStatement.setString(8, pPriceCat);
			prpedStatement.setString(9, pQuantityCat);
			prpedStatement.setInt(10, pInstallationTime);
			prpedStatement.setInt(11, pCalibrationTime);
			prpedStatement.setInt(12, listPriority);
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}		
	}
	
	@Override
	public void modifyStandardParts(int pPartID, String partNumber,
			String partDescription, String partType, double cost, double markup, String pCountry, String pPartTag, String pPriceCat, String pQuantityCat,
			int pInstallationTime, int pCalibrationTime,int listPriority) {
		PreparedStatement prpedStatement=null;		
		
		String query = "UPDATE [CoreStandardParts]  SET [part_number] = ?"+
					   ",[part_description] = ?"+
					   ",[part_type] = ?"+
					   ",[unit_cost] = ?"+
					   ",[country] = ?"+
					   ",[markup] = ?"+
					   ",[part_tag] = ?"+
					   ",[pricecat] = ?"+
					   ",[quantitycat] = ?"+
					   ",[installation_time] = ?"+
					   ",[calibration_time] = ?"+
					   ",[list_priority] = ?"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setString(1, partNumber);
			prpedStatement.setString(2, partDescription);
			prpedStatement.setString(3, partType);
			prpedStatement.setDouble(4, cost);
			prpedStatement.setString(5, pCountry);
			prpedStatement.setDouble(6, markup);
			prpedStatement.setString(7, pPartTag);
			prpedStatement.setString(8, pPriceCat);
			prpedStatement.setString(9, pQuantityCat);
			prpedStatement.setInt(10, pInstallationTime);
			prpedStatement.setInt(11, pCalibrationTime);
			prpedStatement.setInt(12, listPriority);
			prpedStatement.setInt(13, pPartID);
		
					
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}		
	}
	
	@Override
	public void deleteStandardParts(int pPartID) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [CoreStandardParts]"+
					   "WHERE [id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, pPartID);
			prpedStatement.execute();
		}catch (SQLException e){
			e.printStackTrace();
		}	
	}
	@Override
	public void saveSubParts(int passemblyID, List<StandardParts> pSubpartsList) {
		PreparedStatement prpedStatement=null;		
		String query = "DELETE FROM [AssemblyCoreStandardParts]"+
					   "WHERE [assembly_id]= ?";
		dbConnect();
		try{
			prpedStatement=dbConn.prepareStatement(query);
			prpedStatement.setInt(1, passemblyID);
			prpedStatement.execute();
					                      
			query = "INSERT INTO [AssemblyCoreStandardParts]([assembly_id],[part_id],[quantity]) ";
			String insertQueriesCollection ="";
			for(StandardParts subpart: pSubpartsList){
				if(subpart.getQuantity()>0){
					insertQueriesCollection+=" select "+passemblyID+","+subpart.getId()+","+subpart.getQuantity();
					insertQueriesCollection+=" union all";
				}
			}
			if(insertQueriesCollection.compareToIgnoreCase("")!=0){
				insertQueriesCollection=insertQueriesCollection.substring(0, insertQueriesCollection.lastIndexOf("union"))+";";
				prpedStatement=dbConn.prepareStatement(query+insertQueriesCollection);
				prpedStatement.execute();
			}
		}catch (SQLException e){
			e.printStackTrace();
		}	
	}
	
	@Override
	public List<String> getCountryList() {
		List<String> countriesList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(country) FROM [CoreStandardParts] order by country asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				countriesList.add(results.getString("country"));
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
		return countriesList;	
	}
	
	public List<String>getPartsTagList(){
		List<String> partTagsList=new ArrayList<String>();
		dbConnect();
		String query="SELECT distinct(part_tag) FROM [CoreStandardParts] order by part_tag asc";
		Statement stmnt=null;
		ResultSet results = null;
		try{
			stmnt=dbConn.createStatement();
			results=stmnt.executeQuery(query);
			while (results.next()){
				if(results.getString("part_tag")!=null){
					partTagsList.add(results.getString("part_tag"));
				}
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
		return partTagsList;	
	}

	@Override
	public void saveMarkups(List<StandardParts> pPartsList) {
		
		PreparedStatement prpedStatement=null;		
		dbConnect();
		try{
			//String query = "INSERT INTO [AssemblyCoreStandardParts]([assembly_id],[part_id],[quantity]) ";
			String query = "update [CoreStandardParts]" +
			  " set [markup] ="+ 
			  " case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then "+part.getMarkup();
			}
			query+=" else markup";
			query+=" end";
		
			prpedStatement=dbConn.prepareStatement(query);
			
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

	public void saveCategories(List<StandardParts> pPartsList){
		PreparedStatement prpedStatement=null;		
		dbConnect();
		try{
			//String query = "INSERT INTO [AssemblyCoreStandardParts]([assembly_id],[part_id],[quantity]) ";
			String query = "update [CoreStandardParts]" +
			  " set [pricecat] ="+ 
			  " case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then '"+part.getPricecategory()+"'";
			}
			query+=" else pricecat";
			query+=" end";
			query+=" ,[quantitycat] ="+ 
			" case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then '"+part.getQuantitycategory()+"'";
			}
			query+=" else quantitycat";
			query+=" end";
			query+=" ,[quantityfactor] ="+ 
			" case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then "+part.getQuantityfactor();
			}
			query+=" else quantityfactor";
			query+=" end";
//			System.out.println(query);
			prpedStatement=dbConn.prepareStatement(query);
			
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
	public void saveAllParts(List<StandardParts> pPartsList){
		PreparedStatement prpedStatement=null;	
		dbConnect();
		try{
			//String query = "INSERT INTO [AssemblyCoreStandardParts]([assembly_id],[part_id],[quantity]) ";
			String query = "update [CoreStandardParts]" +
			  " set [unit_cost] ="+ 
			  " case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then '"+part.getCost()+"'";
			}
			query+=" else unit_cost";
			query+=" end";
			query+=" ,[markup] ="+ 
			" case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then '"+part.getMarkup()+"'";
			}
			query+=" else markup";
			query+=" end";
			query+=" ,[installation_time] ="+ 
			" case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then "+part.getInstallationTime();
			}
			query+=" else installation_time";
			query+=" end";
			query+=" ,[calibration_time] ="+ 
			" case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then "+part.getCalibrationTime();
			}
			query+=" else calibration_time";
			query+=" end";
			query+=" ,[list_priority] ="+ 
			" case";
			for(StandardParts part: pPartsList){
				query += " when id ="+ part.getId()+" then "+part.getListPriority();
			}
			query+=" else list_priority";
			query+=" end";
			
//			System.out.println(query);
			prpedStatement=dbConn.prepareStatement(query);
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
	public void saveCategory(StandardParts part) {
		// TODO Auto-generated method stub
		String query="UPDATE  [CoreStandardParts] SET "+
		              "[pricecat] = ?  ,[quantitycat] = ? , [quantityfactor] = ?  WHERE [id] = ?";
		PreparedStatement prpedStatement = null; 
		dbConnect();
		try {
			prpedStatement = dbConn.prepareStatement(query);
			prpedStatement.setString(1, part.getPricecategory());
			prpedStatement.setString(2, part.getQuantitycategory());
			prpedStatement.setInt(3,  part.getQuantityfactor());
			prpedStatement.setInt(4, part.getId());
			prpedStatement.executeUpdate();
		}catch(SQLException ex) {
			ex.printStackTrace();
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
	

}
