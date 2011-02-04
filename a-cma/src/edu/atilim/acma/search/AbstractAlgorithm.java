package edu.atilim.acma.search;

import edu.atilim.acma.util.Log;

public abstract class AbstractAlgorithm {
	public static final int STATE_NEW = 0;
	public static final int STATE_RUNNING = 1;
	public static final int STATE_PAUSED = 2;
	public static final int STATE_FINISHED = 3;
	
	private AlgorithmObserver observer;
	protected SolutionDesign initialDesign;
	protected SolutionDesign finalDesign;
	private int stepCount;
	
	private int state;
	
	public int getState() {
		return state;
	}

	public AbstractAlgorithm(SolutionDesign initialDesign) {
		this(initialDesign, null);
	}
	
	public AbstractAlgorithm(SolutionDesign initialDesign, AlgorithmObserver observer) {
		this.observer = observer;
		this.initialDesign = initialDesign;
		this.state = STATE_NEW;
	}
	
	public abstract String getName();
	public abstract boolean step();
	
	protected void beforeStart() {
		
	}
	
	protected void afterFinish() {
		
	}
	
	protected int getStepCount() {
		return stepCount;
	}

	public AlgorithmObserver getObserver() {
		return observer;
	}
	
	public void start() {
		if (state == STATE_NEW)
			beforeStart();
		
		state = STATE_RUNNING;
			
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (state == STATE_RUNNING) {
					stepCount++;
					if (step()) {
						state = STATE_FINISHED;
						afterFinish();
						return;
					}
				}
			}
		}).start();
	}
	
	public void pause() {
		state = STATE_PAUSED;
	}
	
	protected void log(String log) {
		Log.info("[%s] %s", getName(), log);
		
		if (getObserver() != null)
			getObserver().onLog(this, log); //TODO: this!
	}
	
	protected void log(String log, Object... args) {
		log(String.format(log, args));
	}
	
	public void setObserver(AlgorithmObserver observer) {
		this.observer = observer;
	}
}
