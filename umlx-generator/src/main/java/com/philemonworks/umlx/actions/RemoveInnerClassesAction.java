package com.philemonworks.umlx.actions;

import java.util.ArrayList;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.Package;

/**
 * @author E.M.Micklei
 *
 */
public class RemoveInnerClassesAction implements GenerationAction {
	private int removals = 0;
	
	public RemoveInnerClassesAction(){ super(); }
	public RemoveInnerClassesAction(Generator g){ super(); g.addPostAction(this); }
	
	/**
	 * @see com.philemonworks.umlx.actions.GenerationAction#execute(Model)
	 */
	public void execute(Model umlxModel) {
				// Walk through all classes and remove operations that are hidden
		List packages = umlxModel.getPackages();
		for (int p=0;p<packages.size();p++){
			Package eachPackage = (Package)packages.get(p);
			List classes = eachPackage.classes;
			List noInnerClasses = new ArrayList(classes.size());
			for (int c=0;c<classes.size();c++){
				Class eachClass = (Class)classes.get(c);
				if (eachClass.name.indexOf('$') == -1)
					noInnerClasses.add(eachClass);
				else removals++;
			}
			eachPackage.classes = noInnerClasses;
		}
	}
	public String report(){
		return "removed " + removals + " inner class(es)";	
	}
}
