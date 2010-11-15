package edu.atilim.acma.uml;

public abstract class AccessibleElement extends Element {
	private static final long serialVersionUID = 1L;
	
	public AccessibleElement(String name, Design design) {
		super(name, design);
	}

	public Accessibility getAccess() {
		if (getFlag(Element.Flags.ACC_PRIVATE)) return Accessibility.PRIVATE;
		if (getFlag(Element.Flags.ACC_PROTECTED)) return Accessibility.PROTECTED;
		if (getFlag(Element.Flags.ACC_PACKAGE)) return Accessibility.PACKAGE;
		return Accessibility.PUBLIC;
	}
	
	public void setAccess(Accessibility access) {
		setFlag(Element.Flags.ACC_ALL, false);
		switch(access) {
		case PRIVATE:
			setFlag(Element.Flags.ACC_PRIVATE);
			break;
		case PROTECTED:
			setFlag(Element.Flags.ACC_PROTECTED);
			break;
		case PACKAGE:
			setFlag(Element.Flags.ACC_PACKAGE);
			break;
		default:
			setFlag(Element.Flags.ACC_PUBLIC);
		}
	}
}
