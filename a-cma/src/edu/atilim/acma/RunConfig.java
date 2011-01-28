package edu.atilim.acma;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import edu.atilim.acma.util.ACMAUtil;

public class RunConfig implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Action> actions;
	private HashMap<String, Metric> metrics;
	
	public Collection<Action> actions() {
		return actions.values();
	}
	
	public Collection<Metric> metrics() {
		return metrics.values();
	}
	
	public Metric getMetric(String name) {
		return metrics.get(name);
	}
	
	public Action getAction(String name) {
		return actions.get(name);
	}
	
	public RunConfig() {
		actions = new HashMap<String, RunConfig.Action>();
		metrics = new HashMap<String, RunConfig.Metric>();
	}
	
	public class Action implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String name;
		private boolean enabled;
		
		public boolean isEnabled() {
			return enabled;
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		public String getName() {
			return name;
		}
		
		public String getDisplayName() {
			return ACMAUtil.splitCamelCase(name);
		}

		private Action(String name, boolean enabled) {
			this.name = name;
			this.enabled = enabled;
		}
	}
	
	public class Metric implements Serializable {
		private static final long serialVersionUID = 1L;
		
		private String name;
		private double weight;
		private boolean enabled;
		
		public double getWeight() {
			return weight;
		}
		
		public void setWeight(double weight) {
			this.weight = weight;
		}
		
		public boolean isEnabled() {
			return enabled;
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
		
		public String getName() {
			return name;
		}

		private Metric(String name, double weight, boolean enabled) {
			this.name = name;
			this.weight = weight;
			this.enabled = enabled;
		}
	}
}
