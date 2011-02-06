package edu.atilim.acma.metrics;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;

public class InheritanceMetricsTests {
	private static Design design;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		
		Type t1 = design.create("Test1", Type.class);
		Type t2 = design.create("Test2", Type.class);
		Type t3 = design.create("Test3", Type.class);
		Type t4 = design.create("Test4", Type.class);
		Type i1 = design.create("Testi", Type.class);
		
		t2.setSuperType(t1);
		t3.setSuperType(t1);
		t4.setSuperType(t2);
		
		i1.setInterface(true);
		
		t1.addInterface(i1);
	}
	
	@Test
	public void testInterfacesMetrics() {
		MetricTable mt = design.getMetrics();
		
		assertEquals(1.0, mt.get("Test1", "iFImpl"), 0.1);
		assertEquals(0.0, mt.get("Test2", "iFImpl"), 0.1);
		assertEquals(2.0, mt.get("Test1", "NOC"), 0.1);
		assertEquals(1.0, mt.get("Test2", "NOC"), 0.1);
		assertEquals(0.0, mt.get("Test3", "NOC"), 0.1);
	}
	
	@Test
	public void testNumberOfDesc() {
		MetricTable mt = design.getMetrics();
		
		assertEquals(3.0, mt.get("Test1", "NumDesc"), 0.1);
		assertEquals(1.0, mt.get("Test2", "NumDesc"), 0.1);
		assertEquals(0.0, mt.get("Test3", "NumDesc"), 0.1);
	}
	
	@Test
	public void testNumberOfAnc() {
		MetricTable mt = design.getMetrics();
		
		assertEquals(0.0, mt.get("Test1", "NumAnc"), 0.1);
		assertEquals(1.0, mt.get("Test2", "NumAnc"), 0.1);
		assertEquals(1.0, mt.get("Test3", "NumAnc"), 0.1);
		assertEquals(2.0, mt.get("Test4", "NumAnc"), 0.1);
	}
}
