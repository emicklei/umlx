package com.philemonworks.umlx.ant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddAssociationsAction {
	com.philemonworks.umlx.actions.AddAssociationsAction implementor;
	private List pendingAssociationEnds = new ArrayList();

	public com.philemonworks.umlx.actions.AddAssociationsAction getImplementor() {
		return implementor;
	}
	public void setImplementor(com.philemonworks.umlx.actions.AddAssociationsAction implementor) {
		this.implementor = implementor;
	}
	public AssociationEnd createEnd() {
		AssociationEnd end = new AssociationEnd();
		pendingAssociationEnds.add(end);
		return end;
	}
	public void aboutToGenerate() {
		// add all pending association ends
		for (Iterator iter = pendingAssociationEnds.iterator(); iter.hasNext();) {
			AssociationEnd element = (AssociationEnd) iter.next();
			com.philemonworks.umlx.elements.AssociationEnd impl = implementor.addAssociationEnd(element.getContainertype(), element.getRole(), element
					.getElementtype());
			impl.multiplicity = element.getMultiplicity();
		}
	}
}
