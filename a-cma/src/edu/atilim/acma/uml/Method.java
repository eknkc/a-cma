package edu.atilim.acma.uml;

public class Method extends TypeMember {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getFullName() {
		Type parent = getParent();
		if (parent != null) return parent.getFullName() + "." + getName() + "()";
		return getName() + "()";
	}
	
	public Type getParent() {
		return getDeps().getSingle(Dependency.PARENT, Type.class);
	}
	
	public void setParent(Type owner) {
		getDeps().add(Dependency.PARENT, owner);
	}
	
	public Iterable<Type> parameters() {
		return getDeps().get(Dependency.PARAMETER, Type.class);
	}
	
	public void addParameter(Type t) {
		getDeps().add(Dependency.PARAMETER, t);
	}
	
	public Type getReturn() {
		return getDeps().getSingle(Dependency.RETURN, Type.class);
	}
	
	public void setReturn(Type ret) {
		getDeps().add(Dependency.RETURN, ret);
	}
	
	public Method(String name, Design design) {
		super(name, design);
	}
}
