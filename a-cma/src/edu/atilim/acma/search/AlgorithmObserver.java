package edu.atilim.acma.search;

public interface AlgorithmObserver {
	void onStart(Algorithm algorithm);
	void onFinish(Algorithm algorithm);
	
	void onExpansion(int currentExpanded, int totalExpanded);
	void onIteration(int iterNum);
	void onUpdateCurrent(Solution solution);
	void onUpdateBest(Solution solution);
	void onRestart(int restartNum);
}
