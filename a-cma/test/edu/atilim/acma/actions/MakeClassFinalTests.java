package edu.atilim.acma.actions;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.MakeClassFinal;

public class MakeClassFinalTests {
	@Test
	public void testMakeClassFinal() {
		Design design = new Design();
		Type type1 = design.create("TestType", Type.class);
		Type type2 = design.create("TestType2", Type.class);
		HashSet<Action> actions = new HashSet<Action>();
		MakeClassFinal.Checker c = new MakeClassFinal.Checker();
		c.findPossibleActions(design, actions);
		assertEquals(2, actions.size());
		
		type2.setAnnotation(true);
		actions.clear();
		c.findPossibleActions(design, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(design);
		
		assertEquals(true, type1.isFinal()); 
		assertEquals(false, type2.isFinal()); 
		
	}
}
