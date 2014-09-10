package com.nextcontrols.pagebeans;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lowagie.text.DocumentException;
import com.nextcontrols.bureaudao.IExporter;
import com.nextcontrols.bureaudomain.StandardParts;


@ManagedBean(name="xlsexp")
@SessionScoped
public class XLSExporterPageBean implements Serializable, IExporter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String pageTitle;
	private  List<String> columnValues;
	private  List<String> columnValuesForShowParts;
	private Set<String> set;
	private Map<String,Float> tabColumnsWithSize;
	private Map<String,Float> tabColumnsWithSizeForShowParts;
	private String redirectTo;
	private String fileName;
	public XLSExporterPageBean() {
		this.pageTitle="";
		this.columnValues = new ArrayList<String>();
		this.columnValuesForShowParts = new ArrayList<String>();
		this.set = new LinkedHashSet<String>();
		this.tabColumnsWithSize= new LinkedHashMap<String, Float>();
		this.tabColumnsWithSizeForShowParts= new LinkedHashMap<String, Float>();
		this.redirectTo="";
		this.fileName="";
		///
		this.tabColumnsWithSize.put("Part Number", 60f);
		this.tabColumnsWithSize.put("Part Description", 130f);
		this.tabColumnsWithSize.put("Part Type", 60f);
		this.tabColumnsWithSize.put("Part Cost ", 30f);
		this.tabColumnsWithSize.put("Base Cost", 30f);
		this.tabColumnsWithSize.put("Country", 60f);
		
		///
		this.tabColumnsWithSizeForShowParts.put("Part Number", 60f);
		this.tabColumnsWithSizeForShowParts.put("Part Description", 130f);
		/*this.tabColumnsWithSizeForShowParts.put("Part Type", 60f);
		this.tabColumnsWithSizeForShowParts.put("Part Cost ", 30f);
		this.tabColumnsWithSizeForShowParts.put("Base Cost", 30f);
		this.tabColumnsWithSizeForShowParts.put("Country", 60f);*/
		this.tabColumnsWithSizeForShowParts.put("Quantity", 30f);
	}

	@Override
	public void export() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		//
		ExternalContext ectx = facesContext.getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		PartsPageBean partsBean = (PartsPageBean)session.getAttribute("parts");
		List<StandardParts> standarpartsList = partsBean.getStandarpartsList();
		
		
		for(int i=0; i<standarpartsList.size(); i++) {
			if(standarpartsList.get(i).getPartType().compareToIgnoreCase("Assembly")==0){
				StandardParts stP = standarpartsList.get(i);
				for(int j=0; j<stP.getSubpartsList().size(); j++) {
					this.columnValues.add(stP.getSubpartsList().get(j).getPartNumber());
					this.columnValues.add(stP.getSubpartsList().get(j).getPartDescription());
					this.columnValues.add(stP.getSubpartsList().get(j).getPartType());
					this.columnValues.add("£"+stP.getSubpartsList().get(j).getCost());
					this.columnValues.add("_");
					this.columnValues.add(stP.getSubpartsList().get(j).getCountry());
					
				}
				this.columnValues.add(standarpartsList.get(i).getPartNumber());
				this.columnValues.add(standarpartsList.get(i).getPartDescription());
				this.columnValues.add(standarpartsList.get(i).getPartType());
				this.columnValues.add(" ");
				this.columnValues.add("£"+standarpartsList.get(i).getCost());
				this.columnValues.add(standarpartsList.get(i).getCountry());

				this.columnValues.add(" ");
				this.columnValues.add(" ");
				this.columnValues.add(" ");
				this.columnValues.add(" ");
				this.columnValues.add(" ");
				this.columnValues.add(" ");
			}else{
				this.columnValues.add(standarpartsList.get(i).getPartNumber());
				this.columnValues.add(standarpartsList.get(i).getPartDescription());
				this.columnValues.add(standarpartsList.get(i).getPartType());
				this.columnValues.add(" ");
				this.columnValues.add("£"+standarpartsList.get(i).getCost());
				this.columnValues.add(standarpartsList.get(i).getCountry());
			}
			}
		//
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		HSSFRow row =null;
		HSSFCell cell=null;
		set = tabColumnsWithSize.keySet();
		Iterator<String> itr  = set.iterator();
		int i=0;
		row =  sheet.createRow(0);
		while(itr.hasNext()) {
			String itrVal = itr.next();
			cell= row.createCell(i);
			cell.setCellValue(new HSSFRichTextString(itrVal));
			i++;
		}
		Iterator<String> itr1 = columnValues.iterator();
		int j=1;
		int k=0;
		HSSFRow row1=null;
		HSSFCell cell1 =null;
		while(itr1.hasNext()) {
			String itrVal = itr1.next();
			if(k==0) {
			row1 = sheet.createRow(j);
			}
			cell1 = row1.createCell(k);
			cell1.setCellValue(new HSSFRichTextString(itrVal));
			if(k==set.size()-1){ 
				j++;
				k=0;
			}
			else
			k++;
			
		}	
        ByteArrayOutputStream os =null; 
		try {
			os = new ByteArrayOutputStream();
			workBook.write(os);
			writeToResponse((HttpServletResponse)facesContext.getExternalContext().getResponse(),os, getFileName());
			facesContext.responseComplete();	

		}
		catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	@Override
	public void exportShowParts(boolean showParts){
//		System.out.println("showParts "+showParts);
		this.columnValuesForShowParts.clear();
		FacesContext facesContext = FacesContext.getCurrentInstance();
		//
		ExternalContext ectx = facesContext.getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		QuotationsPageBean quotationBean = (QuotationsPageBean)session.getAttribute("quotations");
		UserInfoPageBean userBean = (UserInfoPageBean) session.getAttribute("userInfo");
		List<StandardParts> standarpartsList = quotationBean.getSelectedQuoteParts();
		List<StandardParts> assemblyList=new ArrayList<StandardParts>();
		List<StandardParts> partList=new ArrayList<StandardParts>();
		
		for(int i=0; i<standarpartsList.size(); i++) {
			if(standarpartsList.get(i).getPartType().compareToIgnoreCase("Part")==0){
				partList.add(standarpartsList.get(i));
			}
			if(standarpartsList.get(i).getPartType().compareToIgnoreCase("Assembly")==0){
				assemblyList.add(standarpartsList.get(i));
			}
				
		}
		if(!showParts){
		for(int i=0;i<assemblyList.size();i++){
//			System.out.println("part number::"+assemblyList.get(i).getPartNumber());
			this.columnValuesForShowParts.add(assemblyList.get(i).getPartNumber());
			this.columnValuesForShowParts.add(assemblyList.get(i).getPartDescription());
			this.columnValuesForShowParts.add(""+assemblyList.get(i).getQuoteQuantity());
		}
		}else{
			for(int i=0;i<assemblyList.size();i++){
				List<StandardParts> subParts=assemblyList.get(i).getSubpartsList();
				for(int j=0;j<subParts.size();j++){
//					System.out.println("sub part name::"+subParts.get(j).getPartNumber());
					this.columnValuesForShowParts.add(subParts.get(j).getPartNumber());
					this.columnValuesForShowParts.add(subParts.get(j).getPartDescription());
					this.columnValuesForShowParts.add(""+(subParts.get(j).getQuantity()*assemblyList.get(i).getQuoteQuantity()));	
				}
				this.columnValuesForShowParts.add("");
				this.columnValuesForShowParts.add("");
				this.columnValuesForShowParts.add("");
				
			}
			
		}
		if(!assemblyList.isEmpty()){
			this.columnValuesForShowParts.add("");
			this.columnValuesForShowParts.add("");
			this.columnValuesForShowParts.add("");
		}
		
		for(int i=0;i<partList.size();i++){	
			this.columnValuesForShowParts.add(partList.get(i).getPartNumber());
			this.columnValuesForShowParts.add(partList.get(i).getPartDescription());
			this.columnValuesForShowParts.add(""+partList.get(i).getQuoteQuantity());
		}
		//
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = workBook.createSheet();
		HSSFRow rowHeader1 =sheet.createRow(0);
		HSSFCell cellHeader11=rowHeader1.createCell(0);
		cellHeader11.setCellValue(new HSSFRichTextString("Quote Reference:"));
		HSSFCell cellHeader12=rowHeader1.createCell(1);
		cellHeader12.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getQuotationref()));
		
		HSSFRow rowHeader2 =sheet.createRow(1);
		HSSFCell cellHeader21=rowHeader2.createCell(0);
		cellHeader21.setCellValue(new HSSFRichTextString("Project Name:"));
		HSSFCell cellHeader22=rowHeader2.createCell(1);
		cellHeader22.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getProjectname()));
		
		HSSFRow rowHeader3 =sheet.createRow(2);
		HSSFCell cellHeader31=rowHeader3.createCell(0);
		cellHeader31.setCellValue(new HSSFRichTextString("Customer Name:"));
		HSSFCell cellHeader32=rowHeader3.createCell(1);
		cellHeader32.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getCustomername()));
		
		HSSFRow row =null;
		HSSFCell cell=null;
		set = tabColumnsWithSizeForShowParts.keySet();
		Iterator<String> itr  = set.iterator();
		int i=0;
		row =  sheet.createRow(4);
		while(itr.hasNext()) {
			String itrVal = itr.next();
			cell= row.createCell(i);
			cell.setCellValue(new HSSFRichTextString(itrVal));
			i++;
		}
		Iterator<String> itr1 = columnValuesForShowParts.iterator();
		int j=5;
		int k=0;
		HSSFRow row1=null;
		HSSFCell cell1 =null;
		while(itr1.hasNext()) {
			String itrVal = itr1.next();
			if(k==0) {
			row1 = sheet.createRow(j);
			}
			cell1 = row1.createCell(k);
			cell1.setCellValue(new HSSFRichTextString(itrVal));
			if(k==set.size()-1){ 
				j++;
				k=0;
			}
			else
			k++;
			
		}
		DecimalFormat formater;
		formater = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		DecimalFormatSymbols symbols = formater.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formater.setDecimalFormatSymbols(symbols);
		formater.setMaximumFractionDigits(0);
		
//		j++;
//		j++;
		HSSFRow rowHeaderQuoteSummary =sheet.createRow(j++);
		HSSFCell cellHeaderQuoteSummary=rowHeaderQuoteSummary.createCell(0);
		cellHeaderQuoteSummary.setCellValue(new HSSFRichTextString("Quotation Summary:"));
		
		HSSFRow rowHeaderHardwareCost =sheet.createRow(j++);
		HSSFCell cellHeaderHardwareCost=rowHeaderHardwareCost.createCell(0);
		cellHeaderHardwareCost.setCellValue(new HSSFRichTextString("Hardware Cost"));
		HSSFCell cellHeaderHardwareCostValue=rowHeaderHardwareCost.createCell(1);
		cellHeaderHardwareCostValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getTotalHardwareCost())));
		
		HSSFRow rowHeaderHardwareDiscount =sheet.createRow(j++);
		HSSFCell cellHeaderHardwareDiscount=rowHeaderHardwareDiscount.createCell(0);
		cellHeaderHardwareDiscount.setCellValue(new HSSFRichTextString("Agreed Discount"));
		HSSFCell cellHeaderHardwareDiscountValue=rowHeaderHardwareDiscount.createCell(1);
		cellHeaderHardwareDiscountValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getTotalHardwareDiscount())));
		HSSFCell cellHeaderHardwareDiscountPercent=rowHeaderHardwareDiscount.createCell(2);
		cellHeaderHardwareDiscountPercent.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getHardwarediscount()+"%"));

		HSSFRow rowHeaderHardwareFinal =sheet.createRow(j++);
		HSSFCell cellHeaderHardwareFinal=rowHeaderHardwareFinal.createCell(0);
		cellHeaderHardwareFinal.setCellValue(new HSSFRichTextString("Final Price"));
		HSSFCell cellHeaderHardwareFinalValue=rowHeaderHardwareFinal.createCell(1);
		cellHeaderHardwareFinalValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format( quotationBean.getTotalHardwareCost()-quotationBean.getTotalHardwareDiscount())));
		
		j++;
		
		HSSFRow rowHeaderServiceCost =sheet.createRow(j++);
		HSSFCell cellHeaderServiceCost=rowHeaderServiceCost.createCell(0);
		cellHeaderServiceCost.setCellValue(new HSSFRichTextString("Service cost"));
		HSSFCell cellHeaderServiceCostValue=rowHeaderServiceCost.createCell(1);
		cellHeaderServiceCostValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getTotalServiceCost())));
		
		HSSFRow rowHeaderServiceDiscount =sheet.createRow(j++);
		HSSFCell cellHeaderServiceDiscount=rowHeaderServiceDiscount.createCell(0);
		cellHeaderServiceDiscount.setCellValue(new HSSFRichTextString("Agreed Discount"));
		HSSFCell cellHeaderServiceDiscountValue=rowHeaderServiceDiscount.createCell(1);
		cellHeaderServiceDiscountValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getTotalServiceDiscount())));
		HSSFCell cellHeaderServiceDiscountPercent=rowHeaderServiceDiscount.createCell(2);
		cellHeaderServiceDiscountPercent.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getServiceoptiondiscount()+"%"));

		HSSFRow rowHeaderServiceFinal =sheet.createRow(j++);
		HSSFCell cellHeaderServiceFinal=rowHeaderServiceFinal.createCell(0);
		cellHeaderServiceFinal.setCellValue(new HSSFRichTextString("Final Price"));
		HSSFCell cellHeaderServiceFinalValue=rowHeaderServiceFinal.createCell(1);
		cellHeaderServiceFinalValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format( quotationBean.getTotalServiceCost()-quotationBean.getTotalServiceDiscount())));
		
		j++;
		
		HSSFRow rowHeaderInstallationCost =sheet.createRow(j++);
		HSSFCell cellHeaderInstallationCost=rowHeaderInstallationCost.createCell(0);
		cellHeaderInstallationCost.setCellValue(new HSSFRichTextString("Installation cost"));
		HSSFCell cellHeaderInstallationCostValue=rowHeaderInstallationCost.createCell(1);
		cellHeaderInstallationCostValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getInstallationCost())));
		
		HSSFRow rowHeaderInstallationDiscount =sheet.createRow(j++);
		HSSFCell cellHeaderInstalltionDiscount=rowHeaderInstallationDiscount.createCell(0);
		cellHeaderInstalltionDiscount.setCellValue(new HSSFRichTextString("Agreed Discount"));
		HSSFCell cellHeaderInstallationDiscountValue=rowHeaderInstallationDiscount.createCell(1);
		cellHeaderInstallationDiscountValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getInstallationDiscount())));
		HSSFCell cellHeaderInstallationDiscountPercent=rowHeaderInstallationDiscount.createCell(2);
		cellHeaderInstallationDiscountPercent.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getSiteinstallationdiscount()+"%"));

		HSSFRow rowHeaderInstallationFinal =sheet.createRow(j++);
		HSSFCell cellHeaderInstallationFinal=rowHeaderInstallationFinal.createCell(0);
		cellHeaderInstallationFinal.setCellValue(new HSSFRichTextString("Final Price"));
		HSSFCell cellHeaderInstallationFinalValue=rowHeaderInstallationFinal.createCell(1);
		cellHeaderInstallationFinalValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format( quotationBean.getInstallationCost()-quotationBean.getInstallationDiscount())));
		
		j++;
		
		HSSFRow rowHeaderMonitoringCost =sheet.createRow(j++);
		HSSFCell cellHeaderMonitoringCost=rowHeaderMonitoringCost.createCell(0);
		cellHeaderMonitoringCost.setCellValue(new HSSFRichTextString("Monitoring cost"));
		HSSFCell cellHeaderMonitoringCostValue=rowHeaderMonitoringCost.createCell(1);
		cellHeaderMonitoringCostValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getAlarmingnmonitoring1y())));
		
		HSSFRow rowHeaderMonitoringDiscount =sheet.createRow(j++);
		HSSFCell cellHeaderMonitoringDiscount=rowHeaderMonitoringDiscount.createCell(0);
		cellHeaderMonitoringDiscount.setCellValue(new HSSFRichTextString("Agreed Discount"));
		HSSFCell cellHeaderMonitoringDiscountValue=rowHeaderMonitoringDiscount.createCell(1);
		cellHeaderMonitoringDiscountValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getAlarmingnmonitoring1yDiscount())));
		HSSFCell cellHeaderMonitoringDiscountPercent=rowHeaderMonitoringDiscount.createCell(2);
		cellHeaderMonitoringDiscountPercent.setCellValue(new HSSFRichTextString(quotationBean.getSelectedQuotation().getRemotemonitoringdiscount()+"%"));

		HSSFRow rowHeaderMonitoringFinal =sheet.createRow(j++);
		HSSFCell cellHeaderMonitoringFinal=rowHeaderMonitoringFinal.createCell(0);
		cellHeaderMonitoringFinal.setCellValue(new HSSFRichTextString("Final Price"));
		HSSFCell cellHeaderMonitoringFinalValue=rowHeaderMonitoringFinal.createCell(1);
		cellHeaderMonitoringFinalValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format( quotationBean.getAlarmingnmonitoring1y()-quotationBean.getAlarmingnmonitoring1yDiscount())+" Per Yr"));

		j++;
		
		HSSFRow rowHeaderCalibrationCost =sheet.createRow(j++);
		HSSFCell cellHeaderCalibrationCost=rowHeaderCalibrationCost.createCell(0);
		cellHeaderCalibrationCost.setCellValue(new HSSFRichTextString("Calibration cost"));
		HSSFCell cellHeaderCalibrationCostValue=rowHeaderCalibrationCost.createCell(1);
		cellHeaderCalibrationCostValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format(quotationBean.getCalibrationCost())));
		
		HSSFRow rowHeaderCalibrationDiscount =sheet.createRow(j++);
		HSSFCell cellHeaderCalibrationDiscount=rowHeaderCalibrationDiscount.createCell(0);
		cellHeaderCalibrationDiscount.setCellValue(new HSSFRichTextString("Agreed Discount"));
		HSSFCell cellHeaderCalibrationDiscountValue=rowHeaderCalibrationDiscount.createCell(1);
		cellHeaderCalibrationDiscountValue.setCellValue(new HSSFRichTextString(""));
		HSSFCell cellHeaderCalibrationDiscountPercent=rowHeaderCalibrationDiscount.createCell(2);
		cellHeaderCalibrationDiscountPercent.setCellValue(new HSSFRichTextString("0%"));

		HSSFRow rowHeaderCalibrationFinal =sheet.createRow(j++);
		HSSFCell cellHeaderCalibrationFinal=rowHeaderCalibrationFinal.createCell(0);
		cellHeaderCalibrationFinal.setCellValue(new HSSFRichTextString("Final Price"));
		HSSFCell cellHeaderCalibrationFinalValue=rowHeaderCalibrationFinal.createCell(1);
		cellHeaderCalibrationFinalValue.setCellValue(new HSSFRichTextString(userBean.getUser().getCompany().getCountry().getCurrency()+ formater.format( quotationBean.getCalibrationCost())+" Per Yr"));

		
        ByteArrayOutputStream os =null; 
		try {
			os = new ByteArrayOutputStream();
			workBook.write(os);
			writeToResp((HttpServletResponse)facesContext.getExternalContext().getResponse(),os,quotationBean.getSelectedQuotation().getQuotationref() );
			facesContext.responseComplete();	

		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	@Override
	public void writeToResponse(HttpServletResponse response,  ByteArrayOutputStream baos, String fileName ) throws IOException, DocumentException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must revalidate, post-check=0, pre-check=0");
		response.setHeader("pragma", "public");
		response.setHeader("Content-disposition", "attachment;filename=MasterPartsList "+dateFormat.format(Calendar.getInstance().getTime())+".xls");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream(); 
		baos.writeTo(out);
		baos.flush();
	}
	
	public void writeToResp(HttpServletResponse response,  ByteArrayOutputStream baos, String fileName ) throws IOException, DocumentException {
        response.setContentType("application/vnd.ms-excel");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must revalidate, post-check=0, pre-check=0");
		response.setHeader("pragma", "public");
		response.setHeader("Content-disposition", "attachment;filename="+fileName+".xls");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream(); 
		baos.writeTo(out);
		baos.flush();
	}
	
	
	@Override
	public String pageRedirect() {
		this.columnValues=null;
		this.tabColumnsWithSize=null;
		this.set=null;
		return this.redirectTo;
	}
	public void setRedirectTo(String redirectTo) {
		this.redirectTo=redirectTo;
	}
	public String getRedirectTo() {
		return redirectTo;
	}
	public void setFileName(String fileName) {
       this.fileName=fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle= pageTitle;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setColumnValues(List<String> columnValues) {
		this.columnValues=columnValues;
	}
	public List<String> getColumnValues() {
		return columnValues;
	}

	public List<String> getColumnValuesForShowParts() {
		return columnValuesForShowParts;
	}

	public void setColumnValuesForShowParts(List<String> columnValuesForShowParts) {
		this.columnValuesForShowParts = columnValuesForShowParts;
	}

	public void setTabColumnsWithSize(Map<String,Float> tabColumnsWithSize) {
		this.tabColumnsWithSize= tabColumnsWithSize;
	}
	public Map<String, Float> getTabColumnsWithSize() {
		return tabColumnsWithSize;
	}

	public Map<String, Float> getTabColumnsWithSizeForShowParts() {
		return tabColumnsWithSizeForShowParts;
	}

	public void setTabColumnsWithSizeForShowParts(
			Map<String, Float> tabColumnsWithSizeForShowParts) {
		this.tabColumnsWithSizeForShowParts = tabColumnsWithSizeForShowParts;
	}



}
