package edu.atilim.acma.search;

public interface NeighborSet extends Iterable<Solution> {
	Solution randomNeighbor();
}
