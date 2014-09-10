package com.nextcontrols.pagebeans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.nextcontrols.bureaudomain.Quotation;
import com.nextcontrols.utility.ServiceProperties;


@ManagedBean(name="pdfviewhtmlExp")
@SessionScoped
public class PDFViewFromHTMLPageBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FacesContext facesContext;
	ByteArrayOutputStream baos ;
	Document document;
	PdfWriter writer;
	Quotation quotation;
	boolean insertImage = false;
	int pageSection=0;
	public PDFViewFromHTMLPageBean() {

	}
	/**
	 * This method generates a PDF report 
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParseException
	 */

	public void createPDF() throws DocumentException, IOException {
		pageSection=0;
		facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
		UserInfoPageBean userInfo = (UserInfoPageBean)session.getAttribute("userInfo");

		baos = new ByteArrayOutputStream();
		document = new Document();
		if(userInfo.getUser().getCompany().getCountry().getCountry().compareToIgnoreCase("UK")==0){
			document.setPageSize(PageSize.A4);
		}else{
			document.setPageSize(PageSize.LETTER);
		}
		writer = PdfWriter.getInstance(document, baos);
		writer.setPageEvent(new PDFEventListener());
//		writer.setPageEvent(new TableHeader());
		document.setMargins(70, 70, 125, 0);
		document.open();
		PdfContentByte under = writer.getDirectContentUnder();
		String [] pageContents = quotation.getPagecontents().split("_");
		
//		System.out.println(pageContents[0]);
//		System.out.println(pageContents[1]);
//		System.out.println(pageContents[2]);
//		System.out.println(pageContents[3]);
//		System.out.println(pageContents[4]);
//		System.out.println(pageContents[5]);

		document.newPage();
		this.insertImage = false;
		pageSection=1;
		if (userInfo.getUser().getCompany().getCountry().getCountry().compareToIgnoreCase("UK") == 0) {
		under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator +"Bio Tech And Life Sciences1.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
		}else{
			under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
					File.separator+ "images" + File.separator +"Bio Tech And Life SciencesUS1.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
		}
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[0].getBytes()));

		document.setMargins(70, 90, 35, 48);
		document.newPage();
		this.insertImage = true;
		pageSection=2;
		if (userInfo.getUser().getCompany().getCountry().getCountry().compareToIgnoreCase("UK") == 0) {
		under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator +"Bio Tech And Life Sciences2.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
		}
		else{
		under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator +"Bio Tech And Life SciencesUS2.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
		}
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[1].getBytes()));

		document.setMargins(70, 70, 44, 0);
		document.newPage();
		this.insertImage = false;
		pageSection=3;
		under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
				File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"3.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[2].getBytes()));

		if(pageContents.length>3){
			document.newPage();
			this.insertImage = false;
			pageSection=4;
			under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
					File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"4.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[3].getBytes()));
		}
		if(pageContents.length>4){
			document.newPage();
			this.insertImage = false;
			pageSection=5;
			under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
					File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"5.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[4].getBytes()));
		}
		if(pageContents.length>5){
			document.setMargins(70, 90, 35, 0);
			document.newPage();
			this.insertImage = false;
			pageSection=6;
			under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
					File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"6.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[5].getBytes()));
		}
		if(pageContents.length>6){
			//System.out.println("size: "+pageContents.length);
			document.setMargins(70, 90, 35, 0);
			document.newPage();
			this.insertImage = false;
			pageSection=8;
			under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
					File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"6.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
			XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[6].getBytes()));	
		}
		//document.newPage();
		//under.addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
		//		File.separator+ "images" + File.separator + quotation.getFacilitytype()+"7.jpg", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
		//XMLWorkerHelper.getInstance().parseXHtml(writer, document, new ByteArrayInputStream(pageContents[6].getBytes()));
		document.close();
	}
	class PDFEventListener extends PdfPageEventHelper {
		protected Phrase header;

		protected PdfPTable footer;
		
//		@Override
//		public void onStartPage(PdfWriter writer, Document document) {
//			super.onStartPage(writer, document);
//			//System.out.println("-------------start page: "+writer.getPageNumber());
//			//System.out.println("start section: "+pageSection);
//			if(pageSection==2){
//				document.setMargins(70, 70, 44, 0);		
//			}
//		}
//		@Override
//		public void onEndPage(PdfWriter writer, Document document) {
//			// TODO Auto-generated method stub
//			super.onEndPage(writer, document);
//
//			//System.out.println("-------------end page: "+writer.getPageNumber());
//			//System.out.println("end section: "+pageSection);
//			if(pageSection==2){
//			try {
//				document.setMargins(70, 90, 90, 48);
//				HttpSession session = (HttpSession)facesContext.getExternalContext().getSession(false);
//				UserInfoPageBean userInfo = (UserInfoPageBean) session.getAttribute("userInfo");
//				if (userInfo.getUser().getCompany().getCountry().getCountry().compareToIgnoreCase("UK") == 0) {
//				writer.getDirectContentUnder().addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
//						File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"2.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
//				}
//				else{
//					writer.getDirectContentUnder().addImage(this.setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
//							File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"US2.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
//				}
//			} catch (DocumentException | IOException e) {
//				e.printStackTrace();
//			}
//			}
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
//				System.out.println("-------------start page: "+ writer.getPageNumber());
//				System.out.println("start section: " + pageSection);
//				System.out.println("start section2: " + pageSection2);

				if (pageSection == 2) {
					document.setMargins(70, 70, 90, 70);
				} else if (pageSection == 5) {
					document.setMargins(70, 90, 44, 0);
//					System.out.println("entered condition page section 5 start");
				}
				if(pageSection==3){
					document.setMargins(70, 70, 44, 70);
//					System.out.println("entered condition page section 3 start");
				}
//				if (pageSection == 7) {
//					document.setMargins(70, 90, 44, 0);
////					System.out.println("entered condition page section 7 start");
//				}
//				System.out.println("start margin: " + document.bottomMargin());
//				System.out.println("start margin top: " + document.topMargin());
			}

			@Override
			public void onEndPage(PdfWriter writer, Document document) {

				// super.onEndPage(writer, document);

//				System.out.println("-------------end page: "+ writer.getPageNumber());
//				System.out.println("end section: " + pageSection);
//				System.out.println("end section2: " + pageSection2);
				HttpSession session = (HttpSession) facesContext.getExternalContext()
						.getSession(false);
				UserInfoPageBean userInfo = (UserInfoPageBean) session.getAttribute("userInfo");
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
				} else if(pageSection==3){
					document.setMargins(70, 70, 44, 70);
//					System.out.println("entered condition page section 3 end");
				} 
//				if (pageSection2 == 7) {
	//
////						System.out.println("entered condition page section 7 end");
//						document.setMargins(70, 90, 44, 0);
//					
//				}
//				System.out.println("end margin: " + document.bottomMargin());
//				System.out.println("end margin top: " + document.topMargin());
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
	private class TableHeader extends PdfPageEventHelper {
		/**
		 * Adds a header to every page
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onStartPage(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */
//		public void onStartPage(PdfWriter writer, Document document) {
//			System.out.println("On start page: " + document.getPageNumber() + " " + insertImage);
//			if(insertImage){
//			    PDFViewFromHTMLPageBean.this.document.setMargins(70, 90, 35, 48);
//			}
//		}
		/**
		 * Adds a header to every page
		 * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(
		 *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document)
		 */

		public void onStartPage(PdfWriter writer, Document document) {
			try {
				if(insertImage){
					writer.getDirectContentUnder().addImage(setBackGroundImage(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("") + 
							File.separator+ "images" + File.separator + "Bio Tech And Life Sciences"+"2.png", document.getPageSize().getHeight(), document.getPageSize().getWidth()));
					document.add(new Paragraph());
					System.out.println(document.getPageSize().getHeight() + " " + document.getPageSize().getWidth());
				}
			} catch (BadElementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}





	public void export(Quotation pQuotation) throws DocumentException, IOException{
		quotation = pQuotation;
		createPDF();
		writeToResponse(((HttpServletResponse) facesContext.getExternalContext().getResponse()));
		facesContext.responseComplete();
	}


	public void writeToResponse(HttpServletResponse response) throws IOException, DocumentException {
		response.setContentType("application/pdf");
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control", "must revalidate, post-check=0, pre-check=0");
		response.setHeader("pragma", "public");
		String quoteRef=quotation.getQuotationref();
		System.out.println("quotate ref before: "+quoteRef);
		String postRef="";
		if (quotation.getQuotationref().indexOf("\\") != -1) {
			quoteRef=quotation.getQuotationref()
					.substring(0,
							quotation.getQuotationref().indexOf("\\"));
			postRef=quotation.getQuotationref()
					.substring(
							quotation.getQuotationref().indexOf("\\")+1,quotation.getQuotationref().length());
			System.out.println("quotate ref after: "+quoteRef);
			System.out.println("quotate post ref: "+postRef);
			response.setHeader("Content-disposition", "attachment;filename="+quoteRef+"_"+postRef+".pdf");
		}else{
			response.setHeader("Content-disposition", "attachment;filename="+quotation.getQuotationref()+".pdf");
		}
		response.setContentLength(baos.size());
		ServletOutputStream out = response.getOutputStream();
		baos.writeTo(out);
		baos.flush();
	}

	public Image setBackGroundImage(String path, float height, float width) throws BadElementException, MalformedURLException, IOException{
		//System.out.println(path);
		Image image = Image.getInstance (path);
		image.scaleAbsoluteHeight(document.getPageSize().getHeight());
		image.scaleAbsoluteWidth(document.getPageSize().getWidth());
		image.setAbsolutePosition(0, 0);
		return image;
	}

	public void writeToFileNEmail(String pEmail, String pQuotationRef){
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
			this.createPDF();
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

}

