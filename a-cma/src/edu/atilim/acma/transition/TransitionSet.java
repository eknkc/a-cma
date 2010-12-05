package edu.atilim.acma.transition;

import java.util.HashSet;
import java.util.Iterator;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.search.NeighborSet;
import edu.atilim.acma.search.Solution;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.util.CollectionHelper;

public class TransitionSet implements NeighborSet {
	private HashSet<Action> actions;
	private Design design;
	
	public void add(Action action) {
		actions.add(action);
	}
	
	public TransitionSet(Design design) {
		this.actions = new HashSet<Action>();
		this.design = design;
	}
	
	private Design getModifiedDesign(Action action) {
		Design newDesign = design.copy();
		action.perform(newDesign);
		return newDesign;
	}

	@Override
	public Iterator<Solution> iterator() {
		return new Iter();
	}

	@Override
	public Solution randomNeighbor() {
		Action action = CollectionHelper.getRandom(actions);
		if (action == null) return DesignWrapper.wrap(design);
		return DesignWrapper.wrap(getModifiedDesign(action));
	}
	
	private class Iter implements Iterator<Solution> {
		private Iterator<Action> innerIterator;
		
		public Iter() {
			innerIterator = actions.iterator();
		}

		@Override
		public boolean hasNext() {
			return innerIterator.hasNext();
		}

		@Override
		public Solution next() {
			Action next = innerIterator.next();
			if (next == null) return null;
			
			return DesignWrapper.wrap(getModifiedDesign(next));
		}

		@Override
		public void remove() {
			throw new RuntimeException("Can not remove elements from a Transition Set");
		}
		
	}
}
