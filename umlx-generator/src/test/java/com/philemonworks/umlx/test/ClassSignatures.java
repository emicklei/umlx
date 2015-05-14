/*
 * Created on 25-feb-05
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.philemonworks.umlx.test;

import java.util.List;
import java.util.Map;

/**
 * @author E.M.Micklei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ClassSignatures {
	public static String VERSION = "1";
	protected List protectedElements = null;
	private Map privateMap = null;
	Object[] objectArray;
	
	public void noReturn(){};
	protected String stringReturn(){return "";};
	private String stringReturnParams(String oaram){return "";};
	private void primitiveParams(int i,float f,double d,boolean b, short s,long l,char c){};
	void arrayParam(String[] stringArray){};
}
