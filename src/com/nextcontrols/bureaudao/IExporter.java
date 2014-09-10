package com.nextcontrols.bureaudao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.DocumentException;

public interface IExporter {
	public void export()throws DocumentException, MalformedURLException, IOException, ParseException;
	public void exportShowParts(boolean showParts)throws DocumentException, MalformedURLException, IOException, ParseException;
	public void writeToResponse(HttpServletResponse response,  ByteArrayOutputStream baos, String fileName ) throws IOException, DocumentException;
	public String pageRedirect();
}
