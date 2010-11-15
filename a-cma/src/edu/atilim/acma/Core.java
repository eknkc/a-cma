package edu.atilim.acma;

import edu.atilim.acma.uml.ClassType;
import edu.atilim.acma.uml.Design;
import edu.atilim.acma.uml.Method;
import edu.atilim.acma.uml.Type;


public class Core {
	public static void main(String[] args) {
		Design d = new Design();
		
		//Sample design
		System.out.println("Creating Point Class");		
		ClassType point = d.create("Point", ClassType.class);
		point.setPackage(d.getPackage("edu.test", true));
		System.out.println("Adding method setX");	
		Method method = d.create("setX", Method.class);
		method.setParent(point);
		
		System.out.println("Creating Rectangle Class");	
		ClassType asd = d.create("Rectangle", ClassType.class);
		
		System.out.println("Adding Rectangle class as a nested type of Point class");	
		asd.setParent(point);
		
		System.out.println("Printing all class names:");	
		for (Type t : d.get(Type.class)) {
			System.out.println(t.getFullName());
		}
		
		System.out.println("Beginning new transaction");
		d.beginTrans();
		
		System.out.println("Moving rectangle to edu.test.complex package");
		asd.setPackage(d.getPackage("edu.test.complex", true));
		
		System.out.println("Printing all class names:");	
		for (Type t : d.get(Type.class)) {
			System.out.println(t.getFullName());
		}
		
		System.out.println("Rolling back transaction");
		d.rollbackTrans();
		
		System.out.println("Printing all class names:");	
		for (Type t : d.get(Type.class)) {
			System.out.println(t.getFullName());
		}
	}
}
