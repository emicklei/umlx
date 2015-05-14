package com.philemonworks.umlx.actions;
import java.util.ArrayList;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;
/**
 * @author E.M.Micklei
 *
 */
public class RemoveAttributesAction extends AbstractRemoveAction implements GenerationAction {
    
	public RemoveAttributesAction(){ super(); }
	public RemoveAttributesAction(Generator g){ super(); g.addPostAction(this); }
    
	/**
	 * @see com.philemonworks.umlx.PostGenerationAction#exectute(Model)
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
				List newAttributes = new ArrayList(attributes.size());
				for (int a = 0; a < attributes.size(); a++) {
					Attribute eachAttribute = (Attribute) attributes.get(a);
					if (!isExcluded(eachAttribute.name))
						newAttributes.add(eachAttribute);
				}
				eachClass.attributes = newAttributes;
			}
		}
	}
	/**
	 * Returns the excludedAttributeNames.
	 * @return List
	 */
	public List getExcludedAttributeNames() {
		return excludedNames;
	}
}