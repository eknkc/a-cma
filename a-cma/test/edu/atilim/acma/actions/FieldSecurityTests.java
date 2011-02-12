package edu.atilim.acma.actions;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.DecreaseFieldSecurity;
import edu.atilim.acma.transition.actions.DecreaseMethodSecurity;
import edu.atilim.acma.transition.actions.IncreaseFieldSecurity;
import edu.atilim.acma.transition.actions.IncreaseMethodSecurity;

public class FieldSecurityTests {
	
	private static Design design;
	private static Type type;
	private static HashSet<Action> actions;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		type = design.create("TestType", Type.class);
		type.createField("TestField1");
		type.createField("TestField2");
		type.createMethod("TestMethod1");
		type.createMethod("TestMethod2");
		actions = new HashSet<Action>();
	}
	
	@AfterClass
	public static void destroyDesign() {
		design = null;
		type = null;
		actions = null;
	}
	
	@Test
	public void testIncreaseFieldSecurity() {
		Design d = design.copy();
		d.getType("TestType").getField("TestField1").setAccess(Accessibility.PUBLIC);
		d.getType("TestType").getField("TestField2").setAccess(Accessibility.PROTECTED);
		IncreaseFieldSecurity.Checker c = new IncreaseFieldSecurity.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(Accessibility.PROTECTED, d.getType("TestType").getField("TestField1").getAccess());
		assertEquals(Accessibility.PACKAGE, d.getType("TestType").getField("TestField2").getAccess());
	}
	
	@Test
	public void testDecreaseFieldSecurity() {
		Design d = design.copy();
		d.getType("TestType").getField("TestField1").setAccess(Accessibility.PRIVATE);
		d.getType("TestType").getField("TestField2").setAccess(Accessibility.PACKAGE);
		DecreaseFieldSecurity.Checker c = new DecreaseFieldSecurity.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(Accessibility.PACKAGE, d.getType("TestType").getField("TestField1").getAccess());
		assertEquals(Accessibility.PROTECTED, d.getType("TestType").getField("TestField2").getAccess());
	}
	
	@Test
	public void testIncreaseMethodSecurity() {
		Design d = design.copy();
		Type t = d.create("TestType2", Type.class);
		d.getType("TestType").getMethod("TestMethod1").setAccess(Accessibility.PUBLIC);
		Method m2 = d.getType("TestType").getMethod("TestMethod2");
		m2.setAccess(Accessibility.PACKAGE);
		Method m3 =  t.createMethod("TestMethod3");
		m3.setAccess(Accessibility.PACKAGE);
		m3.addCalledMethod(m2);
		assertEquals(1, m2.getCallerMethods().size());
		IncreaseMethodSecurity.Checker c = new IncreaseMethodSecurity.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(Accessibility.PROTECTED, d.getType("TestType").getMethod("TestMethod1").getAccess());
		assertEquals(Accessibility.PACKAGE, m2.getAccess());
		assertEquals(Accessibility.PRIVATE, m3.getAccess());
	}
	
	@Test
	public void testDecreaseMethodSecurity() {
		Design d = design.copy();
		d.getType("TestType").getMethod("TestMethod1").setAccess(Accessibility.PRIVATE);
		d.getType("TestType").getMethod("TestMethod2").setAccess(Accessibility.PACKAGE);
		DecreaseMethodSecurity.Checker c = new DecreaseMethodSecurity.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(Accessibility.PACKAGE, d.getType("TestType").getMethod("TestMethod1").getAccess());
		assertEquals(Accessibility.PROTECTED, d.getType("TestType").getMethod("TestMethod2").getAccess());
	}

}
