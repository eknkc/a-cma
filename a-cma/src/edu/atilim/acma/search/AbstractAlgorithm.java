package edu.atilim.acma.search;

import edu.atilim.acma.util.Log;

public abstract class AbstractAlgorithm {
	private AlgorithmObserver observer;
	protected SolutionDesign initialDesign;
	protected SolutionDesign finalDesign;
	private int stepCount;
	
	public AbstractAlgorithm(SolutionDesign initialDesign) {
		this(initialDesign, null);
	}
	
	public AbstractAlgorithm(SolutionDesign initialDesign, AlgorithmObserver observer) {
		this.observer = observer;
		this.initialDesign = initialDesign;
	}
	
	public abstract String getName();
	public abstract boolean step();
	
	protected int getStepCount() {
		return stepCount;
	}

	public AlgorithmObserver getObserver() {
		return observer;
	}
	
	protected void log(String log) {
		Log.info("[%s] %s", getName(), log);
		
		if (getObserver() != null)
			getObserver().onLog(null, log); //TODO: this!
	}
	
	protected void log(String log, Object... args) {
		log(String.format(log, args));
	}
	
	public void setObserver(AlgorithmObserver observer) {
		this.observer = observer;
	}
}
