package edu.atilim.acma.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.atilim.acma.metrics.MetricCalculator.Metric;

public final class MetricRegistry {
	
	public static List<Entry> entries() {
		//TODO: This is not a proxy.
		List<Entry> entries = new ArrayList<MetricRegistry.Entry>();
		
		for (Metric m : MetricCalculator.getMetrics()) {
			entries.add(new Entry(m.getName(), m.getWeight(), m.isPackageMetric()));
		}
		
		return Collections.unmodifiableList(entries);
	}

	public static class Entry {
		private String name;
		private double weight;
		private boolean packageMetric;

		public double getWeight() {
			return weight;
		}

		public String getName() {
			return name;
		}

		public boolean isPackageMetric() {
			return packageMetric;
		}

		private Entry(String name, double weight, boolean packageMetric) {
			this.name = name;
			this.weight = weight;
			this.packageMetric = packageMetric;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			return obj != null && obj instanceof Entry && ((Entry)obj).name.equals(name);
		}
	}
}
