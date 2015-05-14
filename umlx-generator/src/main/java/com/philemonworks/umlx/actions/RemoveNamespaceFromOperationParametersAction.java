package com.philemonworks.umlx.actions;

import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.Utils;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Operation;
import com.philemonworks.umlx.elements.Package;
import com.philemonworks.umlx.elements.Parameter;

/**
 * @author E.M.Micklei
 *
 */
public class RemoveNamespaceFromOperationParametersAction implements GenerationAction {

	public RemoveNamespaceFromOperationParametersAction(){ super(); }
	public RemoveNamespaceFromOperationParametersAction(Generator g){ super(); g.addPostAction(this); }  
    
	/**
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {
		// Walk through all classes and interfaces to remove the namespace prefix for each operation parameter
		List packages = umlxModel.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				List operations = eachClass.operations;
				for (int a = 0; a < operations.size(); a++) {
					Operation eachOperation = (Operation) operations.get(a);
					for (int s = 0; s < eachOperation.parameters.size(); s++) {
						Parameter eachParameter = (Parameter) eachOperation.parameters.get(s);
						eachParameter.type = Utils.shortNameFrom(eachParameter.type);
					}
				}
			}
			List interfaces = eachPackage.interfaces;
			for (int c = 0; c < interfaces.size(); c++) {
				Interface eachInterface = (Interface) interfaces.get(c);
				List operations = eachInterface.operations;
				for (int a = 0; a < operations.size(); a++) {
					Operation eachOperation = (Operation) operations.get(a);
					for (int s = 0; s < eachOperation.parameters.size(); s++) {
						Parameter eachParameter = (Parameter) eachOperation.parameters.get(s);
						eachParameter.type = Utils.shortNameFrom(eachParameter.type);
					}
				}
			}

		}
	}
	public String report(){
		return "done";
	}

}
