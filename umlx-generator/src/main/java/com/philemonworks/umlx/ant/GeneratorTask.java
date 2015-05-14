package com.philemonworks.umlx.ant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.Task;
import com.philemonworks.umlx.Generator;
import com.philemonworks.umlx.actions.AbstractRemoveAction;
import com.philemonworks.umlx.actions.GenerationAction;
import com.philemonworks.umlx.actions.RemoveAttributesAction;
import com.philemonworks.umlx.actions.RemoveClassesAction;
import com.philemonworks.umlx.actions.RemoveConstructorsAction;
import com.philemonworks.umlx.actions.RemoveGettersAndSettersAction;
import com.philemonworks.umlx.actions.RemoveGettersAndSettersOfPrefixedAttributesAction;
import com.philemonworks.umlx.actions.RemoveInnerClassesAction;
import com.philemonworks.umlx.actions.RemoveInterfacesAction;
import com.philemonworks.umlx.actions.RemoveOperationsAction;
import com.philemonworks.umlx.actions.RemovePackagesAction;
import com.philemonworks.umlx.actions.SortElementsAction;

/**
 *  GeneratorTask is a task that invokes the UMLX Generator.
 * 
 *  @author E.M.Micklei
 */
public class GeneratorTask extends Task{
	private String classesHome = "./bin";
	private String umlxHome;
	private Generator generator = new Generator();
	private List associationActions = new ArrayList();
/*	
	private Vector paths = new Vector();                      *1
	public void addPath(Path path) {                          *2
		paths.add(path);
	}
*/	

	private RemoveAction createRemoveAction(AbstractRemoveAction implementor){
		RemoveAction newAction = new RemoveAction();
		newAction.setImplementor(implementor);
		generator.addPostAction((GenerationAction)implementor);
		return newAction;
	}
	public RemoveAction createRemoveClass(){
		return createRemoveAction(new RemoveClassesAction());
	}
	public RemoveAction createRemoveInterface(){
		return createRemoveAction(new RemoveInterfacesAction());
	}	
	public RemoveAction createRemoveAttribute(){
		return createRemoveAction(new RemoveAttributesAction());
	}
	public RemoveAction createRemovePackage(){
		return createRemoveAction(new RemovePackagesAction());
	}	
	public RemoveAction createRemoveOperation(){		
		return createRemoveAction(new RemoveOperationsAction());
	}	
	public RemoveInnerClassesAction createRemoveInnerclasses(){
		RemoveInnerClassesAction action = new RemoveInnerClassesAction();
		generator.addPostAction(action);
		return action;
	}
	public RemoveGettersAndSettersAction createRemoveGettersAndSetters(){
		RemoveGettersAndSettersAction action = new RemoveGettersAndSettersAction();
		generator.addPostAction(action);
		return action;
	}
	public RemoveConstructorsAction createRemoveConstructors(){
		RemoveConstructorsAction action = new RemoveConstructorsAction();
		generator.addPostAction(action);
		return action;
	}	
	public RemoveGettersAndSettersOfPrefixedAttributesAction createRemoveGettersAndSettersOfPrefixedAttributes(){
		RemoveGettersAndSettersOfPrefixedAttributesAction action = new RemoveGettersAndSettersOfPrefixedAttributesAction();
		generator.addPostAction(action);
		return action;
	}
	public SortElementsAction createSortElements(){
		SortElementsAction action = new SortElementsAction();
		generator.addPostAction(action);
		return action;
	}		
	public AddAssociationsAction createAddAssociations(){
		AddAssociationsAction aaa = new AddAssociationsAction();
		aaa.setImplementor(new com.philemonworks.umlx.actions.AddAssociationsAction());
		associationActions.add(aaa);
		return aaa;
	}		
	public void execute() {
		generator.setClassesHome(classesHome);
		generator.setUMLXHome(umlxHome);
		for (Iterator iter = associationActions.iterator(); iter.hasNext();) {
			AddAssociationsAction element = (AddAssociationsAction) iter.next();
			element.aboutToGenerate();	
			generator.addPostAction(element.getImplementor());
		} 
		generator.generate();
	}
	
	/**
	 * @param string
	 */
	public void setClasses(String string) {
		classesHome = string;
	}

	/**
	 * @param string
	 */
	public void setTodir(String string) {
		umlxHome = string;
	}
	public void setUmlx(boolean useUMLX){
		generator.setUmlx(useUMLX);
	}
	public void setHtml(boolean useHTML){
		generator.setHtml(useHTML);
	}	
	public void setUmlx(String booleanString){
		generator.setUmlx(booleanString.equals("true"));
	}
	public void setHtml(String booleanString){
		generator.setHtml(booleanString.equals("true"));
	}		
}
