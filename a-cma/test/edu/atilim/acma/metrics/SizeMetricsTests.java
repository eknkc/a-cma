package edu.atilim.acma.metrics;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Type;


public class SizeMetricsTests {
	private static Design design;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		
		Type t1 = design.create("test.Test1", Type.class);
		Type t2 = design.create("test.Test2", Type.class);
		
		t1.createField("field1");
		t1.createField("field2");
		
		Field f1 = t2.createField("field1");
		Field f2 = t2.createField("field2");
		
		f2.setStatic(true);
		f2.setFinal(true);
		
		f1.setAccess(Accessibility.PUBLIC);
		
		t1.createMethod("getMethod1");
		t2.createMethod("setMethod2");
		
		t1.createMethod("method1").setAccess(Accessibility.PUBLIC);
	}
	
	@Test
	public void testFieldMetrics() {
		MetricTable table = design.getMetrics();
		
		assertEquals(2.0, table.get("test.Test1", "numFields"), 0.1);
		assertEquals(2.0, table.get("test.Test2", "numFields"), 0.1);
		
		assertEquals(0.0, table.get("test.Test1", "numConstants"), 0.1);
		assertEquals(1.0, table.get("test.Test2", "numConstants"), 0.1);
	}
	
	@Test
	public void testMethodMetrics() {
		MetricTable table = design.getMetrics();
		
		assertEquals(2.0, table.get("test.Test1", "numOps"), 0.1);
		assertEquals(1.0, table.get("test.Test2", "numOps"), 0.1);
			
		assertEquals(1.0, table.get("test.Test1", "getters"), 0.1);
		assertEquals(0.0, table.get("test.Test2", "getters"), 0.1);
		
		assertEquals(0.0, table.get("test.Test1", "setters"), 0.1);
		assertEquals(1.0, table.get("test.Test2", "setters"), 0.1);
	}
}
