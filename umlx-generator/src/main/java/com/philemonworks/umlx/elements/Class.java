package com.philemonworks.umlx.elements;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.philemonworks.writer.XMLWriter;
/**
 * This class represents an UMLX class definition from the UMLX meta model.
 * Creation date: (3-12-2003 10:29:35)
 * @author: Ernest Micklei
 */
public class Class extends Interface {
	//if proxy
	public String file = null;
	public List realizedInterfaceNames = new ArrayList();
	/**
	 * Class constructor comment.
	 */
	public Class() {
		super();
		name = "Object";
	}
	public void addPrivateOperation(String opName, String opTypeName, String parameterTypeNamesString) {
		Operation op = new Operation(opName, opTypeName, false);
		String[] parameterTypeNames = {
		};
		for (int i = 0; i < parameterTypeNames.length; i++)
			op.parameters.add(new Parameter(parameterTypeNames[i]));
		this.operations.add(op);
	}
	public void addPrivateAttribute(String fieldName, String fieldTypeName) {
		Attribute newAttribute = new Attribute(fieldName, fieldTypeName, false);
		this.attributes.add(newAttribute);
	}
	public String tagName() {
		return "class";
	}
	public void addRealizedInterface(java.lang.Class interfaceClass) {
		realizedInterfaceNames.add(interfaceClass.getName());
	}
	protected void printXMLBodyOn(XMLWriter writer) {
		//Attributes
		Iterator all = realizedInterfaceNames.iterator();
		while (all.hasNext()) {
			String each = (String) all.next();
			writer.tag("interface", writer.newMap("name", each), false);
		}
		super.printXMLBodyOn(writer);
	}
}
