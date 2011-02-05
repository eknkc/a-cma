package edu.atilim.acma.actions;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.MakeClassAbstract;

public class MakeClassAbstractTests {
	@Test
	public void testMakeClassAbstract() {
		Design design = new Design();
		Type type1 = design.create("TestType", Type.class);
		Type type2 = design.create("TestType2", Type.class);
		HashSet<Action> actions = new HashSet<Action>();
		MakeClassAbstract.Checker c = new MakeClassAbstract.Checker();
		c.findPossibleActions(design, actions);
		assertEquals(0, actions.size());
		
		type1.setSuperType(type2);
		assertEquals(1, type2.getExtenders().size());
		actions.clear();
		c.findPossibleActions(design, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(design);
		
		assertEquals(true, type2.isAbstract()); 
		assertEquals(false, type1.isAbstract()); 
		
	}

}
