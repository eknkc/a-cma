package edu.atilim.acma.actions;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.FreezeMethod;

public class FreezeMethodTests {
	
	@Test
	public void testFreezeMethod() {
	
		Design design = new Design();
		HashSet<Action> actions = new HashSet<Action>();
		Type type = design.create("TestType1", Type.class);
		type.setRootType(true);
		Field field1 = type.createField("TestField");
		Method method = type.createMethod("TestMethod");
		assertEquals(false, method.isStatic());
		Method method2 = type.createMethod("TestMethod2");
		Method method3 = type.createMethod("TestMethod3");
		Method method4 = type.createMethod("TestMethod4");
		FreezeMethod.Checker c = new FreezeMethod.Checker();
		c.findPossibleActions(design, actions);
		assertEquals(4, actions.size());
		
		Type type2 = design.create("TestType2", Type.class);
		type2.setRootType(true);
		field1.setOwnerType(type2);
		method.addAccessedField(field1);
		actions.clear();
		c.findPossibleActions(design, actions);
		assertEquals(4, actions.size());
		
		method3.addCalledMethod(method4);
		method4.setOwnerType(type2);
		actions.clear();
		c.findPossibleActions(design, actions);
		assertEquals(4, actions.size());
		
		for (Action a : actions) 
			a.perform(design);
		
		assertEquals(true, method.isStatic());
		assertEquals(1, method.getParameters().size());
		assertEquals(true, method2.isStatic());
		assertEquals(0, method2.getParameters().size());
		assertEquals(true, method3.isStatic());
		assertEquals(1, method3.getParameters().size());
		assertEquals(true, method4.isStatic());
		assertEquals(0, method2.getParameters().size());
	}

}
