package edu.atilim.acma.design.io;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Node;
import edu.atilim.acma.design.Type;

public class ClassReader implements ClassVisitor {
	private static final int STAGE_BASE = 0;
	private static final int STAGE_AFTERBASE = 1;
	private static final int STAGE_FIELDS = 2;
	public static final int STAGE_COUNT = 3;
	
	private File file;
	private Design design;
	private Type type;
	private int stage;
	
	private ArrayList<String> inner;
	private String[] interfaces;
	private String superclass;
	
	public ClassReader(File f, Design d) {
		file = f;
		design = d;
		stage = -1;
	}
	
	public void readStage(int s) {
		stage = s;
		
		if (stage == STAGE_AFTERBASE) {
			afterBase();
		} else {
			try {
				new org.objectweb.asm.ClassReader(new FileInputStream(file)).accept(this, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void afterBase() {
		type.setSuperType(design.getType(superclass.replace('/', '.')));
		for (String i : interfaces) {
			type.addInterface(design.getType(i.replace('/', '.')));
		}
		if (inner != null) {
			for (String i : inner) {
				Type itype = design.getType(i.replace('/', '.'));
				if(itype != null)
					itype.setParentType(type);
			}
		}
		
		superclass = null; interfaces = null; inner = null;
	}
	
	private boolean checkFlag(int flags, int flag) {
		return ((flags & flag) > 0);
	}
	
	private void setAccess(Node n, int access) {
		if (n instanceof Type) {
			if (checkFlag(access, Opcodes.ACC_INTERFACE))
				((Type)n).setInterface(true);
		}
		if (checkFlag(access, Opcodes.ACC_ABSTRACT))
			n.setAbstract(true);
		if (checkFlag(access, Opcodes.ACC_FINAL))
			n.setFinal(true);
		if (checkFlag(access, Opcodes.ACC_STATIC))
			n.setStatic(true);
		if (checkFlag(access, Opcodes.ACC_PUBLIC))
			n.setAccess(Accessibility.PUBLIC);
		if (checkFlag(access, Opcodes.ACC_PROTECTED))
			n.setAccess(Accessibility.PROTECTED);
		if (checkFlag(access, Opcodes.ACC_PRIVATE))
			n.setAccess(Accessibility.PRIVATE);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		if (stage == STAGE_BASE) {
			type = design.create(name.replace('/', '.'), Type.class);

			setAccess(type, access);
			
			this.interfaces = interfaces;
			this.superclass = superName;
		}
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return null;
	}

	@Override
	public void visitAttribute(Attribute attr) {
	}

	@Override
	public void visitEnd() {
	}

	@Override
	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
		if (stage == STAGE_FIELDS) {
			if (DesignReader.isCompilerGenerated(name))
				return null;
			
			org.objectweb.asm.Type ft = org.objectweb.asm.Type.getType(desc);
			
			Field f = type.createField(name);
			setAccess(f, access);
			
			f.setType(design.getType(ft.getClassName()));
		}
		
		return null;
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		if (stage == STAGE_BASE) {
			if (inner == null) inner = new ArrayList<String>();
			inner.add(name);
		}
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return null;
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {

	}

	@Override
	public void visitSource(String source, String debug) {

	}
}
