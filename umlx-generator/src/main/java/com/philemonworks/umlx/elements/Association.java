package com.philemonworks.umlx.elements;

import com.philemonworks.writer.XMLWriter;

/**
 * Insert the type's description here. Creation date: (3-12-2003 16:32:39)
 * 
 * @author: Ernest Micklei
 */
public class Association extends ModelElement {
	public AssociationEnd role1 = null;
	public AssociationEnd role2 = null;

	/**
	 * Association constructor comment.
	 */
	public Association() {
		super();
	}
	public Association(AssociationEnd end1, AssociationEnd end2) {
		super();
		this.role1 = end1;
		this.role2 = end2;
	}
	public void printXMLOn(XMLWriter writer) {
		writer.tag("association");
		role1.printXMLOn(writer);
		role2.printXMLOn(writer);
		writer.end();
	}
	public String storageName() {
		return "association_" + role1.type + "_" + role1.name + "_" + role2.type + "_" + role2.name;
	}
	public AssociationEnd endFor(String roleType) {
		if (role1.type.equals(roleType))
			return role1;
		if (role2.type.equals(roleType))
			return role2;
		throw new RuntimeException("wrong roletype:" + roleType);
	}
	public AssociationEnd otherEndFor(String roleType) {
		if (role1.type.equals(roleType))
			return role2;
		if (role2.type.equals(roleType))
			return role1;
		throw new RuntimeException("wrong roletype:" + roleType);
	}
}
