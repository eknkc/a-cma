package edu.atilim.acma.metrics;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.atilim.acma.metrics.MetricTable.MetricRow;


public class MetricTableTests {
	private static MetricTable table;
	
	@BeforeClass
	public static void initTests() {
		table = new HashedMetricTable();
	}
	
	@AfterClass
	public static void finalizeTests() {
		table = null;
	}
	
	@Test
	public void testSetGet() {
		table.set("Test1", "TestMetric", 10.0);
		assertEquals(10.0, table.get("Test1", "TestMetric"), 0.1);
		assertEquals(10.0, table.getAverage("TestMetric"), 0.1);
		table.set("Test2", "TestMetric", 20.0);
		assertEquals(15.0, table.getAverage("TestMetric"), 0.1);
	}
	
	@Test
	public void testRow() {
		MetricRow row = table.row("TestRow");
		row.set("TestMetric", 5.0);
		assertEquals(5.0, table.get("TestRow", "TestMetric"), 0.1);
		row.increase("TestMetric");
		assertEquals(6.0, table.get("TestRow", "TestMetric"), 0.1);
		row.add("TestMetric", 4.0);
		assertEquals(10.0, table.get("TestRow", "TestMetric"), 0.1);
	}
}
