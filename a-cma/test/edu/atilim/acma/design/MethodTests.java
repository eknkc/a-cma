package edu.atilim.acma.design;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MethodTests {
	private static Design design;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		Type type1 = design.create("Type", Type.class);
		Type type2 = design.create("SuperType", Type.class);
		
		type1.setSuperType(type2);
		type2.setRootType(true);
		
		type1.createMethod("moverride");
		type2.createMethod("moverride");
		
		type1.createMethod("mnotoverride");
	}
	
	@AfterClass
	public static void destroyDesign() {
		design = null;
	}
	
	@Test
	public void testOverride() {
		assertTrue(design.getType("Type").getMethod("moverride").isOverride());
		assertFalse(design.getType("Type").getMethod("mnotoverride").isOverride());
	}
}
