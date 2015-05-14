package com.philemonworks.umlx.elements;

/**
 * Insert the type's description here.
 * Creation date: (3-12-2003 10:48:41)
 * @author: Ernest Micklei
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.philemonworks.umlx.Utils;
import com.philemonworks.writer.XMLWriter;

public class Package extends ModelElement {

	public String namespace = "";
	public List classes = new ArrayList();
	public List interfaces = new ArrayList();
	/**
	 * Package constructor comment.
	 */
	public Package() {
		super();
	}
	public void exportTo(String dir) {

		super.exportTo(dir);
		// all classes
		for (int c = 0; c < classes.size(); c++)
			 ((Class) classes.get(c)).exportTo(dir);
		// all interfaces
		for (int c = 0; c < interfaces.size(); c++)
			 ((Interface) interfaces.get(c)).exportTo(dir);
	}
	public Package(String packageName, String namespaceOfClasses, Class[] itsClasses) {
		super();
		name = packageName;
		namespace = namespaceOfClasses;
		classes = Utils.vectorFromArray(itsClasses);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 15:38:11)
	 */
	public void printXMLOn(XMLWriter writer) {

		writer.tag("package", writer.newMap("name", name, "namespace", namespace), false);
		//Classes by reference
		Iterator all = classes.iterator();
		while (all.hasNext()) {
			((ModelElement) all.next()).printXMLReferenceOn(writer);
		}
		all = interfaces.iterator();
		while (all.hasNext()) {
			((ModelElement) all.next()).printXMLReferenceOn(writer);
		}
		writer.end();
	}
	public String tagName() {
		return "package";
	}
	public void addClass(Class newClass) {
		classes.add(newClass);
		newClass.setPackage(this);
	}

	/**
	 * @param result
	 */
	public void addInterface(Interface result) {
		interfaces.add(result);
		result.setPackage(this);
	}

}
