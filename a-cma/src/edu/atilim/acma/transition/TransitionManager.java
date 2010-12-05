package edu.atilim.acma.transition;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.transition.actions.ActionChecker;
import edu.atilim.acma.transition.actions.Action;

public class TransitionManager {
	static {
		initialize();
	}
	
	private static ArrayList<ActionChecker> checkers;
	
	public static Set<Action> getPossibleActions(Design d) {
		HashSet<Action> actions = new HashSet<Action>();
		
		for (ActionChecker checker : checkers)
			checker.findPossibleActions(d, actions);
		
		return actions;
	}
	
	private static void initialize() {
		checkers = new ArrayList<ActionChecker>();
		
		try {
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
	        Document doc = docBuilder.parse(new File("./data/actions.xml"));
	        
	        NodeList actions = doc.getElementsByTagName("action");
	        for (int i = 0 ; i < actions.getLength(); i++) {
	        	Node action = actions.item(i);

	        	String className = action.getAttributes().getNamedItem("class").getTextContent();
	        	Class<?> actionClass = Class.forName("edu.atilim.acma.transition.actions." + className);
	        	
	        	for (Class<?> innerClass : actionClass.getClasses()) {
	        		if (ActionChecker.class.isAssignableFrom(innerClass)) {
	        			ActionChecker checker = (ActionChecker)innerClass.newInstance();
	        			checkers.add(checker);
	        		}
	        	}
	        }
		} catch (Exception e) {
			System.out.println("Could not initialize transition manager. Details:");
			e.printStackTrace();
		}
	}
}
