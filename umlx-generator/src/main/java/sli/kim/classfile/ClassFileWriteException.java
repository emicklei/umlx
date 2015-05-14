/**
 * This source is part of the ClassFile read/write package.
 * @author Kimberley Burchett, see http://www.kimbly.com/
 */
package sli.kim.classfile;

import java.io.IOException;

/**
* Thrown if a ClassFileWriter encounters a problem.
*
* @see ClassFileWriter
*/
public class ClassFileWriteException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4051331136712225840L;

	public ClassFileWriteException(String msg) {
		super(msg);
	}
}