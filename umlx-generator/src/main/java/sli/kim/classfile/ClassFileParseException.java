/**
 * This source is part of the ClassFile read/write package.
 * @author Kimberley Burchett, see http://www.kimbly.com/
 */
package sli.kim.classfile;

import java.io.IOException;

/**
* Thrown if ClassFileReader encounters a corrupt class file.
* 
* @see ClassFileReader
*/
public class ClassFileParseException extends IOException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3905804192869331760L;

	public ClassFileParseException(String msg) {
		super(msg);
	}
}