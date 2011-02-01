package edu.atilim.acma.transition;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.metrics.MetricCalculator;
import edu.atilim.acma.search.NeighborSet;
import edu.atilim.acma.search.Solution;

public class DesignWrapper implements Solution {
	private Design design;
	private RunConfig config;
	private double score;
	
	public static DesignWrapper wrap(Design d) {
		return new DesignWrapper(d, RunConfig.getDefault());
	}
	
	public static DesignWrapper wrap(Design d, RunConfig c) {
		return new DesignWrapper(d, c);
	}
	
	public Design getDesign() {
		return design;
	}
	
	RunConfig getConfig() {
		return config;
	}

	DesignWrapper(Design design, RunConfig config) {
		this.design = design;
		this.config = config;
		this.score = -1;
	}

	@Override
	public double score() {
		if (score < 0)
			score = MetricCalculator.normalize(MetricCalculator.calculate(design, config));
		
		return score;
	}

	@Override
	public NeighborSet neighbors() {
		return new TransitionSet(this);
	}
}
