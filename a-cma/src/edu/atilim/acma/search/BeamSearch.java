package edu.atilim.acma.search;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BeamSearch implements Algorithm {
	private long maxIterations;
	private int beamLength;
	private int randomizationDepth;
	private PriorityQueue<Solution> pq;

	public long getMaxIterations() {
		return maxIterations;
	}

	public void setMaxIterations(long maxIterations) {
		this.maxIterations = maxIterations;
	}

	public BeamSearch(int beamLength, int randomizationDepth) {
		super();
		this.beamLength = beamLength;
		this.randomizationDepth = randomizationDepth;
	}

	@Override
	public Solution run(Solution initial) {
		Comparator<Solution> comparer = new Solution.Comparer();
		pq = new PriorityQueue<Solution>(beamLength, comparer);

		for (int i = 0; i < beamLength; i++) {
			Solution cur = initial;
			for (int j = 0; j < randomizationDepth; j++)
				cur = cur.neighbors().randomNeighbor();
			pq.add(cur);
		}
		
		int generation = 0;
		
		while (++generation < maxIterations)
		{
			Solution curbest = pq.peek();
			
			PriorityQueue<Solution> newpq = new PriorityQueue<Solution>(beamLength, comparer);
			
			for (int i = 0; i < beamLength; i++)
			{
				Solution s = pq.poll();
				if (s == null) break;
				
				newpq.add(s);
				
				for (Solution n : s.neighbors())
					newpq.add(n);
			}
			
			pq = newpq;
			
			Solution newbest = pq.peek();
			
			if (newbest == curbest)
				break;
		}
		
		return pq.peek();
	}
}
