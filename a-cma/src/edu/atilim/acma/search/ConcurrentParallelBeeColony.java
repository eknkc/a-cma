package edu.atilim.acma.search;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.UUID;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.concurrent.Instance;
import edu.atilim.acma.concurrent.InstanceSet;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.util.ACMAUtil;

public class ConcurrentParallelBeeColony extends ConcurrentAlgorithm {
	private static final int randomDepth = 20;
	
	private static final int COMMAND_GENERATE = 1;
	private static final int COMMAND_STEP = 2;
	private static final int COMMAND_EXIT = 4;
	private static final int COMMAND_RESETEXPANSION = 5;
	private static final int COMMAND_DUMPEXPANSION = 6;
	
	private int maxTrials;
	private int populationSize;
	private int iterations;
	private int runCount;
	
	public ConcurrentParallelBeeColony() {
	}

	public ConcurrentParallelBeeColony(String name, RunConfig config, Design initialDesign, int maxTrials, int populationSize, int iterations, int runCount) {
		super(name, config, initialDesign);
		
		this.maxTrials = maxTrials;
		this.populationSize = populationSize;
		this.iterations = iterations;
		this.runCount = runCount;
	}

	@Override
	public void runMaster(InstanceSet instances) {
		for (int run = 0; run < runCount; run++) {
			long startTime = System.currentTimeMillis();
			
			instances.broadcast(COMMAND_RESETEXPANSION);
			
			System.out.printf("Ordering food source generation to %d instances.\n", instances.size());
			
			boolean first = true;
			int sourcesPerInstance = populationSize / instances.size();
			int remainder = populationSize % instances.size();
			for (Instance i : instances) {
				i.send(COMMAND_GENERATE);
				
				if (first) i.send(sourcesPerInstance + remainder);
				else i.send(sourcesPerInstance);
				
				first = false;
			}
			
			SolutionDesign best = null;
			
			for (int i = 0; i < iterations; i++) {
				System.out.printf("Starting iteration %d, current best score: %.4f.\n", i + 1, best == null ? 0 : best.getScore());
				
				instances.broadcast(COMMAND_STEP);
				
				for (Instance instance : instances) {
					SolutionDesign cur = new SolutionDesign(instance.receive(Design.class), getConfig());
					
					if (best == null || cur.isBetterThan(best))
						best = cur;
				}
				instances.broadcast(best.getDesign());
			}
						
			System.out.printf("Finished %d iterations. Found best design with score: %.6f.\n", iterations, best.getScore());
			
			instances.broadcast(COMMAND_DUMPEXPANSION);
			expansion = 0;
			for (Instance i : instances) {
				expansion += i.receive(Long.class);
			}
			
			Design bestDesign = best.getDesign();
			bestDesign.setTag(new RunInfoTag(System.currentTimeMillis() - startTime, 
					String.format("Artificial Bee Colony. Population Size: %d, Max Trials: %d, Iterations: %d.", populationSize, maxTrials, iterations), expansion));
			onFinish(bestDesign);
		}
		
		instances.broadcast(COMMAND_EXIT);
	}
	
	private transient long expansion = 0;

	@Override
	public void runWorker(Instance master) {
		ArrayList<FoodSource> foods = new ArrayList<ConcurrentParallelBeeColony.FoodSource>();
		FoodSource best = null;
		
		infinite:
		while(true) {
			int command = master.receive(Integer.class);
			
			switch(command) {
			case COMMAND_GENERATE:
				createFoodSources(foods, master.receive(Integer.class));
				break;
			case COMMAND_STEP:
				sendEmployedBees(foods);
				master.send(getBestSource(foods).design.getDesign());
				best = new FoodSource(new SolutionDesign(master.receive(Design.class), getConfig()));
				calculateProbabilities(foods, best.getFitness());
				sendOnlookerBees(foods);
				sendScoutBees(foods, best.design);
				break;
			case COMMAND_EXIT:
				break infinite;
			case COMMAND_RESETEXPANSION:
				expansion = 0;
				break;
			case COMMAND_DUMPEXPANSION:
				master.send(expansion);
				break;
			}
		}
		
		System.out.println("Finalizing...");
	}
	
	private void createFoodSources(ArrayList<FoodSource> foods, int n) {
		System.out.printf("Creating %d food sources as initial population.\n", n);
		foods.clear();
		
		SolutionDesign initial = new SolutionDesign(getInitialDesign(), getConfig());
		for (int i = 0; i < n; i++) {
			foods.add(new FoodSource(initial.getRandomNeighbor(randomDepth)));
		}
		System.out.printf("Created food sources.\n", n);
		
		expansion += n;
	}
	
	private void sendEmployedBees(ArrayList<FoodSource> foods) {
		int better = 0;
		
		System.out.println("Sending employed bees.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			FoodSource neighbor = current.mutate();
			
			if (neighbor.isBetterThan(current)) {
				foods.set(i, neighbor);
				better++;
			} else {
				current.trialCount++;
			}
		}
		
		expansion += foods.size();
	}
	
	private FoodSource getBestSource(ArrayList<FoodSource> foods) {
		FoodSource best = null;
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			if (best == null || current.getFitness() > best.getFitness()) {
				best = current;
			}
		}
		
		return best;
	}
	
	private void calculateProbabilities(ArrayList<FoodSource> foods, double maxFitness) {
		System.out.println("Calculating probabilities.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			current.probability = (0.9 * (current.getFitness() / maxFitness)) + 0.1;
		}
	}
	
	private void sendOnlookerBees(ArrayList<FoodSource> foods) {
		System.out.println("Sending onlooker bees.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			if (current.probability > ACMAUtil.RANDOM.nextDouble()) {
				FoodSource neighbor = current.mutate();
				
				if (neighbor.isBetterThan(current)) {
					foods.set(i, neighbor);
				} else {
					current.trialCount++;
				}
				
				expansion++;
			}
		}
	}
	
	private void sendScoutBees(ArrayList<FoodSource> foods, SolutionDesign randomSource) {
		System.out.println("Sending scout bees.");
		
		for (int i = 0; i < foods.size(); i++) {
			FoodSource current = foods.get(i);
			
			if (current.trialCount > maxTrials) {
				foods.set(i, new FoodSource(randomSource.getRandomNeighbor(randomDepth)));
				expansion++;
			}
		}
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		
		out.writeInt(0); //version
		
		out.writeInt(maxTrials);
		out.writeInt(populationSize);
		out.writeInt(iterations);
		out.writeInt(runCount);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		
		in.readInt();
		maxTrials = in.readInt();
		populationSize = in.readInt();
		iterations = in.readInt();
		runCount = in.readInt();
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
		
		public boolean isBetterThan(FoodSource other) {
			return design.isBetterThan(other.design);
		}
		
		@Override
		public int hashCode() {
			return id.hashCode();
		}
	}
}
