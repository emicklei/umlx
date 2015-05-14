package com.philemonworks.umlx.test;

import java.io.Serializable;
import java.util.List;

/**
 * @author E.M.Micklei
 *
 */
public class ClassA implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3256718489921139254L;
	public List multipleBs;
	public List getMultipleBs(){ return multipleBs; }
	public ClassB[] arrayB;
	public ClassB[] getArrayB(){return arrayB;}
}
