package edu.atilim.acma.design;

import java.io.Serializable;
import java.util.List;

import edu.atilim.acma.util.Log;

public abstract class Node implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ReferenceList incomingReferences;
	private String name;
	private int flags;
	private Design design;
	
	Node(String name, Design design) {
		this.incomingReferences = new ReferenceList();
		this.name = name;
		this.flags = 0;
		this.design = design;
	}
	
	public String getName() {
		return name;
	}
	
	public Design getDesign() {
		return design;
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
	
	public Reference getReference(Node from, Node to, int tag) {
		Reference r = design.getReference(from, to, tag);
		if (r != null && r.getTarget() != null)
			r.getTarget().incomingReferences.add(r);
		return r;
	}
	
	public Reference getReference(Node from, String to, int tag) {
		Reference r = design.getReference(from, to, tag);
		if (r != null && r.getTarget() != null)
			r.getTarget().incomingReferences.add(r);
		return r;
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
	
	public boolean remove() {
		if (incomingReferences.size() > 0) {
			Log.warning("Trying to remove node [%s] from design with incoming references present.", getName());
			return false;
		}
		return true;
	}
	
	public abstract String getPackage();
	public abstract Type getOwnerType();
	
	public boolean canAccess(Node other) {
		return canAccess(other, other.getAccess());
	}
	
	public boolean canAccess(Node other, Accessibility otheroverride) {
		if (otheroverride == Accessibility.PUBLIC)
			return true;
		
		if (otheroverride == Accessibility.PACKAGE)
			return getPackage().equals(other.getPackage());
		
		if (otheroverride == Accessibility.PROTECTED) {
			Type myType = getOwnerType();
			Type otType = other.getOwnerType();
			
			if (myType == null || otType == null) return false;
			
			return otType.isAncestorOf(myType);
		}
		
		if (otheroverride == Accessibility.PRIVATE) {
			return getOwnerType() == other.getOwnerType();
		}
		
		return false;
	}
}
