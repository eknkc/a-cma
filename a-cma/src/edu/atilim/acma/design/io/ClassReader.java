package edu.atilim.acma.design.io;

import java.io.InputStream;
import java.util.ArrayList;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Node;
import edu.atilim.acma.design.Type;

public class ClassReader implements ClassVisitor {
	private static final int STAGE_BASE = 0;
	private static final int STAGE_AFTERBASE = 1;
	private static final int STAGE_FIELDS = 2;
	private static final int STAGE_METHODS = 3;
	static final int STAGE_COUNT = 4;
	
	private Design design;
	private Type type;
	private int stage;
	
	private ArrayList<String> inner;
	private String[] interfaces;
	private String superclass;
	
	public ClassReader(Design d) {
		design = d;
		stage = -1;
	}
	
	public void readStage(int s, InputStream stream) {
		stage = s;
		
		if (stage == STAGE_AFTERBASE) {
			afterBase();
		} else {
			try {
				new org.objectweb.asm.ClassReader(stream).accept(this, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void afterBase() {
		if (superclass.equals("java/lang/Object"))
			type.setRootType(true);
		
		type.setSuperType(design.getType(org.objectweb.asm.Type.getObjectType(superclass).getClassName()));
		for (String i : interfaces) {
			type.addInterface(design.getType(org.objectweb.asm.Type.getObjectType(i).getClassName()));
		}
		if (inner != null) {
			for (String i : inner) {
				Type itype = design.getType(org.objectweb.asm.Type.getObjectType(i).getClassName());
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
			type = design.create(org.objectweb.asm.Type.getObjectType(name).getClassName(), Type.class);

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
		if (stage == STAGE_METHODS) {
			if (DesignReader.isCompilerGenerated(name)) return null;
					
			Method m = type.createMethod(name);
			setAccess(m, access);
			
			m.setReturnType(design.getType(org.objectweb.asm.Type.getReturnType(desc).getClassName()));
			
			for (org.objectweb.asm.Type t : org.objectweb.asm.Type.getArgumentTypes(desc)) {
				int dims = 0;
				if (t.getSort() == org.objectweb.asm.Type.ARRAY)
					dims = t.getDimensions();
				m.addParameter(t.getClassName(), dims);
			}
			
			return new MethodReader(m);
		}
		return null;
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {

	}

	@Override
	public void visitSource(String source, String debug) {

	}
	
	private class MethodReader implements MethodVisitor {
		private Method method;
		
		public MethodReader(Method m) {
			this.method = m;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
			return null;
		}

		@Override
		public AnnotationVisitor visitAnnotationDefault() {
			return null;
		}

		@Override
		public void visitAttribute(Attribute arg0) {
		}

		@Override
		public void visitCode() {			
		}

		@Override
		public void visitEnd() {			
		}

		@Override
		public void visitFieldInsn(int opcode, String owner, String name, String desc)  {
			Type ot = design.getType(org.objectweb.asm.Type.getObjectType(owner).getClassName());
			
			if (ot != null) {
				Field f = ot.getField(name);
				
				if (f != null)
					method.addAccessedField(f);
			}
		}

		@Override
		public void visitFrame(int arg0, int arg1, Object[] arg2, int arg3,
				Object[] arg4) {
			
		}

		@Override
		public void visitIincInsn(int arg0, int arg1) {		
		}

		@Override
		public void visitInsn(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void visitIntInsn(int arg0, int arg1) {			
		}

		@Override
		public void visitJumpInsn(int arg0, Label arg1) {
			
		}

		@Override
		public void visitLabel(Label arg0) {
			
		}

		@Override
		public void visitLdcInsn(Object arg0) {
			
		}

		@Override
		public void visitLineNumber(int arg0, Label arg1) {
		
		}

		@Override
		public void visitLocalVariable(String arg0, String arg1, String arg2,
				Label arg3, Label arg4, int arg5) {
			
		}

		@Override
		public void visitLookupSwitchInsn(Label arg0, int[] arg1, Label[] arg2) {
			
		}

		@Override
		public void visitMaxs(int arg0, int arg1) {			
		}

		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc) {
			Type ot = design.getType(org.objectweb.asm.Type.getObjectType(owner).getClassName());
			if (ot == null) return;
			
			StringBuilder sign = new StringBuilder(name);
			sign.append('(');
			boolean hasparams = false;
			for (org.objectweb.asm.Type t : org.objectweb.asm.Type.getArgumentTypes(desc)) {
				sign.append(t.getClassName()).append(',');
				hasparams = true;
			}
			if (hasparams)
				sign.setLength(sign.length() - 1);
			sign.append(')');
			
			Method m = ot.getMethod(sign.toString());
			if (m == null) return;
			
			method.addCalledMethod(m);
		}

		@Override
		public void visitMultiANewArrayInsn(String arg0, int arg1) {
			
		}

		@Override
		public AnnotationVisitor visitParameterAnnotation(int arg0,
				String arg1, boolean arg2) {

			return null;
		}

		@Override
		public void visitTableSwitchInsn(int arg0, int arg1, Label arg2,
				Label[] arg3) {			
		}

		@Override
		public void visitTryCatchBlock(Label arg0, Label arg1, Label arg2,
				String arg3) {
			
		}

		@Override
		public void visitTypeInsn(int opcode, String type) {
			if (opcode == Opcodes.NEW) {
				Type ot = design.getType(org.objectweb.asm.Type.getObjectType(type).getClassName());
				
				if (ot != null) {
					method.addInstantiatedType(ot);
				}
			}
		}

		@Override
		public void visitVarInsn(int opcode, int var) {
			if (!method.isStatic() && (opcode == Opcodes.ALOAD || opcode == Opcodes.ASTORE) && var == 0)
				method.setAccessingThisPointer(true);
		}
		
	}
}
