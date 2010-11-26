package edu.atilim.acma.uml.io;

import java.io.File;
import java.util.LinkedList;
import java.util.Stack;

import edu.atilim.acma.uml.Design;
import edu.atilim.acma.uml.Package;

public class DesignReader {
	private String classPath;
	private Design design;

	public DesignReader(String classPath) {
		this.classPath = classPath;
	}
	
	public Design getDesign() {
		design = new Design();
		
		readTypes();
		
		return design;
	}
	
	private void readTypes() {
		File root = new File(classPath);
		if (!root.exists()) {
			throw new RuntimeException("Class path could not be located");
		}
		
		LinkedList<TypeEntry> types = new LinkedList<TypeEntry>();
		Stack<DirectoryEntry> dirs = new Stack<DirectoryEntry>();
		dirs.push(new DirectoryEntry("", root));
		
		while(dirs.size() > 0) {
			DirectoryEntry cur = dirs.pop();
			File[] subfiles = cur.directory.listFiles();
			
			for (File f : subfiles) {
				if (f.isDirectory())
					dirs.push(new DirectoryEntry(cur.packageName + "." + f.getName(), f));
				else if (f.getName().endsWith(".class")) {
					String name = f.getName();
					// Check if compiler generated
					int lastDollar = name.lastIndexOf('$');
					if (lastDollar > 0) {
						try { Integer.parseInt(String.valueOf(name.charAt(lastDollar + 1))); }
						catch (NumberFormatException nfe) { continue; }
					}
					
					String pname = cur.packageName.substring(1);
					Package pack = design.getPackage(pname, true);
					types.add(new TypeEntry(pack, name.substring(0, name.length() - 6)));
				}
			}
		}
		
		System.out.println(types);
	}
	
	private String readType(TypeEntry e) {
		return null;
	}
	
	private static class TypeEntry {
		public Package pack;
		public String name;
		
		public TypeEntry(Package pack, String name) {
			super();
			this.pack = pack;
			this.name = name;
		}
	}
	
	private static class DirectoryEntry {
		public String packageName;
		public File directory;
		
		public DirectoryEntry(String packageName, File directory) {
			this.packageName = packageName;
			this.directory = directory;
		}
	}
}
