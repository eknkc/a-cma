package edu.atilim.acma.design;

import java.io.Serializable;

import edu.atilim.acma.util.Delegate.Func1P;

public abstract class Reference implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Node source;
	private int tag;
	private boolean array;
	
	static Reference get(Node from, Node to, int tag) {
		return new InnerReference(from, to, tag);
	}
	
	public static Reference get(Node from, String to, int tag) {
		return new OuterReference(from, to, tag);
	}
	
	public Reference(Node source, int tag) {
		this.source = source;
		this.tag = tag;
		this.array = false;
	}
	
	public Node getSource() {
		return source;
	}
	
	public int getTag() {
		return tag;
	}
	
	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	@SuppressWarnings("unchecked")
	public <T extends Node> T getTarget(Class<T> cls) {
		Node target = getTarget();
		if (target != null && cls.isInstance(target))
			return (T)target;
		
		return null;
	}
	
	public abstract Node getTarget();
	public abstract boolean isInternal();
	
	public void release() {
		
	}
	
	static class InnerReference extends Reference {
		private static final long serialVersionUID = 1L;
		
		private Node target;

		public InnerReference(Node source, Node target, int tag) {
			super(source, tag);
			this.target = target;
		}

		@Override
		public Node getTarget() {
			return target;
		}
		
		@Override
		public boolean isInternal() {
			return true;
		}
		
		@Override
		public void release() {
			getTarget().removeReference(this);
		}
		
		@Override
		public String toString() {
			return target.toString();
		}
	}
	
	static class OuterReference extends Reference {
		private static final long serialVersionUID = 1L;
		
		private String typeName;

		public OuterReference(Node source, String typeName, int tag) {
			super(source, tag);
			this.typeName = typeName;
		}

		@Override
		public Node getTarget() {
			return null;
		}
		
		@Override
		public boolean isInternal() {
			return false;
		}
		
		@Override
		public String toString() {
			return typeName;
		}
	}
	
	static class TargetSelector<T extends Node> implements Func1P<T, Reference> {
		private Class<T> cls;

		public TargetSelector(Class<T> cls) {
			this.cls = cls;
		}
		
		@Override
		public T run(Reference in) {
			return in.getTarget(cls);
		}
		
	}
}
