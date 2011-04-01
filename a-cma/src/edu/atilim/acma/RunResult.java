package edu.atilim.acma;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class RunResult {
	public class DesignInfo {
		private double score;
		
		private HashMap<String, Double> metrics;
		
		protected HashMap<String, Double> getMetrics() {
			return metrics;
		}
		
		protected double getScore() {
			return score;
		}
		
		private DesignInfo() {
			metrics = new HashMap<String, Double>();
		}
	}
	
	private enum ReadStage {
		HEADER,
		INITIAL,
		INITIALMETRICS,
		FINAL,
		FINALMETRICS
	}
	
	public static RunResult readFrom(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		RunResult result = new RunResult();
	
		ReadStage stage = ReadStage.HEADER;
		String line = null;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			
			if (stage == ReadStage.HEADER) {
				if (line.startsWith("* Name:"))
					result.name = line.substring(8);
				else if (line.startsWith("* Run Info:"))
					result.runInfo = line.substring(12);
				else if (line.startsWith("* Initial Design:"))
					stage = ReadStage.INITIAL;
				else if (line.startsWith("* Final Design:"))
					stage = ReadStage.FINAL;
			} else if (stage == ReadStage.INITIAL || stage == ReadStage.FINAL) {
				DesignInfo design = stage == ReadStage.INITIAL ? result.initialDesign : result.finalDesign;
				
				if (line.startsWith("* Score:"))
					design.score = Double.parseDouble(line.substring(9).replace(',', '.'));
				else if (line.startsWith("* Metric Summary:")) {
					if (stage == ReadStage.INITIAL) stage = ReadStage.INITIALMETRICS;
					else if (stage == ReadStage.FINAL) stage = ReadStage.FINALMETRICS;
					continue;
				}
			} else if (stage == ReadStage.INITIALMETRICS || stage == ReadStage.FINALMETRICS) {
				DesignInfo design = stage == ReadStage.INITIALMETRICS ? result.initialDesign : result.finalDesign;
				
				if (line.length() == 0) {
					stage = ReadStage.HEADER;
					continue;
				}
				
				String[] tokens = line.split(" ");
				
				String mName = tokens[1].trim();
				mName = mName.replace(":", "");
				
				double mVal = Double.parseDouble(tokens[2].replace(',', '.'));
				
				design.metrics.put(mName, mVal);
			}
		}
		
		br.close();
		
		return result;
	}
	
	private UUID id;
	private String name;
	private String runInfo;
	private DesignInfo initialDesign;
	private DesignInfo finalDesign;
	
	private RunResult() {
		id = UUID.randomUUID();
		initialDesign = new DesignInfo();
		finalDesign = new DesignInfo();
	}

	private RunResult(UUID id, String name, String runInfo, DesignInfo initialDesign, DesignInfo finalDesign) {
		this.id = id;
		this.name = name;
		this.runInfo = runInfo;
		this.initialDesign = initialDesign;
		this.finalDesign = finalDesign;
	}
	
	public String getBenchmark() {
		return name.split(" ")[0].trim();
	}

	public DesignInfo getFinalDesign() {
		return finalDesign;
	}

	public UUID getId() {
		return id;
	}

	public DesignInfo getInitialDesign() {
		return initialDesign;
	}

	public String getName() {
		return name;
	}

	public String getRunInfo() {
		return runInfo;
	}
}
