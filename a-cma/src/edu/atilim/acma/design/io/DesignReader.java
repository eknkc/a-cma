package edu.atilim.acma.design.io;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.atilim.acma.design.Design;

public class DesignReader {
	private File classPath;
	
	public DesignReader(String classPath) {
		this.classPath = new File(classPath);
	}
	
	public Design read() {
		if (!classPath.exists())
			throw new RuntimeException("Class path for reading design does not exist.");
		
		Design design = new Design();
		List<File> files = findAllClassFiles();
		List<ClassReader> readers = new ArrayList<ClassReader>();
		
		for (File f : files) {
			ClassReader reader = new ClassReader(f, design);
			readers.add(reader);
		}
		
		for (int i = 0; i < ClassReader.STAGE_COUNT; i++) {
			for (ClassReader r : readers) {
				r.readStage(i);
			}
		}
		
		return design;
	}
	
	static boolean isCompilerGenerated(String name) {
		int lastDollar = name.lastIndexOf('$');
		if (lastDollar > 0) {
			try { Integer.parseInt(String.valueOf(name.charAt(lastDollar + 1))); return true; }
			catch (NumberFormatException nfe) { }
		}
		return false;
	}
	
	private List<File> findAllClassFiles() {
		Stack<File> dirs  = new Stack<File>();
		ArrayList<File> files = new ArrayList<File>();
		
		dirs.push(classPath);
		
		while (dirs.size() > 0) {
			File dir = dirs.pop();
			File[] subfiles = dir.listFiles();
			
			for (File f : subfiles) {
				if (f.isDirectory())
					dirs.push(f);
				else if (f.getName().endsWith(".class")) {
					if (isCompilerGenerated(f.getName()))
						continue;
					
					files.add(f);
				}
			}
		}
		
		return files;
	}
}
