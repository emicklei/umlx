package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Operation;
import com.philemonworks.umlx.elements.Package;

/**
 * @author E.M.Micklei
 *
 */
public class RemoveOperationsAction extends AbstractRemoveAction implements GenerationAction {
    
	public RemoveOperationsAction(){ super(); }
	public RemoveOperationsAction(Generator g){ super(); g.addPostAction(this); }  
	/**
	 * @param umlxModel
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {	
		// Walk through all classes and remove operations that are hidden
		List packages = umlxModel.getPackages();
		for (int p=0;p<packages.size();p++){
			Package eachPackage = (Package)packages.get(p);
			List classes = eachPackage.classes;
			for (int c=0;c<classes.size();c++){
				Class eachClass = (Class)classes.get(c);
				List operations = eachClass.operations;
				List newOperations = new ArrayList(operations.size());
				for (int a=0;a<operations.size();a++) {
					Operation eachOperation = (Operation)operations.get(a);
					if (!this.isExcluded(eachOperation.name))
						newOperations.add(eachOperation);
				}
				eachClass.operations = newOperations;
			}
		}
	}
}
