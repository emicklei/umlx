package com.philemonworks.umlx.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.philemonworks.writer.XMLWriter;

public class Operation extends ModelElement {

	public String returnType = "void";
	public List parameters = new ArrayList();
	public boolean isPublic = true;
	public boolean isProtected = false;
	/**
	 * Operation constructor comment.
	 */
	public Operation() {
		super();
	}
	public Operation(String newName, String itsReturnType, boolean bePublic) {
		super();
		this.name = newName;
		this.returnType = itsReturnType;
		this.isPublic = bePublic;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 15:38:11)
	 */
	public void printXMLOn(XMLWriter writer) {

		Map map = writer.newMap("name", name);
		if (returnType != null)
			if (!returnType.equals("void"))
				map.put("return", returnType);
		if (!isPublic)
			if (isProtected)
				map.put("protected", "true");
			else
				map.put("private", "true");
		writer.tag("operation", map, false);
		//Parameters
		Iterator all = parameters.iterator();
		while (all.hasNext()) {
			((ModelElement) all.next()).printXMLOn(writer);
		}
		writer.end();
	}
}
