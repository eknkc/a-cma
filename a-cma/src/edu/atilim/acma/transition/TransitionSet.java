package edu.atilim.acma.transition;

import java.util.Iterator;
import java.util.Set;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.search.NeighborSet;
import edu.atilim.acma.search.Solution;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.util.CollectionHelper;

public class TransitionSet implements NeighborSet {
	private Set<Action> actions;
	private Design design;
	
	
	public TransitionSet(Design design) {
		this.design = design;
		this.actions = design.getPossibleActions();
	}
	
	private Design getModifiedDesign(Action action) {
		Design newDesign = design.copy();
		action.perform(newDesign);
		newDesign.logModification(action.toString());
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
