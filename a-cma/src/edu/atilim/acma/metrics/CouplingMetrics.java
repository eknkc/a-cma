package edu.atilim.acma.metrics;

import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Method.Parameter;
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
	
	@TypeMetric
	public static void calculateMethodMetrics(Type type, MetricRow row) {
		row.set("eC_Par", type.getDependentMethodsAsParameter().size());
		
		row.set("iC_Par", 0);
		
		for (Method method : type.getMethods()) {
			for (Parameter parameter : method.getParameters()) {
				if (type != parameter.getType())
					row.increase("iC_Par");
			}
		}
 	}
}
