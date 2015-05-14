package com.philemonworks.umlx.ant;

import com.philemonworks.umlx.actions.AbstractRemoveAction;

/**
 *  RemoveAction is wrapper for a AbstractRemoveAction
 *  used by the GeneratorTask in Ant.
 * 
 *  @author E.M.Micklei
 */
public class RemoveAction {
	AbstractRemoveAction implementor;
	public void setName(String newName) {
		implementor.add(newName);
	}
	public void setSuffix(String newSuffix) {
		implementor.addSuffix(newSuffix);
	}
	public void setPrefix(String newPrefix) {
		implementor.addPrefix(newPrefix);
	}
	public void setPattern(String newPattern) {
		implementor.setPattern(newPattern);
	}		
	public void setImplementor(AbstractRemoveAction action) {
		implementor = action;
	}
}
