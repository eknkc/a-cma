package edu.atilim.acma.metrics;

import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable.MetricRow;

public final class InheritanceMetrics {
	@TypeMetric
	public static void calculateNumInterfaces(Type type, MetricRow row) {
		row.set("iFImpl", type.getInterfaces().size());
 	}
}
