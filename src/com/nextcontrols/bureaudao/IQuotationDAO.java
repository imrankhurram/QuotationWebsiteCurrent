package com.nextcontrols.bureaudao;

import java.util.Date;
import java.util.List;

import com.nextcontrols.bureaudomain.Quotation;
import com.nextcontrols.bureaudomain.StandardParts;

public interface IQuotationDAO {

	public void saveQuotation(Quotation pQuotation,
			List<List<StandardParts>> pStandardPartsCollection,
			List<String> pSiteNames, List<String> pSiteInstallation,
			List<String> pInstallationTravel, List<String> pSiteCalibration,
			List<String> pCalibartionTravel,
			List<String> pAnnualSiteCalibration,
			List<String> pAnnualDeadTravelDays, String pPdfContents,
			String pEmailedTo);

	public int saveUpdateQuote(int quotationId, Quotation pQuotation,
			String pPdfContents, String pEmailedTo, boolean isCompleted);

	public void saveParts(int quoteId,
			List<List<StandardParts>> pStandardPartsCollection,
			List<String> pSiteNames, List<String> pSiteInstallation,
			List<String> pInstallationTravel, List<String> pSiteCalibration,
			List<String> pCalibartionTravel,
			List<String> pAnnualSiteCalibration,
			List<String> pAnnualDeadTravelDays);
	public List<List<String>> getPartsCost(int quoteId);
	
	public List<Quotation> getQuotations(int pUserID, int pCompanyID,
			String pUserType, Date pDateFrom, Date pDateTo, boolean isCompleted);

	public int getMaxQuotationID(int pUserID);

	public int saveQuotation(Quotation pQuotation, boolean pageNumber);

	public int deleteQuotation(Quotation quotation);

	public int updateQuotation(Quotation pQuotation);

	public void saveQuantitySP(Quotation q, List parts, boolean flag);

	public void saveQuantityNonSP(Quotation q, List parts, boolean flag);

	public void saveDiscountQuotation(Quotation pQuotation, boolean flag);

	public double[] getYearlyServiceCostRecordingOnly(int noOfsensors,
			String currency);

	public double[] getYearlyServiceCostMonitoringandRecording(int noOfsensors,
			String currency);

	public double[] getonsitesensorcalibration(int noOfsensors, String currency);

	public double[] getsiteinstallation(int noOfsensors, String currency);

	public double[] gettraveldays(int noOfsensors, String currency);

	public double[] getnistsensorcalibration(int noOfsensors, String currency);

	public double[] getDQIQOQDocuments(int noOfsensors, String currency);

	public double[] getWebsiteserverdatabase(int noOfsensors, String currency);

	public double[] getWebhostedGUI(int noOfsensors, String currency);

	public void saveCoveringLetter(Quotation pQuotation);
}
