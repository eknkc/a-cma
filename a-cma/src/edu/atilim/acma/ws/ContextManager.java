package edu.atilim.acma.ws;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import edu.atilim.acma.TaskQueue;
import edu.atilim.acma.search.ConcurrentAlgorithm;
import edu.atilim.acma.search.SolutionDesign;
import edu.atilim.acma.util.ACMAUtil;

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
		synchronized (registry) {
			return registry.get(id);
		}
	}
	
	static void register(Context context) {
		synchronized (registry) {
			registry.put(context.getId(), context);
		}
	}
	
	static void initialize() {
		ConcurrentAlgorithm.setListener(new FinishListener());
	}
	
	public static void startAlgorithm(Context context, ConcurrentAlgorithm algorithm) {
		context.setState(ContextState.RUNNING);
		
		algorithm.setName(context.getId().toString());
		TaskQueue.push(algorithm);
	}
	
	private static class FinishListener implements ConcurrentAlgorithm.Listener {
		@Override
		public void onAlgorithmFinish(String name, SolutionDesign finalDesign) {
			Context context = getContext(name);
			
			if (context != null) {
				context.setFinalDesign(finalDesign.getDesign());
				context.setState(ContextState.FINISHED);
				
				String email = context.getEmail();
				if (email != null && email.trim().length() != 0) {
					String message = "Hello,\r\nYour refactoring request on A-CMA has been completed. Please collect your results using the link below:\r\nhttp://online.a-cma.com/?cid=" + name + "\r\n\r\nThank you.";
					String subject = "Refactoring Completed";
					
					ACMAUtil.sendMail(email, subject, message);
				}
			}
		}
	}
}
