package edu.atilim.acma.uml;

public class Package extends Element {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getFullName() {
		Package parent = getParent();
		if (parent != null && !parent.getName().equals("")) return parent.getFullName() + "." + getName();
		return getName();
	}
	
	@Override
	public Element getParentElement() {
		return getParent();
	}
	
	@Override
	public Iterable<Element> childElements() {
		return getDeps().get(Dependency.PACKAGE, Element.class, true);
	}

	public Iterable<Type> types() {
		return getDeps().get(Dependency.PACKAGE, Type.class, true);
	}
	
	public Iterable<Package> packages() {
		return getDeps().get(Dependency.PACKAGE, Package.class, true);
	}
	
	public Package getSubPackage(String name) {
		for (Package p : packages()) {
			if (p.getName().equals(name))
				return p;
		}
		return null;
	}
	
	public Package getParent() {
		return getDeps().getSingle(Dependency.PACKAGE, Package.class);
	}
	
	public void setParent(Package parent) {
		getDeps().add(Dependency.PACKAGE, parent);
	}
	
	public Package(String name, Design design) {
		super(name, design);
	}
}
