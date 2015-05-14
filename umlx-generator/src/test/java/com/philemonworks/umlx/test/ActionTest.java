package com.philemonworks.umlx.test;

import com.philemonworks.umlx.actions.RemoveClassesAction;
import junit.framework.TestCase;

public class ActionTest extends TestCase {
	public void testRemoveClassesAction(){
		RemoveClassesAction rca = new RemoveClassesAction();
		rca.setPattern(".*Action");
		assertTrue(".*Action",rca.isExcluded("com.philemonworks.umlx.actions.RemoveClassesAction"));
	}
}
