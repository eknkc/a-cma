package edu.atilim.acma.uml;

public abstract class TypeMember extends AccessibleElement {
	private static final long serialVersionUID = 1L;
	
	public TypeMember(String name, Design design) {
		super(name, design);
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
