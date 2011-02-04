package edu.atilim.acma.search;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.metrics.MetricCalculator;
import edu.atilim.acma.transition.TransitionManager;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.util.ACMAUtil;

public class SolutionDesign implements Iterable<SolutionDesign>, Comparable<SolutionDesign> {
	private Design design;
	private RunConfig config;
	private List<Action> actions;
	
	private double score = Double.NaN;
	
	public Design getDesign() {
		return design;
	}

	public RunConfig getConfig() {
		return config;
	}
	
	public double getScore() {
		if (Double.isNaN(score)) {
			score = MetricCalculator.normalize(MetricCalculator.calculate(design, config), config);
		}
		return score;
	}
	
	public SolutionDesign getBestNeighbor() {
		SolutionDesign best = this;
		for (SolutionDesign sd : this) {
			if (sd.isBetterThan(best))
				best = sd;
		}
		return best;
	}
	
	public SolutionDesign getRandomNeighbor() {
		List<Action> actions = getAllActions();
		if (actions.isEmpty()) return this;
		return apply(actions.get(ACMAUtil.RANDOM.nextInt(actions.size())));
	}
	
	public SolutionDesign getRandomNeighbor(int depth) {
		SolutionDesign random = this;
		for (int i = 0; i < depth; i++)
			random = random.getRandomNeighbor();
		return random;
	}
	
	public List<Action> getAllActions() {
		if (actions == null) {
			actions = new ArrayList<Action>(TransitionManager.getPossibleActions(design, config));
		}
		return actions;
	}
	
	public SolutionDesign(Design design, RunConfig config) {
		this.design = design;
		this.config = config;
	}
	
	public boolean isBetterThan(SolutionDesign other) {
		return compareTo(other) > 0;
	}
	
	@Override
	public int compareTo(SolutionDesign o) {
		return Double.compare(compareScoreTo(o), 0.0);
	}
	
	public double compareScoreTo(SolutionDesign o) {
		return o.getScore() - getScore();
	}

	@Override
	public Iterator<SolutionDesign> iterator() {
		return new Iter();
	}
	
	private SolutionDesign apply(Action action) {
		Design copyDesign = design.copy();
		action.perform(copyDesign);
		copyDesign.logModification(action.toString());
		return new SolutionDesign(copyDesign, config);
	}
	
	private class Iter implements Iterator<SolutionDesign> {
		private Iterator<Action> innerIterator;
		
		private Iter() {
			innerIterator = getAllActions().iterator();
		}
		
		@Override
		public boolean hasNext() {
			return innerIterator.hasNext();
		}

		@Override
		public SolutionDesign next() {
			return apply(innerIterator.next());
		}

		@Override
		public void remove() {
			innerIterator.remove();
		}
	}
}
