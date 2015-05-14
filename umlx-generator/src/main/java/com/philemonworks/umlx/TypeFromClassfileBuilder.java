package com.philemonworks.umlx;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Logger;
import sli.kim.classfile.AccessFlags;
import sli.kim.classfile.ClassInfo;
import sli.kim.classfile.CommonInfo;
import sli.kim.classfile.FieldInfo;
import sli.kim.classfile.MethodInfo;
import com.philemonworks.umlx.elements.Attribute;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;
import com.philemonworks.umlx.elements.Operation;
import com.philemonworks.umlx.elements.Parameter;

/**
 * TypeBuilder uses the sli.kim.classfile package to read the meta data of .class files
 * Compared to the com.philemonworks.umlx.TypeBuilder, this builder does not load the classes
 * to get the meta data. Loading classes may give problems when static initializations are performed.
 * 
 * The TypeBuilder requires a ClassInfo object to create the UMLX counter parts
 * such as Attribute and Operation.
 * 
 * @author E.M.Micklei
 *
 */
public class TypeFromClassfileBuilder {
	private static final Logger LOG = Logger.getLogger(TypeFromClassfileBuilder.class);
	/**
	 * Return whether the info represent a true Class and not an Java Interface.
	 * @param info : ClassInfo
	 */
	public boolean isClass(ClassInfo info){
		return !this.isFlagSet(info,AccessFlags.INTERFACE);
	}
	/**
	 * Build and return an UMLX Class using the the ClassInfo parameter.
	 * @param info : ClassInfo
	 */
	public Class buildClassFrom(ClassInfo info) {
		Class theClass = new Class();
		this.buildTypeFrom(theClass,info);
		for (int i=0;i<info.getInterfaces().length;theClass.realizedInterfaceNames.add(info.getInterfaces()[i++].replace('/', '.')));
		return theClass;
	}
	/**
	 * Build and return an UMLX Interface using the the ClassInfo parameter.
	 * @param info : ClassInfo that represents an Interface
	 */
	public Interface buildInterfaceFrom(ClassInfo info) {
		Interface theInterface = new Interface();
		this.buildTypeFrom(theInterface,info);
		theInterface.superInterfaces = Arrays.asList(info.getInterfaces());		
		return theInterface;
	}				
	/**
	 * Build and return an UMLX type (Class or Interface) using the the ClassInfo parameter.
	 * @param info : ClassInfo
	 */	
	public void buildTypeFrom(Interface anInterface, ClassInfo info) {
		anInterface.name = info.getName().replace('/', '.');
		// LOG.debug("Building Type:" + anInterface.name);
		anInterface.superclassName = info.getSuperClassName().replace('/', '.');
		List attributes = new ArrayList();
		for (int i = 0; i < info.getFields().length; i++) {
			FieldInfo fld = info.getFields()[i];
			if (!fld.getName().startsWith("class$"))
				try {
					attributes.add(this.buildFrom(fld));
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		anInterface.attributes = attributes;
		List operations = new ArrayList();
		for (int i = 0; i < info.getMethods().length; i++) {
			MethodInfo meth = info.getMethods()[i];
			if (!meth.getName().startsWith("<")) // exclude JVM private operations
				operations.add(this.buildFrom(meth));
		}
		anInterface.operations = operations;
	}
	/**
	 * Build and return a UMLX Operation using the MethodInfo
	 * @param info : MethodInfo
	 */
	public Operation buildFrom(MethodInfo info) {		
		Operation op = new Operation();
		op.name = info.getName();
		// LOG.debug("Building Operation:" + op.name);
		op.isPublic = this.isFlagSet(info, AccessFlags.PUBLIC);
		if (!op.isPublic) {
			op.isProtected = this.isFlagSet(info, AccessFlags.PROTECTED);
		}
		try {
			this.readSignatureInto(info.getSignature(),op);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return op;
	}
	/**
	 * Build and return a UMLX Attribute using the FieldInfo
	 * @param info : FieldInfo
	 */
	public Attribute buildFrom(FieldInfo info) throws IOException {
		Attribute field = new Attribute();
		field.name = info.getName(); 
		// LOG.debug("Building Attribute:" + field.name);
		field.isPublic = this.isFlagSet(info, AccessFlags.PUBLIC);
		field.isProtected = this.isFlagSet(info, AccessFlags.PROTECTED);
		field.isStatic = this.isFlagSet(info, AccessFlags.STATIC);
		StringReader reader = new StringReader(info.getSignature());
		field.type = this.readType((char)reader.read(),reader);
		field.hasPrimitiveType = (field.type.indexOf(".") == -1) && Character.isLowerCase(field.type.charAt(0));
		return field;
	}
	/**
	 * Generic method to test a CommonInfo instance whether it sets a particular Access Flag.
	 * @param info : CommonInfo
	 * @param flag : short
	 * @return
	 */
	public boolean isFlagSet(CommonInfo info, short flag) {
		return (info.getAccessFlags() & flag) == flag;
	}
	/**
	 * Scan the signature of an Operation to read its parameter types.
	 * For each parameter type, a Parameter instance is added to the Operation.
	 * @param signature : String
	 * @param operation : Operation
	 * @throws IOException
	 */
	public void readSignatureInto(String signature, Operation operation) throws IOException {
		// Logger.getLogger(this.getClass()).debug("Scanning: " + signature);
		StringReader reader = new StringReader(signature);
		char token = (char) reader.read(); // (
		token = (char) reader.read();
		while (token != ')') {
			Parameter p = new Parameter(this.readType(token,reader));
			p.name = null;
			operation.parameters.add(p);
			token = (char) reader.read();
		}
		operation.returnType = this.readType((char)reader.read(),reader);
	}
	/**
	 * Interpret the token as a type indicator. The prefix L denotes an Object type.
	 * Answer the qualified type name or the primitive short name.
	 * @param token : char
	 * @param reader : StringReader
	 * @throws IOException
	 */
	public String readType(char token, StringReader reader) throws IOException {
		if ('B' == token)
			return "byte";
		if ('I' == token)
			return "int";
		if ('F' == token)
			return "float";
		if ('D' == token)
			return "double";
		if ('J' == token)
			return "long";
		if ('C' == token)
			return "char";
		if ('Z' == token)
			return "boolean";
		if ('S' == token)
			return "short";
		if ('V' == token)
			return "void";
		if ('L' == token) {
			char nextToken;
			StringBuffer buffer = new StringBuffer();
			do {
				nextToken = (char) reader.read();
				if ('/' == nextToken)
					buffer.append('.');
				else if (';' != nextToken)
					buffer.append(nextToken);
			} while (';' != nextToken);
			return buffer.toString();
		}
		if ('[' == token) {
		    // handle array type
		    String elementType = this.readType((char)reader.read(),reader);
		    return elementType + "[]"; // indicate array
		}
		return "?";
	}
}
