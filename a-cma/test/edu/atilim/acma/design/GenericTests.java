package edu.atilim.acma.design;

import static org.junit.Assert.*;

import org.junit.Test;


public class GenericTests {
	@Test
	public void testFlags() {
		// Field can be used here as it is the most lightweight class extending abstract Node
		Field field = new Field("foo", null);
		
		assertFalse(field.isAbstract());
		field.setAbstract(true);
		assertTrue(field.isAbstract());
		field.setAbstract(false);
		assertFalse(field.isAbstract());
		
		assertFalse(field.isFinal());
		field.setFinal(true);
		assertTrue(field.isFinal());
		field.setFinal(false);
		assertFalse(field.isFinal());
		
		assertFalse(field.isStatic());
		field.setStatic(true);
		assertTrue(field.isStatic());
		field.setStatic(false);
		assertFalse(field.isStatic());
	}
	
	@Test
	public void testAccess() {
		Field field = new Field("foo", null);
		
		String[] access = new String[] {
			"PRIVATE", "PROTECTED", "PACKAGE", "PUBLIC"
		};
		
		for (int i = 0; i < access.length; i++) {
			Accessibility acc = Accessibility.valueOf(access[i]);
			
			field.setAccess(acc);
			
			assertEquals(acc, field.getAccess());
		}
	}
	
	@Test
	public void testName() {
		Field field = new Field("foo", null);
		assertEquals("foo", field.getName());
	}
}
