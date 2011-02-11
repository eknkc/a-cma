package edu.atilim.acma.concurrent;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class InstanceSet implements Iterable<Instance> {
	private Set<Instance> instances;
	
	public void setInstanceListener(InstanceListener listener) {
		for (Instance i : instances) {
			i.setInstanceListener(listener);
		}
	}
	
	public InstanceSet() {
		instances = Collections.synchronizedSet(new HashSet<Instance>());
	}
	
	public void add(Instance instance) {
		instances.add(instance);
	}
	
	public void broadcast(Serializable message) {
		for (Instance i : instances)
			i.send(message);
	}
	
	@Override
	public Iterator<Instance> iterator() {
		return instances.iterator();
	}

	public int size() {
		return instances.size();
	}
}
