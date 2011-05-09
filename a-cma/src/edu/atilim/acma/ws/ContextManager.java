package edu.atilim.acma.ws;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ContextManager {
	private static Map<UUID, Context> registry = new HashMap<UUID, Context>();
	
	public static Context getContext(String id) {
		try {
			return getContext(UUID.fromString(id));
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public static Context getContext(UUID id) {
		return registry.get(id);
	}
	
	static void register(Context context) {
		System.out.println("New context, deleting old!");
		//registry.clear();
		registry.put(context.getId(), context);
	}
}
