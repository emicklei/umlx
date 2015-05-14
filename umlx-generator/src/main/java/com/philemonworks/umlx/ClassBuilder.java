/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, renaming or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 */
package com.philemonworks.umlx;
/**
 * Insert the type's description here.
 * Creation date: (3-12-2003 10:36:05)
 * @author: Ernest Micklei
 */
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.apache.log4j.Logger;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Operation;

public class ClassBuilder extends TypeBuilder {
	public Class result = new Class();
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 10:38:44)
	 */
	public Class buildFrom(java.lang.Class javaClass) {

		Logger.getLogger(ClassBuilder.class).info("Building class: " + javaClass.getName());
		result.name = javaClass.getName();
		result.superclassName = javaClass.getSuperclass().getName();

		java.lang.Class[] itsInterfaces = javaClass.getInterfaces();
		for (int i=0;i<itsInterfaces.length;i++){
			result.addRealizedInterface(itsInterfaces[i]);
		}
		Field[] itsFields = javaClass.getDeclaredFields();
		for (int i = 0; i < itsFields.length; i++) {
			result.attributes.add(this.buildAttributeFrom(itsFields[i]));
		}
		Method[] itsMethods = javaClass.getDeclaredMethods();
		for (int i = 0; i < itsMethods.length; i++) {
			result.operations.add(this.buildOperationFrom(itsMethods[i]));
		}
		return result;
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 10:38:44)
	 */
	public Class buildFrom(String qualifiedClassName) {

		try {
			this.buildFrom(java.lang.Class.forName(qualifiedClassName,false, this.getClass().getClassLoader())); // Do not initialize the class
		} catch (java.lang.ClassNotFoundException ex) {
			Logger.getLogger(ClassBuilder.class).warn("Class not found:" + qualifiedClassName);
		}
		return result;
	}
	/**
	 * Create, initialize and answer a new Operation from a Java Method
	 */
	public Operation buildOperationFrom(Method itsMethod) {

		Operation newOperation = new Operation();
		newOperation.name = itsMethod.getName();
		newOperation.returnType = itsMethod.getReturnType().getName();
		newOperation.isPublic = Modifier.isPublic(itsMethod.getModifiers());
		newOperation.isProtected = Modifier.isProtected(itsMethod.getModifiers());		
		java.lang.Class[] parameterTypes = itsMethod.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			newOperation.parameters.add(buildParameterFrom(parameterTypes[i]));
		}
		return newOperation;
	}
}
