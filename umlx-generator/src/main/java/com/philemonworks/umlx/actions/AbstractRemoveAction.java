package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * AbstractRemoveAction provides the basic behavior for actions
 * that want to remove elements from the model by specifiying
 * exclusion parameters such as the fullname, a suffix, a prefix or a matching pattern.
 * 
 * @author E.M.Micklei
 */
public class AbstractRemoveAction {

	/**
	 * List of names of model elements that will be excluded.
	 */
	protected List excludedNames = new ArrayList();
	/**
	 * List of suffixes of model elements that will be excluded.
	 * A suffix should not be empty.
	 */
	protected List excludedSuffixes = new ArrayList();
	/**
	 * List of prefixes of model elements that will be excluded.
	 * A prefix should not be empty.
	 */
	protected List excludedPrefixes = new ArrayList();
	/**
	 * Regular expression pattern that, if non-empty, is used to filter model elements.
	 */
	protected String pattern = "";
	/**
	 * Counter for creating a report
	 */
	private int removals = 0;
	/**
	 * Return whether an Element with this name is excluded. 
	 * @param elementName : String 
	 * @return true if the action has specified that elements with this name should not be included.
	 */
	public boolean isExcluded(String elementName) {
		if (excludedNames.contains(elementName)){
			removals++;
			return true;
		}
		for (Iterator it = excludedSuffixes.iterator(); it.hasNext();) {
			String postFix = (String) it.next();
			if (elementName.endsWith(postFix)){
				removals++;
				return true;
			}
		}
		for (Iterator it = excludedPrefixes.iterator(); it.hasNext();) {
			String preFix = (String) it.next();
			if (elementName.startsWith(preFix)) {
				removals++;
				return true;
			}
		}	
		if (pattern.length() > 0 && elementName.matches(pattern)){
			removals++;
			return true;
		}			
		return false;
	}
	/**
	 * Add a name to the list of excluded names.
	 * @param name : String
	 */
	public void add(String name){
		excludedNames.add(name);
	}
	/**
	 * Add a suffix to the list of excluded suffixes. 
	 * This means that elements whos name end with this suffix will be excluded.
	 * @param suffix
	 */
	public void addSuffix(String suffix) {
		excludedSuffixes.add(suffix);
	}
	/**
	 * Add a prefix to the list of excluded prefixes. 
	 * This means that elements whos name starts with this prefix will be excluded.
	 * @param prefix
	 */
	public void addPrefix(String prefix) {
		excludedPrefixes.add(prefix);
	}
	/**
	 * Return a short report that is requested after the receiver is executed.
	 * @return
	 */
	public String report(){
		return "removed " + removals + " element(s)";
	}
	/**
	 * Get the regular expression patter
	 * @return String
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * Set the regular expression pattern.
	 * @return String
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
}
