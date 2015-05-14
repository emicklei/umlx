package com.philemonworks.umlx.test;
/**
 * Insert the type's description here.
 * Creation date: (3-12-2003 10:52:17)
 * @author: Ernest Micklei
 */
import java.io.File;
import com.philemonworks.umlx.ClassBuilder;
public class ClassBuilderTest extends junit.framework.TestCase {
	/**
	 * ClassBuilderTest constructor comment.
	 * @param name java.lang.String
	 */
	public ClassBuilderTest(String name) {
		super(name);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 10:52:56)
	 */
	public void testBuildPoint() {
		ClassBuilder builder = new ClassBuilder();
		com.philemonworks.umlx.elements.Class result = builder.buildFrom("java.util.ArrayList");
		assertNotNull(result);
		assertEquals("java.util.ArrayList", result.name);
		assertEquals("java.util.AbstractList", result.superclassName);
		assertEquals(3,result.attributes.size());
		assertTrue(result.operations.size() > 0);
	}
	/**
	 * Insert the method's description here.
	 * Creation date: (3-12-2003 10:52:56)
	 */
	public void testWriteClass() {
		ClassBuilder builder = new ClassBuilder();
		com.philemonworks.umlx.elements.Class result = builder.buildFrom("java.util.ArrayList");
		System.out.println(result);
	}
	public void testExportClass() {
		ClassBuilder builder = new ClassBuilder();
		com.philemonworks.umlx.elements.Class result = builder.buildFrom("java.util.ArrayList");
		result.exportTo(System.getProperty("user.dir")+File.separator+"target");
	}
	public void testExportClassDefinitionSource() {
		ClassBuilder builder = new ClassBuilder();
		com.philemonworks.umlx.elements.Class result = builder.buildFrom("java.util.ArrayList");
		System.out.println(result);
	}
}
