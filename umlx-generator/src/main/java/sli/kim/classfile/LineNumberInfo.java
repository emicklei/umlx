/**
 * This source is part of the ClassFile read/write package.
 * @author Kimberley Burchett, see http://www.kimbly.com/
 */
package sli.kim.classfile;


/**
* A class for storing information about line numbers.
* This class represents one row in the LineNumberTable.
*
* @see sli.kim.classfile.CodeInfo#setLineNumberTable()
*/
public class LineNumberInfo {
	public short startPC, lineNumber;

	public LineNumberInfo(short startPC, short lineNumber) {
		this.startPC = startPC;
		this.lineNumber = lineNumber;
	}
}
