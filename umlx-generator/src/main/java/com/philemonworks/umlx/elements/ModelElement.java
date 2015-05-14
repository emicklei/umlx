package com.philemonworks.umlx.elements;

import com.philemonworks.umlx.XMLExporter;
import com.philemonworks.util.ToStringBuilder;
import com.philemonworks.writer.XMLWriter;

/**

 * @author: Ernest Micklei
 */
public abstract class ModelElement {
	public String fileName = null;
	public String name = "missing name";

	public ModelElement() {
		super();
	}

	public void printXMLOn(XMLWriter writer) {
	}

	public void printXMLReferenceOn(XMLWriter writer) {
		writer.opentag(this.tagName());
		if (fileName != null)
			writer.attribute("file", fileName);
		else
			writer.attribute("file", name);
		writer.closeemptytag();
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 10:38:44)
	 */
	public String tagName() {

		return "missing tagName";
	}
	public String getFileExtension() {
		return "." + this.tagName() + ".umlx";
	}
	public String toString() {
		return ToStringBuilder.build(this);
	}
	public void exportTo(String dir) {
		XMLExporter ex = new XMLExporter();
		ex.setFilePath(dir);
		ex.export(this);
	}
}
