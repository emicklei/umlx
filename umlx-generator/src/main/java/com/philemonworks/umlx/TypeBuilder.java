package com.philemonworks.umlx;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Operation;
import com.philemonworks.umlx.elements.Parameter;

/**
 * @author E.M.Micklei
 *
 */
public class TypeBuilder {
	/**
	 * Create, initialize and answer a new Parameter from a Type
	 */
	public Parameter buildParameterFrom(java.lang.Class parameterType) {

		Parameter parameter = new Parameter();
		parameter.name = null;
		parameter.type = parameterType.getName();
		return parameter;
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

	public Attribute buildAttributeFrom(java.lang.reflect.Field javaField) {

		Attribute newAttribute = new Attribute();
		newAttribute.name = javaField.getName();
		newAttribute.type = javaField.getType().getName();
		newAttribute.isPublic = Modifier.isPublic(javaField.getModifiers());
		newAttribute.isProtected = Modifier.isProtected(javaField.getModifiers());
		newAttribute.hasPrimitiveType = javaField.getType().isPrimitive();
		newAttribute.isStatic = Modifier.isStatic(javaField.getModifiers());
		return newAttribute;
	}

}
