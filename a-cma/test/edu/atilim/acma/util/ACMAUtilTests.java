package edu.atilim.acma.util;

import static org.junit.Assert.*;

import java.io.Serializable;

import org.junit.Test;

public class ACMAUtilTests {
	@Test
	public void testSplitCamelCase() {
		String v1 = "TestCaseOne";
		String v2 = "TestCase_xTwo";
		
		assertEquals("Test Case One", ACMAUtil.splitCamelCase(v1));
		assertEquals("Test Case_x Two", ACMAUtil.splitCamelCase(v2));
	}
	
	@Test
	public void testDeepCopy() {
		CopyTestClass cpt = new CopyTestClass();
		cpt.intval = 56;
		cpt.stringval = "TestString";
		
		CopyTestClass cptcopy = ACMAUtil.deepCopy(cpt);
		
		assertEquals(cpt.intval, cptcopy.intval);
		assertEquals(cpt.stringval, cptcopy.stringval);
	}
	
	private static class CopyTestClass implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private int intval;
		private String stringval;
	}
}
