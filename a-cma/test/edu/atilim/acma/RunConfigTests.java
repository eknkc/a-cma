package edu.atilim.acma;

import static org.junit.Assert.*;

import org.junit.Test;

public class RunConfigTests {
	@Test
	public void testActionEnabled() {
		RunConfig rc = RunConfig.getDefault();
		
		assertTrue(rc.isActionEnabled("TestAction"));
		assertTrue(rc.isActionEnabled("TestAction2"));
		rc.setActionEnabled("TestAction", false);
		assertFalse(rc.isActionEnabled("TestAction"));
		assertTrue(rc.isActionEnabled("TestAction2"));
		rc.setActionEnabled("TestAction", true);
		assertTrue(rc.isActionEnabled("TestAction"));
		assertTrue(rc.isActionEnabled("TestAction2"));
	}
	
	@Test
	public void testMetricEnabled() {
		RunConfig rc = RunConfig.getDefault();
		
		assertTrue(rc.isMetricEnabled("TestMetric"));
		assertTrue(rc.isMetricEnabled("TestMetric2"));
		rc.setMetricEnabled("TestMetric", false);
		assertFalse(rc.isMetricEnabled("TestMetric"));
		assertTrue(rc.isMetricEnabled("TestMetric2"));
		rc.setMetricEnabled("TestMetric", true);
		assertTrue(rc.isMetricEnabled("TestMetric"));
		assertTrue(rc.isMetricEnabled("TestMetric2"));
	}
	
	@Test
	public void testMetricWeights() {
		RunConfig rc = RunConfig.getDefault();
		
		assertEquals(4.87, rc.getMetricWeight("TestMetric", 4.87), 0.1);
		rc.setMetricWeight("TestMetric", 3.16);
		assertEquals(3.16, rc.getMetricWeight("TestMetric", 4.87), 0.1);
		assertEquals(42.87, rc.getMetricWeight("TestMetric2", 42.87), 0.1);
	}
}
