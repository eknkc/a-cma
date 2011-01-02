package edu.atilim.acma.design;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class FieldTests {
	private static Design design;
	private static Type type;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		type = design.create("TestType", Type.class);
	}
	
	@AfterClass
	public static void destroyDesign() {
		type = null;
		design = null;
	}
	
	@Test
	public void testParent() {
		Field field = type.createField("TestField");
		assertEquals(field.getOwnerType(), type);
		assertArrayEquals(type.getFields().toArray(), new Object[] { field });
		field.setOwnerType(null);
		assertEquals(field.getOwnerType(), null);
		assertEquals(type.getFields().size(), 0);
	}
}
