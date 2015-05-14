package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;

/**
 * @author Ronald Pulleman
 * @author Ernest Micklei
 *  
 */
public class RemoveClassesAction extends AbstractRemoveAction implements GenerationAction {
    
	public RemoveClassesAction(){ super(); }
	public RemoveClassesAction(Generator g){ super(); g.addPostAction(this); }
	/**
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {
		// Walk through all classes and remove the classes which where selected
		// to disappear
		List packages = umlxModel.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;

			List lessClasses = new ArrayList();
			// dit?
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				if (this.isExcluded(eachClass.getQualifiedName())) { //contains uses Equals internally, so should work for
					// Strings
					// do nothing
				} else {
					lessClasses.add(eachClass);
				}
			}
			eachPackage.classes = lessClasses;
		}
	}
	public void excludeClass(String klas) {
		excludedNames.add(klas);
	}
}