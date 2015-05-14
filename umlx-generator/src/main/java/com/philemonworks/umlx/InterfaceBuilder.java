/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 18-jun-2004 : created
 */
package com.philemonworks.umlx;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import com.philemonworks.umlx.elements.Interface;

/**
 * @author emicklei
 *
 */
public class InterfaceBuilder extends TypeBuilder {
	public Interface result = new Interface();

	public Interface buildFrom(java.lang.Class javaClass) {

		Logger.getLogger(InterfaceBuilder.class).info("Building interface: " + javaClass.getName());
		result.name = javaClass.getName();
		if (javaClass.getSuperclass() != null)
			result.superclassName = javaClass.getSuperclass().getName();
		else
			result.superclassName = "";

		Field[] itsFields = javaClass.getDeclaredFields();
		for (int i = 0; i < itsFields.length; i++) {
			result.attributes.add(this.buildAttributeFrom(itsFields[i]));
		}

		Method[] itsMethods = javaClass.getDeclaredMethods();
		for (int i = 0; i < itsMethods.length; i++) {
			if (this.mustIncludeMethod(itsMethods[i], javaClass))
				result.operations.add(this.buildOperationFrom(itsMethods[i]));
		}
		return result;
	}
	private boolean mustIncludeMethod(Method javaMethod, java.lang.Class javaClass) {
		return true;
	}
}
