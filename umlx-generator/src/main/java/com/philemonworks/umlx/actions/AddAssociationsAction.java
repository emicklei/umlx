package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.Utils;
import com.philemonworks.umlx.elements.Association;
import com.philemonworks.umlx.elements.AssociationEnd;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;

/**
 * AddAssociationsAction tries to find associations between classes from the model.
 * 
 * @author E.M.Micklei
 */
public class AddAssociationsAction implements GenerationAction {
	private static final Object CLASSNOTAVAIL = new Object();
	private Model model = null;
	private HashMap typeMap = new HashMap();
	private HashMap associationEndMap = new HashMap();	
	private HashSet unresolvedTypeNames = new HashSet();
	private List unresolvedTypePrefixes = new ArrayList();
	private List predefinedAssociations = new ArrayList();

	public AddAssociationsAction(){ super(); }
	public AddAssociationsAction(Generator g){ super(); g.addPostAction(this); }
	
	private void initUnresolved() {
		unresolvedTypePrefixes.add("java");
	}
	private boolean isUnresolved(String typeName) {
		if (unresolvedTypeNames.contains(typeName))
			return true;
		Iterator it = unresolvedTypePrefixes.iterator();
		while (it.hasNext()) {
			String each = (String) it.next();
			if (typeName.startsWith(each))
				return true;
		}
		return false;
	}
	public void add(Association anAssociation){
		predefinedAssociations.add(anAssociation);
	}
	public AssociationEnd addAssociationEnd(String containerType,String attributeName,String elementType){
		AssociationEnd end = new AssociationEnd();
		end.name = attributeName;
		end.type = elementType;
		this.associationEndsFor(containerType).add(end);
		return end;
	}
	/**
	 * Try to extract associations from all the classes that are part of the model
	 * First build a map of association end, then try to find uni- and bi-directional associations.
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {		
		this.initUnresolved();
		model = umlxModel;
		// Walk through all classes and remove fields that should be excluded
		List packages = umlxModel.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				List attributes = eachClass.attributes;
				for (int a = 0; a < attributes.size(); a++) {
					Attribute eachAttribute = (Attribute) attributes.get(a);
					this.resolveAsAssociationEnd(eachClass, eachAttribute);
				}
			}
		}
		this.buildAssociations();
	}
	private void buildAssociations(){
		Iterator it = associationEndMap.keySet().iterator();
		while (it.hasNext()){
			String typeName = (String)it.next();
			List ends = (List)associationEndMap.get(typeName);
			while (ends.size() > 0){
				Association link = new Association();
				AssociationEnd end = (AssociationEnd)ends.remove(0);
				link.role1 = end;
				AssociationEnd otherEnd = this.removeMatchingAssociationEnd(typeName,end);
				if (otherEnd == null) { // unidirectional
					link.role2 = new AssociationEnd();
					link.role2.type = typeName;
					link.role2.name = "";
				} else { // bidirectional
					link.role2 = otherEnd;
				}
				model.associations.add(link);
				this.removeAttributeNamed(typeName,end.name);
				ends.remove(end);
			}
		}
		// Temporary modify assoc type names
		for (int a=0;a<predefinedAssociations.size();a++) {
			Association assoc = (Association)predefinedAssociations.get(a);
		}	
		model.associations.addAll(predefinedAssociations);
	}
	private AssociationEnd removeMatchingAssociationEnd(String typeName,AssociationEnd otherEnd){
		return null;
	}
	private void removeAttributeNamed(String qualifiedName,String attributeName){
		Interface theType = this.resolveTypeOrNull(qualifiedName);
		if (theType == null){
			Logger.getLogger(AddAssociationsAction.class).error("Type not found:" + qualifiedName);
			return;
		}
		List newAttributes = new ArrayList();
		for (int a=0;a<theType.attributes.size();a++){
			Attribute each = (Attribute)theType.attributes.get(a);
			if (!each.name.equals(attributeName))
				newAttributes.add(each);		
		}
		if (theType.attributes.size() == newAttributes.size()) {
			Logger.getLogger(AddAssociationsAction.class).error("Attribute not found:" + attributeName + " in:" + theType);
		}
		theType.attributes = newAttributes;
	}
	private boolean resolveAsAssociationEnd(Class containerType, Attribute anAttribute) {
		if (anAttribute.hasPrimitiveType) return false;
		if (this.isUnresolved(anAttribute.type)) return false;
		if (anAttribute.isArray()){
			Interface typeOrNull = this.resolveTypeOrNull(anAttribute.getElementTypeName());
			if (typeOrNull == null) {
				unresolvedTypeNames.add(anAttribute.getElementTypeName());
				return false;
			}
		} else {
			Interface typeOrNull = this.resolveTypeOrNull(anAttribute.type);
			if (typeOrNull == null) {
				unresolvedTypeNames.add(anAttribute.type);
				return false;
			}
		}
		AssociationEnd end = new AssociationEnd();
		end.name = anAttribute.name;
		end.type = anAttribute.isArray() ? anAttribute.getElementTypeName() : anAttribute.type;
		end.multiplicity = anAttribute.isArray() ? "*" : "";
		this.addAssociationEndFor(end,containerType);
		return true;
	}
	private List associationEndsFor(String containerType){
		List ends = (List)associationEndMap.get(containerType);
		if (ends == null) {
			ends = new ArrayList();
			associationEndMap.put(containerType,ends);
		}
		return ends;
	}
	private void addAssociationEndFor(AssociationEnd end, Class containerClass){
		List ends = this.associationEndsFor(containerClass.getQualifiedName());
		// Check whether an end was specfied explicitly using addAssociationEnd(....)
		for (int e=0;e<ends.size();e++){
			AssociationEnd knownEnd = (AssociationEnd)ends.get(e);
			if (knownEnd.name.equals(end.name)) return;
		}
		ends.add(end);
	}
	private Interface resolveTypeOrNull(String typeName){
		Object entry = typeMap.get(typeName);
		if (entry == CLASSNOTAVAIL) return null;
		if (entry != null) return (Interface)entry;
		Interface typeOrNull = this.findClassOrInterfaceOrNull(typeName);
		if (typeOrNull == null) {
			typeMap.put(typeName,CLASSNOTAVAIL);	
		} else {
			typeMap.put(typeName,typeOrNull);
		}
		return typeOrNull;
	}
	private Interface findClassOrInterfaceOrNull(String typeName){
		List packages = model.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				if (eachClass.getQualifiedName().equals(typeName)) 
					return eachClass;
				else {
					//	Retry using the non-qualified name, assumes no duplicate short names.
					String unqualified = Utils.shortNameFrom(eachClass.getQualifiedName());
					if (unqualified.equals(typeName)) return eachClass;
				}
			}
			List interfaces = eachPackage.interfaces;
			for (int c = 0; c < interfaces.size(); c++) {
				Interface eachInterface = (Interface) interfaces.get(c);
				if (eachInterface.getQualifiedName().equals(typeName)) 
					return eachInterface;
				else {
					// Retry using the non-qualified name, assumes no duplicate short names.
					String shortName = Utils.shortNameFrom(eachInterface.getQualifiedName());
					if (shortName.equals(typeName)) return eachInterface;
				}
			}
		}
		return null;
	}
	public String report(){
		return "added " + model.associations.size() + " associations";
	}
}