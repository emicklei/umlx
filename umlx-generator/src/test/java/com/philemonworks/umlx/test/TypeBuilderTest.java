/*
 * Created on 18-feb-05
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.philemonworks.umlx.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import junit.framework.TestCase;
import sli.kim.classfile.ClassFileReader;
import sli.kim.classfile.ClassInfo;
import sli.kim.classfile.Debug;
import com.philemonworks.umlx.TypeFromClassfileBuilder;
import com.philemonworks.umlx.elements.Class;
import com.philemonworks.umlx.elements.Interface;

/**
 * @author E.M.Micklei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TypeBuilderTest extends TestCase {

	/**
	 * Constructor for TypeBuilderTest.
	 * @param arg0
	 */
	public TypeBuilderTest(String arg0) {
		super(arg0);
	}

	public void testClassSignatures() {
		try {
			Debug.setEnabled(false);
			File input = new File("bin\\com\\philemonworks\\umlx\\test\\ClassSignatures.class");
			if (!input.exists()) {
				input = new File("target\\classes\\com\\philemonworks\\umlx\\test\\ClassSignatures.class");
				if (!input.exists()) return;
			}
			InputStream is = new FileInputStream(input);
			ClassInfo classInfo = new ClassInfo();
			System.out.println("Reading class file");
			new ClassFileReader().read(is, classInfo);
			System.out.println("Building UMLX class");
			Class umlxClass = new TypeFromClassfileBuilder().buildClassFrom(classInfo);
			System.out.println(umlxClass);
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void testSampleInterface() {
		try {
			Debug.setEnabled(false);
			File input = new File("bin\\com\\philemonworks\\umlx\\test\\SampleInterface.class");
			if (!input.exists()) {
				input = new File("target\\classes\\com\\philemonworks\\umlx\\test\\SampleInterface.class");
				if (!input.exists()) return;
			}
			InputStream is = new FileInputStream(input);			
			ClassInfo classInfo = new ClassInfo();
			System.out.println("Reading interface file");
			new ClassFileReader().read(is, classInfo);
			System.out.println("Building UMLX interface");
			TypeFromClassfileBuilder builder = new TypeFromClassfileBuilder();
			Interface umlxInterface = builder.buildInterfaceFrom(classInfo);
			System.out.println(umlxInterface);
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
	public void testClassA() {
		try {
			Debug.setEnabled(false);
			File input = new File("bin\\com\\philemonworks\\umlx\\test\\SampleInterface.class");
			if (!input.exists()) {
				input = new File("target\\classes\\com\\philemonworks\\umlx\\test\\ClassA.class");
				if (!input.exists()) return;
			}
			InputStream is = new FileInputStream(input);			
			ClassInfo classInfo = new ClassInfo();
			System.out.println("Reading interface file");
			new ClassFileReader().read(is, classInfo);
			System.out.println("Building UMLX interface");
			TypeFromClassfileBuilder builder = new TypeFromClassfileBuilder();
			Class umlxClass = builder.buildClassFrom(classInfo);
			System.out.println(umlxClass);
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}		
}
