package edu.atilim.acma.design;

import java.io.Serializable;
import java.util.List;

public abstract class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ReferenceList incomingReferences;
	private String name;
	private int flags;
	
	public String getName() {
		return name;
	}
	
	protected boolean getFlag(int flag) {
		return (flags & flag) > 0;
	}
	
	protected void setFlag(int flag) {
		setFlag(flag, true);
	}
	
	protected void setFlag(int flag, boolean value) {
		if (value)
			flags |= flag;
		else
			flags &= ~flag;
	}

	Node(String name) {
		this.incomingReferences = new ReferenceList();
		this.name = name;
		this.flags = 0;
	}
	
	public static Reference getReference(Node from, Node to, int tag) {
		if (from == null || to == null) return null;
		
		Reference ref = Reference.get(from, to, tag);
		to.incomingReferences.add(ref);
		return ref;
	}
	
	public static Reference getReference(Node from, String to, int tag) {
		if (from == null) return null;
		return Reference.get(from, to, tag);
	}
	
	public void removeReference(Reference ref) {
		incomingReferences.remove(ref);
	}
	
	public <T extends Node> List<T> getReferers(Class<T> cls) {
		return getReferers(-1, cls);
	}
	
	public <T extends Node> List<T> getReferers(int tag, Class<T> cls) {
		return incomingReferences.getSourcesByTag(tag, cls);
	}
	
	public boolean isFinal() {
		return getFlag(Tags.FINAL);
	}
	
	public void setFinal(boolean value) {
		setFlag(Tags.FINAL, value);
	}
	
	public boolean isStatic() {
		return getFlag(Tags.STATIC);
	}
	
	public void setStatic(boolean value) {
		setFlag(Tags.STATIC, value);
	}
	
	public boolean isAbstract() {
		return getFlag(Tags.ABSTRACT);
	}
	
	public void setAbstract(boolean value) {
		setFlag(Tags.ABSTRACT, value);
	}
	
	public Accessibility getAccess() {
		if (getFlag(Tags.PRIVATE)) return Accessibility.PRIVATE;
		if (getFlag(Tags.PROTECTED)) return Accessibility.PROTECTED;
		if (getFlag(Tags.PUBLIC)) return Accessibility.PUBLIC;
		return Accessibility.PACKAGE;
	}
	
	public void setAccess(Accessibility access) {
		setFlag(Tags.PUBLIC | Tags.PRIVATE | Tags.PROTECTED | Tags.PACKAGE, false);
		switch(access) {
		case PRIVATE:
			setFlag(Tags.PRIVATE);
			break;
		case PROTECTED:
			setFlag(Tags.PROTECTED);
			break;
		case PUBLIC:
			setFlag(Tags.PUBLIC);
			break;
		default:
			setFlag(Tags.PACKAGE);
		}
	}
}