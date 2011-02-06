package edu.atilim.acma.metrics;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;

public class CouplingMetricsTests {
	@Test
	public void testAttrMetrics() {
		Design d = new Design();
		Type t1 = new Type("TestType", d);
		Type t2 = new Type("TestType2", d);
		
		HashedMetricTable hmt = new HashedMetricTable();
		CouplingMetrics.calculateAttrMetrics(t1, hmt.row(t1));
		assertEquals(0.0, hmt.get(t1, "eC_Attr"), 0.1);
		assertEquals(0.0, hmt.get(t1, "iC_Attr"), 0.1);
		
		t2.createField("testField", t1);
		t1.createField("testField", t2);
		
		CouplingMetrics.calculateAttrMetrics(t1, hmt.row(t1));
		assertEquals(1.0, hmt.get(t1, "eC_Attr"), 0.1);
		assertEquals(1.0, hmt.get(t1, "iC_Attr"), 0.1);
		
		t1.createField("testField", t1);
		
		hmt.set(t1, "iC_Attr", 0);
		CouplingMetrics.calculateAttrMetrics(t1, hmt.row(t1));
		assertEquals(2.0, hmt.get(t1, "eC_Attr"), 0.1);
		assertEquals(1.0, hmt.get(t1, "iC_Attr"), 0.1);
	}
	
	@Test
	public void testMethodMetrics() {
		Design d = new Design();
		Type t1 = new Type("TestType", d);
		Type t2 = new Type("TestType2", d);
		HashedMetricTable hmt = new HashedMetricTable();
		
		t2.createMethod("testMethod").addParameter(t1);
		CouplingMetrics.calculateMethodMetrics(t1, hmt.row(t1));
		CouplingMetrics.calculateMethodMetrics(t2, hmt.row(t2));
		
		assertEquals(1.0, hmt.get(t1, "eC_Par"), 0.1);
		assertEquals(0.0, hmt.get(t1, "iC_Par"), 0.1);
		
		assertEquals(0.0, hmt.get(t2, "eC_Par"), 0.1);
		assertEquals(1.0, hmt.get(t2, "iC_Par"), 0.1);
	}
}
