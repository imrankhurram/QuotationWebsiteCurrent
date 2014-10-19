package com.nextcontrols.bureaudao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.internet.AddressException;

import com.nextcontrols.bureaudomain.NonStandardParts;
import com.nextcontrols.bureaudomain.Quotation;
import com.nextcontrols.bureaudomain.StandardParts;
import com.nextcontrols.bureaudomain.User;

/////////////////////////////////////////////////////////////
///Every MySQL connection with ResultSet requires finally////
///////////////to prevent a memory leak//////////////////////
/////////////////////////////////////////////////////////////

public class QuotationDAO implements IQuotationDAO, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Connection dbConn = null;

	private static IQuotationDAO instance;

	public static IQuotationDAO getInstance() {
		if (instance != null) {
			return instance;
		} else {
			return new QuotationDAO();
		}
	}

	public static void setInstance(IQuotationDAO ins) {
		instance = ins;
	}

	private QuotationDAO() {

	}

	private void dbConnect() {
		try {
			dbConn = ConnectionBean.getInstance().getDBConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void saveQuotation(Quotation pQuotation,
			List<List<StandardParts>> pStandardPartsCollection,
			List<String> pSiteNames, List<String> pSiteInstallation,
			List<String> pInstallationTravel, List<String> pSiteCalibration,
			List<String> pCalibartionTravel,
			List<String> pAnnualSiteCalibration,
			List<String> pAnnualDeadTravelDays, String pPdfContents,
			String pEmailedTo) {
		String query = "INSERT INTO [Quotation] ([quotation_ref],[quotation_coveringletter],[project_name],[customer_name]"
				+ ",[contact_name],[address1],[address2],[town_city],[country_state],[zip_pincode],[facility_type]"
				+ ",[contact_telno],[site_telno],[contact_email],[site_email],[onsite_sensorcalibration]"
				+ ",[factory_sensorcalibration],[dqiqoq_protocoldocs],[websitedb_setup],[webhosted_gui]"
				+ ",[combinedrna_monitorying],[recording_only],[yearly_recalibrationservice],[number_of_sites]"
				+ ",[temperature_mapping],[individual_quotepersite],[hardware_discount],[siteinstallation_discount]"
				+ ",[serviceoption_discount],[remotemonitoring_discount],[user_id],[creationtime],[pdfcontents],[emailto],[revisionnumber],[emailcc],[emailbcc],[emailsubject],[address3],[department],[disable_coveringletter])"
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

		PreparedStatement stmnt = null;
		dbConnect();
		int quoteid = 0;
		try {
			stmnt = dbConn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			stmnt.setString(1, pQuotation.getQuotationref());
			stmnt.setString(2, pQuotation.getQuotationcoveringletter());
			stmnt.setString(3, pQuotation.getProjectname());
			stmnt.setString(4, pQuotation.getCustomername());
			stmnt.setString(5, pQuotation.getContactname());
			stmnt.setString(6, pQuotation.getAddress1());
			stmnt.setString(7, pQuotation.getAddress2());
			stmnt.setString(8, pQuotation.getTowncity());
			stmnt.setString(9, pQuotation.getCountrystate());
			stmnt.setString(10, pQuotation.getZippincode());
			stmnt.setString(11, pQuotation.getFacilitytype());
			stmnt.setString(12, pQuotation.getContacttelno());
			stmnt.setString(13, pQuotation.getSitetelno());
			stmnt.setString(14, pQuotation.getContactemail());
			stmnt.setString(15, pQuotation.getSiteemail());
			stmnt.setBoolean(16, pQuotation.getOnsitesensorcalibration());
			stmnt.setBoolean(17, pQuotation.getFactorysensorcalibration());
			stmnt.setBoolean(18, pQuotation.getDqiqoqprotocoldocs());
			stmnt.setBoolean(19, pQuotation.getWebsitedbsetup());
			stmnt.setBoolean(20, pQuotation.getWebhostedgui());
			stmnt.setBoolean(21, pQuotation.getCombinedrnamonitoring());
			stmnt.setBoolean(22, pQuotation.getRecordingonly());
			stmnt.setBoolean(23, pQuotation.getYearlyrecalibrationservice());
			stmnt.setInt(24, pQuotation.getNumberofsites());
			stmnt.setBoolean(25, pQuotation.getTemperatureMapping());
			stmnt.setBoolean(26, pQuotation.getIndividualquotepersite());
			stmnt.setDouble(27, pQuotation.getHardwarediscount());
			stmnt.setDouble(28, pQuotation.getSiteinstallationdiscount());
			stmnt.setDouble(29, pQuotation.getServiceoptiondiscount());
			stmnt.setDouble(30, pQuotation.getRemotemonitoringdiscount());
			stmnt.setInt(31, pQuotation.getUser().getUserId());
			stmnt.setTimestamp(32, new Timestamp(Calendar.getInstance()
					.getTime().getTime()));
			stmnt.setString(33, pPdfContents);
			stmnt.setString(34, pEmailedTo);
			stmnt.setInt(35, pQuotation.getRevisionnumber());
			stmnt.setString(36, pQuotation.getEmailbcc());
			stmnt.setString(37, pQuotation.getEmailbcc());
			stmnt.setString(38, pQuotation.getEmailsubject());
			stmnt.setString(39, pQuotation.getAddress3());
			stmnt.setString(40, pQuotation.getDepartment());
			stmnt.setBoolean(41, pQuotation.isDisable_coveringletter());
			stmnt.executeUpdate();
			ResultSet rs = stmnt.getGeneratedKeys();
			while (rs.next()) {
				quoteid = rs.getInt(1);
				break;
			}

			for (int i = 0; i < pStandardPartsCollection.size(); i++) {
				List<StandardParts> tempStList = pStandardPartsCollection
						.get(i);
				for (StandardParts st : tempStList) {
					if (st.getQuoteQuantity() > 0) {
						/*System.out.println(st.getPartNumber() + " : "
								+ st.getPartType());*/
						if (st.getPartType().compareToIgnoreCase(
								"NonStandard Part") != 0) {
							query = "INSERT INTO [Quotation_standardpart]"
									+ "([quotation_id],[id],[site_name],[quantity]"
									+ ",[site_installation_days],[installation_travel_days],[site_calibration_days]"
									+ ",[calibration_travel_days],[annual_nist_calibration_days],[annual_travel_days])"
									+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
							stmnt = dbConn.prepareStatement(query);
							stmnt.setInt(1, quoteid);
							stmnt.setInt(2, st.getId());
							stmnt.setString(3, pSiteNames.get(i));
							stmnt.setInt(4, st.getQuoteQuantity());
							stmnt.setString(5, pSiteInstallation.get(i));
							stmnt.setString(6, pInstallationTravel.get(i));
							stmnt.setString(7, pSiteCalibration.get(i));
							stmnt.setString(8, pCalibartionTravel.get(i));
							stmnt.setString(9, pAnnualSiteCalibration.get(i));
							stmnt.setString(10, pAnnualDeadTravelDays.get(i));
							stmnt.executeUpdate();
						} else {
							query = "INSERT INTO [Quotation_NonStandardPart]"
									+ " ([quotation_id],[quantity],[part_number],[part_description],[unit_cost])"
									+ " VALUES(?,?,?,?,?)";
							stmnt = dbConn.prepareStatement(query);
							stmnt.setInt(1, quoteid);
							stmnt.setInt(2, st.getQuoteQuantity());
							stmnt.setString(3, st.getPartNumber());
							stmnt.setString(4, st.getPartDescription());
							stmnt.setDouble(5, st.getListPrice());
							stmnt.executeUpdate();
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}
	}

	public int saveUpdateQuote(int quotationId, Quotation pQuotation,
			String pPdfContents, String pEmailedTo, boolean isCompleted) {
		PreparedStatement stmnt = null;
		dbConnect();
		int quoteid = 0;

		if (quotationId != 0) {
			String query = "UPDATE [Quotation] SET [quotation_ref]=?,[quotation_coveringletter]=?,[project_name]=?,[customer_name]=?"
					+ ",[contact_name]=?,[address1]=?,[address2]=?,[town_city]=?,[country_state]=?,[zip_pincode]=?,[facility_type]=?"
					+ ",[contact_telno]=?,[site_telno]=?,[contact_email]=?,[site_email]=?,[onsite_sensorcalibration]=?"
					+ ",[factory_sensorcalibration]=?,[dqiqoq_protocoldocs]=?,[websitedb_setup]=?,[webhosted_gui]=?"
					+ ",[combinedrna_monitorying]=?,[recording_only]=?,[yearly_recalibrationservice]=?,[number_of_sites]=?"
					+ ",[temperature_mapping]=?,[individual_quotepersite]=?,[hardware_discount]=?,[siteinstallation_discount]=?"
					+ ",[serviceoption_discount]=?,[remotemonitoring_discount]=?,[user_id]=?,[creationtime]=?,[pdfcontents]=?,[emailto]=?,[revisionnumber]=?"
					+ ",[completed]=?,[emailcc]=?,[emailbcc]=?,[emailsubject]=?,[customer_salutation]=?,[update_installation]=?,[remote_monitoring]=?"
					+ ",[annual_calibration]=?,[address3]=?,[department]=?,[disable_coveringletter]=?,[hardwareonly]=?"
					+ "WHERE id=?";

			try {
				stmnt = dbConn.prepareStatement(query);
				stmnt.setString(1, pQuotation.getQuotationref());
				stmnt.setString(2, pQuotation.getQuotationcoveringletter());
				stmnt.setString(3, pQuotation.getProjectname());
				stmnt.setString(4, pQuotation.getCustomername());
				stmnt.setString(5, pQuotation.getContactname());
				stmnt.setString(6, pQuotation.getAddress1());
				stmnt.setString(7, pQuotation.getAddress2());
				stmnt.setString(8, pQuotation.getTowncity());
				stmnt.setString(9, pQuotation.getCountrystate());
				stmnt.setString(10, pQuotation.getZippincode());
				stmnt.setString(11, pQuotation.getFacilitytype());
				stmnt.setString(12, pQuotation.getContacttelno());
				stmnt.setString(13, pQuotation.getSitetelno());
				stmnt.setString(14, pQuotation.getContactemail());
				stmnt.setString(15, pQuotation.getSiteemail());
				stmnt.setBoolean(16, pQuotation.getOnsitesensorcalibration());
				stmnt.setBoolean(17, pQuotation.getFactorysensorcalibration());
				stmnt.setBoolean(18, pQuotation.getDqiqoqprotocoldocs());
				stmnt.setBoolean(19, pQuotation.getWebsitedbsetup());
				stmnt.setBoolean(20, pQuotation.getWebhostedgui());
				stmnt.setBoolean(21, pQuotation.getCombinedrnamonitoring());
				stmnt.setBoolean(22, pQuotation.getRecordingonly());
				stmnt.setBoolean(23, pQuotation.getYearlyrecalibrationservice());
				stmnt.setInt(24, pQuotation.getNumberofsites());
				stmnt.setBoolean(25, pQuotation.getTemperatureMapping());
				stmnt.setBoolean(26, pQuotation.getIndividualquotepersite());
				stmnt.setDouble(27, pQuotation.getHardwarediscount());
				stmnt.setDouble(28, pQuotation.getSiteinstallationdiscount());
				stmnt.setDouble(29, pQuotation.getServiceoptiondiscount());
				stmnt.setDouble(30, pQuotation.getRemotemonitoringdiscount());
				stmnt.setInt(31, pQuotation.getUser().getUserId());
				stmnt.setTimestamp(32, new Timestamp(Calendar.getInstance()
						.getTime().getTime()));
				stmnt.setString(33, pPdfContents);
				stmnt.setString(34, pEmailedTo);
				stmnt.setInt(35, pQuotation.getRevisionnumber());
				stmnt.setBoolean(36, isCompleted);
				stmnt.setString(37, pQuotation.getEmailcc());
				stmnt.setString(38, pQuotation.getEmailbcc());
				stmnt.setString(39, pQuotation.getEmailsubject());
				stmnt.setString(40,pQuotation.getCustomersalutation());
				stmnt.setBoolean(41,pQuotation.isUpdateInstallation());
				stmnt.setDouble(42,pQuotation.getRemoteMonitoring());
				stmnt.setDouble(43,pQuotation.getAnnualCalibration());
				stmnt.setString(44,pQuotation.getAddress3());
				stmnt.setString(45,pQuotation.getDepartment());
				stmnt.setBoolean(46, pQuotation.isDisable_coveringletter());
				stmnt.setBoolean(47, pQuotation.isHardwareOnly());
				stmnt.setInt(48, quotationId);
				stmnt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}

			return quotationId;
		} else {
			String query = "INSERT INTO [Quotation] ([quotation_ref],[quotation_coveringletter],[project_name],[customer_name]"
					+ ",[contact_name],[address1],[address2],[town_city],[country_state],[zip_pincode],[facility_type]"
					+ ",[contact_telno],[site_telno],[contact_email],[site_email],[onsite_sensorcalibration]"
					+ ",[factory_sensorcalibration],[dqiqoq_protocoldocs],[websitedb_setup],[webhosted_gui]"
					+ ",[combinedrna_monitorying],[recording_only],[yearly_recalibrationservice],[number_of_sites]"
					+ ",[temperature_mapping],[individual_quotepersite],[hardware_discount],[siteinstallation_discount]"
					+ ",[serviceoption_discount],[remotemonitoring_discount],[user_id],[creationtime],[pdfcontents]"
					+ ",[emailto],[revisionnumber],[completed],[emailcc],[emailbcc],[emailsubject],[customer_salutation]"
					+ ",[update_installation],[remote_monitoring],[annual_calibration],[address3],[department],[disable_coveringletter],[hardwareonly])"
					+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			try {
				stmnt = dbConn.prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);
				stmnt.setString(1, pQuotation.getQuotationref());
				stmnt.setString(2, pQuotation.getQuotationcoveringletter());
				stmnt.setString(3, pQuotation.getProjectname());
				stmnt.setString(4, pQuotation.getCustomername());
				stmnt.setString(5, pQuotation.getContactname());
				stmnt.setString(6, pQuotation.getAddress1());
				stmnt.setString(7, pQuotation.getAddress2());
				stmnt.setString(8, pQuotation.getTowncity());
				stmnt.setString(9, pQuotation.getCountrystate());
				stmnt.setString(10, pQuotation.getZippincode());
				stmnt.setString(11, pQuotation.getFacilitytype());
				stmnt.setString(12, pQuotation.getContacttelno());
				stmnt.setString(13, pQuotation.getSitetelno());
				stmnt.setString(14, pQuotation.getContactemail());
				stmnt.setString(15, pQuotation.getSiteemail());
				stmnt.setBoolean(16, pQuotation.getOnsitesensorcalibration());
				stmnt.setBoolean(17, pQuotation.getFactorysensorcalibration());
				stmnt.setBoolean(18, pQuotation.getDqiqoqprotocoldocs());
				stmnt.setBoolean(19, pQuotation.getWebsitedbsetup());
				stmnt.setBoolean(20, pQuotation.getWebhostedgui());
				stmnt.setBoolean(21, pQuotation.getCombinedrnamonitoring());
				stmnt.setBoolean(22, pQuotation.getRecordingonly());
				stmnt.setBoolean(23, pQuotation.getYearlyrecalibrationservice());
				stmnt.setInt(24, pQuotation.getNumberofsites());
				stmnt.setBoolean(25, pQuotation.getTemperatureMapping());
				stmnt.setBoolean(26, pQuotation.getIndividualquotepersite());
				stmnt.setDouble(27, pQuotation.getHardwarediscount());
				stmnt.setDouble(28, pQuotation.getSiteinstallationdiscount());
				stmnt.setDouble(29, pQuotation.getServiceoptiondiscount());
				stmnt.setDouble(30, pQuotation.getRemotemonitoringdiscount());
				stmnt.setInt(31, pQuotation.getUser().getUserId());
				stmnt.setTimestamp(32, new Timestamp(Calendar.getInstance()
						.getTime().getTime()));
				stmnt.setString(33, pPdfContents);
				stmnt.setString(34, pEmailedTo);
				stmnt.setInt(35, pQuotation.getRevisionnumber());
				stmnt.setBoolean(36, isCompleted);
				stmnt.setString(37, pQuotation.getEmailcc());
				stmnt.setString(38, pQuotation.getEmailbcc());
				stmnt.setString(39, pQuotation.getEmailsubject());
				stmnt.setString(40, pQuotation.getCustomersalutation());
				stmnt.setBoolean(41, pQuotation.isUpdateInstallation());
				stmnt.setDouble(42, pQuotation.getRemoteMonitoring());
				stmnt.setDouble(43, pQuotation.getAnnualCalibration());
				stmnt.setString(44, pQuotation.getAddress3());
				stmnt.setString(45, pQuotation.getDepartment());
				stmnt.setBoolean(46, pQuotation.isDisable_coveringletter());
				stmnt.setBoolean(47, pQuotation.isHardwareOnly());
				stmnt.executeUpdate();
				ResultSet rs = stmnt.getGeneratedKeys();
				while (rs.next()) {
					quoteid = rs.getInt(1);
					break;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}
			return quoteid;

		}
	}

	public void saveParts(int quoteId,
			List<List<StandardParts>> pStandardPartsCollection,
			List<String> pSiteNames, List<String> pSiteInstallation,
			List<String> pInstallationTravel, List<String> pSiteCalibration,
			List<String> pCalibartionTravel,
			List<String> pAnnualSiteCalibration,
			List<String> pAnnualDeadTravelDays) {
		String query;
		PreparedStatement stmnt = null;
		dbConnect();
		int quoteid = quoteId;

		try {

			for (int i = 0; i < pStandardPartsCollection.size(); i++) {
				List<StandardParts> tempStList = pStandardPartsCollection
						.get(i);
				for (StandardParts st : tempStList) {
					if (st.getQuoteQuantity() > 0) {
						/*System.out.println(st.getPartNumber() + " : "
								+ st.getPartType());*/

						if (st.getPartType().compareToIgnoreCase(
								"NonStandard Part") != 0) {
							query = "UPDATE [Quotation_standardpart]"
									+ " SET [quotation_id]=?,[id]=?,[site_name]=?,[quantity]=?"
									+ ",[site_installation_days]=?,[installation_travel_days]=?,[site_calibration_days]=?"
									+ ",[calibration_travel_days]=?,[annual_nist_calibration_days]=?,[annual_travel_days]=?"
									+ "WHERE [quotation_id]=? AND [id]=?";
							stmnt = dbConn.prepareStatement(query);
							stmnt.setInt(1, quoteid);
							stmnt.setInt(2, st.getId());
							stmnt.setString(3, pSiteNames.get(i));
							stmnt.setInt(4, st.getQuoteQuantity());
							stmnt.setString(5, pSiteInstallation.get(i));
							stmnt.setString(6, pInstallationTravel.get(i));
							stmnt.setString(7, pSiteCalibration.get(i));
							stmnt.setString(8, pCalibartionTravel.get(i));
							stmnt.setString(9, pAnnualSiteCalibration.get(i));
							stmnt.setString(10, pAnnualDeadTravelDays.get(i));
							stmnt.setInt(11, quoteid);
							stmnt.setInt(12, st.getId());
							int row = stmnt.executeUpdate();
							if (row <= 0) {
								query = "INSERT INTO [Quotation_standardpart]"
										+ "([quotation_id],[id],[site_name],[quantity]"
										+ ",[site_installation_days],[installation_travel_days],[site_calibration_days]"
										+ ",[calibration_travel_days],[annual_nist_calibration_days],[annual_travel_days])"
										+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
								stmnt = dbConn.prepareStatement(query);
								stmnt.setInt(1, quoteid);
								stmnt.setInt(2, st.getId());
								stmnt.setString(3, pSiteNames.get(i));
								stmnt.setInt(4, st.getQuoteQuantity());
								stmnt.setString(5, pSiteInstallation.get(i));
								stmnt.setString(6, pInstallationTravel.get(i));
								stmnt.setString(7, pSiteCalibration.get(i));
								stmnt.setString(8, pCalibartionTravel.get(i));
								stmnt.setString(9,
										pAnnualSiteCalibration.get(i));
								stmnt.setString(10,
										pAnnualDeadTravelDays.get(i));
								stmnt.executeUpdate();
							}
						} else {
							query = "UPDATE [Quotation_NonStandardPart]"
									+ "SET [quotation_id]=?,[quantity]=?,[part_number]=?,[part_description]=?,[unit_cost]=?"
									+ "WHERE [quotation_id]=? AND [id]=?";
							stmnt = dbConn.prepareStatement(query);
							stmnt.setInt(1, quoteid);
							stmnt.setInt(2, st.getQuoteQuantity());
							stmnt.setString(3, st.getPartNumber());
							stmnt.setString(4, st.getPartDescription());
							stmnt.setDouble(5, st.getListPrice());
							stmnt.setDouble(6, quoteid);
							stmnt.setDouble(7, st.getId());
							int row = stmnt.executeUpdate();
							if (row <= 0) {
								query = "INSERT INTO [Quotation_NonStandardPart]"
										+ " ([quotation_id],[quantity],[part_number],[part_description],[unit_cost])"
										+ " VALUES(?,?,?,?,?)";
								stmnt = dbConn.prepareStatement(query);
								stmnt.setInt(1, quoteid);
								stmnt.setInt(2, st.getQuoteQuantity());
								stmnt.setString(3, st.getPartNumber());
								stmnt.setString(4, st.getPartDescription());
								stmnt.setDouble(5, st.getListPrice());
								stmnt.executeUpdate();
							}
						}

					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

	}
	public List<List<String>> getPartsCost(int quoteId) {
		List<String> siteInstallation=new ArrayList<String>();
		List<String> installationTravel=new ArrayList<String>();
		List<String> siteCalibration=new ArrayList<String>();
		List<String> calibrationTravel=new ArrayList<String>();
		List<List<String>> result=new ArrayList<List<String>>(4);
		String query;
		Statement stmnt = null;
		ResultSet results = null;
		dbConnect();

		try {
			query="Select * from [Quotation_standardpart] where [quotation_id]= "+quoteId;
			stmnt = dbConn.createStatement();
//			stmnt.setInt(1, quoteId);
			results = stmnt.executeQuery(query);
			while (results.next()){
				siteInstallation.add(results.getString("site_installation_days"));
//				System.out.println("results.getString(site_installation_days)::"+results.getString("site_installation_days"));
				installationTravel.add(results.getString("installation_travel_days"));
				siteCalibration.add(results.getString("site_calibration_days"));
				calibrationTravel.add(results.getString("calibration_travel_days"));
			}
			result.add(siteInstallation);
			result.add(installationTravel);
			result.add(siteCalibration);
			result.add(calibrationTravel);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		results=null;
		try {
			query="Select * from [Quotation_NonStandardPart] where [quotation_id]= "+quoteId;
			stmnt = dbConn.createStatement();
//			stmnt.setInt(1, quoteId);
			results = stmnt.executeQuery(query);
			while (results.next()){
				siteInstallation.add(results.getString("site_installation_days"));
				System.out.println("results.getString(site_installation_days)::"+results.getString("site_installation_days"));
				installationTravel.add(results.getString("installation_travel_days"));
				siteCalibration.add(results.getString("site_calibration_days"));
				calibrationTravel.add(results.getString("calibration_travel_days"));
			}
			result.add(siteInstallation);
			result.add(installationTravel);
			result.add(siteCalibration);
			result.add(calibrationTravel);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}
		return result;

	}
	public List<Quotation> getQuotations(int pUserID, int pCompanyID,
			String pUserType, Date pDateFrom, Date pDateTo, boolean isCompleted) {
		List<Quotation> quotationsList = new ArrayList<Quotation>();
		dbConnect();
		String query = "SELECT * FROM [Quotation] as q "
				+ " Inner Join [user] as us on q.user_id = us.user_id "
				+ " where (q.creationtime >= '"
				+ new java.sql.Timestamp(pDateFrom.getTime())
				+ "' AND q.creationtime <= '"
				+ new java.sql.Timestamp(pDateTo.getTime()) + "' "
				+ " AND q.completed='" + isCompleted + "') "
				+ " order by q.revisionnumber desc,q.creationtime asc";
		if (pUserType.compareToIgnoreCase("Admin") == 0
				|| pUserType.compareToIgnoreCase("Master") == 0|| pUserType.compareToIgnoreCase("ProjectAdmin") == 0) {
			query = "SELECT * FROM [Quotation] as q "
					+ " Inner Join [user] as us on q.user_id = us.user_id "
					+ " where (q.creationtime >= '"
					+ new java.sql.Timestamp(pDateFrom.getTime())
					+ "' AND q.creationtime <= '"
					+ new java.sql.Timestamp(pDateTo.getTime()) + "' "
					+ " AND q.completed='" + isCompleted + "') "
					+ " order by q.revisionnumber desc,q.creationtime asc";
		} else if (pUserType.compareToIgnoreCase("Supervisor") == 0) {
			query = "SELECT * FROM [Quotation] as q "
					+ " Inner join [user] as puser on q.user_id = puser.user_id"
					+ " where puser.company_id = " + pCompanyID + " AND "
					+ " (q.creationtime >= '"
					+ new java.sql.Timestamp(pDateFrom.getTime())
					+ "' AND q.creationtime <= '"
					+ new java.sql.Timestamp(pDateTo.getTime()) + "' "
					+ " AND q.completed='" + isCompleted + "') "
					+ " order by q.revisionnumber desc,q.creationtime asc";
		} else {
			query = "SELECT * FROM [Quotation] as q "
					+ " Inner join [user] as puser on q.user_id = puser.user_id"
					+ " where puser.user_id = " + pUserID + " AND "
					+ " (q.creationtime >= '"
					+ new java.sql.Timestamp(pDateFrom.getTime())
					+ "' AND q.creationtime <= '"
					+ new java.sql.Timestamp(pDateTo.getTime()) + "' "
					+ " AND q.completed='" + isCompleted + "') "
					+ " order by q.revisionnumber desc,q.creationtime asc";
		}
		//System.out.println(query);
		Statement stmnt = null;
		ResultSet results = null;
		try {
			stmnt = dbConn.createStatement();
			results = stmnt.executeQuery(query);
			while (results.next()) {

				User user = new User(results.getInt("user_id"),
						results.getString("user_name"),
						results.getString("password"),
						results.getString("first_name"),
						results.getString("last_name"),
						results.getString("email"),
						results.getDouble("max_alloweddiscount"),
						results.getBoolean("enabled"),
						results.getString("user_type"), null,
						results.getBoolean("termsnconditions"),
						results.getString("title"),
						results.getString("phone_number"));

				Quotation quotation = new Quotation(results.getInt("id"),
						results.getString("customer_salutation"),
						results.getString("quotation_ref"),
						results.getString("quotation_coveringletter"),
						results.getString("facility_type"),
						results.getString("project_name"),
						results.getString("customer_name"),
						results.getString("contact_name"),
						results.getString("address1"),
						results.getString("address2"),
						results.getString("town_city"),
						results.getString("country_state"),
						results.getString("zip_pincode"),
						results.getString("facility_type"),
						results.getString("contact_telno"),
						results.getString("site_telno"),
						results.getString("contact_email"),
						results.getString("site_email"),
						"www.nextcontrols.com",
						results.getBoolean("onsite_sensorcalibration"),
						results.getBoolean("factory_sensorcalibration"),
						results.getBoolean("dqiqoq_protocoldocs"),
						results.getBoolean("websitedb_setup"),
						results.getBoolean("webhosted_gui"),
						results.getBoolean("combinedrna_monitorying"),
						results.getBoolean("recording_only"),
						results.getBoolean("yearly_recalibrationservice"),
						false, results.getBoolean("individual_quotepersite"),
						results.getInt("number_of_sites"),
						results.getDouble("hardware_discount"),
						results.getDouble("siteinstallation_discount"),
						results.getDouble("serviceoption_discount"), 0, 0,
						user, results.getBoolean("temperature_mapping"),
						results.getDouble("remotemonitoring_discount"),
						results.getString("pdfcontents"),
						results.getTimestamp("creationtime"),
						results.getString("emailto"),
						results.getInt("revisionnumber"),
						results.getBoolean("completed"),
						results.getBoolean("accepted"),
						results.getString("emailcc"),
						results.getString("emailbcc"),
						results.getString("emailsubject"));
				quotation.setUpdateInstallation(results.getBoolean("update_installation"));
				quotation.setRemoteMonitoring(results.getDouble("remote_monitoring"));
				quotation.setAnnualCalibration(results.getDouble("annual_calibration"));
				quotation.setAddress3(results.getString("address3"));
				quotation.setDepartment(results.getString("department"));
				quotation.setDisable_coveringletter(results.getBoolean("disable_coveringletter"));
				quotation.setHardwareOnly(results.getBoolean("hardwareonly"));
				quotationsList.add(quotation);
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
		return quotationsList;
	}

	@Override
	public int getMaxQuotationID(int pUserID) {
		/*
		 * String query =
		 * "SELECT max(id)+1 as maxid FROM [Quotation]";
		 * dbConn = null; PreparedStatement stmnt = null; int maxid=0; try {
		 * dbConnect(); stmnt = dbConn.prepareStatement(query);
		 * 
		 * ResultSet rs = stmnt.executeQuery(); if (rs.next()) { maxid =
		 * rs.getInt("maxid"); }
		 * 
		 * } catch (SQLException e) { e.printStackTrace(); } finally { dbConn =
		 * null; stmnt = null; }
		 * 
		 * return maxid;
		 */
		String query = "INSERT INTO [QuotationIDGenerator] ([user_id]) VALUES (?);";

		PreparedStatement stmnt = null;
		dbConnect();
		int maxid = 0;
		try {
			stmnt = dbConn.prepareStatement(query,
					Statement.RETURN_GENERATED_KEYS);
			stmnt.setInt(1, pUserID);

			stmnt.executeUpdate();
			ResultSet rs = stmnt.getGeneratedKeys();
			while (rs.next()) {
				maxid = rs.getInt(1);
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}
		return maxid;
	}

	public void saveQuantitySP(Quotation pQuotation, List sparts, boolean flag) {
		StandardParts pPart;
		if (flag == false) {
			String query = "INSERT INTO [Quotation_standardpart] ([quotation_ref],[id],[site_name],[quantity]) VALUES(?,?,?,?);";

			PreparedStatement stmnt = null;
			dbConnect();
			try {

				for (int i = 0; i < sparts.size(); i++) {
					pPart = (StandardParts) sparts.get(i);

					stmnt = dbConn.prepareStatement(query);
					stmnt.setInt(1, pQuotation.getId());
					stmnt.setInt(2, pPart.getId());
					stmnt.setString(3, "bgfb");
					stmnt.setInt(4, pPart.getQuantity());
					stmnt.executeUpdate();
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}
		}// if
		else {

			String query = "UPDATE [Quotation_standardpart]"
					+ "SET quantity = ?" + "WHERE quotation_ref=? AND id=? ;";
			PreparedStatement stmnt = null;
			dbConnect();
			try {

				for (int i = 0; i < sparts.size(); i++) {
					pPart = (StandardParts) sparts.get(i);

					stmnt = dbConn.prepareStatement(query);
					stmnt.setInt(1, pPart.getQuantity());
					stmnt.setInt(2, pQuotation.getId());
					stmnt.setInt(3, pPart.getId());

					stmnt.executeUpdate();

				}// for

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}

		}// else
	}

	public void saveQuantityNonSP(Quotation pQuotation, List sparts,
			boolean flag) {
		NonStandardParts pPart;
		if (flag == false) {
			String query = "INSERT INTO [Quotation_NonStandardPart] ([Quotation_ref],[id],[quantity],[site_name]) VALUES(?,?,?,?);";

			PreparedStatement stmnt = null;
			dbConnect();
			try {

				for (int i = 0; i < sparts.size(); i++) {
					pPart = (NonStandardParts) sparts.get(i);

					stmnt = dbConn.prepareStatement(query);
					stmnt.setInt(1, pQuotation.getId());
					stmnt.setInt(2, pPart.getId());
					stmnt.setInt(3, pPart.getQuantity());
					stmnt.setString(4, "bgfb");

					// System.out.println("saved");
					stmnt.executeUpdate();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}
		}// if
		else {

			String query = "UPDATE [Quotation_NonStandardPart]"
					+ "SET quantity = ?" + "WHERE quotation_ref=? AND id=? ;";
			PreparedStatement stmnt = null;
			dbConnect();
			try {

				for (int i = 0; i < sparts.size(); i++) {
					pPart = (NonStandardParts) sparts.get(i);

					stmnt = dbConn.prepareStatement(query);
					stmnt.setInt(1, pPart.getQuantity());
					stmnt.setInt(2, pQuotation.getId());
					stmnt.setInt(3, pPart.getId());

					stmnt.executeUpdate();

				}// for

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}

		}// else
	}

	@Override
	public int saveQuotation(Quotation pQuotation, boolean flag) {
		int quoteid = 0;

		if (flag == false) {
			String query = "INSERT INTO [Quotation] ([project_name],[customer_name]"
					+ ",[contact_name],[address1],[address2],[town_city],[country_state],[zip_pincode]"
					+ ",[facility_type],[contact_telno],[site_telno],[contact_email]"
					+ ",[site_email],[customer_website],[user_id]) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			PreparedStatement stmnt = null;
			dbConnect();

			try {
				stmnt = dbConn.prepareStatement(query,
						Statement.RETURN_GENERATED_KEYS);
				stmnt.setString(1, pQuotation.getProjectname());
				stmnt.setString(2, pQuotation.getCustomername());
				stmnt.setString(3, pQuotation.getContactname());
				stmnt.setString(4, pQuotation.getAddress1());
				stmnt.setString(5, pQuotation.getAddress2());
				stmnt.setString(6, pQuotation.getTowncity());
				stmnt.setString(7, pQuotation.getCountrystate());
				stmnt.setString(8, pQuotation.getZippincode());
				stmnt.setString(9, pQuotation.getFacilitytype());
				stmnt.setString(10, pQuotation.getContacttelno());
				stmnt.setString(11, pQuotation.getSitetelno());
				stmnt.setString(12, pQuotation.getContactemail());
				stmnt.setString(13, pQuotation.getSiteemail());
				stmnt.setString(14, pQuotation.getCustomerwebsite());
				stmnt.setInt(15, pQuotation.getUser().getUserId());

				stmnt.executeUpdate();
				ResultSet rs = stmnt.getGeneratedKeys();
				while (rs.next()) {
					quoteid = rs.getInt(1);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}

		}// if quotation
		else {
			PreparedStatement stmnt = null;
			try {
				String query = "UPDATE [Quotation]"
						+ "SET project_name = ?,customer_name =?,contact_name =?"
						+ ",address1 = ?,address2 =?"
						+ ",town_city =?,country_state =?,zip_pincode =? ,facility_type =? "
						+ ",contact_telno =?,site_telno =?,contact_email =?"
						+ ",site_email =?,customer_website =?,user_id = ?"
						+ "WHERE quotation_ref=? ;";

				dbConnect();
				stmnt = dbConn.prepareStatement(query);
				stmnt.setString(1, pQuotation.getProjectname());
				stmnt.setString(2, pQuotation.getCustomername());
				stmnt.setString(3, pQuotation.getContactname());
				stmnt.setString(4, pQuotation.getAddress1());
				stmnt.setString(5, pQuotation.getAddress2());
				stmnt.setString(6, pQuotation.getTowncity());
				stmnt.setString(7, pQuotation.getCountrystate());
				stmnt.setString(8, pQuotation.getZippincode());
				stmnt.setString(9, pQuotation.getFacilitytype());
				stmnt.setString(10, pQuotation.getContacttelno());
				stmnt.setString(11, pQuotation.getSitetelno());
				stmnt.setString(12, pQuotation.getContactemail());
				stmnt.setString(13, pQuotation.getSiteemail());
				stmnt.setString(14, pQuotation.getCustomerwebsite());
				stmnt.setInt(16, pQuotation.getId());

				stmnt.executeUpdate();
				quoteid = pQuotation.getId();
			}// try
			catch (SQLException e) {
				e.printStackTrace();
			} finally {
				dbConn = null;
				stmnt = null;
			}
		}// else
		return quoteid;
	}

	@Override
	public void saveDiscountQuotation(Quotation pQuotation, boolean flag) {

		String query = "UPDATE [Quotation]"
				+ "SET hardware_discount = ?,siteinstallation_discount =?"
				+ "WHERE quotation_ref=? ;";

		PreparedStatement stmnt = null;
		dbConnect();

		try {
			stmnt = dbConn.prepareStatement(query);
			// System.out.println("*************"+pQuotation.getSiteinstallationdiscount());
			// System.out.println("*********** "+pQuotation.getHardwarediscount());

			stmnt.setDouble(1, pQuotation.getHardwarediscount());
			stmnt.setDouble(2, pQuotation.getSiteinstallationdiscount());
			stmnt.setDouble(3, pQuotation.getId());

			// System.out.println("site installation "+pQuotation.getSiteinstallationdiscount());
			// System.out.println("hardware discount "+pQuotation.getHardwarediscount());

			stmnt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

	}

	public double[] getYearlyServiceCostRecordingOnly(int noOfsensors,
			String currency) {

		double[] list = new double[3];
		list[0] = 10;
		list[1] = 10;
		list[2] = 10;

		String query = "";

		if (currency.equals("£"))
			query = "SELECT * from [YearlyServiceOptionUK] where noofsensors>=? AND service_description=?;";
		else if (currency.equals("$"))
			query = "SELECT * from [YearlyServiceOptionUS] where noofsensors>=? AND service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setInt(1, noOfsensors);
			stmnt.setString(2, "Web site/electronic recording only service");
			// System.out.println("JUST B4  result set");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				// System.out.println("In result set");
				list[0] = rs.getDouble("peryear1year");
				list[1] = rs.getDouble("peryear3year");
				list[2] = rs.getDouble("peryear5year");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getYearlyServiceCostMonitoringandRecording(int noOfsensors,
			String currency) {

		double[] list = new double[3];
		list[0] = 10;
		list[1] = 10;
		list[2] = 10;

		String query = "";

		if (currency.equals("£"))

			query = "SELECT * from [YearlyServiceOptionUK] where noofsensors>=? AND service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [YearlyServiceOptionUS] where noofsensors>=? AND service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setInt(1, noOfsensors);
			stmnt.setString(2,
					"Combined alarm monitoring and recording service");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {

				list[0] = rs.getDouble("peryear1year");
				list[1] = rs.getDouble("peryear3year");
				list[2] = rs.getDouble("peryear5year");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getonsitesensorcalibration(int noOfsensors, String currency) {

		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "ON SITE SENSOR CALIBRATION");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getsiteinstallation(int noOfsensors, String currency)

	{

		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "WIRELESS SYSTEM INSTALLATION");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] gettraveldays(int noOfsensors, String currency) {

		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "TRAVEL DAYS ALLOWED");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getnistsensorcalibration(int noOfsensors, String currency) {

		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "NIST SENSOR CALIBRATION");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getDQIQOQDocuments(int noOfsensors, String currency) {
		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "DQ, IQ, OQ Protocol Documents");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getWebsiteserverdatabase(int noOfsensors, String currency) {
		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "Website server database setup");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public double[] getWebhostedGUI(int noOfsensors, String currency) {
		double[] list = new double[2];
		list[0] = 10;
		list[1] = 10;
		String query = "";
		if (currency.equals("£"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		else if (currency.equals("$"))

			query = "SELECT * from [OnceServiceOption] where service_description=?;";

		dbConn = null;

		PreparedStatement stmnt = null;
		try {

			dbConnect();
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, "Web hosted GUI");

			ResultSet rs = stmnt.executeQuery();
			if (rs.next()) {
				list[0] = rs.getDouble("minimum_charge");
				list[1] = rs.getDouble("unit_cost");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return list;

	}

	public void saveCoveringLetter(Quotation pQuotation) {

		String query = "UPDATE [Quotation]"
				+ "SET quotation_coveringletter = ?"
				+ "WHERE quotation_ref=? ;";
		PreparedStatement stmnt = null;
		dbConnect();
		try {

			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, pQuotation.getQuotationcoveringletter());
			stmnt.setInt(2, pQuotation.getId());

			stmnt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

	}

	@Override
	public int updateQuotation(Quotation pQuotation) {
		PreparedStatement stmnt = null;
		dbConnect();
		int result = 0;

		String query = "UPDATE [Quotation] SET [quotation_ref]=?,[quotation_coveringletter]=?,[project_name]=?,[customer_name]=?"
				+ ",[contact_name]=?,[address1]=?,[address2]=?,[town_city]=?,[country_state]=?,[zip_pincode]=?,[facility_type]=?"
				+ ",[contact_telno]=?,[site_telno]=?,[contact_email]=?,[site_email]=?,[onsite_sensorcalibration]=?"
				+ ",[factory_sensorcalibration]=?,[dqiqoq_protocoldocs]=?,[websitedb_setup]=?,[webhosted_gui]=?"
				+ ",[combinedrna_monitorying]=?,[recording_only]=?,[yearly_recalibrationservice]=?,[number_of_sites]=?"
				+ ",[temperature_mapping]=?,[individual_quotepersite]=?,[hardware_discount]=?,[siteinstallation_discount]=?"
				+ ",[serviceoption_discount]=?,[remotemonitoring_discount]=?,[user_id]=?,[creationtime]=?,[pdfcontents]=?,[emailto]=?,[revisionnumber]=?"
				+ ",[completed]=?,[accepted]=?,[emailcc]=?,[emailbcc]=?,[emailsubject]=?,[customer_salutation]=?,[update_installation]=?"
				+ ",[remote_monitoring]=?,[annual_calibration]=?,[address3]=?,[department]=?,[disable_coveringletter]=?,[hardwareonly]=?"
				+ "WHERE id=?";

		try {
			stmnt = dbConn.prepareStatement(query);
			stmnt.setString(1, pQuotation.getQuotationref());
			stmnt.setString(2, pQuotation.getQuotationcoveringletter());
			stmnt.setString(3, pQuotation.getProjectname());
			stmnt.setString(4, pQuotation.getCustomername());
			stmnt.setString(5, pQuotation.getContactname());
			stmnt.setString(6, pQuotation.getAddress1());
			stmnt.setString(7, pQuotation.getAddress2());
			stmnt.setString(8, pQuotation.getTowncity());
			stmnt.setString(9, pQuotation.getCountrystate());
			stmnt.setString(10, pQuotation.getZippincode());
			stmnt.setString(11, pQuotation.getFacilitytype());
			stmnt.setString(12, pQuotation.getContacttelno());
			stmnt.setString(13, pQuotation.getSitetelno());
			stmnt.setString(14, pQuotation.getContactemail());
			stmnt.setString(15, pQuotation.getSiteemail());
			stmnt.setBoolean(16, pQuotation.getOnsitesensorcalibration());
			stmnt.setBoolean(17, pQuotation.getFactorysensorcalibration());
			stmnt.setBoolean(18, pQuotation.getDqiqoqprotocoldocs());
			stmnt.setBoolean(19, pQuotation.getWebsitedbsetup());
			stmnt.setBoolean(20, pQuotation.getWebhostedgui());
			stmnt.setBoolean(21, pQuotation.getCombinedrnamonitoring());
			stmnt.setBoolean(22, pQuotation.getRecordingonly());
			stmnt.setBoolean(23, pQuotation.getYearlyrecalibrationservice());
			stmnt.setInt(24, pQuotation.getNumberofsites());
			stmnt.setBoolean(25, pQuotation.getTemperatureMapping());
			stmnt.setBoolean(26, pQuotation.getIndividualquotepersite());
			stmnt.setDouble(27, pQuotation.getHardwarediscount());
			stmnt.setDouble(28, pQuotation.getSiteinstallationdiscount());
			stmnt.setDouble(29, pQuotation.getServiceoptiondiscount());
			stmnt.setDouble(30, pQuotation.getRemotemonitoringdiscount());
			stmnt.setInt(31, pQuotation.getUser().getUserId());
			stmnt.setTimestamp(32, pQuotation.getCreationTime());
			stmnt.setString(33, pQuotation.getPagecontents());
			stmnt.setString(34, pQuotation.getEmailedto());
			stmnt.setInt(35, pQuotation.getRevisionnumber());
			stmnt.setBoolean(36, pQuotation.isCompleted());
			stmnt.setBoolean(37, pQuotation.isAccepted());
			stmnt.setString(38, pQuotation.getEmailcc());
			stmnt.setString(39, pQuotation.getEmailbcc());
			stmnt.setString(40, pQuotation.getEmailBody());
			stmnt.setString(41, pQuotation.getCustomersalutation());
			stmnt.setBoolean(42, pQuotation.isUpdateInstallation());
			stmnt.setDouble(43, pQuotation.getRemoteMonitoring());
			stmnt.setDouble(44, pQuotation.getAnnualCalibration());
			stmnt.setString(45, pQuotation.getAddress3());
			stmnt.setString(46, pQuotation.getDepartment());
			stmnt.setBoolean(47, pQuotation.isDisable_coveringletter());
			stmnt.setBoolean(48, pQuotation.isHardwareOnly());
			stmnt.setInt(49, pQuotation.getId());
			result = stmnt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConn = null;
			stmnt = null;
		}

		return result;
	}

	@Override
	public int deleteQuotation(Quotation quotation) {
		// TODO Auto-generated method stub
		int success = 0;
		String query = "DELETE FROM [Quotation] WHERE id = ?;";
		PreparedStatement stmnt = null;
		this.dbConnect();
		try {
			stmnt = this.dbConn.prepareStatement(query);
			stmnt.setInt(1, quotation.getId());
			success = stmnt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			dbConn = null;
			if (stmnt != null) {
				try {
					stmnt.close();
					stmnt = null;
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return success;
	}
}
