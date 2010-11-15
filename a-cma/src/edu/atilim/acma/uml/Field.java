package edu.atilim.acma.uml;

public class Field extends TypeMember {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getFullName() {
		ClassType parent = getParent();
		if (parent != null) return parent.getFullName() + "." + getName();
		return getName();
	}
	
	public ClassType getParent() {
		return getDeps().getSingle(Dependency.PARENT, ClassType.class);
	}
	
	public void setParent(ClassType owner) {
		getDeps().add(Dependency.PARENT, owner);
	}
	
	public Type getType() {
		return getDeps().getSingle(Dependency.RETURN, Type.class);
	}
	
	public void setType(Type type) {
		getDeps().add(Dependency.RETURN, type);
	}

	public Field(String name, Design design) {
		super(name, design);
	}

}
