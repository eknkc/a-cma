package edu.atilim.acma.util;

import java.util.SortedSet;
import java.util.TreeSet;

public class RouletteWheel<T> {
	private SortedSet<Piece> wheel;
	
	public RouletteWheel() {
		wheel = new TreeSet<RouletteWheel<T>.Piece>();
	}
	
	public void add(T item, double fitness) {
		wheel.add(new Piece(item, fitness));
	}
	
	public T roll() {
		double slice = ACMAUtil.RANDOM.nextDouble() * wheel.last().fitness;
		
		Piece selected = null;
		for (Piece p : wheel) {
			if (slice <= p.fitness) {
				selected = p;
			} else if (selected != null) {
				break;
			}
		}
		
		if (selected != null) {
			wheel.remove(selected);
		}
		
		return selected.item;
	}
	
	private class Piece implements Comparable<Piece> {
		private T item;
		private double fitness;
		
		private Piece(T item, double fitness) {
			this.item = item;
			this.fitness = fitness;
		}

		@Override
		public int compareTo(Piece o) {
			return Double.compare(fitness, o.fitness);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public boolean equals(Object obj) {
			return new Double(fitness).equals(((Piece)obj).fitness);
		}
	}
}
