package edu.atilim.acma.search;

import java.util.Comparator;

public interface Solution {
	double score();
	NeighborSet neighbors();
	
	public static class Comparer implements Comparator<Solution>
	{
		@Override
		public int compare(Solution arg0, Solution arg1) {
			return -Double.compare(arg0.score(), arg1.score());
		}
	}
}
