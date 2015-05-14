package com.philemonworks.umlx.actions;

import java.util.List;
import java.util.Vector;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;

/**
 * @author Ronald Pulleman
 *  
 */
public class RemovePackagesAction extends AbstractRemoveAction implements GenerationAction {

	public RemovePackagesAction(){ super(); }
	public RemovePackagesAction(Generator g){ super(); g.addPostAction(this); }  
	/**
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {
		// Walk through all packages and remove the packages which where selected
		// to disappear

		List packages = umlxModel.getPackages();
		Vector lessPackages = new Vector();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			if (this.isExcluded(eachPackage.name)) {
				//nop
			} else {
				lessPackages.add(eachPackage);
			}
		}
		umlxModel.setPackages(lessPackages);
	}

	public void excludePackage(String pack) {
		excludedNames.add(pack);
	}
}