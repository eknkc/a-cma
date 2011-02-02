package edu.atilim.acma.metrics;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;

public class PackageMetricsTests {
	private static Design design;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		
		Type t1 = design.create("test.p1.Test1", Type.class);
		Type t2 = design.create("test.p1.Test2", Type.class);
		design.create("test.p1.Test3", Type.class);
		design.create("test.p1.Test4", Type.class);
		
		t1.setAbstract(true);
		t2.setInterface(true);
		
		t1.createMethod("TestMethod");
		t2.createMethod("TestMethod");
	}
	
	@Test
	public void testAbstractnessMetric() {
		MetricTable table = design.getMetrics();
		assertEquals(0.5, table.get("test.p1", "abstractness"), 0.1);
	}
	
	@Test
	public void testCounts() {
		MetricTable table = design.getMetrics();
		assertEquals(3.0, table.get("test.p1", "numCls"), 0.1);
		assertEquals(1.0, table.get("test.p1", "numInterf"), 0.1);
		assertEquals(2.0, table.get("test.p1", "numOpsCls"), 0.1);
	}
	
	@Test
	public void testNesting() {
		MetricTable table = design.getMetrics();
		assertEquals(1.0, table.get("test.p1", "packagenesting"), 0.1);
	}
}
