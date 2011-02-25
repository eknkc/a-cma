package edu.atilim.acma;

import edu.atilim.acma.design.Design;
import edu.atilim.acma.design.io.ZIPDesignReader;
import edu.atilim.acma.search.AbstractAlgorithm;
import edu.atilim.acma.search.AlgorithmObserver;
import edu.atilim.acma.search.BeeColonyAlgorithm;
import edu.atilim.acma.search.SolutionDesign;
import edu.atilim.acma.ui.ConfigManager;

public class BeeColonyTester implements Runnable {
	@Override
	public void run() {
		System.out.println("Leading Beaver for bee colony testing.");
		Design d = new ZIPDesignReader("./data/benchmarks/beaver.zip").read();
		
		if (d == null) {
			System.out.println("Could not read 'beaver.zip'.");
			System.exit(1);
		}
		
		BeeColonyAlgorithm abc = new BeeColonyAlgorithm(new SolutionDesign(d, ConfigManager.getRunConfig("Default")), new AlgorithmObserver() {
			@Override
			public void onUpdateItems(AbstractAlgorithm algorithm,
					SolutionDesign current, SolutionDesign best, int updateType) {
			}
			
			@Override
			public void onStep(AbstractAlgorithm algorithm, int step) {
			}
			
			@Override
			public void onStart(AbstractAlgorithm algorithm, SolutionDesign initial) {
			}
			
			@Override
			public void onLog(AbstractAlgorithm algorithm, String log) {
				System.out.println(log);
			}
			
			@Override
			public void onFinish(AbstractAlgorithm algorithm, SolutionDesign last) {
			}
			
			@Override
			public void onExpansion(AbstractAlgorithm algorithm, int currentExpanded,
					int totalExpanded) {
			}
			
			@Override
			public void onAdvance(AbstractAlgorithm algorithm, int current, int total) {
			}
		});
		
		abc.start(false);
	}
}
