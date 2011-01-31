package edu.atilim.acma.transition.actions;

import java.util.Set;

import edu.atilim.acma.design.Accessibility;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.Method;
import edu.atilim.acma.design.Type;
import edu.atilim.acma.util.Log;

public class RemoveMethod {
	
	public static class Checker implements ActionChecker {
		@Override
		public void findPossibleActions(Design design, Set<Action> set) {
			for (Type t : design.getTypes())
			{
				method:
				for(Method m : t.getMethods())
				{
					if(m.getAccess() == Accessibility.PUBLIC || m.getAccess() == Accessibility.PROTECTED ) continue;
					
					if(!m.isStatic())
					{
						Type superType = t.getSuperType();
						
						while(superType != null)
						{
							for(Method mt : superType.getMethods() )
							{
								if(mt.getSignature().equals(m.getSignature()))
								   break method;
							}
							superType = superType.getSuperType();
						}
					}
					
					if(m.getCallerMethods().isEmpty())
						set.add(new Performer(t.getName(), m.getName()));
				}
			}
		}
	}
	
	public static class Performer implements Action {
		
		private String typeName;
		private String methodName;
		
		public Performer(String typeName, String methodName) {
			
			this.typeName = typeName;
			this.methodName = methodName;
		}

		@Override
		public void perform(Design d) {
			
			Type t = d.getType(typeName);
			Method m = d.getType(typeName).getMethod(methodName);
			if (t == null || m == null) {
				Log.severe("[RemoveMethod] Can not find method %s of type: %s.", methodName,typeName);
				return;
			}
			m.remove();
			
		}
		
		@Override
		public String toString() {
			return String.format("Remove method '%s' of type '%s'", methodName,typeName);
		}
	}

}
