/*
 * Created on 16-mrt-05
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.philemonworks.umlx.actions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Model;
import com.philemonworks.umlx.elements.ModelElement;
import com.philemonworks.umlx.elements.Package;

/**
 * @author E.M.Micklei
 *
 */
public class SortElementsAction implements GenerationAction {
	public boolean sortAttributes = true;
	public boolean sortOperations = true;
	
	public SortElementsAction(){ super(); }
	public SortElementsAction(Generator g){ super(); g.addPostAction(this); } 	
	/* (non-Javadoc)
	 * @see com.philemonworks.umlx.GenerationAction#execute(com.philemonworks.umlx.Model)
	 */
	public void execute(Model umlxModel) {
		// Walk through all classes and remove fields that should be excluded
		List packages = umlxModel.getPackages();
		for (int p = 0; p < packages.size(); p++) {
			Package eachPackage = (Package) packages.get(p);
			List classes = eachPackage.classes;
			for (int c = 0; c < classes.size(); c++) {
				Class eachClass = (Class) classes.get(c);
				this.sortElementsOf(eachClass);
			}
		}
	}
	private void sortElementsOf(Class aClass){
		Collections.sort(aClass.attributes, this.nameComparator());
		Collections.sort(aClass.operations, this.nameComparator());
	}
	private Comparator nameComparator(){
		return new Comparator(){
		public int compare(Object arg0, Object arg1) {
			ModelElement e1 = (ModelElement)arg0;
			ModelElement e2 = (ModelElement)arg1;
			return e1.name.compareTo(e2.name);
		}

		};
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.umlx.GenerationAction#report()
	 */
	public String report() {
		return "done";
	}

}
