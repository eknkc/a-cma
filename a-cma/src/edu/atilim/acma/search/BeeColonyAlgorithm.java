package edu.atilim.acma.search;

import java.util.ArrayList;
import java.util.UUID;

import edu.atilim.acma.util.ACMAUtil;

public class BeeColonyAlgorithm extends AbstractAlgorithm {
	private static final int randomDepth = 20;
	
	private int maxTrials = 70;
	private int populationSize = 50;
	private int iterations = 5000;
	
	private ArrayList<FoodSource> foods;
	private FoodSource best;

	public BeeColonyAlgorithm(SolutionDesign initialDesign,	AlgorithmObserver observer) {
		super(initialDesign, observer);
	}

	@Override
	public String getName() {
		return "Artificial Bee Colony";
	}

	@Override
	public boolean step() {
		if (foods == null) {
			log("Generating initial food sources.");
			
			foods = new ArrayList<BeeColonyAlgorithm.FoodSource>();
			
			for (int i = 0; i < populationSize; i++) {
				foods.add(new FoodSource(initialDesign.getRandomNeighbor(randomDepth)));
			}
			
			log("Generated %d food sources.", populationSize);
		}
		
		if (getStepCount() > iterations) {
			finalDesign = best.design;
			return true;
		}
		
		if (best == null)
			log("Starting iteration %d.", getStepCount());
		else
			log("Starting iteration %d. Best score: %.6f", getStepCount(), best.design.getScore());
		
		sendEmployedBees();
		calculateProbabilities();
		sendOnlookerBees();
		memorizeBestSource();
		sendScoutBees();
		
		return false;
	}
	
	private void sendEmployedBees() {
		int better = 0;
		
		log("Sending employed bees.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			FoodSource neighbor = current.mutate();
			
			if (neighbor.getFitness() > current.getFitness()) {
				foods.set(i, neighbor);
				better++;
			} else {
				current.trialCount++;
			}
		}
	}
	
	private void calculateProbabilities() {
		log("Calculating probabilities.");
		
		double maxFitness = 0.0;
		
		for (int i = 0; i < foods.size(); i++) {
			double currentFitness = foods.get(i).getFitness();
			
			if (currentFitness > maxFitness) 
				maxFitness = currentFitness;
		}
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			current.probability = (0.9 * (current.getFitness() / maxFitness)) + 0.1;
		}
	}
	
	private void sendOnlookerBees() {
		log("Sending onlooker bees.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			if (current.probability > ACMAUtil.RANDOM.nextDouble()) {
				FoodSource neighbor = current.mutate();
				
				if (neighbor.getFitness() > current.getFitness()) {
					foods.set(i, neighbor);
				} else {
					current.trialCount++;
				}
			}
		}
	}
	
	private void memorizeBestSource() {
		log("Memorizing best food source.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			if (best == null || current.getFitness() > best.getFitness()) {
				best = current;
			}
		}
	}
	
	private void sendScoutBees() {
		log("Sending scout bees.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			if (current.trialCount > maxTrials) {
				foods.set(i, new FoodSource(initialDesign.getRandomNeighbor(randomDepth)));
			}
		}
	}
	
	private class FoodSource {
		private UUID id;
		private SolutionDesign design;
		private int trialCount;
		private double probability;

		private FoodSource(SolutionDesign design) {
			this.id = UUID.randomUUID();
			
			this.design = design;
			this.trialCount = 0;
			this.probability = 0.0;
		}

		public double getFitness() {
			return 1.0 / design.getScore();
		}
		
		public FoodSource mutate() {
			return new FoodSource(design.getRandomNeighbor());
		}
		
		@Override
		public int hashCode() {
			return id.hashCode();
		}
	}
}
