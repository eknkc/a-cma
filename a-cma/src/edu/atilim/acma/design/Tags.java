package edu.atilim.acma.design;

public final class Tags {
	public static final int REF_SUPERCLASS = 1;
	public static final int REF_PARENT = 2;
	public static final int REF_IMPLEMENT = 3;
	public static final int REF_RETURN = 4;
	public static final int REF_PARAMETER = 5;
	public static final int REF_DEPEND = 6;
	public static final int REF_INSTANTIATE = 7;
	
	// Accessibility
	public static final int PUBLIC = 0x00000001;
	public static final int PRIVATE = 0x00000002; 
	public static final int PROTECTED = 0x00000004;
	public static final int PACKAGE = 0x00000000;
	
	// Modifiers
	public static final int FINAL = 0x00000010;
	public static final int STATIC = 0x00000020;
	public static final int ABSTRACT = 0x00000040;
	
	// Type flags
	public static final int TYP_INTERFACE = 0x00000100;
}
