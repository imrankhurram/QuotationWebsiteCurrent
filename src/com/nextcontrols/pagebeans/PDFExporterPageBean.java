package com.nextcontrols.pagebeans;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.nextcontrols.bureaudao.IExporter;
import com.nextcontrols.bureaudomain.OnceService;
import com.nextcontrols.bureaudomain.PriceCategory;
import com.nextcontrols.bureaudomain.QuantityCategory;
import com.nextcontrols.utility.Email;
import com.nextcontrols.utility.ServiceProperties;
@ManagedBean(name="pdfExp")
@SessionScoped
public class PDFExporterPageBean implements Serializable, IExporter {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String fileName;
	private  String pageTitle;
	private  List<String> columnValues ;
	private  Set<String> set;
	private  Map<String, String> headerContent ;
	private  Map<String,Float> tabColumnsWithSize ;
	private  String logo;
	private  String redirectTo;
	private  String emptyMsg;
	DecimalFormat intFormat = new DecimalFormat("#");
	NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
	Font contentFont = new Font(Font.TIMES_ROMAN, 9,Font.BOLD);
	Font titleFont = new Font(Font.TIMES_ROMAN, 12,Font.BOLD);
	Font titleFontUnderLined = new Font(Font.TIMES_ROMAN, 12,Font.BOLD);
	Color cell_background_color=new Color(255,255,255);
    PdfPCell table_cell;
    PdfPTable my_first_table;
	FacesContext facesContext ;
	Document pdf;
	ByteArrayOutputStream baos ;
	
	public PDFExporterPageBean() {
		this.fileName="";
		this.pageTitle="";
		this.columnValues= new ArrayList<String>();
		this.set= new LinkedHashSet<String>();
		this.headerContent= new LinkedHashMap<String, String>();
		this.tabColumnsWithSize= new LinkedHashMap<String, Float>();
		tabColumnsWithSize.put("22", Float.valueOf("22"));
		tabColumnsWithSize.put("33", Float.valueOf("33"));
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		this.logo=context.getRealPath("") + File.separator+ "images" + File.separator + "Bio Tech And Life Sciences1.jpg";;
		this.redirectTo="DisplaynEditPage.xhtml?faces-redirect=true";
		this.emptyMsg="";
		contentFont.setColor(Color.BLACK);
		titleFont.setColor(Color.decode("0x41918A"));
		titleFontUnderLined.setColor(Color.decode("0x41918A"));
		titleFontUnderLined.setStyle("underline");
	}
	/**
	 * This method generates a PDF report 
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */
	@Override
	public void export() throws DocumentException, MalformedURLException, IOException, ParseException{
		facesContext = FacesContext.getCurrentInstance();
		ExternalContext ectx = facesContext.getExternalContext();
		HttpSession session = (HttpSession)ectx.getSession(false);
		QuotationCreationPageBean quotationBean = (QuotationCreationPageBean)session.getAttribute("newquote");
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");
		if(quotationBean != null){
			this.fileName = quotationBean.getQuotation().getQuotationref();
			this.pageTitle= quotationBean.getQuotation().getQuotationref();
		}
			
		//FacesContext facesContext = FacesContext.getCurrentInstance();
		pdf= new Document(PageSize.A4);
        baos = new ByteArrayOutputStream();
       	PdfWriter writer = PdfWriter.getInstance(pdf, baos);
		pdf.open();
		
		pdf.newPage();
		PdfContentByte under = writer.getDirectContentUnder();
		Image imageMainPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"1.jpg");
		imageMainPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imageMainPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imageMainPage.setAbsolutePosition(0, 0);
		under.addImage(imageMainPage);
				
		pdf.newPage();
		Image imageSecondPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"2.jpg");
		imageSecondPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imageSecondPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imageSecondPage.setAbsolutePosition(0, 0);
		under.addImage(imageSecondPage);
		
		HTMLWorker htmlWorker = new HTMLWorker(pdf);
		pdf.add(Chunk.NEWLINE );
	    pdf.add(Chunk.NEWLINE );
		htmlWorker.parse(new StringReader(quotationBean.getQuotation().getQuotationcoveringletter()));
		
		pdf.newPage();
		Image imageThirdPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"3.jpg");
		imageThirdPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imageThirdPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imageThirdPage.setAbsolutePosition(0, 0);
		under.addImage(imageThirdPage);
		
		pdf.add(Chunk.NEWLINE );
		Paragraph warpOptionPara = new Paragraph();
		warpOptionPara.setSpacingAfter(05);
		warpOptionPara.setSpacingBefore(200);
		warpOptionPara.setAlignment(Element.ALIGN_RIGHT);
		warpOptionPara.setIndentationLeft(50);
		warpOptionPara.setIndentationRight(50);
		warpOptionPara.add(new Paragraph("WARP (Wireless alarm receiver panels)", titleFont));
		warpOptionPara.add(new Paragraph("AC powered."));
		warpOptionPara.add(new Paragraph("Max capacity 254 sensing channels."));
		warpOptionPara.add(new Paragraph("Battery standby operation up to 12 hours."));
		warpOptionPara.add(new Paragraph("Local visual/audible alarm/alert notification."));
		warpOptionPara.add(new Paragraph("Scrolling colour display."));
		warpOptionPara.add(new Paragraph("Web browser access via local Ethernet port."));
		warpOptionPara.add(new Paragraph("Local operator audit trail."));
		warpOptionPara.add(new Paragraph("Local settings change activity audit trail."));
		warpOptionPara.add(new Paragraph("Local web browser access to graph plots."));
		warpOptionPara.add(new Paragraph("Local web page uploading of log records."));
		warpOptionPara.add(new Paragraph("250Gbyte local record storage (30 years)."));
		warpOptionPara.add(new Paragraph("External beacon/sounder connection."));
		warpOptionPara.add(new Paragraph("Self-healing Mi-Wi meesh communications network."));
		warpOptionPara.add(new Paragraph("24 Months return to manufactor warranty."));
		warpOptionPara.add(new Paragraph("Wireless sensors and transmitter modules", titleFont));
		warpOptionPara.add(new Paragraph("External 10K3A NTC Thermistor temperature sensor option."));
		warpOptionPara.add(new Paragraph("External PT1000 RTD temperature sensor option."));
		warpOptionPara.add(new Paragraph("Mapping temperature sensor option."));
		warpOptionPara.add(new Paragraph("O2, CO2 gas sensing option."));
		warpOptionPara.add(new Paragraph("Pressure and differential pressure sensing option."));
		warpOptionPara.add(new Paragraph("Alarm contact and door contact monitoring option."));
		warpOptionPara.add(new Paragraph("7 day onboard local memory storage."));
		warpOptionPara.add(new Paragraph("Temperature sensing ranges from -200C to +200C."));
		warpOptionPara.add(new Paragraph("Temperature accuracy +/- 0.3C."));
		warpOptionPara.add(new Paragraph("Humidity 0 - 100%RH, 1.8% accuracy."));
		warpOptionPara.add(new Paragraph("NIST calibration traceability."));
		warpOptionPara.add(new Paragraph("Self-healing Mi-Wi mesh communications network."));
		warpOptionPara.add(new Paragraph("24 months return to manufacturer warranty."));
		warpOptionPara.add(new Paragraph("Wireless signal repeater modules", titleFont));
		warpOptionPara.add(new Paragraph("AC powered."));
		warpOptionPara.add(new Paragraph("Battery standby operation up to 21 hours."));
		warpOptionPara.add(new Paragraph("Self-healing Mi-Wi mesh communications network."));
		//warpOptionPara.add(new Paragraph("Up to 8 signal repeaters per WARP."));
		pdf.add(warpOptionPara);
		
		pdf.newPage();
		Image imageFourthPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"4.jpg");
		
		imageFourthPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imageFourthPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imageFourthPage.setAbsolutePosition(0, 0);
		under.addImage(imageFourthPage);
		pdf.add(Chunk.NEWLINE );
		pdf.add(Chunk.NEWLINE );
		Paragraph summaryPara = new Paragraph();
		summaryPara.setSpacingAfter(05);
		summaryPara.setSpacingBefore(205);
		summaryPara.setAlignment(Element.ALIGN_RIGHT);
		summaryPara.setIndentationLeft(50);
		summaryPara.setIndentationRight(50);
		Paragraph sysConf = new Paragraph("Your System Configuration", titleFont);
		sysConf.setAlignment(Element.ALIGN_LEFT);
		summaryPara.add(sysConf);
		summaryPara.add(Chunk.NEWLINE);
              
		for(QuantityCategory qc :quotationBean.getQuantityCategoriesList()){
			summaryPara.add(new Paragraph(qc.getDescription()+" - "+qc.getTempquantity()));
		}
		summaryPara.add(new Paragraph("Total Sensor Count"+" : "+quotationBean.getTotalSensorCount()));
		pdf.add(summaryPara);
		
		Paragraph serviceOptionPara = new Paragraph();
		serviceOptionPara.setSpacingAfter(05);
		serviceOptionPara.setSpacingBefore(05);
		serviceOptionPara.setAlignment(Element.ALIGN_RIGHT);
		serviceOptionPara.setIndentationLeft(50);
		serviceOptionPara.setIndentationRight(50);
		Paragraph chosenService = new Paragraph("Your Chosen Service Options", titleFont);
		chosenService.setAlignment(Element.ALIGN_LEFT);
		serviceOptionPara.add(chosenService);
		serviceOptionPara.add(Chunk.NEWLINE);
		
		serviceOptionPara.add(new Paragraph("Remote Ethernet Communications Link"+" : "+"Yes"));
		serviceOptionPara.add(new Paragraph("On-site system installation"+" : "+"Yes"));
		serviceOptionPara.add(new Paragraph("On-Site NIST/NPL Sensor Calibration"+" : "+(quotationBean.getQuotation().getOnsitesensorcalibration() ? "Yes" : "No")));
		serviceOptionPara.add(new Paragraph("Factory NIST/NPL Sensor Calibration"+" : "+(quotationBean.getQuotation().getFactorysensorcalibration() ? "Yes" : "No")));
		serviceOptionPara.add(new Paragraph("Remote Alarm Monitoring Service"+" : "+(quotationBean.getQuotation().getCombinedrnamonitoring() ? "Yes" : "No")));
		serviceOptionPara.add(new Paragraph("Remote Electronic Recording Service"+" : "+(quotationBean.getQuotation().getRecordingonly() ? "Yes" : "No")));
		serviceOptionPara.add(new Paragraph("Web Hosted Sensor Display Graphics (Web GUI)"+" : "+(quotationBean.getQuotation().getWebhostedgui() ? "Yes" : "No")));
		serviceOptionPara.add(new Paragraph("DQ, IQ and OQ, QC Record Documentation"+" : "+(quotationBean.getQuotation().getDqiqoqprotocoldocs() ? "Yes" : "No")));
		serviceOptionPara.add(new Paragraph("Refrigerator Temperature Mapping service"+" : "+(quotationBean.getQuotation().getTemperatureMapping() ? "Yes" : "No")));
		pdf.add(serviceOptionPara);

		pdf.newPage();
		Image imageFifthPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"5.jpg");
		
		imageFifthPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imageFifthPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imageFifthPage.setAbsolutePosition(0, 0);
		under.addImage(imageFifthPage);
		pdf.add(Chunk.NEWLINE );
		pdf.add(Chunk.NEWLINE );
		Paragraph systemBreakdownPara = new Paragraph();
		systemBreakdownPara.setSpacingAfter(05);
		systemBreakdownPara.setSpacingBefore(205);
		systemBreakdownPara.setAlignment(Element.ALIGN_RIGHT);
		systemBreakdownPara.setIndentationLeft(50);
		systemBreakdownPara.setIndentationRight(50);
		Paragraph sysCostBreakdown = new Paragraph("System Cost Breakdown", titleFont);
		sysCostBreakdown.setAlignment(Element.ALIGN_LEFT);
		systemBreakdownPara.add(sysCostBreakdown);
		systemBreakdownPara.add(new Paragraph("Hardware", titleFontUnderLined));
		//systemBreakdownPara.add(Chunk.NEWLINE);
        
		my_first_table = new PdfPTable(2);
        my_first_table.setWidthPercentage(588 / 5.23f);
        my_first_table.setWidths(new int[]{4, 1});
        my_first_table.setHorizontalAlignment(Element.ALIGN_RIGHT);
		for(PriceCategory pc :quotationBean.getPriceCategoriesList()){
			table_cell=new PdfPCell(new Phrase(pc.getDescription()));  
	        table_cell.setBorderColor(cell_background_color.WHITE);
	        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        my_first_table.addCell(table_cell);
	        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(pc.getTempquantity()))); 
	        table_cell.setBorderColor(cell_background_color.WHITE);
	        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        my_first_table.addCell(table_cell);
			//systemBreakdownPara.add(new Paragraph(pc.getDescription()+" - "+pc.getTempquantity()));
		}
		table_cell=new PdfPCell(new Phrase("Sub Total"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getTotalsystembreakdown()))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Less Discount Applied"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getTotalsystembreakdown()*quotationBean.getmHardwareDiscount()/100))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Total"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.getTotalsystembreakdown()-(quotationBean.getTotalsystembreakdown()*quotationBean.getmHardwareDiscount()/100))))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        systemBreakdownPara.add(my_first_table);
		pdf.add(systemBreakdownPara);
		
		Paragraph servicesPara = new Paragraph();
		servicesPara.setSpacingAfter(05);
		servicesPara.setSpacingBefore(05);
		servicesPara.setAlignment(Element.ALIGN_RIGHT);
		servicesPara.setIndentationLeft(50);
		servicesPara.setIndentationRight(50);
		servicesPara.add(new Paragraph("Services", titleFontUnderLined));
		//servicesPara.add(Chunk.NEWLINE);
        
		my_first_table = new PdfPTable(2);
        my_first_table.setWidthPercentage(588 / 5.23f);
        my_first_table.setWidths(new int[]{4, 1});
        my_first_table.setHorizontalAlignment(Element.ALIGN_RIGHT);

		for(OnceService os :quotationBean.getOnceservicesList()){
			table_cell=new PdfPCell(new Phrase(os.getServiceDescription()));  
	        table_cell.setBorderColor(cell_background_color.WHITE);
	        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        my_first_table.addCell(table_cell);
	        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(os.getUnitCost()*quotationBean.getTotalSensorCount()))); 
	        table_cell.setBorderColor(cell_background_color.WHITE);
	        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
	        my_first_table.addCell(table_cell);
			//servicesPara.add(new Paragraph(os.getServiceDescription()+" - "+os.getUnitCost()*quotationBean.getTotalSensorCount()));
		}
		table_cell=new PdfPCell(new Phrase("Sub Total"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getOnceServicestotal()))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Less Discount Applied"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getOnceServicestotal()*quotationBean.getmServiceOptionsDiscount()/100))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Total"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.getOnceServicestotal()-(quotationBean.getOnceServicestotal()*quotationBean.getmServiceOptionsDiscount()/100))))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        servicesPara.add(my_first_table);
		pdf.add(servicesPara);
		
		Paragraph installationPara = new Paragraph();
		installationPara.setSpacingAfter(05);
		installationPara.setSpacingBefore(05);
		installationPara.setAlignment(Element.ALIGN_RIGHT);
		installationPara.setIndentationLeft(50);
		installationPara.setIndentationRight(50);
		installationPara.add(new Paragraph("Installation", titleFontUnderLined));
		
		my_first_table = new PdfPTable(2);
        my_first_table.setWidthPercentage(588 / 5.23f);
        my_first_table.setWidths(new int[]{4, 1});
        my_first_table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell=new PdfPCell(new Phrase("On Site System Installation And Comissioning"));
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+(quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib()))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Sub Total"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+(quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib()))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Less Discount Applied"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(((quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib())*quotationBean.getmSiteInstallationDiscount()/100)))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase("Total"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(((quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib())-((quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib())*quotationBean.getmSiteInstallationDiscount()/100))))); 
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
		installationPara.add(my_first_table);
        pdf.add(installationPara);
		
		pdf.newPage();
		Image imagesixthPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"6.jpg");
		
		imagesixthPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imagesixthPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imagesixthPage.setAbsolutePosition(0, 0);
		under.addImage(imagesixthPage);
		pdf.add(Chunk.NEWLINE );
		pdf.add(Chunk.NEWLINE );
		Paragraph monitoringPara = new Paragraph();
		monitoringPara.setSpacingAfter(05);
		monitoringPara.setSpacingBefore(205);
		monitoringPara.setAlignment(Element.ALIGN_RIGHT);
		monitoringPara.setIndentationLeft(50);
		monitoringPara.setIndentationRight(50);
		monitoringPara.add(new Paragraph("Recurring Service Option", titleFont));
		monitoringPara.add(Chunk.NEWLINE);
	
        my_first_table = new PdfPTable(2);
        my_first_table.setWidthPercentage(588 / 5.23f);
        my_first_table.setWidths(new int[]{4, 1});
        my_first_table.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell=new PdfPCell(new Phrase("ALARM MONITORING AND RECORDING SERVICE"));
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase("Original")); 
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase("Cost"));  
        table_cell.setBorderColor(cell_background_color.WHITE);
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase("1 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
       // table_cell=new PdfPCell(new Phrase(intFormat.format(quotationBean.alarmingnmonitoring1y)+userInfo.getUser().getCompany().getCountry().getCurrency())); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.alarmingnmonitoring1y - (quotationBean.getAlarmingnmonitoring1y()*quotationBean.getmRemoteMonitoringDiscount()/100)))));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("3 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
       // table_cell=new PdfPCell(new Phrase(intFormat.format(quotationBean.alarmingnmonitoring3y)+userInfo.getUser().getCompany().getCountry().getCurrency())); 
       // table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
       // table_cell.setBorderColor(cell_background_color.WHITE);
       // my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.alarmingnmonitoring3y - (quotationBean.getAlarmingnmonitoring3y()*quotationBean.getmRemoteMonitoringDiscount()/100)))));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("5 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase(intFormat.format(quotationBean.alarmingnmonitoring5y)+userInfo.getUser().getCompany().getCountry().getCurrency())); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.alarmingnmonitoring5y - (quotationBean.getAlarmingnmonitoring5y()*quotationBean.getmRemoteMonitoringDiscount()/100)))));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        
        table_cell=new PdfPCell(new Phrase(" "));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(" ")); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(" "));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
                
        table_cell=new PdfPCell(new Phrase("RECORDING ONLY SERVICE"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase("Original")); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase("Cost"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("1 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase(intFormat.format(quotationBean.recording1y)+userInfo.getUser().getCompany().getCountry().getCurrency())); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format( (quotationBean.recording1y - (quotationBean.recording1y*quotationBean.getmRemoteMonitoringDiscount()/100)))));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("3 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase(intFormat.format(quotationBean.recording3y)+userInfo.getUser().getCompany().getCountry().getCurrency())); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.recording3y - (quotationBean.recording3y*quotationBean.getmRemoteMonitoringDiscount()/100)))));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase("5 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase(intFormat.format(quotationBean.recording5y)+userInfo.getUser().getCompany().getCountry().getCurrency())); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format((quotationBean.recording5y - (quotationBean.recording5y*quotationBean.getmRemoteMonitoringDiscount()/100)))));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(" "));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase(" ")); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(" "));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("NIST/UKAS ON SITE RE-CALIBRATION AND MAINTENANCE SERVICE"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("Cost")); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(""));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("1 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.annualnistcalibration1y))); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(""));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("3 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.annualnistcalibration3y))); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(""));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase("5 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.annualnistcalibration5y))); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
       // table_cell=new PdfPCell(new Phrase(""));
       // table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(" "));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase(" ")); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(" "));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("REFRIGERATOR TEMPERATURE MAPPING SERVICE"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("Cost")); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(""));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("1 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("")); 
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        //table_cell=new PdfPCell(new Phrase(""));
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);
        table_cell=new PdfPCell(new Phrase("3 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase("")); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(""));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase("5 Year Service Agreement"));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        //table_cell=new PdfPCell(new Phrase("")); 
        //table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //table_cell.setBorderColor(cell_background_color.WHITE);
        //my_first_table.addCell(table_cell);            
        table_cell=new PdfPCell(new Phrase(""));
        table_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table_cell.setBorderColor(cell_background_color.WHITE);
        my_first_table.addCell(table_cell);
        monitoringPara.add(my_first_table);
		pdf.add(monitoringPara);
		
		pdf.newPage();
		Image imageSeventhPage = Image.getInstance (((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + quotationBean.getQuotation().getFacilitytype()+"7.jpg");
		
		imageSeventhPage.scaleAbsoluteHeight(pdf.getPageSize().getHeight());
		imageSeventhPage.scaleAbsoluteWidth(pdf.getPageSize().getWidth());
		imageSeventhPage.setAbsolutePosition(0, 0);
		under.addImage(imageSeventhPage);
		monitoringPara.add(Chunk.NEWLINE);
		String str = "<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><font color='0x339999' style='font-family: Arial, Verdana; font-size: 10pt; font-style: normal; font-variant:normal; line-height: normal;'>"+
			  "<strong>TERMS</strong></font><div><ol><li><font face='Arial, Verdana' size='2'>Tutela systems are supplied and installed against our standard terms and conditions of sale and purchase.</font></li>"+
			  "<li><font face='Arial, Verdana' size='2'>Remote recording and alarm monitoring services are subject to our standard Remote Monitoring and Alarm service agreement which must be signed prior to the alarms monitoring service commencing.</font></li>"+
			  "<li><font face='Arial, Verdana' size='2'>All prices exclude any applicable taxes and or duty.</font></li>"+
			  "<li><font face='Arial, Verdana' size='2'>PAYMENT TERMS&nbsp;</font></li></ol>"+
			  "<font face='Arial, Verdana' size='2'><span class='Apple-tab-span' style='white-space:pre'> </span>&nbsp; Stage payment invoices will be raised forall projects over ú25000 as follows:</font></div>"+
			  "<div><ul><ul><ul><li>15% of project value on receipt of purchase order.</li>"+
			  "<li>50% of project value followingequipment delivery to site.</li><li>25% of project value on completion of installation.</li>"+
			  "<li>10% of project value on completion of training and handover.</li></ul></ul></ul>"+
			  "<span class='Apple-tab-span' style='font-family: Arial, Verdana; font-size: 10pt; white-space: pre;'>  </span>Remote recording and or remote alarm monitoring services will be invoiced either annually or&nbsp;quarterly&nbsp;in"+
			  "<span class='Apple-tab-span' tyle='white-space:pre'> </span>advance of the service provision.<span class='Apple-tab-span' style='font-family: Arial, Verdana; font-size: 10pt; white-space: pre;'>          </span></div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'> </span>Annual&nbsp;sensor recalibrationservices will be invoiced on completion of the work.</div>"+
			  "<div><span class='pple-tab-span' style='white-space:pre'>    </span>Payment of all invoices is required net 30 days following original date of invoice.</div>"+
			  "<div><ol><li>Prices exclude delivery charges.</li><li>Warranty on monitoring system hardware is 24 months following date of system handover on a return to manufacturer basis.</li>"+
			  "<li>This proposal remains valid for net 60 days following the original quotation date.</li>"+
			  "<li>The customer will be required to provide suitable AC power outlets for the following sensing equipment where applicable :</li></ol>"+
			  "<span class='Apple-tab-span' style='white-space:pre'></span>Master wireless receiver panel (WARP)</div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'></span>Wireless signal repeater modules</div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'></span>Clean room differential pressure sensors</div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'></span>Clean room airflow sensors</div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'></span>O2 gas sensors</div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'></span>CO2 gas sensors</div>"+
			  "<div><span class='Apple-tab-span' style='white-space:pre'></span>Water and air pressure sensors</div>";;/**/
		pdf.add(Chunk.NEWLINE );
	    pdf.add(Chunk.NEWLINE );
		htmlWorker.parse(new StringReader(str));
	    //XMLWorkerHelper.getInstance().parseXHtml(writer, pdf,new ByteArrayInputStream(str.getBytes());

		pdf.close();
			
	}

	public void createPDF()throws DocumentException, MalformedURLException, IOException, ParseException{
		export();
		writeToResponse(((HttpServletResponse) facesContext.getExternalContext().getResponse()), baos, fileName);
		facesContext.responseComplete();
	}

	@Override
	public void writeToResponse(HttpServletResponse response,  ByteArrayOutputStream baos, String fileName ) throws IOException, DocumentException {
		response.setContentType("application/pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must revalidate, post-check=0, pre-check=0");
		response.setHeader("pragma", "public");
		response.setHeader("Content-disposition", "attachment;filename="+getFileName()+".pdf");
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
        baos.writeTo(out);
		baos.flush();
	}
	
	public void writeToFile(String pEmail, String pQuotationRef){
		//System.out.println(pEmail);
		String subject = "Quotation Ref : "+pQuotationRef; //this will be the subject of the email
        ServiceProperties.getInstance().loadProperties();
		Properties props = ServiceProperties.getInstance().getProperties();
		Session session = Session.getDefaultInstance(props);
		StringBuilder sb = new StringBuilder();
		sb.append("Privacy and Confidentiality Notice"+
                  "This e-mail and any file attachments transmitted with it are confidential and intended solely for the personal attention of the addressee and may contain confidential and/or privileged information.  If you are not the intended recipient any unauthorised use, copying, disclosure or other access is strictly prohibited.  Senders of messages shall be taken to consent to the monitoring and recording of e-mails addressed to our employees.  The views expressed in this communication may not be those of Next Control Systems Limited.  Any representation made in this e-mail is subject to contract and should not be taken to create any legal relations.  If you have received this e-mail in error, please notify the sender and immediately delete the e-mail and all attachments."+
                  "Disclaimer"+
                  "The contents of this e-mail or any file attachments may contain viruses that could damage your own computer system.  Next Control Systems Limited will not accept liability for any damage which you sustain as a result of computer viruses introduced by this e-mail or any file attachments and you must rely upon your own virus checks for protection.  E-mail transmissions cannot be guaranteed to be secure or error free."+
                  "Next Control Systems Ltd"+
                  "6 Farnborough Business Center"+
                  "Eelmoor Road"+
                  "Farnborough, Hampshire"+
                  "GU14 7XA"+
                  "P: +44 1252406398"+
                  "F: +44 1252406401"+
                  "Registered in England.  Company Registration No. 2540171 "	);
		
         try {          
		            //construct the text body part
		            MimeBodyPart textBodyPart = new MimeBodyPart();
		           // textBodyPart.setHeader("Content-Type", "text/html");
		            textBodyPart.setText(sb.toString());
		            //now write the PDF content to the output stream
		            export();
		            byte[] bytes = baos.toByteArray();
		            //construct the pdf body part
		            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
		            MimeBodyPart pdfBodyPart = new MimeBodyPart();
		            pdfBodyPart.setDataHandler(new DataHandler(dataSource));
		            pdfBodyPart.setFileName(pQuotationRef+".pdf");
		            //construct the mime multi part
		            MimeMultipart mimeMultipart = new MimeMultipart();
		            mimeMultipart.addBodyPart(textBodyPart);
		            mimeMultipart.addBodyPart(pdfBodyPart);
		            //create the sender/recipient addresses
		            InternetAddress iaSender = new InternetAddress(ServiceProperties.getInstance().FROM);
		            //InternetAddress iaRecipient = new InternetAddress(pEmail);
		            //construct the mime message
		            MimeMessage mimeMessage = new MimeMessage(session);
		            mimeMessage.setSender(iaSender);
		            mimeMessage.setSubject(subject);
		            mimeMessage.setRecipients(Message.RecipientType.TO, pEmail);
		            mimeMessage.setContent(mimeMultipart);
		            //send off the email
		            Transport.send(mimeMessage);
		        } catch(Exception ex) {
		            ex.printStackTrace();
		        } finally {

		        	if(null != baos) {
		                try { baos.flush();baos = null; }
		                catch(Exception ex) { }
		            }
		        }
	}
	
	private String getDisclaimer() {

		String tDisclaimer = "";
		InputStream stream = Email.class.getResourceAsStream("/footer.txt");

		try {
			byte[] disclaimer = new byte[1500];
			while (!(-1 == (stream.read()))) {

				stream.read(disclaimer);
			}
			for (int i = 0; i <= disclaimer.length - 1; i++) {

				tDisclaimer = (tDisclaimer + (char) disclaimer[i]);

			}
		} catch (IOException e1) {
			System.out.println("Reading disclaimer file failed");

		}
		return tDisclaimer;

	}
	
	@Override
	public String pageRedirect() {
		return redirectTo;
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
	public void setHeaderContent(Map<String, String> headerContent) {
		this.headerContent=headerContent;
	}
	public Map<String, String> getHeaderContent() {
		return headerContent;
	}
	public void setTabColumnsWithSize(Map<String,Float> tabColumnsWithSize) {
		this.tabColumnsWithSize= tabColumnsWithSize;
	}
	public Map<String, Float> getTabColumnsWithSize() {
		return tabColumnsWithSize;
	}
	public void setLogo(String logo) {
		this.logo=logo;
	}
	public String getLogo() {
		return logo;
	}
	public void setEmptyMsg(String emptyMsg) {
		this.emptyMsg=emptyMsg;
	}
	public String getEmptyMsg() {
		return emptyMsg;
	}
	@Override
	public void exportShowParts(boolean showParts) throws DocumentException,
			MalformedURLException, IOException, ParseException {
		// TODO Auto-generated method stub
		
	}


}
