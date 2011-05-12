package edu.atilim.acma.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.ZIPDesignReader;
import edu.atilim.acma.metrics.MetricCalculator;
import edu.atilim.acma.metrics.MetricTable;
import edu.atilim.acma.transition.TransitionManager;
import edu.atilim.acma.ui.ConfigManager;
import edu.atilim.acma.ui.ConfigManager.Action;
import edu.atilim.acma.ui.ConfigManager.Metric;
import edu.atilim.acma.util.ACMAUtil;

public class WebService {
	public String createContext() {
		Context c = Context.create();
		return c.getId().toString();
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
		status.put("email", c.getEmail());
		
		if (c.getDesign() != null) {
			HashMap<String, Object> df = new HashMap<String, Object>();
			df.put("types", c.getDesign().getTypes().size());
			df.put("packages", c.getDesign().getPackages().size());
			status.put("dinfo", df);
		}
		
		return status;
	}
	
	public boolean setEmail(String context, String email) throws XmlRpcException {
		Context c = getContext(context);
		c.setEmail(email);
		return true;
	}
	
	// Refactoring
	public boolean startRefactoring(String context, Map<String, Object> parameters) {
		return false;
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
		
		Set<edu.atilim.acma.transition.actions.Action> posActions = TransitionManager.getPossibleActions(c.getDesign());
		List<Action> actions = ConfigManager.getActions(c.getRunConfig());
		
		List<Map<String, Object>> response = new ArrayList<Map<String,Object>>();
		for (Action action : actions) {
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("name", action.getName());
			aMap.put("splitname", ACMAUtil.splitCamelCase(action.getName()));
			aMap.put("enabled", action.isEnabled());
			
			int count = 0;
			for (edu.atilim.acma.transition.actions.Action a : posActions) {
				if (a.getClass().getEnclosingClass().getSimpleName().equals(action.getName()))
					count++;
			}
			aMap.put("applicible", count);
			
			response.add(aMap);
		}
		
		return response;
	}
	
	public boolean setActionEnabled(String context, String action, boolean value) throws XmlRpcException {
		Context c = getContext(context);
		c.getRunConfig().setActionEnabled(action, value);
		return true;
	}
	
	public boolean setActionsEnabled(String context, List<String> data) throws XmlRpcException {
		Context c = getContext(context);
		
		List<Action> actions = ConfigManager.getActions(c.getRunConfig());
		for (Action a : actions) {
			if (data.contains(a.getName()))
				c.getRunConfig().setActionEnabled(a.getName(), true);
			else
				c.getRunConfig().setActionEnabled(a.getName(), false);
		}
		
		return true;
	}
	
	public List<Map<String, Object>> getMetrics(String context) throws XmlRpcException {
		Context c = getContext(context);
		
		List<Metric> metrics = ConfigManager.getMetrics(c.getRunConfig());
		MetricTable values = MetricCalculator.calculate(c.getDesign(), c.getRunConfig());
		
		List<Map<String, Object>> response = new ArrayList<Map<String,Object>>();
		for (Metric metric : metrics) {
			Map<String, Object> aMap = new HashMap<String, Object>();
			aMap.put("name", metric.getName());
			aMap.put("enabled", metric.isEnabled());
			aMap.put("package", metric.isPackageMetric());
			aMap.put("minimized", metric.isMinimized());
			aMap.put("value", values.getAverage(metric.getName()));
			response.add(aMap);
		}
		
		return response;
	}
	
	public boolean setMetricEnabled(String context, String metric, boolean value) throws XmlRpcException {
		Context c = getContext(context);
		c.getRunConfig().setMetricEnabled(metric, value);
		return true;
	}
	
	public boolean setMetricsEnabled(String context, List<String> data) throws XmlRpcException {
		Context c = getContext(context);
		
		List<Metric> metrics = ConfigManager.getMetrics(c.getRunConfig());
		for (Metric m : metrics) {
			if (data.contains(m.getName()))
				c.getRunConfig().setMetricEnabled(m.getName(), true);
			else
				c.getRunConfig().setMetricEnabled(m.getName(), false);
		}
		
		return true;
	}
	
	private Context getContext(String context) throws XmlRpcException {
		Context c = ContextManager.getContext(context);
		if (c == null) throw new XmlRpcException(1, "Context not found. ID: " + context);
		return c;
	}
}
