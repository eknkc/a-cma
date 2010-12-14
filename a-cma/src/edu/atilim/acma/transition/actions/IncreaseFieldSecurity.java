package edu.atilim.acma.transition.actions;

import java.util.List;
import java.util.Set;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Field;
import edu.atilim.acma.design.Type;

public class IncreaseFieldSecurity {
	public static class Checker implements ActionChecker {
		@Override
		public void findPossibleActions(Design design, Set<Action> set) {
			List<Type> types = design.getTypes();
			
			for (Type t : types) {
				for (Field f : t.getFields()) {
					if (f.isConstant() ||  f.getAccess() == Accessibility.PUBLIC) continue;
					
					Accessibility newaccess = Accessibility.PUBLIC;
					
					if (f.getAccess() == Accessibility.PRIVATE)
						newaccess = Accessibility.PROTECTED;
					if (f.getAccess() == Accessibility.PROTECTED)
						newaccess = Accessibility.PACKAGE;
					if (f.getAccess() == Accessibility.PACKAGE)
						newaccess = Accessibility.PUBLIC;
					
					set.add(new Performer(t.getName(), f.getName(), newaccess));
				}
			}
		}
	}
	
	public static class Performer extends DecreaseFieldSecurity.Performer {
		public Performer(String typeName, String fieldName,
				Accessibility newAccess) {
			super(typeName, fieldName, newAccess);
		}
	}
}
