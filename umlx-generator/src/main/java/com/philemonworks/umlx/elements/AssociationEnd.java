package com.philemonworks.umlx.elements;

import java.util.Map;
import com.philemonworks.writer.XMLWriter;

/**
 * Insert the type's description here. Creation date: (3-12-2003 16:33:52)
 * 
 * @author: Ernest Micklei
 */
public class AssociationEnd extends ModelElement {
	public String type = "Object";
	public boolean navigatable = false;
	public String multiplicity = "";

	/**
	 * AssociationEnd constructor comment.
	 */
	public AssociationEnd() {
		super();
	}
	public AssociationEnd(String typeName, String roleName, String itsMultiplicity, boolean isNavigatable) {
		super();
		this.name = roleName;
		this.type = typeName;
		this.multiplicity = itsMultiplicity;
		this.navigatable = isNavigatable;
	}
	public void printXMLOn(XMLWriter writer) {
		Map att = writer.newMap("name", name);
		att.put("class", type);
		att.put("multiplicity", multiplicity);
		writer.tag("end", att, true);
	}
}
