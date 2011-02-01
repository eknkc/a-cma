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
	private DesignWrapper design;
	
	
	public TransitionSet(DesignWrapper design) {
		this.design = design;
		this.actions = TransitionManager.getPossibleActions(design.getDesign(), design.getConfig());
	}
	
	private Design getModifiedDesign(Action action) {
		Design newDesign = design.getDesign().copy();
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
		if (action == null) return DesignWrapper.wrap(design.getDesign(), design.getConfig());
		return DesignWrapper.wrap(getModifiedDesign(action), design.getConfig());
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
			
			return DesignWrapper.wrap(getModifiedDesign(next), design.getConfig());
		}

		@Override
		public void remove() {
			throw new RuntimeException("Can not remove elements from a Transition Set");
		}
		
	}
}
