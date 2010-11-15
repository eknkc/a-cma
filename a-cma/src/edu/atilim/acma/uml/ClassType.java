package edu.atilim.acma.uml;

public class ClassType extends Type {
	private static final long serialVersionUID = 1L;

	public ClassType(String name, Design design) {
		super(name, design);
		setFlag(Element.Flags.CLASS);
	}
	
	public ClassType getSuper() {
		return getDeps().getSingle(Dependency.EXTEND, ClassType.class);
	}
	
	public void setSuper(ClassType sup) {
		getDeps().add(Dependency.EXTEND, sup);
	}
	
	public Iterable<ClassType> subClasses() {
		return getDeps().get(Dependency.EXTEND, ClassType.class, true);
	}
	
	public boolean isAbstract() {
		return getFlag(Element.Flags.ABSTRACT);
	}
	
	public void setAbstract(boolean value) {
		setFlag(Element.Flags.ABSTRACT, value);
	}
	
	public boolean isFinal() {
		return getFlag(Element.Flags.FINAL);
	}
	
	public void setFinal(boolean value) {
		setFlag(Element.Flags.FINAL, value);
	}
	
	public boolean isStatic() {
		return getFlag(Element.Flags.STATIC);
	}
	
	public void setStatic(boolean value) {
		setFlag(Element.Flags.STATIC, value);
	}

}
