/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.umlx.maven;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import com.philemonworks.writer.XMLWriter;

/**
 * Reporter produces the umlx-report.xml using XDocs by scanning the generated output.
 * 
 * @author E.M.Micklei
 */
public class Reporter {
	private final static String REPORT = "umlx-report.xml";
	private final static String UMLXPATH = "umlx/";
	String umlxLocation;
	String reportLocation;

	/**
	 * 0 = input location of generated HTML/XML 1 = output location of umlx-report.xml
	 * 
	 * @param args
	 *        String[]
	 */
	public static void main(String[] args) {
		Reporter reporter = new Reporter();
		reporter.setUmlxLocation(args[0]);
		reporter.setReportLocation(args[1]);
		reporter.generate();
	}
	public String getReportLocation() {
		return reportLocation;
	}
	public void setReportLocation(String reportLocation) {
		this.reportLocation = reportLocation;
	}
	public String getUmlxLocation() {
		return umlxLocation;
	}
	public void setUmlxLocation(String umlxLocation) {
		this.umlxLocation = umlxLocation;
	}
	public void generate() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(reportLocation + File.separator + REPORT);
			XMLWriter xml = new XMLWriter(new PrintStream(fos));
			xml.pretty = true;
			xml.raw(XMLWriter.XMLHEADER).raw("\n\n");
			this.generateOn(xml);
			xml.flush();
		} catch (Exception e) {
			throw new RuntimeException("Report generation failed:" + e.getMessage(), e);
		} finally {
			if (fos != null) try { fos.close(); } catch(Exception ex) {};
		}
	}
	private void generateOn(XMLWriter xml) throws Exception {
		xml.tag("document");
		xml.tag("properties");
		xml.tagged("title", "UMLX Report", false);
		xml.end(); // properties
		xml.tag("body");
		this.generateBodyOn(xml);
		xml.end(); // body
		xml.end(); // document
	}
	private void generateBodyOn(XMLWriter xml) throws Exception {
		xml.tag("section", "name", "UML Class Diagrams");
		xml.tag("p");
		xml.raw("The following document contains links to UML diagrams generated with <a href=\"http://www.philemonworks.com/maven-umlx-plugin\">UMLX</a>");
		xml.end("p");
		xml.tag("table");
		xml.tag("tr");
		xml.tagged("th","Package",false);
		xml.end(); // tr
		
		File dir = new File(umlxLocation);
		String[] fileNames = new String[0]; // empty if nothing generated
		if (dir.exists())	 fileNames = dir.list();
		for (int f=0;f<fileNames.length;f++){
			String each = fileNames[f];
			if (each.endsWith(".html")) {
				String pkg = each.substring(0,each.length()-5);
				xml.tag("tr").tag("td");
				xml.tag("a",xml.newMap("href",UMLXPATH + each,"class","newWindow"),false);
				xml.print(pkg);
				xml.end(); // a				
				xml.end().end(); //td,tr
			}
		}
		xml.end(); // table
		
		xml.end(); // section
	}
}
