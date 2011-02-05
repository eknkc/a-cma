package edu.atilim.acma.actions;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.RemoveClass;
import edu.atilim.acma.transition.actions.RemoveField;
import edu.atilim.acma.transition.actions.RemoveInterface;
import edu.atilim.acma.transition.actions.RemoveMethod;

public class RemoveActionsTests {
	
	private static Design design;
	private static HashSet<Action> actions;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		design.create("TestType", Type.class);
		actions = new HashSet<Action>();
	}
	
	@AfterClass
	public static void destroyDesign() {
		design = null;
		actions = null;
	}
	
	@Test
	public void testRemoveMethod() {
		Design d = design.copy();
		d.getType("TestType").setRootType(true);
		Method m1 = d.getType("TestType").createMethod("TestMethod1");
		d.getType("TestType").getMethod("TestMethod1").setAccess(Accessibility.PRIVATE);
		Method m2 = d.getType("TestType").createMethod("TestMethod2");
		m2.addCalledMethod(m1);
		d.getType("TestType").createMethod("TestMethod3");
		RemoveMethod.Checker c = new RemoveMethod.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(null, d.getType("TestType").getMethod("TestMethod2"));
		assertEquals(null, d.getType("TestType").getMethod("TestMethod3"));
	}
	
	@Test
	public void testRemoveClass() {	
		Design d = design.copy();
		d.create("TestType2", Type.class);
		d.create("TestType3", Type.class);
		d.getType("TestType3").setAnnotation(true);
		RemoveClass.Checker c = new RemoveClass.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(null, d.getType("TestType"));
		assertEquals(null, d.getType("TestType2"));
	}
	
	@Test
	public void testRemoveField() {	
		Design d = design.copy();
		d.getType("TestType").createField("TestField1");
		Field f2 = d.getType("TestType").createField("TestField2");
		RemoveField.Checker c = new RemoveField.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		f2.setFinal(true);
		f2.setStatic(true);
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size() );
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(null, d.getType("TestType").getField("TestField1"));		
	}
	
	@Test
	public void testRemoveInterface() {		
		Design d = design.copy();
		d.getType("TestType").setInterface(true);
		RemoveInterface.Checker c = new RemoveInterface.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size() );
		
		d.getType("TestType").setAnnotation(true);
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(0, actions.size());
		d.getType("TestType").setAnnotation(false);
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size() );
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(null, d.getType("TestType"));
	}
}
