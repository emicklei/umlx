package com.philemonworks.umlx.elements;

import java.util.Map;
import com.philemonworks.writer.XMLWriter;

/**
 * Insert the type's description here. Creation date: (3-12-2003 10:34:44)
 * 
 * @author: Ernest Micklei
 */
public class Attribute extends ModelElement {
	public String type = "Object";
	public boolean isPublic = true;
	public boolean isProtected = false;
	public boolean hasPrimitiveType = false;
	public boolean isStatic = false;

	/**
	 * Attribute constructor comment.
	 */
	public Attribute() {
		super();
	}
	public Attribute(String newName, String typeName, boolean bePublic) {
		super();
		this.name = newName;
		this.type = typeName;
		this.isPublic = bePublic;
	}
	public boolean isArray(){
		return type.endsWith("[]");		
	}
	/**
	 * Pre: || isArray ||
	 * @return
	 */
	public String getElementTypeName(){
		return type.substring(0,type.length()-2);
	}
	/**
	 * Insert the method's description here. Creation date: (3-12-2003 15:38:11)
	 */
	public void printXMLOn(XMLWriter writer) {
		Map att = writer.newMap("name", name);
		att.put("type", type);
		if (!isPublic)
			if (isProtected)
				att.put("protected", "true");
			else
				att.put("private", "true");
		if (isStatic)
			att.put("static", "true");
		writer.tag("attribute", att, true);
	}
}
