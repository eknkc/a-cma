package edu.atilim.acma.uml;

public class InterfaceType extends Type {
	private static final long serialVersionUID = 1L;

	public Iterable<Type> implementers() {
		return getDeps().get(Dependency.IMPLEMENT, Type.class, true);
	}
	
	public InterfaceType(String name, Design design) {
		super(name, design);
	}

}
