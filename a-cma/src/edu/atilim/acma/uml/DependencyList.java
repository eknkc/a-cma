package edu.atilim.acma.uml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import edu.atilim.acma.util.FilteredIterable;
import edu.atilim.acma.util.Selector;

class DependencyList implements Iterable<Dependency>, Serializable {
	private static final long serialVersionUID = 1L;

	private Element owner;
	private LinkedList<Dependency> list;
	
	public DependencyList(Element owner) {
		this.owner = owner;
		this.list = new LinkedList<Dependency>();
	}
	
	public void add(int type, Element other) {
		add(new Dependency(owner, other, type));
	}
	
	public void add(Dependency dep) {
		if (!isRelated(dep)) return;
		
		if ((dep.getType() & Dependency.FLAG_SINGULARDEP) != 0) {
			remove(dep.getType());
		}
		
		if ((dep.getType() & Dependency.FLAG_SINGULARTYPES) != 0) {
			remove(dep.getType(), getOtherParty(dep));
		}
		
		if (getOtherParty(dep) != null) {
			owner.getDesign().notifyAction(new Transaction.Action.DependencyAdd(dep));
			
			dep.getSource().getDeps().list.add(dep);
			dep.getTarget().getDeps().list.add(dep);
		}
	}
	
	private void remove(int type, Element other) {
		ArrayList<Dependency> deps = new ArrayList<Dependency>(list);
		for (Dependency dep : deps) {
			if (dep.getType() == type && (dep.getSource() == other || dep.getTarget() == other))
				remove(dep);
		}
	}
	
	public void remove(int type) {
		ArrayList<Dependency> deps = new ArrayList<Dependency>(list);
		for (Dependency dep : deps) {
			if (dep.getType() == type)
				remove(dep);
		}
	}
	
	public void remove(Dependency dep) {
		if (isRelated(dep)) {
			owner.getDesign().notifyAction(new Transaction.Action.DependencyRemove(dep));
			
			dep.getSource().getDeps().list.remove(dep);
			dep.getTarget().getDeps().list.remove(dep);
		}
	}
	
	public <T extends Element> T getSingle(int type, Class<T> cls) {
		return getSingle(type, cls, false);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Element> T getSingle(int type, Class<T> cls, boolean passive) {
		Dependency dep = getSingle(type, passive);
		if (dep == null) return null;
		if (cls.isInstance(getOtherParty(dep))) return (T)getOtherParty(dep);
		return null;
	}
	
	public Dependency getSingle(int type) {
		return getSingle(type, false);
	}
	
	public Dependency getSingle(int type, boolean passive) {
		if ((type & Dependency.FLAG_SINGULARDEP) == 0) return null;
		return get(type, passive).iterator().next();
	}
	
	public Iterable<Dependency> get(int type) {
		return get(type, false);
	}
	
	public Iterable<Dependency> get(final int type, final boolean passive) {
		return new FilteredIterable<Dependency, Dependency>(list, new Selector<Dependency, Dependency>() {
			@Override
			public Dependency select(Dependency in) {
				if (in.getType() == type && (passive ? isPassive(in) : isActive(in)))
					return in;
				return null;
			}
		});
	}
	
	public <T extends Element> Iterable<T> get(int type, Class<T> cls) {
		return get(type, cls, false);
	}
	
	public <T extends Element> Iterable<T> get(final int type, final Class<T> cls, final boolean passive) {
		return new FilteredIterable<Dependency, T>(list, new Selector<Dependency, T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T select(Dependency in) {
				if (in.getType() == type && (passive ? isPassive(in) : isActive(in)) && cls.isInstance(getOtherParty(in)))
					return (T)getOtherParty(in);
				return null;
			}
		});
	}
	
	public void clear() {
		ArrayList<Dependency> deps = new ArrayList<Dependency>(list);
		for (Dependency dep : deps)
			remove(dep);
	}
	
	private boolean isRelated(Dependency dep) {
		return isActive(dep) || isPassive(dep);
	}
	
	private boolean isActive(Dependency dep) {
		return dep.getSource() == owner;
	}
	
	private boolean isPassive(Dependency dep) {
		return dep.getTarget() == owner;
	}
	
	private Element getOtherParty(Dependency dep) {
		return dep.getOther(owner);
	}
	
	@Override
	public Iterator<Dependency> iterator() {
		return list.iterator();
	}
}
