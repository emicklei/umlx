package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;

/**
 * @author Ernest Micklei
 *  
 */
public class RemoveInterfacesAction extends AbstractRemoveAction implements GenerationAction {
    
	public RemoveInterfacesAction(){ super(); }
	public RemoveInterfacesAction(Generator g){ super(); g.addPostAction(this); }
	/**
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {
		// Walk through all classes and remove the classes which where selected
		// to disappear
		List packages = umlxModel.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List interfaces = eachPackage.interfaces;

			List lessInterfaces = new ArrayList();
			// dit?
			for (int c = 0; c < interfaces.size(); c++) {
				Interface eachInterface = (Interface) interfaces.get(c);
				if (this.isExcluded(eachInterface.getQualifiedName())) { //contains uses Equals internally, so should work for
					// Strings
					// do nothing
				} else {
					lessInterfaces.add(eachInterface);
				}
			}
			eachPackage.interfaces = lessInterfaces;
		}
	}
	public void excludeInterface(String interfaze) {
		excludedNames.add(interfaze);
	}
}