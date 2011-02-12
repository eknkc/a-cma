package edu.atilim.acma.actions;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.MakeClassConcrete;

public class MakeClassConcreteTests {
	@Test
	public void testMakeClassConcrete() {
		Design design = new Design();
		Type type1 = design.create("TestType", Type.class);
		Type type2 = design.create("TestType2", Type.class);
		HashSet<Action> actions = new HashSet<Action>();
		MakeClassConcrete.Checker c = new MakeClassConcrete.Checker();
		c.findPossibleActions(design, actions);
		assertEquals(0, actions.size());
		
		type1.setAbstract(true);
		type2.setAbstract(true);
		type2.setAnnotation(true);
		actions.clear();
		c.findPossibleActions(design, actions);
		assertEquals(1, actions.size());
		
		for (Action a : actions) 
			a.perform(design);
		
		assertEquals(true, type2.isAbstract()); 
		assertEquals(false, type1.isAbstract()); 
		
	}

}
