package edu.atilim.acma.metrics;

import java.util.List;

import edu.atilim.acma.design.Package;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.metrics.MetricTable.MetricRow;

public final class PackageMetrics {
	@PackageMetric
	public static void calculateAbstractness(Package pack, MetricRow row) {
		int abst = 0;
		int totl = 0;
		
		for (Type t : pack.getTypes()) {
			if (t.isAbstract() || t.isInterface())
				abst++;
			totl++;
		}
		
		if (totl > 0) {
			row.set("abstractness", (double)abst / (double)totl);
		}
	}
	
	@PackageMetric
	public static void calculateCounts(Package pack, MetricRow row) {
		List<Type> types = pack.getTypes();
		
		row.set("numCls", 0);
		row.set("numInterf", 0);
		
		for (Type type : types)
		{
			if (type.isInterface()) {
				row.increase("numInterf");
			} else {
				row.increase("numCls");
			}
		}
	}
}
