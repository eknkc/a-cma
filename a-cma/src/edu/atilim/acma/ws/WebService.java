package edu.atilim.acma.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.xmlrpc.XmlRpcException;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.ZIPDesignReader;
import edu.atilim.acma.ui.ConfigManager;
import edu.atilim.acma.ui.ConfigManager.Action;
import edu.atilim.acma.ui.ConfigManager.Metric;

public class WebService {
	public String createContext() {
		return Context.create().getId().toString();
	}
	
	public Map<String, Object> getStatus(String context) {
		HashMap<String, Object> status = new HashMap<String, Object>();
		
		Context c = ContextManager.getContext(context);
		if (c == null) {
			status.put("state", "NOT_EXISTS");
			return status;
		}
		
		status.put("id", c.getId().toString());
		status.put("state", c.getState().toString());
		status.put("hasdesign", c.getDesign() != null);
		
		return status;
	}
	
	// Design
	public boolean putDesign(String context, byte[] data) throws IOException, XmlRpcException {
		Context c = getContext(context);
		
		File tempFile = File.createTempFile("acma", "zip");
		OutputStream out = new FileOutputStream(tempFile);
		out.write(data);
		out.close();
		
		Design d = new ZIPDesignReader(tempFile.getAbsolutePath()).read();
		if (d == null) throw new XmlRpcException(2, "Malformed design.");
		
		c.setDesign(d);
		return true;
	}
	
	// Config related
	public Map<String, Object> getConfig(String context) throws XmlRpcException {
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("actions", getActions(context));
		response.put("metrics", getMetrics(context));
		return response;
	}
	
	public List<Map<String, Object>> getActions(String context) throws XmlRpcException {
		Context c = getContext(context);
		
		List<Action> actions = ConfigManager.getActions(c.getRunConfig());
		
		List<Map<String, Object>> response = new ArrayList<Map<String,Object>>();
		for (Action action : actions) {
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("name", action.getName());
			aMap.put("enabled", action.isEnabled());
			response.add(aMap);
		}
		
		return response;
	}
	
	public boolean setActionEnabled(String context, String action, boolean value) throws XmlRpcException {
		Context c = getContext(context);
		c.getRunConfig().setActionEnabled(action, value);
		return true;
	}
	
	public List<Map<String, Object>> getMetrics(String context) throws XmlRpcException {
		Context c = getContext(context);
		
		List<Metric> metrics = ConfigManager.getMetrics(c.getRunConfig());
		
		List<Map<String, Object>> response = new ArrayList<Map<String,Object>>();
		for (Metric metric : metrics) {
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("name", metric.getName());
			aMap.put("enabled", metric.isEnabled());
			response.add(aMap);
		}
		
		return response;
	}
	
	public boolean setMetricEnabled(String context, String metric, boolean value) throws XmlRpcException {
		Context c = getContext(context);
		c.getRunConfig().setMetricEnabled(metric, value);
		return true;
	}
	
	private Context getContext(String context) throws XmlRpcException {
		Context c = ContextManager.getContext(context);
		if (c == null) throw new XmlRpcException(1, "Context not found.");
		return c;
	}
}
