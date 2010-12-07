package edu.atilim.acma.transition;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.search.NeighborSet;
import edu.atilim.acma.search.Solution;

public class DesignWrapper implements Solution {
	private Design design;
	private double score;
	
	public static DesignWrapper wrap(Design d) {
		return new DesignWrapper(d);
	}
	
	public Design getDesign() {
		return design;
	}

	DesignWrapper(Design design) {
		this.design = design;
		this.score = -1;
	}

	@Override
	public double score() {
		if (score < 0)
			score = design.getMetrics().getWeightedSum();
		
		return score;
	}

	@Override
	public NeighborSet neighbors() {
		return new TransitionSet(design);
	}
}
