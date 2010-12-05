package edu.atilim.acma.metrics;

import java.util.Map;

public class MetricScore implements Comparable<MetricScore> {
	private Map<String, Double> metricAverages;
	private double weightedSum;
	
	public double getAverage(String metricName) {
		Double d = metricAverages.get(metricName);
		if (d == null) return 0.0;
		return d;
	}
	
	public double getWeightedSum() {
		return weightedSum;
	}

	public MetricScore(Map<String, Double> metricAverages, double weightedSum) {
		this.metricAverages = metricAverages;
		this.weightedSum = weightedSum;
	}

	@Override
	public int compareTo(MetricScore o) {
		return Double.compare(weightedSum, o.weightedSum);
	}
}