package edu.atilim.acma.metrics;

import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable.MetricRow;

public final class CouplingMetrics {
	@TypeMetric
	public static void calculateAttrMetrics(Type type, MetricRow row) {
		row.set("eC_Attr", type.getDependentFields().size());
		
		row.set("iC_Attr", 0);
		
		for (Field field : type.getFields()) {
			if (field.getType() != type) {
				row.increase("iC_Attr");
			}
		}
 	}
}
