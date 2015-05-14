package com.philemonworks.umlx.elements;

import com.philemonworks.writer.XMLWriter;

/**
 * Insert the type's description here.
 * Creation date: (4-12-2003 15:20:54)
 * @author: Ernest Micklei
 */
public class Parameter extends ModelElement {

	public String type = null;
	/**
	 * Parameter constructor comment.
	 */
	public Parameter() {
		super();
	}
	public Parameter(String typeName) {
		super();
		this.type = typeName;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 15:38:11)
	 */
	public void printXMLOn(XMLWriter writer) {
		writer.tag("parameter", writer.newMap("name", name, "type", type), true);
	}
}
