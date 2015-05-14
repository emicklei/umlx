package com.philemonworks.umlx.elements;

/**
 * Insert the type's description here.
 * Creation date: (3-12-2003 16:32:54)
 * @author: Ernest Micklei
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.philemonworks.umlx.Utils;
import com.philemonworks.writer.XMLWriter;

public class Model extends ModelElement {
	private Map subclasses = null; // will be lazy initialized
	private List packages = new ArrayList();
	public List associations = new ArrayList();

	/**
	 * Model constructor comment.
	 */
	public Model() {
		super();
	}
	/**
	 * Model constructor comment.
	 */
	public Model(String modelName, Package[] itsPackages, Association[] itsAssociations) {
		super();
		name = modelName;
		packages = Utils.vectorFromArray(itsPackages);
		associations = Utils.vectorFromArray(itsAssociations);
	}
	/**
	 * Insert the method's description here. Creation date: (3-12-2003 15:38:11)
	 */
	public void printXMLOn(XMLWriter writer) {
		writer.opentag(this.tagName());
		writer.attribute("name", name);
		writer.closetag();
		for (Iterator it = packages.iterator(); it.hasNext(); ((Package) it.next()).printXMLReferenceOn(writer));
		for (Iterator it = associations.iterator(); it.hasNext(); ((ModelElement) it.next()).printXMLOn(writer));
		writer.end();
	}
	public String tagName() {
		return "model";
	}
	public void exportTo(String dir) {
		super.exportTo(dir);
		for (Iterator it = packages.iterator(); it.hasNext(); ((Package) it.next()).exportTo(dir))
			;
	}
	public List getAssociationsTo(Class aClass) {
		List assocs = new ArrayList();
		for (int i = 0; i < associations.size(); i++) {
			Association each = (Association) associations.get(i);
			if (aClass.name.equals(each.role1.type))
				assocs.add(each);
			else if (aClass.name.equals(each.role2.type))
				assocs.add(each);
		}
		return assocs;
	}
	public List getLocalSubclassesFor(Class umlxClass) {
		if (subclasses == null)
			this.buildSubclassesMap();
		List localSubclasses = (List) subclasses.get(umlxClass.name);
		if (localSubclasses == null)
			return new ArrayList();
		return localSubclasses;
	}
	private void buildSubclassesMap() {
		subclasses = new HashMap();
		// Walk through all classes and collect all subclasses
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				List subs = (List) subclasses.get(eachClass.superclassName);
				if (subs == null) {
					subs = new ArrayList();
					subclasses.put(eachClass.superclassName, subs);
				}
				subs.add(eachClass);
			}
		}
	}
	public List getPackages(){
		return packages;
	}
	public void setPackages(List newList){
		packages = newList;
	}	
}
