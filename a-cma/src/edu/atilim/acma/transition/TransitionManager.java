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
import edu.atilim.acma.transition.actions.Action;
import edu.atilim.acma.transition.actions.ActionChecker;
import edu.atilim.acma.util.Log;

public class TransitionManager {
	static {
		initialize();
	}
	
	private static ArrayList<ActionChecker> checkers;
	
	public static Set<Action> getPossibleActions(final Design d) {
		final Set<Action> actions = new HashSet<Action>();
		
		for (ActionChecker checker : checkers)
			checker.findPossibleActions(d, actions);
			
		return actions;
	}
	
	private static void initialize() {
		checkers = new ArrayList<ActionChecker>();
		
		try {
			Log.config("Loading actions.xml for action configuration.");
			
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
	        
	        Log.config("Found %d action configurers. Action manager is ready.", checkers.size());
		} catch (Exception e) {
			System.out.println("Could not initialize transition manager. Details:");
			e.printStackTrace();
		}
	}
}
