package edu.atilim.acma.design;

import java.util.List;

public class Field extends Node {
	private static final long serialVersionUID = 1L;
	
	private Reference ownerType;
	private Reference type;
	
	public void setOwnerType(Type ownerType) {
		if (this.ownerType != null)
			this.ownerType.release();
		
		this.ownerType = getReference(this, ownerType, Tags.REF_PARENT);
	}

	public Type getOwnerType() {
		return ownerType == null ? null : ownerType.getTarget(Type.class);
	}
	
	public void setType(Type type) {
		if (this.type != null)
			this.type.release();
		
		this.type = getReference(this, type, Tags.REF_RETURN);
	}

	public Type getType() {
		return type == null ? null : type.getTarget(Type.class);
	}
	
	public List<Method> getAccessors() {
		return getReferers(Tags.REF_DEPEND, Method.class);
	}

	public Field(String name) {
		super(name);
	}
	
	@Override
	public String toString() {
		return getName() + (getType() == null ? ":Unknown" : ":" + getType().getName());
	}
}
