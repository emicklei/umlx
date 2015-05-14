package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.philemonworks.umlx.elements.Model;

public class AbstractFilterAction implements GenerationAction {
	List includedPrefixes = new ArrayList();
	List includedSuffixes = new ArrayList();
	List includedNames = new ArrayList();
	
	public void execute(Model umlxModel) {
		// TODO Auto-generated method stub
	}
	private int additions = 0;
	/**
	 * Return whether an Element with this name is included. 
	 * @param elementName : String 
	 * @return true if the action has specified that elements with this name should be included.
	 */
	protected boolean isIncluded(String elementName) {
		if (includedNames.contains(elementName)){
			additions++;
			return true;
		}
		for (Iterator it = includedSuffixes.iterator(); it.hasNext();) {
			String postFix = (String) it.next();
			if (elementName.endsWith(postFix)){
				additions++;
				return true;
			}
		}
		for (Iterator it = includedPrefixes.iterator(); it.hasNext();) {
			String preFix = (String) it.next();
			if (elementName.startsWith(preFix)) {
				additions++;
				return true;
			}
		}
		return false;
	}
	/**
	 * Add a name to the list of included names.
	 * @param name : String
	 */
	public void add(String name){
		includedNames.add(name);
	}
	/**
	 * Add a suffix to the list of included suffixes. 
	 * This means that elements whos name end with this suffix will be included.
	 * @param suffix
	 */
	public void addSuffix(String suffix) {
		includedSuffixes.add(suffix);
	}	
	/**
	 * Add a prefix to the list of included prefixes. 
	 * This means that elements whos name starts with this prefix will be included.
	 * @param prefix
	 */
	public void addPrefix(String prefix) {
		includedPrefixes.add(prefix);
	}
	/**
	 * Return a short report that is requested after the receiver is executed.
	 * @return
	 */
	public String report(){
		return "passed " + additions + " elements";
	}	
}
