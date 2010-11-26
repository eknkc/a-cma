package edu.atilim.acma.uml;

import java.io.Serializable;
import java.util.UUID;

import edu.atilim.acma.uml.Transaction.Action;

public abstract class Element implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int flags;
	private String name;
	private Design design;
	private UUID serial;
	private DependencyList dependencies;
	
	public Element(String name, Design design) {
		super();
		this.name = name;
		this.design = design;
		this.flags = 0;
		this.serial = UUID.randomUUID();
		this.dependencies = new DependencyList(this);
	}
	
	public abstract String getFullName();
	
	public Element getParentElement() {
		return getDeps().getSingle(Dependency.PARENT, Element.class);
	}
	
	public Iterable<Element> childElements() {
		return getDeps().get(Dependency.PARENT, Element.class, true);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		design.notifyAction(new Action.NameChange(this, name));
		this.name = name;
	}

	public Design getDesign() {
		return design;
	}

	public void setDesign(Design design) {
		this.design = design;
	}

	public UUID getSerial() {
		return serial;
	}
	
	protected DependencyList getDeps() {
		return dependencies;
	}

	protected boolean getFlag(int flag) {
		return ((flags & flag) != 0);
	}
	
	protected void setFlag(int flag, boolean value) {
		design.notifyAction(new Action.FlagChange(this, flags));
		if (value) flags |= flag;
		else flags &= ~flag;
	}
	
	protected void setFlag(int flag) {
		setFlag(flag, true);
	}
	
	protected void setFlags(int flags) {
		design.notifyAction(new Action.FlagChange(this, flags));
		this.flags = flags;
	}
	
	public void remove() {
		getDeps().clear();
	}
	
	@Override
	public int hashCode() {
		return serial.hashCode();
	}

	protected static final class Flags {
		public static final int PACKAGE			=	0x00000001;
		public static final int CLASS			=	0x00000002;
		public static final int INTERFACE		=	0x00000004;
		public static final int METHOD			=	0x00000008;
		public static final int FIELD			=	0x00000010;
		
		public static final int ABSTRACT		=	0x00000100;
		public static final int FINAL			=	0x00000200;
		public static final int STATIC			=	0x00000400;
		
		public static final int ACC_PRIVATE		=	0x00010000;
		public static final int ACC_PACKAGE		=	0x00020000;
		public static final int ACC_PROTECTED	=	0x00040000;
		public static final int ACC_PUBLIC		=	0x00000000;
		public static final int ACC_ALL			=	0x000F0000;
	}
}
