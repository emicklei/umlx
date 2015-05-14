/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 18-jun-2004 : created
 */
package com.philemonworks.umlx.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.philemonworks.umlx.Utils;
import com.philemonworks.writer.XMLWriter;

/**
 * @author emicklei
 *
 */
public class Interface extends ModelElement {

	public String superclassName = "Object";
	public List operations = new ArrayList();
	public List attributes = new ArrayList();
	public List superInterfaces = new ArrayList();
	protected Package myPackage;

	public String tagName() {
		return "interface";
	}
	public Package getPackage() {
		return myPackage;
	}
	public String getQualifiedName() {
		return this.name;
	}
	public void setPackage(Package itsPackage) {
		myPackage = itsPackage;
	}

	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 15:38:11)
	 */
	public void printXMLOn(XMLWriter writer) {

		writer.opentag(this.tagName());
		writer.attribute("name", Utils.shortNameFrom(name));
		writer.attribute("superclass", Utils.shortNameFrom(superclassName)); // Bug, only correct if in same namespace
		writer.closetag();
		this.printXMLBodyOn(writer);
		writer.end();
	}
	protected void printXMLBodyOn(XMLWriter writer) {
		//Attributes
		Iterator all = attributes.iterator();
		while (all.hasNext()) {
			((ModelElement) all.next()).printXMLOn(writer);
		}
		//Operations
		all = operations.iterator();
		while (all.hasNext()) {
			((ModelElement) all.next()).printXMLOn(writer);
		}
	}
}
