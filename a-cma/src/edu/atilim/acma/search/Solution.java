package edu.atilim.acma.search;

public interface Solution {
	double score();
	NeighborSet neighbors();
}
