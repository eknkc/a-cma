package edu.atilim.acma.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import edu.atilim.acma.search.NeighborSet;
import edu.atilim.acma.search.Solution;

public class RastriginSolution implements Solution {
	private double x, y;
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public RastriginSolution(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public double score() {
		return -(20.0 + (x * x - 10 * Math.cos(2 * Math.PI * x)) + (y * y + (x * x - 10 * Math.cos(2 * Math.PI * y))));
	}

	@Override
	public NeighborSet neighbors() {
		return new NeighborSetImpl();
	}

	private class NeighborSetImpl implements NeighborSet
	{
		private ArrayList<Solution> neighbors;
		
		public NeighborSetImpl() {
			neighbors = new ArrayList<Solution>(360);
			
			// Get 360 points around the current solution, on a circle with radius 0.2 units
			for (double radian = 0; radian < 2 * Math.PI; radian += (Math.PI / 180.0))
			{
				double nx = x + Math.cos(radian) * 0.1;
				double ny = y + Math.sin(radian) * 0.1;
				
				if (Math.abs(nx) > 50.0 || Math.abs(ny) > 50.0)
					continue;
				
				neighbors.add(new RastriginSolution(nx, ny));
			}
		}
		
		@Override
		public Iterator<Solution> iterator() {
			return neighbors.iterator();
		}

		@Override
		public Solution randomNeighbor() {
			return neighbors.get(new Random().nextInt(neighbors.size()));
		}
	}
}
