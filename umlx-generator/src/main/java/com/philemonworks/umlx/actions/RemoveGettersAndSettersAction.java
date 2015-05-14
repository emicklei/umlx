package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.Utils;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Operation;
import com.philemonworks.umlx.elements.Package;

/**
 * @author E.M.Micklei
 *
 */
public class RemoveGettersAndSettersAction implements GenerationAction {
	int getters = 0;
	int setters = 0;
	
	public RemoveGettersAndSettersAction(){ super(); }
	public RemoveGettersAndSettersAction(Generator g){ super(); g.addPostAction(this); }
	
	/* (non-Javadoc)
	 * @see com.philemonworks.umlx.GenerationAction#execute(com.philemonworks.umlx.Model)
	 */
	public void execute(Model umlxModel) {
		// Walk through all classes and remove operations that are hidden
		List packages = umlxModel.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				List operations = eachClass.operations;
				List newOperations = new ArrayList(operations.size());
				for (int a = 0; a < operations.size(); a++) {
					Operation eachOperation = (Operation) operations.get(a);
					if (!this.isExcludedIn(eachOperation.name, eachClass))
						newOperations.add(eachOperation);
				}
				eachClass.operations = newOperations;
			}
		}
	}
	private boolean isExcludedIn(String operationName, Class aClass) {
		List allAttributes = aClass.attributes;
		for (int f = 0; f < allAttributes.size(); f++) {
			String setter = this.setterFrom((Attribute) allAttributes.get(f));
			if (operationName.equals(setter)) {
				setters++;
				return true;
			}
			String getter = this.getterFrom((Attribute) allAttributes.get(f));
			if (operationName.equals(getter)) {
				getters++;
				return true;
			}
		}
		return false;
	}
	public String getterFrom(Attribute anAtt) {
		if ("boolean".equals(anAtt.type))
			return Utils.accessorFrom("is", anAtt.name);
		else
			return Utils.accessorFrom("get", anAtt.name);
	}
	public String setterFrom(Attribute anAtt) {
		return Utils.accessorFrom("set", anAtt.name);
	}

	/* (non-Javadoc)
	 * @see com.philemonworks.umlx.GenerationAction#report()
	 */
	public String report() {
		return "removed " + getters + " getters and " + setters + " setters";
	}

}
