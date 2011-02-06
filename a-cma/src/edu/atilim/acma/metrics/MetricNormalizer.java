package edu.atilim.acma.metrics;

import java.util.List;

import edu.atilim.acma.RunConfig;

public class MetricNormalizer {
	public static double normalize(MetricSummary current, RunConfig config) {
		return weightedNormalize(current, config);
	}
	
	private static double weightedNormalize(MetricSummary current, RunConfig config) {
		// All metrics
		List<MetricRegistry.Entry> metrics = MetricRegistry.entries();
		
		// Design set
		List<MetricSummary> designs = config.getNormalMetrics();
		
		int nummetrics = metrics.size();
		int numdesigns = designs.size();
		
		// Table
		double[][] table = new double[nummetrics][numdesigns + 1];
			
		for (int i = 0; i < nummetrics; i++) {
			MetricRegistry.Entry metric = metrics.get(i);
			
			for (int j = 0; j < numdesigns; j++) {
				MetricSummary design = designs.get(j);
				table[i][j] = design.get(metric.getName());
			}
			
			table[i][numdesigns] = current.get(metric.getName());
		}
		
		double[] weights = getWeights(table, nummetrics, numdesigns);
		double[] normals = getNormals(table, nummetrics, numdesigns + 1);
		
		double normalvalue = 0;
		double minimalizevalue = 0;
		
		for (int i = 0; i < nummetrics; i++) {
			MetricRegistry.Entry metric = metrics.get(i);
			
			if (!config.isMetricEnabled(metric.getName())) continue;
			
			if (metric.isMinimized()) {
				normalvalue += current.get(metric.getName());
			} else {
				if (!Double.isNaN(weights[i]) && !Double.isNaN(normals[i])) {
					normalvalue += weights[i] * Math.abs(normals[i]);
				}
			}
		}
		
		return normalvalue + minimalizevalue;
	}
	
	private static double[] getNormals(double[][] table, int rows, int cols) {
		double[] means = new double[rows];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				means[i] += table[i][j];
			}
		}
		
		for (int i = 0; i < rows; i++) {
			means[i] /= cols;
		}
		
		double[] stdevs = new double[rows];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double diff = table[i][j] - means[i];
				stdevs[i] += diff * diff;
			}
		}
		
		for (int i = 0; i < rows; i++) {
			stdevs[i] = Math.sqrt(stdevs[i] / cols);
		}
		
		double[] normals = new double[rows];
		
		for (int i = 0; i < rows; i++) {
			normals[i] = ((table[i][cols - 1] - means[i]) / stdevs[i]);
		}
		
		return normals;
	}
	
	private static double[] getWeights(double[][] table, int rows, int cols) {
		double[] means = new double[cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				means[j] += table[i][j];
			}
		}
		
		for (int j = 0; j < cols; j++) {
			means[j] /= rows;
		}
		
		double[] stdevs = new double[cols];
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				double diff = table[i][j] - means[j];
				stdevs[j] += diff * diff;
			}
		}
		
		for (int j = 0; j < cols; j++) {
			stdevs[j] = Math.sqrt(stdevs[j] / rows);
		}
		
		double[] weights = new double[rows];
		
		for (int i = 0; i < rows; i++) {
			double sum = 0;
			for (int j = 0; j < cols; j++) {
				sum += Math.abs((table[i][j] - means[j]) / stdevs[j]);
			}
			weights[i] = sum / cols;
		}
		
		return weights;
	}
}
