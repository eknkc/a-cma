package edu.atilim.acma.search;

public class HillClimbingAlgorithm extends AbstractAlgorithm {
	public HillClimbingAlgorithm(SolutionDesign initialDesign,
			AlgorithmObserver observer) {
		super(initialDesign, observer);
		current = initialDesign;
	}

	private SolutionDesign current;
	private SolutionDesign best;
	
	@Override
	public String getName() {
		return "Hill Climbing";
	}

	@Override
	public boolean step() {
		log("Starting iteration %d. Current score: %.2f, Best score: %.2f", getStepCount() + 1, current.getScore(), best.getScore());
		SolutionDesign bestNeighbor = current.getBestNeighbor();
		log("Found neighbor with score %.2f score", bestNeighbor.getScore());
		
		if (bestNeighbor == current) {
			log("Found local best point.");
		}
		
		return false;
	}
}
