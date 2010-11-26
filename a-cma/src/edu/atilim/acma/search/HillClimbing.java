package edu.atilim.acma.search;

import edu.atilim.acma.util.Log;

public class HillClimbing implements Algorithm {
	private int restartCount, curRestartCount;
	private int restartDepth;
	private long maxIterations;
	private long iterations;
	
	Solution curBest;
	
	public long getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(long maxIterations) {
		this.maxIterations = maxIterations;
	}

	public HillClimbing() {
		this(0, 0);
	}
	
	public HillClimbing(int restartCount, int restartDepth) {
		this.restartCount = restartCount;
		this.restartDepth = restartDepth;
		this.curBest = null;
		this.iterations = 0;
		this.maxIterations = Long.MAX_VALUE;
	}

	@Override
	public Solution run(Solution initial) {
		Log.info("[Hill Climbing] Starting with %d restarts and %d restart depth.", restartCount, restartDepth);
		
		Solution cur = curBest = initial;
		
		while(true)
		{
			if (iterations++ >= maxIterations)
				return curBest;
			
			Solution next = cur;
			
			for (Solution n : cur.neighbors()) {
				if (n.score() > next.score())
					next = n;
			}
			
			if (next.score() > curBest.score())
				curBest = cur;
			
			if (next == cur) {
				Log.info("[Hill Climbing] Found optimum point with score %f", cur.score());
				
				if (curRestartCount == restartCount)
					return curBest;
				
				curRestartCount++;
				
				Log.info("[Hill Climbing] Restarting %d/%d", curRestartCount, restartCount);
				
				for (int i = 0; i < restartDepth; i++)
					next = next.neighbors().randomNeighbor();
			}
			
			cur = next;
		}
	}
}
