package edu.atilim.acma;

import java.util.List;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.design.io.DesignReader;

public class Core {
	@SuppressWarnings("rawtypes")
	private static void printList(String name, List list) {
		if (list.size() == 0) return;
		
		System.out.println("\t" + name);
		for (Object n : list) {
			System.out.println("\t\t" + n);
		}
	}
	
	public static void main(String[] args) {
		// Kendini oku...
		DesignReader dr = new DesignReader("./bin");
		
		Design d = dr.read();
		
		// Yaz
		for (Type t : d.getTypes()) {
			if (!t.getPackage().equals("edu.atilim.acma.search")) continue;
			
			System.out.println(t);
			
			printList("implements", t.getInterfaces());
			printList("implementedby", t.getImplementers());
			printList("fields", t.getFields());
			printList("methods", t.getMethods());
		}
	}
}
