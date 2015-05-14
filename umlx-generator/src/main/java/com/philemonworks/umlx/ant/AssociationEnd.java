package com.philemonworks.umlx.ant;

public class AssociationEnd {
	private String containertype;
	private String role;
	private String elementtype;
	private String multiplicity;
	public String getContainertype() {
		return containertype;
	}
	public void setContainertype(String containertype) {
		this.containertype = containertype;
	}
	public String getElementtype() {
		return elementtype;
	}
	public void setElementtype(String elementtype) {
		this.elementtype = elementtype;
	}
	public String getMultiplicity() {
		return multiplicity;
	}
	public void setMultiplicity(String multiplicity) {
		this.multiplicity = multiplicity;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
}
