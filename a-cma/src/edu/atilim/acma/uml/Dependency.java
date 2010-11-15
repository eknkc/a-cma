package edu.atilim.acma.uml;

import java.io.Serializable;

public class Dependency implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final int FLAG_SINGULARDEP = 0x01000000;
	public static final int FLAG_SINGULARTYPES = 0x02000000;
	
	public static final int EXTEND 		= 0x01000001;
	public static final int IMPLEMENT	= 0x02000002;
	public static final int PARENT 		= 0x01000004;
	public static final int PACKAGE		= 0x01000008;
	public static final int PARAMETER	= 0x00000010;
	public static final int RETURN		= 0x01000020;
	
	private Element source;
	private Element target;
	private int type;
	
	public Dependency(Element source, Element target, int type) {
		super();
		this.source = source;
		this.target = target;
		this.type = type;
	}
	
	public Element getOther(Element party) {
		if (source == party) return target;
		else if (target == party) return source;
		return null;
	}

	public Element getSource() {
		return source;
	}
	
	public void setSource(Element source) {
		this.source = source;
	}
	
	public Element getTarget() {
		return target;
	}
	
	public void setTarget(Element target) {
		this.target = target;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
}
