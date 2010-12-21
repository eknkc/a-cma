package edu.atilim.acma.design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import edu.atilim.acma.util.CollectionHelper;

public class Type extends Node {
	private static final long serialVersionUID = 1L;
	
	private Reference parentType;
	private Reference superType;
	private List<Reference> interfaces;
	
	public Type(String name, Design design) {
		super(name, design);
		this.interfaces = new ArrayList<Reference>();
	}
	
	@Override
	public boolean remove() {
		if (!super.remove()) return false;
		parentType.release();
		superType.release();
		for (Reference ref : interfaces)
			ref.release();
		getDesign().removeType(this);
		return true;
	}
	
	public boolean isInterface() {
		return getFlag(Tags.TYP_INTERFACE);
	}
	
	public void setInterface(boolean value) {
		setFlag(Tags.TYP_INTERFACE, value);
	}
	
	public Type getParentType() {
		return parentType == null ? null : parentType.getTarget(Type.class);
	}
	
	public void setParentType(Type parentType) {
		if (this.parentType != null)
			this.parentType.release();
		
		this.parentType = getReference(this, parentType, Tags.REF_PARENT);
	}

	public Type getSuperType() {
		return superType == null ? null : superType.getTarget(Type.class);
	}

	public void setSuperType(Type superType) {
		if (superType != null && superType.isInterface())
			throw new RuntimeException("Can not set an interface as superclass");
		
		if (this.superType != null)
			this.superType.release();
		
		this.superType = getReference(this, superType, Tags.REF_SUPERCLASS);
	}

	public List<Type> getInterfaces() {
		List<Type> typeList = CollectionHelper.map(interfaces, new Reference.TargetSelector<Type>(Type.class));
		return Collections.unmodifiableList(typeList);
	}
	
	public boolean containsInterface(Type t) {
		if (!t.isInterface()) return false;
		for (Reference r : interfaces)
			if (t == r.getTarget()) return true;
		return false;
	}
	
	public void addInterface(Type t) {
		if (t == null) return;
		if (!t.isInterface())
			throw new RuntimeException("Can not add a class as interface.");
		if (containsInterface(t)) return;
		interfaces.add(getReference(this, t, Tags.REF_IMPLEMENT));
	}
	
	public void removeInterface(Type t) {
		for (Reference r : interfaces) {
			if (t == r.getTarget()) {
				r.release();
				interfaces.remove(r);
				return;
			}
		}
	}
	
	public Field getField(String name) {
		for (Field f : getFields()) {
			if (name.equals(f.getName()))
				return f;
		}
		return null;
	}
	
	public Method getMethod(String name) {
		for (Method m : getMethods()) {
			if (name.equals(m.getName()) || name.equals(m.getSignature()))
				return m;
		}
		return null;
	}
	
	public List<Method> getMethods() {
		return getReferers(Tags.REF_PARENT, Method.class);
	}
	
	public List<Field> getFields() {
		return getReferers(Tags.REF_PARENT, Field.class);
	}
	
	public List<Type> getNestedTypes() {
		return getReferers(Tags.REF_PARENT, Type.class);
	}
	
	public List<Type> getExtenders() {
		return getReferers(Tags.REF_SUPERCLASS, Type.class);
	}
	
	public List<Type> getImplementers() {
		return getReferers(Tags.REF_IMPLEMENT, Type.class);
	}
	
	public List<Field> getDependentFields() {
		return getReferers(Tags.REF_RETURN, Field.class);
	}
	
	public List<Method> getDependentMethodsAsParameter() {
		return getReferers(Tags.REF_PARAMETER, Method.class);
	}
	
	public List<Method> getDependentMethodsAsReturnType() {
		return getReferers(Tags.REF_RETURN, Method.class);
	}
	
	public List<Method> getDependentMethodsAsInstantiator() {
		return getReferers(Tags.REF_INSTANTIATE, Method.class);
	}
	
	public Method createMethod(String name) {
		Method m = new Method(name, getDesign());
		m.setOwnerType(this);
		return m;
	}
	
	public Field createField(String name) {
		Field f = new Field(name, getDesign());
		f.setOwnerType(this);
		return f;
	}
	
	public Field createField(String name, Type fieldType) {
		Field f = createField(name);
		f.setType(fieldType);
		return f;
	}
	
	@Override
	public Package getPackage() {
		String name = getName();
		int lastDot = name.lastIndexOf(".");
		if (lastDot < 0) return Package.emptyPackage(getDesign());
		return new Package(name.substring(0, lastDot), getDesign());
	}
	
	@Override
	public Type getOwnerType() {
		return this;
	}
	
	public boolean isAncestorOf(Type other) {
		if (other == null) return false;
		Stack<Type> supTypes = new Stack<Type>();
		
		supTypes.push(other);
		
		while (supTypes.size() > 0) {
			Type sup = supTypes.pop();
			if (this == sup) return true;
			
			if (sup.getSuperType() != null)
				supTypes.add(sup.getSuperType());
			
			supTypes.addAll(sup.getInterfaces());
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}