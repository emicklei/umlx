package com.philemonworks.umlx.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import org.apache.log4j.Logger;
import com.philemonworks.umlx.IExporter;
import com.philemonworks.umlx.XMLExporter;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;
import com.philemonworks.writer.HTMLWriter;
import com.philemonworks.writer.Table;

public class HTMLExporter extends XMLExporter implements IExporter {
	private static final Logger LOG = Logger.getLogger(HTMLExporter.class);
	private int exportcount = 0;
	private boolean noNamespaceForTypes = false;

	public void copyResource(String resourceName) throws IOException {
		File target = new File(filePath + File.separator + resourceName);
		LOG.info("Copying [" + resourceName + "] to [" + target.getPath() + "]");		
		InputStream imis = this.getClass().getClassLoader().getResourceAsStream(resourceName);
		FileOutputStream imos = new FileOutputStream(target);
		while (imis.available() > 0)
			imos.write(imis.read());
		imos.close();
	}
	public void exportIn(Package umlxPackage, Model umlxModel) {
		String localName = umlxPackage.name + ".html";
		try {
			exportcount++;
			new File(filePath).mkdirs();
			this.copyResource("generalization.png");
			this.copyResource("generalization_rev.png");
			this.copyResource("implements.png");
			Logger.getLogger(HTMLExporter.class).debug("Writing to " + filePath + File.separator + localName);
			FileOutputStream fos = new FileOutputStream(filePath + File.separator + localName);
			HTMLWriter w = new HTMLWriter(new PrintStream(fos));
			w.pretty = false;
			w.doctype();
			w.html();
			w.tagged("style", TableBuilder.STYLE, false);
			w.body();
			Table grid = new Table();
			grid.firstRowIsHeader = false;
			int gridrow = 1;
			int gridcol = 1;
			int maxcols = 2;
			grid.setAttribute("cellspacing", "10");
			for (Iterator it = umlxPackage.classes.iterator(); it.hasNext();) {
				Class each = (Class) it.next();
				grid.put(gridrow, gridcol, this.newBuilder(umlxPackage).buildForClassIn(each, umlxModel)).setAttribute("valign","top");
				gridcol++;
				if (gridcol > maxcols) {
					gridcol = 1;
					gridrow++;
				}
			}
			for (Iterator it = umlxPackage.interfaces.iterator(); it.hasNext();) {
				Interface each = (Interface) it.next();
				grid.put(gridrow, gridcol, this.newBuilder(umlxPackage).buildForInterfaceIn(each, umlxModel));
				gridcol++;
				if (gridcol > maxcols) {
					gridcol = 1;
					gridrow++;
				}
			}			
			w.table(grid);
			this.footer(w);
			w.end(); // body
			w.end(); // html
			fos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void export(Interface umlxInterface) {
		this.export((Class) umlxInterface);
	}
	public void export(Model umlxModel) {
		for (int p = 0; p < umlxModel.getPackages().size(); p++) {
			Package each = (Package) umlxModel.getPackages().get(p);
			this.exportIn(each, umlxModel);
		}
		Logger.getLogger(this.getClass()).info(
				"exported: " + exportcount + " HTML files in: " + filePath);
	}
	public boolean isNoNamespaceForTypes() {
		return noNamespaceForTypes;
	}
	public void setNoNamespaceForTypes(boolean noNamespaceForAttributes) {
		this.noNamespaceForTypes = noNamespaceForAttributes;
	}
	private TableBuilder newBuilder(Package currentPkg) {
		TableBuilder builder = new TableBuilder();
		builder.setCurrentPackage(currentPkg);
		builder.setNoNamespaceForTypes(this.isNoNamespaceForTypes());
		return builder;
	}
	private void footer(HTMLWriter w){
		w.h(5,"Generated on " + new Date() + " from UMLX, (c) PhilemonWorks.com");
	}
}