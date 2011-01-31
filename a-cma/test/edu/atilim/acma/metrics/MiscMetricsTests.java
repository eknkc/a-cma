package edu.atilim.acma.metrics;

import java.util.Random;

import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;
import static org.junit.Assert.*;

public class MiscMetricsTests {
	@Test
	public void testNestingMetric() {
		Design design = new Design();
		int nesting = new Random().nextInt(100) + 50;
		
		Type t = design.create("0", Type.class);
		
		for (int i = 1; i < nesting; i++) {
			Type newt = design.create(String.valueOf(i), Type.class);
			newt.setParentType(t);
			t = newt;
		}
		
		MetricTable table = design.getMetrics();
		
		while(t != null) {
			int tnesting = Integer.parseInt(t.getName());
			assertEquals((double)tnesting, table.get(t.getName(), "nesting"), 0.1);
			t = t.getParentType();
		}
	}
}
