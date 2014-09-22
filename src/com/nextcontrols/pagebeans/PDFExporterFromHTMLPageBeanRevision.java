package com.nextcontrols.pagebeans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PostRestoreStateEvent;
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

import org.primefaces.context.RequestContext;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.nextcontrols.bureaudomain.OnceService;
import com.nextcontrols.bureaudomain.PriceCategory;
import com.nextcontrols.bureaudomain.QuantityCategory;
import com.nextcontrols.bureaudomain.StandardParts;
import com.nextcontrols.utility.ServiceProperties;

@ManagedBean(name = "pdfhtmlExpRev")
@SessionScoped
public class PDFExporterFromHTMLPageBeanRevision implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FacesContext facesContext;
	QuotationRevisionPageBean quotationBean;
	UserInfoPageBean userInfo;
	DecimalFormat intFormat = new DecimalFormat("#");
	// NumberFormat formatter = NumberFormat.getCurrencyInstance();
	DecimalFormat formater;
	ByteArrayOutputStream baos;
	Document document;
	PdfWriter writer;
	String contents = "";
	int pageSection = 0;
	int pageSection2 = 0;
	String extraUKBr = "";
	int thirdPart = 0;
	int secondPart = 0;
	String pageContents2 = "";
	String pageContents3 = "";
	boolean isFirstBreak = false;
	boolean loading = false;
	String loadingClass = "hide";
	int status = 0;

	// String country;

	public PDFExporterFromHTMLPageBeanRevision() {

	}

	/**
	 * This method generates a PDF report
	 * 
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */

	public void createPDF() throws DocumentException, IOException {
		pageSection = 0;
		contents = "";
		facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext()
				.getSession(false);
		quotationBean = (QuotationRevisionPageBean) session
				.getAttribute("revisedquotation");
		userInfo = (UserInfoPageBean) session.getAttribute("userInfo");
		//

		formater = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
		DecimalFormatSymbols symbols = formater.getDecimalFormatSymbols();
		symbols.setCurrencySymbol("");
		formater.setDecimalFormatSymbols(symbols);
		formater.setMaximumFractionDigits(0);

		//
		baos = new ByteArrayOutputStream();
		document = new Document();
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			// country = "UK";
			document.setPageSize(PageSize.A4);
			extraUKBr = "<br/>";
		} else {
			// country = "US";
			document.setPageSize(PageSize.LETTER);
			extraUKBr = "";
		}
		document.setMargins(70, 70, 125, 0);
		writer = PdfWriter.getInstance(document, baos);
		writer.setPageEvent(new PDFEventListener());

		document.open();
		PdfContentByte under = writer.getDirectContentUnder();

		pageSection2 = 0;
		document.newPage();
		contents += getFirstPage() + "_";
		pageSection = 1;
		// System.out.println("page 0");
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			under.addImage(this.setBackGroundImage(
					((ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext()).getRealPath("")
							+ File.separator + "images" + File.separator
							// + quotationBean.getQuotation().getFacilitytype()
							+ "Bio Tech And Life Sciences" + "1.png", document
							.getPageSize().getHeight(), document.getPageSize()
							.getWidth()));
		} else {
			under.addImage(this.setBackGroundImage(
					((ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext()).getRealPath("")
							+ File.separator + "images" + File.separator
							// + quotationBean.getQuotation().getFacilitytype()
							+ "Bio Tech And Life Sciences" + "US1.png",
					document.getPageSize().getHeight(), document.getPageSize()
							.getWidth()));
		}
		XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				new ByteArrayInputStream(getFirstPage().getBytes()));
		// System.out.println("page 1");
		if (!quotationBean.getQuotation().isDisable_coveringletter()) {
			document.setMargins(70, 90, 35, 70);
			document.newPage();
			contents += getSecondPage() + "_";
			pageSection = 2;
			if (userInfo.getUser().getCompany().getCountry().getCountry()
					.compareToIgnoreCase("UK") == 0) {
				under.addImage(this.setBackGroundImage(
						((ServletContext) FacesContext.getCurrentInstance()
								.getExternalContext().getContext())
								.getRealPath("")
								+ File.separator
								+ "images"
								+ File.separator
								// +
								// quotationBean.getQuotation().getFacilitytype()
								+ "Bio Tech And Life Sciences" + "2.png",
						document.getPageSize().getHeight(), document
								.getPageSize().getWidth()));
			} else {
				under.addImage(this.setBackGroundImage(
						((ServletContext) FacesContext.getCurrentInstance()
								.getExternalContext().getContext())
								.getRealPath("")
								+ File.separator
								+ "images"
								+ File.separator
								// +
								// quotationBean.getQuotation().getFacilitytype()
								+ "Bio Tech And Life Sciences" + "US2.png",
						document.getPageSize().getHeight(), document
								.getPageSize().getWidth()));
			}
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new ByteArrayInputStream(getSecondPage().getBytes()));
			// System.out.println("page 2");
		}
		/*
		 * int pageNumber=1; XMLWorkerHelper.getInstance().parseXHtml(writer,
		 * document, new
		 * ByteArrayInputStream(("<div style=\"margin-top: "+(document
		 * .getPageSize
		 * ().getHeight()+30)+"px;margin-left:"+(document.getPageSize
		 * ().getWidth()/2)+"px\">"+pageNumber+"</div>").getBytes()));
		 */
		if (!quotationBean.getQuotation().isUpdateInstallation()) {
			document.setMargins(70, 70, 44, 0);
			document.newPage();
			contents += getThirdPage() + "_";
			pageSection = 3;
			under.addImage(this.setBackGroundImage(
					((ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext()).getRealPath("")
							+ File.separator + "images" + File.separator
							// + quotationBean.getQuotation().getFacilitytype()
							+ "Bio Tech And Life Sciences" + "3.png", document
							.getPageSize().getHeight(), document.getPageSize()
							.getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new ByteArrayInputStream(getThirdPage().getBytes()));
			// System.out.println("page 3");
			document.newPage();
			contents += getFourthPage() + "_";
			pageSection = 4;
			under.addImage(this.setBackGroundImage(
					((ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext()).getRealPath("")
							+ File.separator + "images" + File.separator
							// + quotationBean.getQuotation().getFacilitytype()
							+ "Bio Tech And Life Sciences" + "4.png", document
							.getPageSize().getHeight(), document.getPageSize()
							.getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new ByteArrayInputStream(getFourthPage().getBytes()));
			// System.out.println("page 4");
		}
		document.setMargins(70, 90, 35, 70);
		document.newPage();
		contents += getFifthPage() + "_";
		pageSection = 5;
		under.addImage(this.setBackGroundImage(
				((ServletContext) FacesContext.getCurrentInstance()
						.getExternalContext().getContext()).getRealPath("")
						+ File.separator + "images" + File.separator
						// + quotationBean.getQuotation().getFacilitytype()
						+ "Bio Tech And Life Sciences" + "5.png", document
						.getPageSize().getHeight(), document.getPageSize()
						.getWidth()));
		XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				new ByteArrayInputStream(getFifthPage().getBytes()));
		// System.out.println("page 5");
		if (isFirstBreak) {
			document.setMargins(70, 90, 35, 70);
			document.newPage();
			String page6 = pageContents2 + pageContents3;
			contents += page6 + "_";
			pageSection = 5;
			under.addImage(this.setBackGroundImage(
					((ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext()).getRealPath("")
							+ File.separator + "images" + File.separator
							// + quotationBean.getQuotation().getFacilitytype()
							+ "Bio Tech And Life Sciences" + "5.png", document
							.getPageSize().getHeight(), document.getPageSize()
							.getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new ByteArrayInputStream(page6.getBytes()));
			// System.out.println("page 6");
		}
		/*
		 * document.newPage(); contents += getSixthPage()+"_";
		 * under.addImage(this.setBackGroundImage(((ServletContext)
		 * FacesContext.
		 * getCurrentInstance().getExternalContext().getContext()).getRealPath
		 * ("") + File.separator+ "images" + File.separator +
		 * quotationBean.getQuotation().getFacilitytype()+"6.jpg",
		 * document.getPageSize().getHeight(),
		 * document.getPageSize().getWidth()));
		 * XMLWorkerHelper.getInstance().parseXHtml(writer, document, new
		 * ByteArrayInputStream(getSixthPage().getBytes()));
		 */

		pageSection2 = 7;
		// document.setMargins(70, 90, 44, 0);
		document.setMargins(70, 90, 230, 0);
		document.newPage();
		contents += getSeventhPage();
		pageSection = 7;
		under.addImage(this.setBackGroundImage(
				((ServletContext) FacesContext.getCurrentInstance()
						.getExternalContext().getContext()).getRealPath("")
						+ File.separator + "images" + File.separator
						// + quotationBean.getQuotation().getFacilitytype()
						+ "Bio Tech And Life Sciences" + "6.png", document
						.getPageSize().getHeight(), document.getPageSize()
						.getWidth()));
		XMLWorkerHelper.getInstance().parseXHtml(writer, document,
				new ByteArrayInputStream(getSeventhPage().getBytes()));
		// System.out.println("page 7");

		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("US") == 0) {
			document.setMargins(70, 90, 44, 0);
			document.newPage();
			contents += "_";
			contents += getEighthPage();
			pageSection = 8;
			under.addImage(this.setBackGroundImage(
					((ServletContext) FacesContext.getCurrentInstance()
							.getExternalContext().getContext()).getRealPath("")
							+ File.separator + "images" + File.separator
							// + quotationBean.getQuotation().getFacilitytype()
							+ "Bio Tech And Life Sciences" + "6.png", document
							.getPageSize().getHeight(), document.getPageSize()
							.getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new ByteArrayInputStream(getEighthPage().getBytes()));
			// System.out.println("page 8");
		}

		document.close();
	}

	class PDFEventListener extends PdfPageEventHelper {
		protected Phrase header;

		protected PdfPTable footer;

		public PDFEventListener() {
			// super();
			header = new Phrase("**** Header ****");

			footer = new PdfPTable(1);

			footer.setTotalWidth(150);

			footer.getDefaultCell()
					.setHorizontalAlignment(Element.ALIGN_CENTER);

		}

		@Override
		public void onStartPage(PdfWriter writer, Document document) {
			super.onStartPage(writer, document);
			// System.out.println("-------------start page: "+
			// writer.getPageNumber());
			// System.out.println("start section: " + pageSection);
			// System.out.println("start section2: " + pageSection2);

			if (pageSection == 2) {
				document.setMargins(70, 70, 90, 70);
			}
			if (pageSection == 5) {
				document.setMargins(70, 90, 44, 0);
				// System.out.println("entered condition page section 5 start");
			}
			if (pageSection == 3) {
				document.setMargins(70, 70, 44, 70);
				// System.out.println("entered condition page section 3 start");
			}
			// if (pageSection == 7) {
			// document.setMargins(70, 90, 44, 0);
			// // System.out.println("entered condition page section 7 start");
			// }

			// System.out.println("start margin: " + document.bottomMargin());
			// System.out.println("start margin top: " + document.topMargin());
		}

		@Override
		public void onEndPage(PdfWriter writer, Document document) {

			// super.onEndPage(writer, document);

			// System.out.println("-------------end page: "+
			// writer.getPageNumber());
			// System.out.println("end section: " + pageSection);
			// System.out.println("end section2: " + pageSection2);

			if (pageSection == 2) {
				try {
					document.setMargins(70, 90, 90, 48);
					if (userInfo.getUser().getCompany().getCountry()
							.getCountry().compareToIgnoreCase("UK") == 0) {
						writer.getDirectContentUnder().addImage(
								this.setBackGroundImage(
										((ServletContext) FacesContext
												.getCurrentInstance()
												.getExternalContext()
												.getContext()).getRealPath("")
												+ File.separator
												+ "images"
												+ File.separator
												+ "Bio Tech And Life Sciences"
												+ "2.png", document
												.getPageSize().getHeight(),
										document.getPageSize().getWidth()));
					} else {
						writer.getDirectContentUnder().addImage(
								this.setBackGroundImage(
										((ServletContext) FacesContext
												.getCurrentInstance()
												.getExternalContext()
												.getContext()).getRealPath("")
												+ File.separator
												+ "images"
												+ File.separator
												+ "Bio Tech And Life Sciences"
												+ "US2.png", document
												.getPageSize().getHeight(),
										document.getPageSize().getWidth()));
					}

				} catch (DocumentException | IOException e) {
					e.printStackTrace();
				}
			} else if (pageSection == 5) {
				// System.out.println("entered condition page section 5 end");
				try {
					document.setMargins(70, 90, 230, 70);

					writer.getDirectContentUnder().addImage(
							this.setBackGroundImage(
									((ServletContext) FacesContext
											.getCurrentInstance()
											.getExternalContext().getContext())
											.getRealPath("")
											+ File.separator
											+ "images"
											+ File.separator
											+ "Bio Tech And Life Sciences"
											+ "5.png", document.getPageSize()
											.getHeight(), document
											.getPageSize().getWidth()));

				} catch (DocumentException | IOException e) {
					e.printStackTrace();
				}
			} else if (pageSection == 3) {
				document.setMargins(70, 70, 44, 70);
				// System.out.println("entered condition page section 3 end");
			}
			// if (pageSection2 == 7) {
			// System.out.println("entered condition page section 7 end");
			// document.setMargins(70, 90, 44, 0);
			//
			// }
			// System.out.println("end margin: " + document.bottomMargin());
			// System.out.println("end margin top: " + document.topMargin());
			PdfContentByte cb = writer.getDirectContent();
			float bottomPos;
			if (document.bottom() > 4) {
				bottomPos = document.bottom() - document.bottomMargin() + 4;

			} else
				bottomPos = 4;
			float yPageNumber;
			// = (document.right() - document.left()
			// + document.rightMargin() - 35)
			// + document.leftMargin();
			// if ("US".equals(country)) {
			yPageNumber = (document.right() - document.left()) / 2
					+ document.leftMargin();
			// }

			if (writer.getPageNumber() > 1) {
				Font pageNumberFont = FontFactory.getFont("Arial", 10);
				Phrase pageNumber = new Phrase(new Chunk(String.format("%d",
						writer.getPageNumber() - 1), pageNumberFont));

				// pageNumber.setFont(pageNumberFont);

				ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, pageNumber,
						yPageNumber, bottomPos, 0);

				footer.writeSelectedRows(0, -1,
						(document.right() - document.left() - 300) / 2
								+ document.leftMargin(), bottomPos, cb);
			}

		}

		public Image setBackGroundImage(String path, float height, float width)
				throws BadElementException, MalformedURLException, IOException {
			Image image = Image.getInstance(path);
			image.scaleAbsoluteHeight(document.getPageSize().getHeight());
			image.scaleAbsoluteWidth(document.getPageSize().getWidth());
			image.setAbsolutePosition(0, 0);
			return image;
		}

	}

	public String getFirstPage() {
		String extraBreaks = "";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			extraBreaks = "<br/><br/><br/>";
		}
		// System.out.println("quotation ref: "
		// + quotationBean.getQuotation().getQuotationref());
		String quoteRef = quotationBean.getQuotation().getQuotationref();
		String postRef = "";
		if (quotationBean.getQuotation().getQuotationref().indexOf("\\") != -1) {
			quoteRef = quotationBean
					.getQuotation()
					.getQuotationref()
					.substring(
							0,
							quotationBean.getQuotation().getQuotationref()
									.indexOf("\\"));
			postRef = "\\"
					+ quotationBean
							.getQuotation()
							.getQuotationref()
							.substring(
									quotationBean.getQuotation()
											.getQuotationref().indexOf("\\") + 1,
									quotationBean.getQuotation()
											.getQuotationref().length());
		}

		// if(!quotationBean.postQuotationRef.isEmpty()){
		// postRef= "\\" + quotationBean.postQuotationRef;
		// }
		// System.out.println("post ref: " + postRef);
		return "<div style='font-family: Arial, Verdana; font-size: 13pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#18A3B2;'><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
				+ extraBreaks
				+ "<b><p style='padding-left: 25px;'>QUOTATION REF :"
				+ quoteRef
				+ postRef
				+ "<br/>PROJECT : "
				+ quotationBean.getQuotation().getProjectname()
				+ "</p></b></div>";
	}

	public String getSecondPage() {
		// System.out.println(quotationBean.getQuotationcoveringletter());
		ExternalContext context = FacesContext.getCurrentInstance()
				.getExternalContext();
		HttpSession session = (HttpSession) context.getSession(false);
		UserInfoPageBean bean = (UserInfoPageBean) session
				.getAttribute("userInfo");
		String contactName = (quotationBean.getQuotation().getContactname() != null) ? quotationBean
				.getQuotation().getContactname() : "";
		String customerName = (quotationBean.getQuotation().getCustomername() != null) ? quotationBean
				.getQuotation().getCustomername() : "";
		String address1 = (quotationBean.getQuotation().getAddress1() != null) ? quotationBean
				.getQuotation().getAddress1() : "";
		String address2 = (quotationBean.getQuotation().getAddress2() != null) ? quotationBean
				.getQuotation().getAddress2() : "";
		String address3 = (quotationBean.getQuotation().getAddress3() != null) ? quotationBean
				.getQuotation().getAddress3() : "";
		String department = (quotationBean.getQuotation().getDepartment() != null) ? quotationBean
				.getQuotation().getDepartment() : "";
		String city = (quotationBean.getQuotation().getTowncity() != null) ? quotationBean
				.getQuotation().getTowncity() : "";
		String state = (quotationBean.getQuotation().getCountrystate() != null) ? quotationBean
				.getQuotation().getCountrystate() : "";
		String postCode = (quotationBean.getQuotation().getZippincode() != null) ? quotationBean
				.getQuotation().getZippincode() : "";
		String quotationcoveringletter = "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ "" + contactName + "<br/>";

		if (!department.isEmpty()) {
			quotationcoveringletter += department + "<br/> ";
		}
		quotationcoveringletter += customerName + "<br/> " + address1
				+ "<br/> ";
		if (!address2.isEmpty()) {
			quotationcoveringletter += address2 + "<br/> ";
		}
		if (!address3.isEmpty()) {
			quotationcoveringletter += address3 + "<br/> ";
		}
		quotationcoveringletter += city
				+ "<br/> "
				+ state
				+ "<br/> "
				+ postCode
				+ "<br /><br/>"
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; "
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
				+ "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
				+ new SimpleDateFormat("dd/MM/yyyy").format(new Date())
				+ " <br /><br /> Dear "
				+ quotationBean.getQuotation().getCustomersalutation()
				+ ",</div>"
				// +
				// "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal;'>"
				// +
				// "&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;"
				// + "&nbsp; &nbsp; &nbsp; &nbsp; "
				// + "<u><strong>"
				// + "Re "
				// + quotationBean.getQuotation().getProjectname()
				// + " remote wireless monitoring system</strong></u></div> "
				// +quotationBean.getQuotationcoveringletter().substring(quotationBean.getQuotationcoveringletter().indexOf("<input type='hidden' value='' id='start'/>")+42,quotationBean.getQuotationcoveringletter().indexOf("<input type='hidden' value='' id='end'/>"))
				+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ quotationBean.getQuotationcoveringletter()
				+ "</div>"
				+ "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ bean.getUser().getFirstName() + " "
				+ bean.getUser().getLastName() + "<br/>"
				+ bean.getUser().getTitle() + ",&nbsp;" + "<br/>"
				+ bean.getUser().getPhoneNumber() + "<br/>"
				+ bean.getUser().getUsername() + "</div>";
		// System.out.println("<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><br/><br/><br/><br/><br/><br/><br/></div>"+quotationBean.getQuotation().getQuotationcoveringletter());
		return "<div style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><br/><br/><br/><br/><br/><br/><br/></div>"
				// + quotationBean.getQuotation().getQuotationcoveringletter();
				+ quotationcoveringletter;
	}

	public String getThirdPage() {// <br/><br/><br/>
		String extraBreaks = "";// just to adjust formatting
		if (quotationBean.getQuotation().isDisable_coveringletter()) {
			extraBreaks = "<br/><br/><br/>";
		}
		return "<div ><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
				+ extraBreaks
				+ extraUKBr
				+ "</div>"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>TUTELA MONITORING SYSTEMS BENEFITS </p>"
				+

				"<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 84px;'>Predictable, low ownership costs:<br/>"
				+ " &nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Price fixed for duration of Service Agreement.<br/>"
				+ " &nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Zero software upgrade and licensing costs.<br/>"
				+ " &nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Zero IT software & hardware support costs.<br/>"
				+ " &nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Elimination of local staff costs."
				+ "</p>"
				+

				"<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 84px; '>Intuitive and simple to use systems:<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Simple web browser access, anytime, anywhere.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Minimal operator training required (less than 15 minutes).<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Mobile access through Smart Phones and Tablets.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Intuitive web based graphical sensor status displays.<br/>"
				+ "</p>"
				+

				"<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 84px; '>Fully compliant paperless electronic records:<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Full FDA 21 CFR part 11 compliance.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Exceeds all current regulatory compliance requirements.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Full electronic alarm incident audit trails.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Secure off site record storage for up to 30 years.<br/>"
				+ "</p>"
				+

				"<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 84px; '>Highly safe and secure electronic records: <br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Secure HTTPS Electronic Banking standards web site.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Fully 21 CFR part 11 compliant access passwords & PIN codes 11.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Fully traceable user activity audit records.<br/>"
				+ "&nbsp; &nbsp;&bull;&nbsp; &nbsp; &nbsp; &nbsp;Mirrored record storage for ultimate security.<br/>"
				+ "</p>"
				+

				"<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>TUTELA MONITORING SYSTEMS FEATURES </p>"
				+ "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 94px; padding-top: 0px;padding-down: 0px;'>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Remote wireless monitoring systems.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;5-year sensor battery life.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Self-healing Mi-Wi communications network.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;NIST/NPL sensor probe traceability.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Scalability from 1 to 254 sensing devices.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Stand alone and remote communications options.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Temperature sensing from -200 to +200 Deg C.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Standby battery options.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Dual alarm alert capability.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Clean room differential pressure sensing.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Medical gas pressure sensing.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;LN2 Cryogenic temperature sensing.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Local sensor 7-day memory storage.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Up to 30 years local record storage.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Volt free contact alarm/status monitoring.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;External beacon/sounder connection.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Local audible and visual alarm display.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;24 months system hardware warranty.<br/>"
				+ "</p>";

	}

	public String getFourthPage() {
		//
		String pageContents = "<div ><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
				+ extraUKBr
				+ "<br/>"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>YOUR SYSTEM CONFIGURATION </p></div>"
				+ "<table  border='0' width='80%'>"
				+ "<tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: center;'>ITEM</th>"
				+ "<th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: right;'>QTY</th></tr>";
		pageContents += "<tr><td >&nbsp;</td><td style='text-align: right;'>&nbsp;</td></tr>";
		for (QuantityCategory qc : quotationBean.getQuantityCategoriesList()) {
			pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>";
			if (qc.getTempquantity() > 0)
				pageContents += "<strong>";
			pageContents += qc.getDescription();
			if (qc.getTempquantity() > 0)
				pageContents += "</strong>";
			pageContents += "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>";
			if (qc.getTempquantity() > 0)
				pageContents += "<strong>";
			pageContents += qc.getTempquantity();
			if (qc.getTempquantity() > 0)
				pageContents += "</strong>";
			pageContents += "</td></tr>";
		}
		pageContents += "<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: center;text-decoration:underline;'>TOTAL SENSOR COUNT : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ quotationBean.getTotalSensorCount() + "</td></tr>";
		pageContents += "</table><br/>";

		pageContents += "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>YOUR CHOSEN SERVICE OPTIONS </p>"
				+ "<table  border='0' width='80%'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: center;'>ITEM</th>"
				+ "<th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: right;'>CHOICE</th></tr>";
		pageContents += "<tr><td >&nbsp;</td><td style='text-align: right;'>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><strong>Remote Ethernet Communications Link</strong></td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'><strong>Yes</strong></td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'><strong>On-Site System Installation</strong></td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'><strong>Yes</strong></td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ (quotationBean.getQuotation().getOnsitesensorcalibration() ? "<strong>"
						: "")
				+ "On-Site NIST Probe Calibration"
				+ (quotationBean.getQuotation().getOnsitesensorcalibration() ? "</strong>"
						: "")
				+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (quotationBean.getQuotation().getOnsitesensorcalibration() ? "<strong>Yes</strong>"
						: "No") + "</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ (quotationBean.getQuotation().getFactorysensorcalibration() ? "<strong>"
						: "")
				+ "Factory NIST Probe Calibration"
				+ (quotationBean.getQuotation().getFactorysensorcalibration() ? "</strong>"
						: "")
				+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (quotationBean.getQuotation().getFactorysensorcalibration() ? "<strong>Yes</strong>"
						: "No") + "</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ (quotationBean.getQuotation().getCombinedrnamonitoring() ? "<strong>"
						: "")
				+ "Remote Alarm Monitoring Service"
				+ (quotationBean.getQuotation().getCombinedrnamonitoring() ? "</strong>"
						: "")
				+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (quotationBean.getQuotation().getCombinedrnamonitoring() ? "<strong>Yes</strong>"
						: "No") + "</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ ((quotationBean.getQuotation().getRecordingonly() || quotationBean
						.getQuotation().getCombinedrnamonitoring()) ? "<strong>"
						: "")
				+ "Remote Electronic Recording Service"
				+ ((quotationBean.getQuotation().getRecordingonly() || quotationBean
						.getQuotation().getCombinedrnamonitoring()) ? "</strong>"
						: "")
				+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ ((quotationBean.getQuotation().getRecordingonly() || quotationBean
						.getQuotation().getCombinedrnamonitoring()) ? "<strong>Yes</strong>"
						: "No") + "</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ (quotationBean.getQuotation().getWebhostedgui() ? "<strong>"
						: "")
				+ "Web Hosted Sensor Display Graphics (Web GUI)"
				+ (quotationBean.getQuotation().getWebhostedgui() ? "</strong>"
						: "")
				+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (quotationBean.getQuotation().getWebhostedgui() ? "<strong>Yes</strong>"
						: "No") + "</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
				+ (quotationBean.getQuotation().getDqiqoqprotocoldocs() ? "<strong>"
						: "")
				+ "DQ, IQ and OQ, QC Record Documentation"
				+ (quotationBean.getQuotation().getDqiqoqprotocoldocs() ? "</strong>"
						: "")
				+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ (quotationBean.getQuotation().getDqiqoqprotocoldocs() ? "<strong>Yes</strong>"
						: "No") + "</td></tr>";
		/*
		 * pageContents +=
		 * "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Refrigerator Temperature Mapping service</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
		 * + (quotationBean.getQuotation().getTemperatureMapping() ? "Yes" :
		 * "No") + "</td></tr>";
		 */
		pageContents += "</table>";
		return pageContents;
	}

//	public String getFifthPage() {
////		<br/><br/><br/><br/><br/><br/><br/><br/>
//		String page = "";
//		for (int i = 0; i < 44; i++) {
//			page = page + "<p>" + i + "</p><br/>";
//		}
//		return page;
//	}

	public String getFifthPage() {
		int totalLines = 32;
		int firstPart = 0;
		pageContents2 = "";
		pageContents3 = "";

		String breaks = "<br/><br/><br/><br/>";// just for formatting
		if (!quotationBean.getQuotation().isDisable_coveringletter()
				&& quotationBean.getQuotation().isUpdateInstallation()) {
			breaks = "";
		}
		String costHeading = "SYSTEM COST BREAKDOWN ";
		if (quotationBean.getQuotation().isUpdateInstallation()) {
			costHeading = "SYSTEM UPGRADE COST";
		}

		String pageContents = "<div ><br/><br/><br/><br/><br/><br/><br/><br/>"
				+ breaks
				+ extraUKBr
				+ "<br/>"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>"
				+ costHeading
				+ "</p></div>"
				+ "<table  border='0' width='80%'>"
				+ "<tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 36px; text-decoration:underline;color:#41918A;text-align: left;'>HARDWARE</th>"
				+ "<th >&nbsp;</th></tr>";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("US") == 0) {

			pageContents += "</table>";
			pageContents += "<table cellpadding='3' style='margin-top:10px;border-collapse: collapse;border:0.25px solid black;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;margin-left:50px;'><tr style='font-size:10pt;'><th style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>ITEM</th><th style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>PART NO.</th><th style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>UNIT COST</th><th style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>QTY</th><th style='border-collapse: collapse;border:0.25px solid black;padding:5px;text-align:right;'>TOTAL COST</th></tr>";
			int itemCounter = 1;
			firstPart = 3;
			// pageContents+="<tr><td></td></tr>";
			int lineCounter = 5;
			for (StandardParts st : quotationBean.getTempStParts()) {
				if (st.getQuoteQuantity() != 0) {
					pageContents += "<tr style='font-size:9pt;'><td style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>"
							+ itemCounter
							+ "</td>"
							+ "<td style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>"
							+ st.getPartNumber()
							+ "</td>"
							+ "<td style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>"
							+ userInfo.getUser().getCompany().getCountry()
									.getCurrency()
							+ formater.format(st.getListPrice())
							+ "</td>"
							+ "<td style='border-collapse: collapse;border:0.25px solid black;padding:5px;'>"
							+ st.getQuoteQuantity()
							+ "</td>"
							+ "<td style='border-collapse: collapse;border:0.25px solid black;padding:5px;text-align:right;'>"
							+ userInfo.getUser().getCompany().getCountry()
									.getCurrency()
							+ formater.format(st.getListPrice()
									* st.getQuoteQuantity())
							+ "</td>"
							+ "</tr>";
					if (lineCounter == 5) {
						firstPart++;
					}
					lineCounter = 9 - lineCounter;
					firstPart++;
					itemCounter++;
				}

			}

			// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
			if (quotationBean.getmHardwareDiscount() != 0) {
				// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getTotalsystembreakdown())+"</td></tr>";
				pageContents += "<tr><td style='border-collapse: collapse;border:0.25px solid black;'></td><td style='border-collapse: collapse;border:0.25px solid black;'></td><td style='border-collapse: collapse;border:0.25px solid black;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='border-collapse: collapse;border:0.25px solid black;'></td><td style='border-collapse: collapse;border:0.25px solid black;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;padding:5px;'>"
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(quotationBean
								.getTotalsystembreakdown()
								* quotationBean.getmHardwareDiscount() / 100)
						+ "</td></tr>";
				firstPart++;
			}

			pageContents += "<tr><td style='border-collapse: collapse;border:0.25px solid black;'></td><td style='border-collapse: collapse;border:0.25px solid black;'></td><td style='border-collapse: collapse;border:0.25px solid black;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='border-collapse: collapse;border:0.25px solid black;'></td><td style='border-collapse: collapse;border:0.25px solid black;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;padding:5px;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater
							.format((quotationBean.getTotalsystembreakdown() - (quotationBean
									.getTotalsystembreakdown()
									* quotationBean.getmHardwareDiscount() / 100)))
					+ "</td></tr>";
			firstPart++;
			pageContents += "</table>";
			// if(itemCounter>7 && itemCounter<20){
			// for(int i=0;i<15;i++){
			// System.out.println("applying breaks");
			// pageContents+="<br/>";
			// }
			// }
		} else {
			for (PriceCategory pc : quotationBean.getPriceCategoriesList()) {
				pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
						+ pc.getDescription()
						+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(pc.getTempquantity()) + "</td></tr>";
			}
			// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
			if (quotationBean.getmHardwareDiscount() != 0) {
				// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getTotalsystembreakdown())+"</td></tr>";
				pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(quotationBean
								.getTotalsystembreakdown()
								* quotationBean.getmHardwareDiscount() / 100)
						+ "</td></tr>";
			}
			pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater
							.format((quotationBean.getTotalsystembreakdown() - (quotationBean
									.getTotalsystembreakdown()
									* quotationBean.getmHardwareDiscount() / 100)))
					+ "</td></tr>";
			pageContents += "</table>";
		}
		// pageContents+="<br/>";

		secondPart = 0;
		this.pageContents2 = getSixthPage0();
		thirdPart = 0;
		this.pageContents3 += getSixthPage();
		//
		System.out.println("total lines: " + totalLines);
		System.out.println("first part: " + firstPart);
		System.out.println("second part: " + secondPart);
		System.out.println("third part: " + thirdPart);

		int firstBreaks = 0;
		// if((firstPart+secondPart)>totalLines){
		// firstBreaks=totalLines-firstPart;
		// System.out.println("first breaks: "+firstBreaks);
		// for(int i=0;i<firstBreaks;i++){
		// pageContents+="<br/>";
		// }
		// isFirstBreak=true;
		// }
		isFirstBreak = false;
		if ((firstPart + secondPart + thirdPart) > totalLines
				&& firstPart < totalLines) {
			// firstBreaks = totalLines - firstPart + 2;
			// System.out.println("first breaks: " + firstBreaks);
			// for (int i = 0; i < firstBreaks; i++) {
			// pageContents += "<br/>";
			// }
			isFirstBreak = true;
		} else if ((firstPart + secondPart + thirdPart) > (totalLines * 2)
				&& firstPart < (totalLines * 2)) {
			// firstBreaks = (totalLines * 2) - firstPart + 2;
			// System.out.println("first breaks: " + firstBreaks);
			// for (int i = 0; i < firstBreaks; i++) {
			// pageContents += "<br/>";
			// }
			isFirstBreak = true;
		} else if ((firstPart + secondPart + thirdPart) > (totalLines * 3)
				&& firstPart < (totalLines * 3)) {
			// firstBreaks = (totalLines * 3) - firstPart + 2;
			// System.out.println("first breaks: " + firstBreaks);
			// for (int i = 0; i < firstBreaks; i++) {
			// pageContents += "<br/>";
			// }
			isFirstBreak = true;
		} else if ((firstPart + secondPart + thirdPart) > (totalLines * 4)
				&& firstPart < (totalLines * 4)) {
			// firstBreaks = (totalLines * 3) - firstPart + 2;
			// System.out.println("first breaks: " + firstBreaks);
			// for (int i = 0; i < firstBreaks; i++) {
			// pageContents += "<br/>";
			// }
			isFirstBreak = true;
		}// max 4 pages for parts
		if (!isFirstBreak) {
			pageContents += pageContents2;
			// int secondBreaks=0;
			// if(!isFirstBreak &&
			// ((firstPart+secondPart+thirdPart)>totalLines)){
			// secondBreaks=totalLines-(firstPart+secondPart)+1;
			// System.out.println("second breaks: "+secondBreaks);
			// for(int i=0;i<secondBreaks;i++){
			// pageContents+="<br/>";
			// }
			// }

			pageContents += pageContents3;
		}
		return pageContents;
	}

	public String getSixthPage0() {
		String pageContents2 = "";
		pageContents2 += "<table  border='0' width='80%' style='margin-top:10px;'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: left;'>SERVICE</th>"
				+ "<th >&nbsp;</th></tr>";
		// secondPart++;
		secondPart++;
		for (OnceService os : quotationBean.getOnceservicesList()) {
			if (os.isShowCost()) {
				if ((os.getUnitCost() * quotationBean.getTotalSensorCount()) > os
						.getMinimumCharge()) {
					pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
							+ os.getServiceDescription()
							+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
							+ userInfo.getUser().getCompany().getCountry()
									.getCurrency()
							+ formater.format(os.getUnitCost()
									* quotationBean.getTotalSensorCount())
							+ "</td></tr>";
				} else {
					if (quotationBean.getTotalSensorCount() == 0) {
						pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
								+ os.getServiceDescription()
								+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
								+ userInfo.getUser().getCompany().getCountry()
										.getCurrency()
								+ formater.format(0)
								+ "</td></tr>";
					} else {
						pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"
								+ os.getServiceDescription()
								+ "</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
								+ userInfo.getUser().getCompany().getCountry()
										.getCurrency()
								+ formater.format(os.getMinimumCharge())
								+ "</td></tr>";
					}
				}
			} else {
				// pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>"+os.getServiceDescription()+"</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(0)+"</td></tr>";
			}
			// secondPart++;

		}
		// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		if (quotationBean.getmServiceOptionsDiscount() != 0) {
			// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+intFormat.format(quotationBean.getOnceServicestotal())+"</td></tr>";
			pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(quotationBean.getOnceServicestotal()
							* quotationBean.getmServiceOptionsDiscount() / 100)
					+ "</td></tr>";
			secondPart++;
		}
		pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater
						.format((quotationBean.getOnceServicestotal() - (quotationBean
								.getOnceServicestotal()
								* quotationBean.getmServiceOptionsDiscount() / 100)))
				+ "</td></tr>";
		secondPart++;
		pageContents2 += "</table>";

		pageContents2 += "<div align='left'><table  border='0' width='80%'><tr><th style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: left;'>INSTALLATION</th>"
				+ "<th >&nbsp;</th></tr>";
		secondPart++;
		pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>On Site System Installation</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater.format(quotationBean.getOnSiteInst()) + "</td></tr>";
		secondPart++;
		if (quotationBean.getQuotation().getOnsitesensorcalibration()) {
			pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>On Site System Calibration</td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(quotationBean.getOnSiteCalib())
					+ "</td></tr>";
			secondPart++;
		}
		// pageContents+="<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		if (quotationBean.getmSiteInstallationDiscount() != 0) {
			// /pageContents+="<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>SUB TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"+userInfo.getUser().getCompany().getCountry().getCurrency()+(quotationBean.getOnSiteInst()+quotationBean.getOnSiteCalib())+"</td></tr>";
			pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>Less Discount Applied : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater
							.format(((quotationBean.getOnSiteInst() + quotationBean
									.getOnSiteCalib())
									* quotationBean
											.getmSiteInstallationDiscount() / 100))
					+ "</td></tr>";
			secondPart++;
		}
		pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>TOTAL : </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater
						.format(((quotationBean.getOnSiteInst() + quotationBean
								.getOnSiteCalib()) - ((quotationBean
								.getOnSiteInst() + quotationBean
								.getOnSiteCalib())
								* quotationBean.getmSiteInstallationDiscount() / 100)))
				+ "</td></tr>";
		secondPart++;
		pageContents2 += "<tr><td >&nbsp;</td><td style='text-align: center;'>&nbsp;</td></tr>";
		secondPart++;
		pageContents2 += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;font-weight: bold;'>TOTAL PROJECT PRICE ";
		secondPart++;
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			pageContents2 += "(Ex Vat) ";
		}
		pageContents2 += ": </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;text-decoration:underline;'>"
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ formater
						.format((quotationBean.getTotalsystembreakdown() - (quotationBean
								.getTotalsystembreakdown()
								* quotationBean.getmHardwareDiscount() / 100))
								+ (quotationBean.getOnceServicestotal() - (quotationBean
										.getOnceServicestotal()
										* quotationBean
												.getmServiceOptionsDiscount() / 100))
								+ ((quotationBean.getOnSiteInst() + quotationBean
										.getOnSiteCalib()) - ((quotationBean
										.getOnSiteInst() + quotationBean
										.getOnSiteCalib())
										* quotationBean
												.getmSiteInstallationDiscount() / 100)))
				+ "</td></tr>";

		pageContents2 += "</table></div>";
		return pageContents2;
	}

	public String getSixthPage() {
		String VATLabel = "";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			VATLabel = " (Ex Vat)";
		}
		String pageContents = "";
		thirdPart++;
		if ((quotationBean.getQuotation().getCombinedrnamonitoring() && !quotationBean
				.getQuotation().isUpdateInstallation())
				|| quotationBean.getQuotation().getRecordingonly()
				|| quotationBean.getQuotation().getYearlyrecalibrationservice()
				|| quotationBean.getQuotation().getTemperatureMapping()) {
			pageContents += "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>RECURRING SERVICE OPTIONS </p>";
			thirdPart++;
		}

		if (quotationBean.getQuotation().getCombinedrnamonitoring()
				&& !quotationBean.getQuotation().isUpdateInstallation()) {
			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>ALARM MONITORING AND RECORDING SERVICE</p>";
			thirdPart++;
			thirdPart++;

			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			pageContents += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater
							.format((quotationBean.alarmingnmonitoring1y - (quotationBean
									.getAlarmingnmonitoring1y()
									* quotationBean
											.getmRemoteMonitoringDiscount() / 100)))
					+ " Per year" + VATLabel + "<br/>";
			thirdPart++;
			thirdPart++;
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				pageContents += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater
								.format((quotationBean.alarmingnmonitoring3y - (quotationBean
										.getAlarmingnmonitoring3y()
										* quotationBean
												.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel + "<br/>";
				thirdPart++;
				pageContents += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater
								.format((quotationBean.alarmingnmonitoring5y - (quotationBean
										.getAlarmingnmonitoring5y()
										* quotationBean
												.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel + "";
				thirdPart++;
			}
			pageContents += "</p>";
		}

		if (quotationBean.getQuotation().getRecordingonly()) {
			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>RECORDING ONLY SERVICE</p>";
			thirdPart++;
			thirdPart++;

			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			pageContents += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater
							.format((quotationBean.recording1y - (quotationBean.recording1y
									* quotationBean
											.getmRemoteMonitoringDiscount() / 100)))
					+ " Per year" + VATLabel + "<br/>";
			thirdPart++;
			thirdPart++;
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				pageContents += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater
								.format((quotationBean.recording3y - (quotationBean.recording3y
										* quotationBean
												.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel + "<br/>";
				thirdPart++;
				pageContents += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater
								.format((quotationBean.recording5y - (quotationBean.recording5y
										* quotationBean
												.getmRemoteMonitoringDiscount() / 100)))
						+ " Per year" + VATLabel + "";
				thirdPart++;
			}
			pageContents += "</p>";
		}
		if (quotationBean.getQuotation().getYearlyrecalibrationservice()
				&& !quotationBean.getQuotation().isUpdateInstallation()) {
			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>NPL RE-CALIBRATION AND MAINTENANCE SERVICE</p>";
			thirdPart++;
			thirdPart++;

			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			pageContents += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(quotationBean.annualnistcalibration1y)
					+ " Per year" + VATLabel + "<br/>";
			thirdPart++;
			thirdPart++;
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				pageContents += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater
								.format(quotationBean.annualnistcalibration3y)
						+ " Per year" + VATLabel + "<br/>";
				thirdPart++;
				pageContents += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater
								.format(quotationBean.annualnistcalibration5y)
						+ " Per year" + VATLabel + "";
				thirdPart++;
			}
			pageContents += "</p>";
		}
		if (quotationBean.getQuotation().getTemperatureMapping()) {
			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 144px; text-decoration:underline;color:#41918A;'>REFRIGERATOR TEMPERATURE MAPPING SERVICE</p>";
			thirdPart++;
			thirdPart++;

			pageContents += "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 164px; padding-top: 0px;padding-down: 0px;'>";
			pageContents += "1 Year "
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format(quotationBean.temperaturemapping1y)
					+ " Per year" + VATLabel + "<br/>";
			thirdPart++;
			thirdPart++;
			if (userInfo.getUser().getCompany().isMultiyear_options()) {
				pageContents += "3 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(quotationBean.temperaturemapping3y)
						+ " Per year" + VATLabel + "<br/>";
				thirdPart++;
				pageContents += "5 Year "
						+ userInfo.getUser().getCompany().getCountry()
								.getCurrency()
						+ formater.format(quotationBean.temperaturemapping5y)
						+ " Per year" + VATLabel + "";
				thirdPart++;
			}
			pageContents += "</p>";
		}
		if (quotationBean.getQuotation().isUpdateInstallation()) {
			pageContents += "<div align='left'><table  border='0' width='80%' style='padding-left:30px'><tr><th style='padding-top:10px;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 46px; text-decoration:underline;color:#41918A;text-align: left;'>ADDITIONAL RECURRING SERVICE COSTS</th>"
					+ "<th >&nbsp;</th></tr>";
			thirdPart++;

			pageContents += "<tr><td style='padding-top:10px;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Addition to existing remote monitoring service: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format((Double.valueOf(quotationBean
							.getTempRemoteMonitoring()) * quotationBean
							.getTotalSensorCount())) + " Per Year</td></tr>";
			thirdPart++;
			pageContents += "<tr><td style='padding-top:10px;font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>Addition to existing annual calibration service: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: right;'>"
					+ userInfo.getUser().getCompany().getCountry()
							.getCurrency()
					+ formater.format((Double.valueOf(quotationBean
							.getTempAnnualCalibration()) * quotationBean
							.getTotalSensorCount())) + " Per Year</td></tr>";
			thirdPart++;
			pageContents += "</table></div>";
		}

		return pageContents;
	}

	public String getSeventhPage() {
		String item2 = "Remote recording and alarm monitoring services are subject to our standard Remote Monitoring and Alarm service agreement which must be signed prior to the alarms monitoring service commencing.";
		if (quotationBean.getQuotation().isUpdateInstallation()) {
			item2 = "Additional recurring probe monitoring and calibration costs will be automatically added to your existing Monitoring and Maintenance Service Agreement cost and invoiced accordingly.";
		}
		String traingLable = "";
		String excludingLabel = "  All prices exclude any applicable taxes and or duty.";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("UK") == 0) {
			excludingLabel = " All prices exclude VAT, any applicable taxes and or duty.";
			traingLable = "We will train up to 10 administrator personnel in one session of up to 2 hours duration.  "
					+ "Additional training hours are available by request at an extra cost.  Bespoke training courses are also available by request, price on application.";
		}
		String endOfPage = "";
		if (userInfo.getUser().getCompany().getCountry().getCountry()
				.compareToIgnoreCase("US") == 0) {
			// endOfPage = getEighthPage();
			traingLable = "Tutela hardware and software training will be provided for up to 10 users, for a maximum duration of 2 hours at the time of installation. "
					+ "Additional training is available for an additional fee.";
		}
		// <br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>
		return "<div >"
				+ extraUKBr
				+ ""
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;'>TERMS </p></div>"
				+ "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; padding-top:0px;margin-top:0px'>"
				+ "<ol>"
				+ "<li>  Tutela systems are supplied and installed against our standard terms and conditions of sale and purchase.</li>"
				+ "<li> "
				+ item2
				+ " </li>"
				+ "<li>"
				+ excludingLabel
				+ "</li>"
				+ "<li>  PAYMENT TERMS<br/>"
				+ "      Stage payment invoices will be raised for all projects over "
				+ userInfo.getUser().getCompany().getCountry().getCurrency()
				+ "25,000 as follows:<br/>"
				+ "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 124px;padding-top: 0px;padding-down: 0px;'>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;15% of project value on receipt of purchase order.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;50% of project value following equipment delivery to site.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;25% of project value on completion of installation.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;10% of project value on completion of training and handover.<br/>"
				+ "</p>"
				+ "      Remote recording and or remote alarm monitoring services will be invoiced either annually or&nbsp;quarterly&nbsp;in"
				+ "      advance of the service provision.<br/><br/>"
				+ "      Annual sensor recalibration services will be invoiced on completion of the work.<br/>"
				+ "      Payment of all invoices is required net 30 days following original date of invoice.<br/></li>"
				+ "<li>  Prices exclude delivery charges.</li>"
				+ "<li>  Warranty on monitoring system hardware is 24 months following date of system handover on a return to manufacturer basis.</li>"
				+ "<li>  This proposal remains valid for net 60 days following the original quotation date.</li>"
				+ "<li>  The customer will be required to provide suitable AC power outlets for the following sensing equipment where applicable :"
				+ "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-left: 124px;padding-top: 0px;padding-down: 0px;'>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Master wireless receiver panel (WARP).<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Wireless signal repeater modules.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Clean room differential pressure sensors.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Clean room airflow sensors.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;O2 gas sensors.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;CO2 gas sensors.<br/>"
				+ "&bull;&nbsp; &nbsp; &nbsp; &nbsp;Water and air pressure sensors."
				+ "</p>" + "</li>" + "<li>" + traingLable + "</li>" + "</ol>"
				+ "</p>" + endOfPage;
	}

	// public String getEighthPage() {
	// String pageContents =
	// "<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
	// +
	// "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;padding-top:-12px'>CUSTOMER ACCEPTANCE </p>"
	// + "<table  border='0' width='100%'>";
	//
	// pageContents +=
	// "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Title: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
	// pageContents +=
	// "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Print Name: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
	// pageContents +=
	// "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Signature: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
	// pageContents +=
	// "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;'>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Date: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
	// pageContents += "</table>";
	// return pageContents;
	// }
	public String getEighthPage() {
		// <br/><br/><br/><br/><br/><br/>
		String pageContents = "<br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/>"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;padding-top:-12px'>CUSTOMER ACCEPTANCE OF QUOTATION</p>"
				+ "<p style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>This quotation has been agreed and approved for system installation by:</p>"
				+ "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;padding-top:12px'>CUSTOMER</p>"
				+ "<table  border='0' width='100%'>";

		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>SIGNED BY: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>";// &nbsp;
																																																																																																																																																																																																// &nbsp;
																																																																																																																																																																																																// &nbsp;
																																																																																																																																																																																																// &nbsp;
																																																																																																																																																																																																// &nbsp;
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>PRINT NAME: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>TITLE: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>DATE: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
		pageContents += "</table>";
		pageContents += "<p style='font-family: Arial, Verdana; font-size: 10pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;color:#41918A;padding-top:5px'>TUTELA REPRESENTATIVE</p>";
		pageContents += "<table  border='0' width='100%'>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>SIGNED BY: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>PRINT NAME: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>TITLE: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr><tr><td>&nbsp;</td><td>&nbsp;</td></tr>";
		pageContents += "<tr><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;padding-top:5px'>DATE: </td><td style='font-family: Arial, Verdana; font-size: 9pt; font-variant: normal; line-height: normal; font-weight: normal; font-style:normal;text-align: left;text-decoration:underline;padding-top:5px' width='80%'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>";
		pageContents += "</table>";
		return pageContents;
	}

	public void export() throws DocumentException, IOException {
		this.status = 1;
		createPDF();
		this.status = 2;
		writeToResponse(((HttpServletResponse) facesContext
				.getExternalContext().getResponse()));
		facesContext.responseComplete();
	}

	public void checkStatus() {
		if (this.status == 1) {
			this.loading = true;
			this.loadingClass = "show";
			// this.loadingAnimation = "./images/LoadingGraphicAnimation.gif";
			RequestContext.getCurrentInstance().update("pdfDownloadForm");
			// RequestContext.getCurrentInstance().update(
			// "pdfDownloadForm:animateLoading");

		}
		if (this.status == 2) {
			this.loading = false;
			this.loadingClass = "hide";
			// this.loadingAnimation = "";
			RequestContext.getCurrentInstance().update("pdfDownloadForm");
			// RequestContext.getCurrentInstance().update(
			// "pdfDownloadForm:animateLoading");

			this.status = 0;
		}
	}

	public void writeToResponse(HttpServletResponse response)
			throws IOException, DocumentException {
		response.setContentType("application/pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control",
				"must revalidate, post-check=0, pre-check=0");
		response.setHeader("pragma", "public");
		String quoteRef = quotationBean.getQuotation().getQuotationref();
		String postRef = "";
		if (quotationBean.getQuotation().getQuotationref().indexOf("\\") != -1
				&& quotationBean.getQuotation().isCompleted()) {
			quoteRef = quotationBean
					.getQuotation()
					.getQuotationref()
					.substring(
							0,
							quotationBean.getQuotation().getQuotationref()
									.indexOf("\\"));
			postRef = "_"
					+ quotationBean
							.getQuotation()
							.getQuotationref()
							.substring(
									quotationBean.getQuotation()
											.getQuotationref().indexOf("\\") + 1,
									quotationBean.getQuotation()
											.getQuotationref().length());
		}
		// if(!quotationBean.postQuotationRef.isEmpty()){
		// response.setHeader("Content-disposition", "attachment;filename="
		// + quoteRef+ "_"
		// + quotationBean.postQuotationRef + ".pdf");
		// }
		// else{
		response.setHeader("Content-disposition", "attachment;filename="
				+ quoteRef + postRef + ".pdf");

		// }
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		baos.flush();
	}

	public Image setBackGroundImage(String path, float height, float width)
			throws BadElementException, MalformedURLException, IOException {
		Image image = Image.getInstance(path);
		image.scaleAbsoluteHeight(document.getPageSize().getHeight());
		image.scaleAbsoluteWidth(document.getPageSize().getWidth());
		image.setAbsolutePosition(0, 0);
		return image;
	}

	public void writeToFileNEmail(String pEmail, String ccEmail,
			String bccEmail, String subject, String pQuotationRef,
			String postRef, boolean isCompleted, String pFrom, String emailBody) {
		// System.out.println(pEmail);

		/*
		 * String subject = "Quotation Ref : "+pQuotationRef; //this will be the
		 * subject of the email
		 */

		ServiceProperties.getInstance().loadProperties();
		Properties props = ServiceProperties.getInstance().getProperties();
		Session session = Session.getDefaultInstance(props);
		StringBuilder sb = new StringBuilder();
		sb.append(emailBody);
		String disclaimer = "<p>" + this.getDisclaimer() + "</p>";
		sb.append(disclaimer);
		// sb.append("Privacy and Confidentiality Notice"+
		// "This e-mail and any file attachments transmitted with it are confidential and intended solely for the personal attention of the addressee and may contain confidential and/or privileged information.  If you are not the intended recipient any unauthorised use, copying, disclosure or other access is strictly prohibited.  Senders of messages shall be taken to consent to the monitoring and recording of e-mails addressed to our employees.  The views expressed in this communication may not be those of Next Control Systems Limited.  Any representation made in this e-mail is subject to contract and should not be taken to create any legal relations.  If you have received this e-mail in error, please notify the sender and immediately delete the e-mail and all attachments."+
		// "Disclaimer"+
		// "The contents of this e-mail or any file attachments may contain viruses that could damage your own computer system.  Next Control Systems Limited will not accept liability for any damage which you sustain as a result of computer viruses introduced by this e-mail or any file attachments and you must rely upon your own virus checks for protection.  E-mail transmissions cannot be guaranteed to be secure or error free."+
		// "Next Control Systems Ltd"+
		// "6 Farnborough Business Center"+
		// "Eelmoor Road"+
		// "Farnborough, Hampshire"+
		// "GU14 7XA"+
		// "P: +44 1252406398"+
		// "F: +44 1252406401"+
		// "Registered in England.  Company Registration No. 2540171 " );
		String quoteRef = pQuotationRef;
		if (pQuotationRef.indexOf("\\") != -1 && isCompleted) {
			quoteRef = pQuotationRef.substring(0, pQuotationRef.indexOf("\\"));
		}
		try {
			// construct the text body part
			MimeBodyPart textBodyPart = new MimeBodyPart();
			// textBodyPart.setHeader("Content-Type", "text/html");
			// textBodyPart.setText(sb.toString());
			textBodyPart.setContent(sb.toString(), "text/html");
			// now write the PDF content to the output stream
			this.createPDF();
			byte[] bytes = baos.toByteArray();
			// construct the pdf body part
			DataSource dataSource = new ByteArrayDataSource(bytes,
					"application/pdf");
			MimeBodyPart pdfBodyPart = new MimeBodyPart();
			pdfBodyPart.setDataHandler(new DataHandler(dataSource));
			if (!postRef.isEmpty()) {
				pdfBodyPart.setFileName(quoteRef + "_" + postRef + ".pdf");
			} else {
				pdfBodyPart.setFileName(quoteRef + ".pdf");

			}
			pdfBodyPart.setFileName(quoteRef + ".pdf");
			// construct the mime multi part
			MimeMultipart mimeMultipart = new MimeMultipart();
			mimeMultipart.addBodyPart(textBodyPart);
			mimeMultipart.addBodyPart(pdfBodyPart);
			// create the sender/recipient addresses
			InternetAddress iaSender = new InternetAddress(pFrom);
			// InternetAddress iaRecipient = new InternetAddress(pEmail);
			// construct the mime message
			MimeMessage mimeMessage = new MimeMessage(session);
			// mimeMessage.setSender(iaSender);
			mimeMessage.setFrom(iaSender);
			mimeMessage.setSubject(subject);
			mimeMessage.setRecipients(Message.RecipientType.TO, pEmail);
			mimeMessage.setRecipients(Message.RecipientType.CC, ccEmail);
			mimeMessage.setRecipients(Message.RecipientType.BCC, bccEmail);
			mimeMessage.setContent(mimeMultipart);
			// send off the email
			Transport.send(mimeMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {

			if (null != baos) {
				try {
					baos.flush();
					baos = null;
				} catch (Exception ex) {
				}
			}
		}
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	private String getDisclaimer() {
		String disclaimer = "";
		InputStream stream = this.getClass().getResourceAsStream("/footer.txt");
		try {
			byte[] disclaimerBytes = new byte[1500];
			while (!(-1 == (stream.read()))) {

				stream.read(disclaimerBytes);
			}
			for (int i = 0; i <= disclaimerBytes.length - 1; i++) {

				disclaimer += ((char) disclaimerBytes[i]);

			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.out.println("Reading disclaimer file failed");

		}
		return disclaimer;
	}

	public boolean isLoading() {
		return loading;
	}

	public void setLoading(boolean loading) {
		this.loading = loading;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getLoadingClass() {
		return loadingClass;
	}

	public void setLoadingClass(String loadingClass) {
		this.loadingClass = loadingClass;
	}

	@PreDestroy
	public void destroyBean() {
		this.baos = null;
		this.contents = null;
		this.document = null;
		this.facesContext = null;
		this.formater = null;
		this.intFormat = null;
		this.quotationBean = null;
		this.userInfo = null;
		this.writer = null;
		// System.out.println("DESTROYED PDFEXPORTERHTMLBEAN .....");
	}
}
