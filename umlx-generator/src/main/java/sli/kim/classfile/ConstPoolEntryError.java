/**
 * This source is part of the ClassFile read/write package.
 * @author Kimberley Burchett, see http://www.kimbly.com/
 */
package sli.kim.classfile;

/**
* An instance of this class is thrown when a ConstPoolEntry is 
* used incorrectly.  For example, calling setUTF(null), or calling 
* getClassName() on an entry of type INT.  It is a subclass of 
* Error instead of Exception because checking for it all the time 
* would make working with ConstPoolEntries very tedious.
*/
public class ConstPoolEntryError extends Error {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3761967159709873971L;

	public ConstPoolEntryError(String msg) {
		super(msg);
	}
}