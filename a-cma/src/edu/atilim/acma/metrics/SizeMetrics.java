package edu.atilim.acma.metrics;

import java.util.List;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable.MetricRow;

public final class SizeMetrics {
	@TypeMetric
	public static void calculateFieldMetrics(Type type, MetricRow row) {
		row.set("numFields", type.getFields().size());
		
		row.set("numPubFields", 0);
		row.set("numConstants", 0);
		
		for (Field f : type.getFields()) {
			if (f.isConstant())
				row.increase("numConstants");
			else if (f.getAccess() == Accessibility.PUBLIC)
				row.increase("numPubFields");
		}
 	}
	
	@TypeMetric
	public static void calculateMethodMetrics(Type type, MetricRow row) {
		List<Method> methods = type.getMethods();
		
		row.set("numOps", methods.size());
		
		row.set("numPubOps", 0);
		row.set("getters", 0);
		row.set("setters", 0);
		
		for (Method m : methods) {
			if (m.getAccess() == Accessibility.PUBLIC)
				row.increase("numPubOps");
			
			if (m.isGetter())
				row.increase("getters");
			else if (m.isSetter())
				row.increase("setters");
		}
	}
}
