package edu.atilim.acma.transition;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.metrics.MetricCalculator;
import edu.atilim.acma.search.NeighborSet;
import edu.atilim.acma.search.Solution;

public class DesignWrapper implements Solution {
	private Design design;
	
	static DesignWrapper wrap(Design d) {
		return new DesignWrapper(d);
	}
	
	public Design getDesign() {
		return design;
	}

	DesignWrapper(Design design) {
		this.design = design;
	}

	@Override
	public double score() {
		return MetricCalculator.calculate(design).getWeightedSum();
	}

	@Override
	public NeighborSet neighbors() {
		return null;
	}

}
