package edu.atilim.acma.search;

import edu.atilim.acma.design.Design;

public interface AlgorithmObserver {
	void onStart(Algorithm algorithm, Design initial);
	void onFinish(Algorithm algorithm, Design last);
	
	void onExpansion(Algorithm algorithm, int currentExpanded, int totalExpanded);
	void onUpdateCurrent(Algorithm algorithm, Solution solution);
	void onUpdateBest(Algorithm algorithm, Solution solution);
	
	void onLog(Algorithm algorithm, String log);
}
