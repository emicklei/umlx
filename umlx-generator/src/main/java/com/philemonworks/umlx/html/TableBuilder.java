package com.philemonworks.umlx.html;

import java.util.Iterator;
import com.philemonworks.umlx.Utils;
import com.philemonworks.umlx.elements.Association;
import com.philemonworks.umlx.elements.AssociationEnd;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Operation;
import com.philemonworks.umlx.elements.Package;
import com.philemonworks.umlx.elements.Parameter;
import com.philemonworks.writer.Table;

public class TableBuilder extends Object {
	private static final int CLASS_COLUMNS = 1;
	public static String STYLE;
	int col = 1;
	int row = 1;
	public Table table;
	private boolean noNamespaceForTypes = false;
	private Package currentPackage;
	static {
		STYLE = "body{font-family:arial;} ";
		STYLE += ".uml_type{background:#FFFFCC;border-style:solid;border-color:#990033;border-width:1px} ";
		STYLE += ".uml_type_reference{background:#FFFFCC;border-style:dashed;border-color:#990033;border-width:1px} ";
		STYLE += "td{padding:0;white-space:nowrap;font-size:8pt} ";
		STYLE += "td.relation_left{padding:0;border-bottom: 1px solid #990033;text-align:left;} ";
		STYLE += "td.relation_right{padding:0;border-bottom: 1px solid #990033;text-align:right;} ";
		STYLE += "td.relation_center{padding:0;border-bottom: 1px solid #990033;text-align:center;} ";
		STYLE += "hr{height:1px;color:#990033;background:#990033;} ";
		STYLE += ".uml_typename{font-weight:bold;text-align:center;}";
		STYLE += "td.generalization{background-image: url(generalization.png); background-position:center right; background-repeat: no-repeat}";
		STYLE += "td.generalization_rev{background-image: url(generalization_rev.png); background-position:center right; background-repeat: no-repeat}";
		STYLE += "td.implements{background-image: url(implements.png); background-position:center right; background-repeat: no-repeat}";
		STYLE += "h5{font-color:#CCCCCC;font-size:80%}";
	}

	public void addNameTo(String name, StringBuffer buffer) {
		buffer.append("<b>" + name + "</b>");
	}
	public Table buildForClassIn(Class umlxClass, Model umlxModel) {
		this.init();
		int actualRowsRight = 0;
		TableBuilder builder = this.newBuilder();
		// superclass
		builder.initForType();
		if (builder.insertSuperclass(umlxClass)) {
			// Apply reference style if out of this package			
			if (this.isFromCurrentPackage(umlxClass.superclassName))
				builder.setStyleForReference();
			Table.Cell cell = this.table.put(row, col + 1, "&nbsp;");
			cell.setAttribute("class", "generalization");
			cell.setAttribute("width", "100px");
			this.putElement(row,col+2,builder.table);
			row += 2;
			actualRowsRight += builder.table.getMaxRows();
		}
		// interfaces
		for (Iterator iti = umlxClass.realizedInterfaceNames.iterator(); iti.hasNext();) {
			String each = (String) iti.next();
			builder.initForType();
			builder.insertInterface(each);
			// Apply reference style if out of this package			
			if (this.isFromCurrentPackage(each))
				builder.setStyleForReference();
			Table.Cell cell = this.table.put(row, col + 1, "&nbsp;");
			cell.setAttribute("class", "implements");
			cell.setAttribute("width", "100px");
			this.putElement(row, col + 2, builder.table);
			row++;
			actualRowsRight += builder.table.getMaxRows();
		}
		// associations
		for (Iterator ita = umlxModel.getAssociationsTo(umlxClass).iterator(); ita.hasNext();) {
			Association assoc = (Association) ita.next();
			builder.initForType();
			builder.insertAssociationEndFrom(assoc, umlxClass);
			TableBuilder sub = this.newBuilder();
			sub.buildForAssociationFrom(assoc, umlxClass);
			sub.table.setAttribute("cellspacing", "0");
			this.putElement(row, col + 1, sub.table);
			this.putElement(row, col + 2, builder.table);
			row++;
			actualRowsRight += (builder.table.getMaxRows()*2);
		}
		// subclasses
		for (Iterator its = umlxModel.getLocalSubclassesFor(umlxClass).iterator(); its.hasNext();) {
			Class sub = (Class) its.next();
			builder.initForType();
			builder.insertSubclass(sub);
			if (this.isFromCurrentPackage(sub.name))
				builder.setStyleForReference();
			Table.Cell cell = this.table.put(row, col + 1, "&nbsp;");
			cell.setAttribute("class", "generalization_rev");
			cell.setAttribute("width", "100px");
			this.putElement(row, col + 2, builder.table);
			row++;
			actualRowsRight += builder.table.getMaxRows();
		}
		// class
		builder.initForType();
		builder.insert(umlxClass);
		int rowsLeft = builder.table.getMaxRows();
		if (actualRowsRight > rowsLeft) builder.insertEmptyLines(actualRowsRight - rowsLeft);
		this.table.put(1, 1, builder.table.getMaxRows(), builder.table.getMaxColumns(), builder.table).setAttribute("valign","top");
		return table;
	}
	public Table buildForInterfaceIn(Interface umlxInterface, Model umlxModel) {
		this.init();
		int rowsOnRight = 0;
		TableBuilder builder = this.newBuilder();
		// superclass
		builder.initForType();
		if (builder.insertSuperclass(umlxInterface)) {
			rowsOnRight += 2;
			Table.Cell cell = this.table.put(row, col + 1, "&nbsp;");
			cell.setAttribute("class", "generalization");
			cell.setAttribute("width", "100px");
			this.putElement(row, col + 2, builder.table);
			row += 2;
		}
		// interface
		builder.initForType();
		builder.insertStereotype("Interface");
		builder.insert(umlxInterface);
		this.table.put(1, 1, builder.table.getMaxRows(), builder.table.getMaxColumns(), builder.table);
		return table;
	}
	private void buildForAssociationFrom(Association assoc, Class umlxClass) {
		this.init();
		AssociationEnd end = assoc.endFor(umlxClass.name);
		AssociationEnd otherEnd = assoc.otherEndFor(umlxClass.name);
		String endMultiplicity = end.multiplicity;
		if (endMultiplicity.length() == 0)
			endMultiplicity = "&nbsp;";
		String otherEndMultiplicity = otherEnd.multiplicity;
		if (otherEndMultiplicity.length() == 0)
			otherEndMultiplicity = "&nbsp;";
		String endName = end.name;
		if (endName.length() == 0)
			endName = "&nbsp;";
		String otherEndName = otherEnd.name;
		if (otherEndName.length() == 0)
			otherEndName = "&nbsp;";
		this.table.setAttribute("width", "100%");
		this.table.put(row, col, endMultiplicity).setAttribute("class", "relation_left");
		this.table.put(row++, col + 1, otherEndMultiplicity).setAttribute("class", "relation_right");
		this.table.put(row, col, "<div align=left>" + endName + "</div>");
		this.table.put(row++, col + 1, "<div align=right>" + otherEndName + "</div>");
	}
	public boolean insertAssociationEndFrom(Association assoc, Class umlxClass) {
		String typeName = assoc.otherEndFor(umlxClass.name).type;
		// Apply reference style if defined elsewhere in diagram of this package
		if (this.isFromCurrentPackage(typeName))
			this.setStyleForReference();
		StringBuffer buffer = new StringBuffer();
		this.addNameTo("<div align=\"center\">" + Utils.shortNameFrom(typeName) + "</div>", buffer);
		table.put(row++, col, buffer);
		return this.insertPackage(Utils.packageNameFrom(typeName));
	}
	public void init() {
		row = 1;
		col = 1;
		table = new Table();
		table.firstRowIsHeader = false;
	}
	public void initForType() {
		this.init();
		table.firstRowIsHeader = false;
		table.setAttribute("class", "uml_type");
		table.setAttribute("width", "100%");
	}
	private void setStyleForReference(){
		table.setAttribute("class", "uml_type_reference");
	}
	public void insert(Attribute umlxAttribute) {
		StringBuffer buffer = new StringBuffer();
		String vis = "&nbsp;-";
		if (umlxAttribute.isProtected)
			vis = "#";
		if (umlxAttribute.isPublic)
			vis = "+";
		buffer.append(vis);
		buffer.append(' ');
		this.addNameTo(umlxAttribute.name, buffer);
		buffer.append(" : ");
		buffer.append(noNamespaceForTypes ? Utils.shortNameFrom(umlxAttribute.type) : umlxAttribute.type);
		table.put(row, col, buffer);
	}
	/**
	 * Add an element for a subclass and answer whether a link to another package was added.
	 * @param umlxClass
	 * @return true if umlxClass is from another package
	 */
	public boolean insertSubclass(Class umlxClass) {
		String subName = Utils.shortNameFrom(umlxClass.name);
		table.put(row++, col, subName).setAttribute("class", "uml_typename");
		return this.insertPackage(Utils.packageNameFrom(umlxClass.name));
	}
	public void insert(Interface umlxClass) {
		Table.Cell nameCell = table.put(row++, col, 1, CLASS_COLUMNS, Utils.shortNameFrom(umlxClass.name));
		nameCell.setAttribute("class", "uml_typename");
		insertLine();
		for (Iterator it = umlxClass.attributes.iterator(); it.hasNext();) {
			Attribute att = (Attribute) it.next();
			this.insert(att);
			row++;
		}
		insertLine();
		for (Iterator it = umlxClass.operations.iterator(); it.hasNext();) {
			Operation op = (Operation) it.next();
			this.insert(op);
			row++;
		}
	}
	public void insert(Operation umlxOperation) {
		StringBuffer buffer = new StringBuffer();
		String vis = "&nbsp;-";
		if (umlxOperation.isProtected)
			vis = "#";
		if (umlxOperation.isPublic)
			vis = "+";
		buffer.append(vis);
		buffer.append(' ');
		this.addNameTo(umlxOperation.name, buffer);
		buffer.append(" (");
		for (Iterator it = umlxOperation.parameters.iterator(); it.hasNext();) {
			Parameter p = (Parameter) it.next();
			buffer.append(noNamespaceForTypes ? Utils.shortNameFrom(p.type) : p.type);
			if (it.hasNext())
				buffer.append(',');
		}
		buffer.append(") : ");
		buffer.append(noNamespaceForTypes ? Utils.shortNameFrom(umlxOperation.returnType) : umlxOperation.returnType);
		table.put(row, col, buffer);
	}
	public boolean insertInterface(String interfaceName) {
		this.insertStereotype("Interface");
		table.put(row++, col, Utils.shortNameFrom(interfaceName)).setAttribute("class", "uml_typename");
		return this.insertPackage(Utils.packageNameFrom(interfaceName));
	}
	private void insertStereotype(String stereotype) {
		table.put(row++, col, "<div align=\"center\">&lt;&lt;" + stereotype + "&gt;&gt;</div>");
	}
	private void insertLine() {
		table.put(row++, col, 1, CLASS_COLUMNS, "<hr noshade/>");
	}
	private void insertEmptyLines(int howMany) {
		for (int i=howMany;i>0;i--) table.put(row++, col, 1, CLASS_COLUMNS, "&nbsp;");
	}
	/**
	 * Add a hyperlink to another package unless the argument package is the current one.
	 * Return whether a link was added.
	 * @param packageName
	 * @return true if a link was added
	 */
	public boolean insertPackage(String packageName) {
		if (packageName.equals(currentPackage.name))
			return false;
		String href = "<a href=\"" + packageName + ".html\">" + packageName + "</a>";
		table.put(row++, col, 1, CLASS_COLUMNS, "<div align=\"center\">(from " + href + ")</div>");
		return true;
	}
	/**
	 * @param umlxClass
	 * @return whether the superclass entry was added to the table
	 */
	public boolean insertSuperclass(Interface umlxClass) {
		String superName = Utils.shortNameFrom(umlxClass.superclassName);
		if (superName == null || ("Object".equals(superName)))
			return false;
		table.put(row++, col, superName).setAttribute("class", "uml_typename");
		this.insertPackage(Utils.packageNameFrom(umlxClass.superclassName));
		return true;
	}
	public void setNoNamespaceForTypes(boolean noNamespaceForAttributes) {
		this.noNamespaceForTypes = noNamespaceForAttributes;
	}
	private TableBuilder newBuilder() {
		TableBuilder builder = new TableBuilder();
		builder.setCurrentPackage(this.getCurrentPackage());
		builder.setNoNamespaceForTypes(noNamespaceForTypes);
		return builder;
	}
	public Package getCurrentPackage() {
		return currentPackage;
	}
	public void setCurrentPackage(Package currentPackage) {
		this.currentPackage = currentPackage;
	}
	private boolean isFromCurrentPackage(String typeName){
		return Utils.packageNameFrom(typeName).equals(currentPackage.name);
	}	
	private void putElement(int row, int col, Table elementTable){
		this.table.put(row, col, elementTable).setAttribute("valign","top");
	}
}
