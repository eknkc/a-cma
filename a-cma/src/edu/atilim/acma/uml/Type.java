package edu.atilim.acma.uml;

public abstract class Type extends AccessibleElement {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getFullName() {
		Type parent = getParent();
		if (parent != null) return parent.getFullName() + "." +  getName();
		Package pack = getPackage();
		if (pack != null) return pack.getFullName() + "." + getName();
		return getName();
	}
	
	@Override
	public Element getParentElement() {
		Element parent = getParent();
		if (parent != null) return parent;
		return getDeps().getSingle(Dependency.PACKAGE, Package.class);
	}
	
	public Package getPackage() {
		Element parent = getParentElement();
		if (parent instanceof Package) return (Package) parent;
		else if (parent instanceof Type) return ((Type)parent).getPackage();
		return null;
	}
	
	public void setPackage(Package value) {
		if (getParent() == null)
			getDeps().add(Dependency.PACKAGE, value);
	}
	
	public Type getParent() {
		return getDeps().getSingle(Dependency.PARENT, Type.class);
	}
	
	public void setParent(Type parent) {
		getDeps().add(Dependency.PARENT, parent);
		getDeps().add(Dependency.PACKAGE, null);
	}
	
	public Iterable<Type> nestedTypes() {
		return getDeps().get(Dependency.PARENT, Type.class, true);
	}
	
	public Iterable<Method> methods() {
		return getDeps().get(Dependency.PARENT, Method.class, true);
	}
	
	public Iterable<InterfaceType> interfaces() {
		return getDeps().get(Dependency.IMPLEMENT, InterfaceType.class);
	}

	public Type(String name, Design design) {
		super(name, design);
	}

}
