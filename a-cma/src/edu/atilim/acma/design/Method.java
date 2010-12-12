package edu.atilim.acma.design;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.atilim.acma.util.CollectionHelper;
import edu.atilim.acma.util.Delegate.Func1P;

public class Method extends Node {
	private static final long serialVersionUID = 1L;
	
	private Reference ownerType;
	private Reference returnType;
	private List<Reference> paramTypes;
	private List<Reference> calledMethods;
	private List<Reference> accessedFields;
	private List<Reference> instantiatedTypes;
	
	public String getSignature() {
		StringBuilder sb = new StringBuilder();
		sb.append(getName())
		.append('(');
		for (int i = 0; i < paramTypes.size(); i++) {
			sb.append(paramTypes.get(i).toString());
			
			if (i != paramTypes.size() - 1)
				sb.append(',');
		}
		sb.append(')');
		
		return sb.toString();
	}
	
	public boolean isGetter() {
		return getName().startsWith("get");
	}
	
	public boolean isSetter() {
		return getName().startsWith("set");
	}
	
	public void setOwnerType(Type ownerType) {
		if (this.ownerType != null)
			this.ownerType.release();
		
		this.ownerType = getReference(this, ownerType, Tags.REF_PARENT);
	}

	public Type getOwnerType() {
		return ownerType == null ? null : ownerType.getTarget(Type.class);
	}
	
	public void setReturnType(Type returnType) {
		if (this.returnType != null)
			this.returnType.release();
		
		this.returnType = getReference(this, returnType, Tags.REF_RETURN);
	}

	public Type getReturnType() {
		return returnType == null ? null : returnType.getTarget(Type.class);
	}
	
	public List<Parameter> getParameters() {
		List<Parameter> typeList = CollectionHelper.map(paramTypes, new ParameterMapper());
		return Collections.unmodifiableList(typeList);
	}
	
	public boolean containsParameter(Type t) {
		return containsParameter(t, false);
	}
	
	public boolean containsParameter(Type t, boolean array) {
		for (Reference r : paramTypes)
			if (t == r.getTarget()) return true;
		return false;
	}
	
	public void addParameter(Type t) {
		addParameter(t, 1);
	}
	
	public void addParameter(Type t, int dimension) {
		Reference ref = getReference(this, t, Tags.REF_PARAMETER);
		ref.setDimension(dimension);
		paramTypes.add(ref);
	}
	
	public void addParameter(String t, int dimension) {
		Reference ref = getReference(this, t, Tags.REF_PARAMETER);
		ref.setDimension(dimension);
		paramTypes.add(ref);
	}
	
	public void removeParameter(Parameter p) {
		for (Reference r : paramTypes) {
			if (p.getType() == r.getTarget() && p.getDimension() == r.getDimension()) {
				r.release();
				paramTypes.remove(r);
				return;
			}
		}
	}
	
	public void removeParameter(int i) {
		Reference ref = paramTypes.get(i);
		ref.release();
		paramTypes.remove(ref);
	}
	
	public List<Method> getCalledMethods() {
		List<Method> methodList = CollectionHelper.map(calledMethods, new Reference.TargetSelector<Method>(Method.class));
		return Collections.unmodifiableList(methodList);
	}
	
	public void addCalledMethod(Method m) {
		if (m == null) return;
		for (Reference ref : calledMethods)
			if (m == ref.getTarget()) return;
		calledMethods.add(getReference(this, m, Tags.REF_DEPEND));
	}
	
	public void removeCalledMethod(Method m) {
		for (Reference r : calledMethods) {
			if (m == r.getTarget()) {
				r.release();
				calledMethods.remove(r);
				return;
			}
		}
	}
	
	public List<Field> getAccessedFields() {
		List<Field> methodList = CollectionHelper.map(accessedFields, new Reference.TargetSelector<Field>(Field.class));
		return Collections.unmodifiableList(methodList);
	}
	
	public void addAccessedField(Field f) {
		if (f == null) return;
		for (Reference ref : accessedFields)
			if (f == ref.getTarget()) return;
		accessedFields.add(getReference(this, f, Tags.REF_DEPEND));
	}
	
	public void removeAccessedField(Field f) {
		for (Reference r : accessedFields) {
			if (f == r.getTarget()) {
				r.release();
				accessedFields.remove(r);
				return;
			}
		}
	}
	
	public List<Type> getInstantiatedTypes() {
		List<Type> methodList = CollectionHelper.map(instantiatedTypes, new Reference.TargetSelector<Type>(Type.class));
		return Collections.unmodifiableList(methodList);
	}
	
	public void addInstantiatedType(Type t) {
		if (t == null) return;
		for (Reference ref : instantiatedTypes)
			if (t == ref.getTarget()) return;
		instantiatedTypes.add(getReference(this, t, Tags.REF_INSTANTIATE));
	}
	
	public void removeInstantiatedType(Type t) {
		for (Reference r : instantiatedTypes) {
			if (t == r.getTarget()) {
				r.release();
				instantiatedTypes.remove(r);
				return;
			}
		}
	}
	
	public List<Method> getCallerMethods() {
		return getReferers(Tags.REF_DEPEND, Method.class);
	}
	
	public Method(String name, Design design) {
		super(name, design);
		
		this.paramTypes = new ArrayList<Reference>();
		this.calledMethods = new ArrayList<Reference>();
		this.accessedFields = new ArrayList<Reference>();
		this.instantiatedTypes = new ArrayList<Reference>();
	}
	
	@Override
	public String toString() {
		return getSignature();
	}
	
	@Override
	public String getPackage() {
		Type owner = getOwnerType();
		if (owner == null) return "";
		return owner.getPackage();
	}
	
	private class ParameterMapper implements Func1P<Parameter, Reference> {
		@Override
		public Parameter run(Reference in) {
			Type t = in.getTarget(Type.class);
			if (t != null)
				return new Parameter(in.getTarget(Type.class), in.getDimension());
			return null;
		}
	}
	
	public class Parameter {
		private Type type;
		private int dimension;
		
		public Type getType() {
			return type;
		}
		
		public int getDimension() {
			return dimension;
		}

		Parameter(Type type, int dimension) {
			this.type = type;
			this.dimension = dimension;
		}
	}
}
