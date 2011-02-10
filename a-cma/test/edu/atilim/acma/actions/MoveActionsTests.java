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
import edu.atilim.acma.transition.actions.InstantiateMethod;
import edu.atilim.acma.transition.actions.MoveDownField;
import edu.atilim.acma.transition.actions.MoveDownMethod;
import edu.atilim.acma.transition.actions.MoveMethod;
import edu.atilim.acma.transition.actions.MoveUpField;
import edu.atilim.acma.transition.actions.MoveUpMethod;

public class MoveActionsTests {
	private static Design design;
	private static HashSet<Action> actions;
	
	@BeforeClass
	public static void createDesign() {
		design = new Design();
		design.create("TestType1", Type.class);
		design.create("TestType2", Type.class);
		design.create("TestType3", Type.class);
		actions = new HashSet<Action>();
	}
	
	@AfterClass
	public static void destroyDesign() {
		design = null;
		actions = null;
	}
	
	@Test
	public void testMoveUpMethod() {
		Design d = design.copy();
		Type t1 = d.getType("TestType1");
		Type t2 = d.getType("TestType2");
		Method m1 = t1.createMethod("TestMethod1");
		Method m2 = t1.createMethod("TestMethod2");
		t1.setSuperType(t2);
		MoveUpMethod.Checker c = new MoveUpMethod.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		m2.setAccess(Accessibility.PRIVATE);
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(t2, m1.getOwnerType());
		assertEquals(t1, m2.getOwnerType());
	}
	
	@Test
	public void testMoveDownMethod() {
		Design d = design.copy();
		Type t1 = d.getType("TestType1");
		Type t2 = d.getType("TestType2");
		Method m1 = t1.createMethod("TestMethod1");
		Method m2 = t1.createMethod("TestMethod2");
		t2.setSuperType(t1);
		MoveDownMethod.Checker c = new MoveDownMethod.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(t2, m1.getOwnerType());
		assertEquals(t2, m2.getOwnerType());
	}
	
	@Test
	public void testMoveMethod() {
		Design d = design.copy();
		Type t1 = d.getType("TestType1");
		Type t2 = d.getType("TestType2");
		Type t3 = d.getType("TestType3");
		Method m1 = t1.createMethod("TestMethod1");
		m1.addParameter(t2);
		Method m2 = t1.createMethod("TestMethod2");
		m2.addParameter(t3);
		t3.setInterface(true);
		MoveMethod.Checker c = new MoveMethod.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(t2, m1.getOwnerType());
		assertEquals(t1, m2.getOwnerType());
	}
	
	@Test
	public void testInstantiateMethod() {
		Design d = design.copy();
		Type t1 = d.getType("TestType1");
		Type t2 = d.getType("TestType2");
		Type t3 = d.getType("TestType3");
		Method m1 = t1.createMethod("TestMethod1");
		m1.addParameter(t2);
		assertEquals(1, m1.getParameters().size());
		m1.setStatic(true);
		Method m2 = t1.createMethod("TestMethod2");
		m2.addParameter(t3);
		t3.setAnnotation(true);
		InstantiateMethod.Checker c = new InstantiateMethod.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(t2, m1.getOwnerType());
		assertEquals(0, m1.getParameters().size());
		assertEquals(t1, m2.getOwnerType());
	}
	
	@Test
	public void testMoveDownField() {
		Design d = design.copy();
		Type t1 = d.getType("TestType1");
		Type t2 = d.getType("TestType2");
		t2.setAccess(Accessibility.PRIVATE);
		Type t3 = d.getType("TestType3");
		t3.setAccess(Accessibility.PRIVATE);
		Field f1 = t1.createField("TestField1");
		Field f2 = t1.createField("TestField2");
		Method m1 = t3.createMethod("TestMethod1");
		m1.setAccess(Accessibility.PRIVATE);
		t2.setSuperType(t1);
		MoveDownField.Checker c = new MoveDownField.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(2, actions.size());
		assertEquals(false, f2.canAccess(m1));
		m1.addAccessedField(f2);
		assertEquals(1, f2.getAccessors().size());
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(t2, f1.getOwnerType());
		assertEquals(t1, f2.getOwnerType());
	}
	
	@Test
	public void testMoveUpField() {
		Design d = design.copy();
		Type t1 = d.getType("TestType1");
		Type t2 = d.getType("TestType2");
		Field f1 = t1.createField("TestField1");
		Field f2 = t1.createField("TestField2");
		f2.setAccess(Accessibility.PRIVATE);
		Field f3 = t2.createField("TestField3");
		t1.setSuperType(t2);
		MoveUpField.Checker c = new MoveUpField.Checker();
		actions.clear();
		c.findPossibleActions(d, actions);
		assertEquals(1, actions.size());	
		
		for (Action a : actions) 
			a.perform(d);
		
		assertEquals(t2, f1.getOwnerType());
		assertEquals(t1, f2.getOwnerType());
		assertEquals(t2, f3.getOwnerType());
	}

}
