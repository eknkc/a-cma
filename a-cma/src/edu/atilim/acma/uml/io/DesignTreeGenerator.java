package edu.atilim.acma.uml.io;

import edu.atilim.acma.uml.ClassType;
import edu.atilim.acma.uml.Design;
import edu.atilim.acma.uml.Field;
import edu.atilim.acma.uml.InterfaceType;
import edu.atilim.acma.uml.Method;
import edu.atilim.acma.uml.Package;
import edu.atilim.acma.uml.Type;

public final class DesignTreeGenerator {
	private Design design;
	private int treeLevel;
	private StringBuilder out;
	
	public DesignTreeGenerator(Design design)
	{
		this.design = design;
		this.treeLevel = 0;
	}
	
	private void write(Package p) {
		write(p.getName());
		goIn();
		for (Package inp : p.packages())
			write(inp);
		for (Type t : p.types())
			write(t);		
		goOut();
	}
	
	private void write(Type t) {
		if (t instanceof ClassType)
			write((ClassType)t);
		else if (t instanceof InterfaceType)
			write((InterfaceType)t);
	}
	
	private void write(ClassType c) {
		write(c.getName());
		goIn();
		for (Field f : c.fields())
			write("<f> " + f.getName());
		for (Method m : c.methods())
			write("<m> " + m.getName() + "()");
		for (Type t : c.nestedTypes())
			write(t);
		goOut();
	}
	
	private void write(InterfaceType i) {
		write(i.getName());
		goIn();
		for (Method m : i.methods())
			write("<m> " + m.getName() + "()");
		for (Type t : i.nestedTypes())
			write(t);
		goOut();
	}
	
	private void goIn() {
		treeLevel++;
	}
	
	private void goOut() {
		treeLevel = Math.max(0, treeLevel - 1);
	}
	
	private void write(String elem) {
		for (int i = 0; i < treeLevel; i++)
			out.append("| ");
		
		out.append("+-");
		out.append(elem);
		out.append("\r\n");
	}
	
	@Override
	public String toString() {
		out = new StringBuilder();
		write(design.getRootPackage());
		return out.toString();
	}
}
