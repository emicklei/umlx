package com.philemonworks.umlx.actions;

import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.Utils;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;

/**
 * @author E.M.Micklei
 *
 */
public class RemoveNamespaceFromAttributesAction implements GenerationAction {

	public RemoveNamespaceFromAttributesAction(){ super(); }
	public RemoveNamespaceFromAttributesAction(Generator g){ super(); g.addPostAction(this); }    
    
	/**
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {
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
					eachAttribute.type = Utils.shortNameFrom(eachAttribute.type);
				}
			}
		}
	}
	public String report(){
		return "done";
	}
}
