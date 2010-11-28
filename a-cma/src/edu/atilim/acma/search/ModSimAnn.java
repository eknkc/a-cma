package edu.atilim.acma.search;

import java.util.Random;

public class ModSimAnn implements Algorithm {
	private Cooler cooler;
	private EnergySet energySet;
	private Random randomizer;
	
	public ModSimAnn(Cooler cooler) {
		this.cooler = cooler;
		this.energySet = new EnergySet(100);
		this.randomizer = new Random();
	}
	
	public ModSimAnn(int numIterations) {
		this(numIterations, 3.5);
	}
	
	public ModSimAnn(int numIterations, double startTemperature) {
		this(new DefaultCooler(startTemperature, numIterations));
	}

	@Override
	public Solution run(Solution initial) {
		Solution cur, best;
		cur = best = initial;
		int i = 0;
		
		while (true) {
			double t = cooler.getTemperature(i++);
			
			if (t <= 0) break;
			
			Solution neighbor = cur.neighbors().randomNeighbor();
			
			if (neighbor.score() > best.score())
				best = neighbor;
			
			if (checkProbability(cur, neighbor, t))
				cur = neighbor;
		}
		
		return best;
	}
	
	private boolean checkProbability(Solution cur, Solution candidate, double t) {
		double deltaE = candidate.score() - cur.score();
		energySet.push(Math.abs(deltaE));
		
		if (deltaE > 0.0)
			return true;
		
		return Math.exp(((deltaE / energySet.getAverage()) / t)) > randomizer.nextDouble();
	}
	
	public static interface Cooler {
		double getTemperature(int iteration);
	}
	
	private static class DefaultCooler implements Cooler {
		private double start;
		private int maxIters;
		
		public DefaultCooler(double start, int maxIters) {
			this.start = start;
			this.maxIters = maxIters;
		}

		@Override
		public double getTemperature(int iteration) {
			if (iteration >= maxIters)
				return -1.0;
			
			return start * Math.sqrt((maxIters - iteration) / (double)maxIters);
		}
	}

	/* This one should act as an O(1) set for obtaining average of last n elements
	 * Now, it is 3:00 AM and I have been studying Parallel Programming for a while
	 * so this just might not be what I'm imagining.
	 * Check later.
	 * 
	 * - Ekin
	 */
	private static class EnergySet {
		private double[] values;
		private int head;
		private int tail;
		private int count;
		private double average;
		
		public double getAverage() {
			return average;
		}
		
		public EnergySet(int capacity) {
			values = new double[capacity];
			head = tail = count = 0;
			average = 0.0;
		}
		
		public void push(double val) {
			if (count == values.length) {
				count--;
				double first = values[head];
				average += (average - first) / count;
				head = (head + 1) % values.length;
			}
			
			count++;
			values[tail] = val;
			average += (val  - average) / count;
			tail = (tail + 1) % values.length;
		}
	}
}
