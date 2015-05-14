package com.philemonworks.umlx.actions;

import com.philemonworks.umlx.elements.Model;

/**
 * @author E.M.Micklei
 *
 */
public interface GenerationAction {
	public void execute(Model umlxModel);
	public String report();
}
