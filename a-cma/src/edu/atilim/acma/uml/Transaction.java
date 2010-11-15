package edu.atilim.acma.uml;

import java.util.Iterator;
import java.util.LinkedList;

public class Transaction {
	private LinkedList<Action> actions;
	
	public Transaction() {
		actions = new LinkedList<Transaction.Action>();
	}
	
	public void queue(Action action) {
		if (action != null)
			actions.addLast(action);
	}
	
	public void rollback() {
		for (Iterator<Action> i = actions.descendingIterator(); i.hasNext();)
			i.next().undo();
		actions.clear();
	}

	public static abstract class Action {
		public abstract void undo();
		
		public static class FlagChange extends Action {
			private int oldFlags;
			private Element owner;

			public FlagChange(Element owner, int oldFlags) {
				super();
				this.oldFlags = oldFlags;
				this.owner = owner;
			}

			@Override
			public void undo() {
				owner.setFlags(oldFlags);
			}
		}
		
		public static class NameChange extends Action {
			private String oldName;
			private Element owner;

			public NameChange(Element owner, String oldName) {
				super();
				this.oldName = oldName;
				this.owner = owner;
			}

			@Override
			public void undo() {
				owner.setName(oldName);
			}
		}
		
		public static class DependencyRemove extends Action {
			private Dependency dependency;
			
			public DependencyRemove(Dependency dependency) {
				super();
				this.dependency = dependency;
			}

			@Override
			public void undo() {
				dependency.getSource().getDeps().add(dependency);
			}
		}
		
		public static class DependencyAdd extends Action {
			private Dependency dependency;
			
			public DependencyAdd(Dependency dependency) {
				super();
				this.dependency = dependency;
			}

			@Override
			public void undo() {
				dependency.getSource().getDeps().remove(dependency);				
			}
		}
	}
}
