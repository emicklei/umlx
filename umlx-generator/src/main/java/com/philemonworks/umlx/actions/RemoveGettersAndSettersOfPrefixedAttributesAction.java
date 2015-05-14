package com.philemonworks.umlx.actions;

import com.philemonworks.umlx.Utils;
import com.philemonworks.umlx.elements.Attribute;


/**
 * RemoveCastorGettersAndSettersAction is a specialization that knows about the underscore prefix used in fields.
 */
public class RemoveGettersAndSettersOfPrefixedAttributesAction extends RemoveGettersAndSettersAction {
	private String prefix = "";

	public String getterFrom(Attribute anAtt) {
		String attName = anAtt.name;
		if (attName.startsWith(this.getPrefix()))
			attName = attName.substring(this.getPrefix().length(), attName.length());
		if ("boolean".equals(anAtt.type))
			return Utils.accessorFrom("is", attName);
		else
			return Utils.accessorFrom("get", attName);
	}
	public String setterFrom(Attribute anAtt) {
		String attName = anAtt.name;
		if (attName.startsWith(this.getPrefix()))
			attName = attName.substring(this.getPrefix().length(), attName.length());
		return Utils.accessorFrom("set", attName);
	}
	/**
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param string
	 */
	public void setPrefix(String string) {
		prefix = string;
	}

}
