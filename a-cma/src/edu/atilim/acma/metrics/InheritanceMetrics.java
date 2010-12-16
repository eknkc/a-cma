package edu.atilim.acma.metrics;

import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable.MetricRow;

public final class InheritanceMetrics {
	@TypeMetric
	public static void calculateInterfacesMetrics(Type type, MetricRow row) {
		row.set("iFImpl", type.getInterfaces().size());
		
		row.set("NOC", type.getImplementers().size());
		
		row.set("NumDesc", type.getExtenders().size());
		
		row.set("NumAnc", type.getImplementers().size());
 	}
}
