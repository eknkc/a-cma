package edu.atilim.acma;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import edu.atilim.acma.metrics.MetricNormalizer;
import edu.atilim.acma.metrics.MetricSummary;
import edu.atilim.acma.ui.ConfigManager;

public class ParetoFrontCalculator implements Runnable {
	@Override
	public void run() {
		ArrayList<RunResult> results = new ArrayList<RunResult>();
		
		System.out.println("Please enter the results folder location: ");
		String root = Console.readLine();
		
		if (root.length() == 0) {
			root = ".//data//results";
		}
		
		File rd = new File(root);
		
		try {
			if (rd.exists() && rd.isDirectory()) {
				for (File inner : rd.listFiles()) {
					if (inner.isDirectory()) {
						for (File result : inner.listFiles()) {
							if (!result.isFile() || !result.getName().endsWith(".txt")) continue;
							
							results.add(RunResult.readFrom(result.getAbsolutePath()));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		results = getParetoFront(results);
		
		int b = 0;
		int h = 0;
		int a = 0;
		int s = 0;
		
		for (RunResult r : results) {
			switch(r.getAttribute("Algorithm").charAt(0)) {
			case 'B':
				b++;
				break;
			case 'H':
				h++;
				break;
			case 'A':
				a++;
				break;
			case 'S':
				s++;
				break;
			}
			
			System.out.println(r.toCSVString());
		}
		
		System.out.printf("b=%d, h=%d, a=%d, s=%d", b, h, a, s);
	}
	
	private ArrayList<RunResult> getParetoFront(ArrayList<RunResult> res) {
		RunConfig config = ConfigManager.getRunConfig("Default");
		ArrayList<RunResult> front = new ArrayList<RunResult>();
		
		cur:
		for (int i = 0; i < res.size(); i++) {
			RunResult cur = res.get(i);

			HashMap<String, Double> curMetrics = MetricNormalizer.normalizeEach(new MetricSummary(cur.getFinalDesign().getMetrics()), config);
			
			for (int j = 0; j < res.size(); j++) {
				if (i == j) continue;
				
				RunResult check = res.get(j);
				
				if (!cur.getBenchmark().equalsIgnoreCase(check.getBenchmark())) continue;
				
				HashMap<String, Double> checkMetrics = MetricNormalizer.normalizeEach(new MetricSummary(check.getFinalDesign().getMetrics()), config);
				
				boolean paretobetter = true;
				
				for (Entry<String, Double> metric : checkMetrics.entrySet()) {
					double checkVal = metric.getValue();
					double curVal = curMetrics.get(metric.getKey());
					
					if (checkVal > curVal)
						paretobetter = false;
				}
				
				if (paretobetter)
					continue cur;
			}
			
			front.add(cur);
		}
		
		return front;
	}
}
