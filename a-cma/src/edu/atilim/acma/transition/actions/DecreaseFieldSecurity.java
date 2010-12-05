package edu.atilim.acma.transition.actions;

import java.util.List;
import java.util.Set;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Type;

public class DecreaseFieldSecurity {
	public static class Checker implements ActionChecker {

		@Override
		public void findPossibleActions(Design design, Set<Action> set) {
			List<Type> types = design.getTypes();
			
			for (Type t : types) {
				field:
				for (Field f : t.getFields()) {
					// Daha nereye?
					if (f.isConstant() ||  f.getAccess() == Accessibility.PRIVATE) continue;
					
					if (f.getName().equals("REF_SUPERCLASS"))
						System.out.println();
					
					Accessibility newaccess = Accessibility.PRIVATE;
					
					if (f.getAccess() == Accessibility.PUBLIC)
						newaccess = Accessibility.PACKAGE;
					if (f.getAccess() == Accessibility.PACKAGE)
						newaccess = Accessibility.PROTECTED;
					if (f.getAccess() == Accessibility.PROTECTED)
						newaccess = Accessibility.PRIVATE;
					
					for (Method m : f.getAccessors()) {
						if (!m.canAccess(f, newaccess))
							break field;
					}
					
					set.add(new Performer(f, newaccess));
				}
			}
		}
		
	}
	
	public static class Performer implements Action {
		private Field field;
		private Accessibility newAccess;

		public Performer(Field field, Accessibility newAccess) {
			this.field = field;
			this.newAccess = newAccess;
		}

		@Override
		public void perform(Design d) {
			field.setAccess(newAccess);
		}
		
		@Override
		public String toString() {
			return String.format("Change accessibility of field %s to %s", field, newAccess);
		}
	}
}
