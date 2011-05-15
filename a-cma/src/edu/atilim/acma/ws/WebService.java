package edu.atilim.acma.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.xmlrpc.XmlRpcException;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.ZIPDesignReader;
import edu.atilim.acma.metrics.MetricCalculator;
import edu.atilim.acma.metrics.MetricSummary;
import edu.atilim.acma.metrics.MetricTable;
import edu.atilim.acma.search.ConcurrentAlgorithm;
import edu.atilim.acma.search.ConcurrentBeamSearch;
import edu.atilim.acma.search.ConcurrentHillClimbing;
import edu.atilim.acma.search.ConcurrentParallelBeeColony;
import edu.atilim.acma.search.RunInfoTag;
import edu.atilim.acma.search.SolutionDesign;
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
	public boolean startRefactoring(String context, Map<String, Object> parameters) throws XmlRpcException {
		Context c = getContext(context);
		
		if (!parameters.containsKey("algorithm"))
			return false;
		
		ConcurrentAlgorithm task = null;
		Object algorithm = parameters.get("algorithm");
		
		if ("ABC".equals(algorithm)) {
			int population = (Integer)parameters.get("population");
			int iterations = (Integer)parameters.get("iterations");
			
			task = new ConcurrentParallelBeeColony(context, c.getRunConfig(), c.getDesign(), 100, population, iterations, 1);
		} else if ("MSD".equals(algorithm)) {
			int randomRestarts = (Integer)parameters.get("randomRestarts");
			int restartDepth = (Integer)parameters.get("restartDepth");
			
			task = new ConcurrentHillClimbing(context, c.getRunConfig(), c.getDesign(), randomRestarts, restartDepth, 1);
		} else if ("LBS".equals(algorithm)) {
			int population = (Integer)parameters.get("population");
			int randomDepth = (Integer)parameters.get("randomDepth");
			int iterations = (Integer)parameters.get("iterations");
			
			task = new ConcurrentBeamSearch(context, c.getRunConfig(), c.getDesign(), population, randomDepth, iterations, 1);
		}
		
		if (task == null)
			return false;
		
		ContextManager.startAlgorithm(c, task);
		return true;
	}
	
	public Map<String, Object> getResult(String context) throws XmlRpcException {
		Context c = getContext(context);
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		Object tag = c.getFinalDesign().getTag();
		if (tag instanceof RunInfoTag) {
			RunInfoTag rtag = (RunInfoTag)tag;
			
			result.put("timetakenms", (int)rtag.getRunDuration());
			result.put("nodeexpansion", (int)rtag.getExpansionCount());
			result.put("runinfo", rtag.getRunInfo());
		}
		
		SolutionDesign id = new SolutionDesign(c.getDesign(), c.getRunConfig());
		SolutionDesign fd = new SolutionDesign(c.getFinalDesign(), c.getRunConfig());
		
		result.put("initial", GetDesignInfo(id));
		result.put("final", GetDesignInfo(fd));
		
		return result;
	}
	
	private static Map<String, Object> GetDesignInfo(SolutionDesign design) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("metrics", GetMetricInfo(design));
		result.put("score", design.getScore());
		result.put("numpossibleactions", design.getAllActions().size());
		result.put("numappliedactions", design.getDesign().getModifications().size());
		result.put("appliedactions", GetAppliedActionsInfo(design));
		
		return result;
	}
	
	private static Map<String, Object> GetMetricInfo(SolutionDesign design) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		MetricSummary summary = design.getMetricSummary();
		for (Entry<String, Double> me : summary.getMetrics().entrySet()) {
			result.put(me.getKey(), me.getValue());
		}
		
		return result;
	}
	
	private static List<Object> GetAppliedActionsInfo(SolutionDesign design) {
		List<Object> result = new ArrayList<Object>();
		Pattern re = Pattern.compile("\\[([0-9.,]*)\\]\\[([A-z ]*)\\](.*)");
		
		for (String mod : design.getDesign().getModifications()) {
			Matcher m = re.matcher(mod);
			
			if (m.find()) {
				double score = Double.parseDouble(m.group(1).replace(',', '.'));
				String action = m.group(2);
				String description = m.group(3).trim();
				
				result.add(new Object[] { score, action, description });
			}
		}
		
		return result;
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
