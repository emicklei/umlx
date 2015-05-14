/**
 * This source is part of the ClassFile read/write package.
 * @author Kimberley Burchett, see http://www.kimbly.com/
 */
package sli.kim.classfile;


/**
* A class for storing information about local variables.
* This class represents one row in the LocalVariableTable.
*
* @see sli.kim.classfile.CodeInfo#setLocalVariableTable()
*/
public class LocalVariableInfo {
	public short startPC, length, slot;
	public String name, signature; 

	public LocalVariableInfo(short startPC, short length, String name,
		String signature, short slot) 
	{
		this.startPC = startPC;
		this.length = length;
		this.name = name;
		this.signature = signature;
		this.slot = slot;
	}
}
