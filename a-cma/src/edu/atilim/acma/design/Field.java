package edu.atilim.acma.design;

import java.util.List;

public class Field extends Node {
	private static final long serialVersionUID = 1L;
	
	private Reference ownerType;
	private Reference type;
	
	public Field(String name, Design design) {
		super(name, design);
	}
	
	public List<Method> getAccessors() {
		return getReferers(Tags.REF_DEPEND, Method.class);
	}
	
	public Type getOwnerType() {
		return ownerType == null ? null : ownerType.getTarget(Type.class);
	}
	
	@Override
	public Package getPackage() {
		Type owner = getOwnerType();
		if (owner == null) return Package.emptyPackage(getDesign());
		return owner.getPackage();
	}

	public Type getType() {
		return type == null ? null : type.getTarget(Type.class);
	}
	
	public boolean isConstant() {
		return isStatic() && isFinal();
	}

	@Override
	public boolean remove() {
		if (!super.remove()) return false;
		if (ownerType != null)
			ownerType.release();
		if (type != null)
			type.release();
		return true;
	}
	
	public void setOwnerType(Type ownerType) {
		if (this.ownerType != null)
			this.ownerType.release();
		
		this.ownerType = getReference(this, ownerType, Tags.REF_PARENT);
	}
	
	public void setType(Type type) {
		if (this.type != null)
			this.type.release();
		
		this.type = getReference(this, type, Tags.REF_RETURN);
	}
	
	@Override
	public String toString() {
		return String.format("%s.%s:%s", getOwnerType(), getName(), getType());
	}
}
