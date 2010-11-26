package edu.atilim.acma.uml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

import edu.atilim.acma.uml.Transaction.Action;
import edu.atilim.acma.uml.io.DesignTreeGenerator;
import edu.atilim.acma.util.Selector;

public class Design implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Package root;
	private transient Transaction transaction;
	
	public Design() {
		super();
		
		root = new Package("", this);
		transaction = null;
	}
	
	public Package getRootPackage() {
		return root;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Element> LinkedList<T> get(final Class<T> cls) {
		return this.get(new Selector<Element, T>() {
			@Override
			public T select(Element in) {
				if (cls.isInstance(in)) {
					return (T)in;
				}
				return null;
			}
		});
	}
	
	public <T extends Element> LinkedList<T> get(Selector<Element, T> selector) {
		HashSet<Element> discovered = new HashSet<Element>();
		Stack<Element> waiting = new Stack<Element>();
		LinkedList<T> list = new LinkedList<T>();
		
		discovered.add(root);
		waiting.push(root);
		
		while (waiting.size() > 0) {
			Element cur = waiting.pop();
			for (Element e : cur.childElements()) {
				if (discovered.contains(e)) continue;
				
				discovered.add(e);
				waiting.add(e);
				
				T out = selector.select(e);
				if (out != null)
					list.add(out);
			}
		}
		
		return list;
	}
	
	public <T extends Element> T get(String signature, Class<T> cls) {
		LinkedList<T> list = get(cls);
		for (T t : list) {
			if (t.getFullName().equals(signature))
				return t;
		}
		return null;
	}
	
	public Package getPackage(String name) {
		return getPackage(name, false);
	}
	
	public Package getPackage(String name, boolean create) {
		String[] pname = name.split("\\.");
		Package cur = root;
		for (int i = 0; i < pname.length; i++) {
			Package p = cur.getSubPackage(pname[i]);
			if (p == null) {
				if (create) {
					p = create(pname[i], Package.class);
					p.setParent(cur);
				}
				else
					return null;
			}
			cur = p;
		}
		return cur;
	}
	
	public <T extends Element> T create(String name, Class<T> cls) {
		try {
			Constructor<T> ctor = cls.getConstructor(String.class, Design.class);
			return ctor.newInstance(name, this);
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public void notifyAction(Action action) {
		if (transaction != null)
			transaction.queue(action);
	}
	
	public void beginTrans() {
		if (transaction == null)
			transaction = new Transaction();
	}
	
	public void rollbackTrans() {
		if (transaction != null) {
			Transaction trans = transaction;
			transaction = null;
			trans.rollback();
		}
	}
	
	public void commitTrans() {
		transaction = null;
	}
	
	public Design copy() {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ObjectOutputStream oout = new ObjectOutputStream(out);
			oout.writeObject(this);
			ObjectInputStream oin = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
			return (Design)oin.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String toString() {
		return new DesignTreeGenerator(this).toString();
	}
}
