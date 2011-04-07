package edu.atilim.acma.search;

import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Map.Entry;
import java.util.UUID;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.WikiBot;
import edu.atilim.acma.concurrent.ConcurrentTask;
import edu.atilim.acma.design.Design;

public abstract class ConcurrentAlgorithm implements ConcurrentTask, Externalizable {
	private String name;
	private RunConfig config;
	private Design initialDesign;
	private volatile boolean interrupted = false;
	
	public String getName() {
		return name;
	}
	
	protected RunConfig getConfig() {
		return config;
	}
	
	protected Design getInitialDesign() {
		return initialDesign;
	}
	
	protected boolean isInterrupted() {
		return interrupted;
	}
	
	public void clearInterrupt() {
		interrupted = false;
	}
	
	@Override
	public void interrupt() {
		interrupted = true;
	}

	public ConcurrentAlgorithm() {
	}
	
	public ConcurrentAlgorithm(String name, RunConfig config, Design initialDesign) {
		this.name = name;
		this.config = config;
		this.initialDesign = initialDesign;
	}
	
	protected synchronized void onFinish(Design fDesign) {
		UUID id = UUID.randomUUID();
		
		String pathName = String.format("./data/results/%s/", getName().replace('/', '-'));
		String runName = String.format("%s%s.txt", pathName, id.toString());
		
		File dir = new File(pathName);
		if (!dir.exists()) dir.mkdirs();
		
		SolutionDesign initialDesign = new SolutionDesign(this.initialDesign, config);
		SolutionDesign finalDesign = new SolutionDesign(fDesign, config);
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(runName, true));
			bw.write("====== Run Result ======\n");
			bw.write(String.format("  * Name: %s\n", getName()));
			
			if (fDesign.getTag() != null && fDesign.getTag() instanceof RunInfoTag) {
				RunInfoTag tag = (RunInfoTag)fDesign.getTag();
				bw.write(String.format("    * Run Info: %s\n", tag.getRunInfo()));
				bw.write(String.format("    * Time taken: %.2f seconds\n", tag.getRunDuration() / 1000.0));
				bw.write(String.format("    * Metric Mode: %s\n", tag.isPareto() ? "Pareto" : "Aggregate"));
				bw.write(String.format("    * Expanded Designs: %d\n", tag.getExpansionCount()));
				bw.write("\n");
			}
			
			bw.write("  * Initial Design:\n");
			bw.write(getDesignInfo(initialDesign));
			bw.write("\n");
			bw.write("  * Final Design:\n");
			bw.write(getDesignInfo(finalDesign));
			bw.write("\n");
			bw.write("  * Applied Actions:\n");
			for (String act : finalDesign.getDesign().getModifications()) {
				bw.write(String.format("    - %s\n", act));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try { bw.close(); } catch (Exception e) { }
		}
		
		WikiBot.pushRun(getName(), id.toString(), new File(runName));
	}
	
	private String getDesignInfo(SolutionDesign design) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("    * Score: ").append(String.format("%.6f", design.getScore())).append("\n");
		builder.append("    * Possible Actions: ").append(design.getAllActions().size()).append("\n");
		builder.append("    * Applied Actions: ").append(design.getDesign().getModifications().size()).append("\n");
		builder.append("    * Num Types: ").append(design.getDesign().getTypes().size()).append("\n");
		builder.append("    * Num Packages: ").append(design.getDesign().getPackages().size()).append("\n");
		
		builder.append("\n");
		
		builder.append("  * Metric Summary:").append("\n");
		for (Entry<String, Double> e : design.getMetricSummary().getMetrics().entrySet()) {
			builder.append("    * ").append(e.getKey()).append(": ").append(String.format("%.4f", e.getValue())).append("\n");
		}
		
		builder.append("\n");
		
		return builder.toString();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(0); //version
		
		out.writeUTF(name);
		out.writeObject(config);
		out.writeObject(initialDesign);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		in.readInt();
		
		name = in.readUTF();
		config = (RunConfig)in.readObject();
		initialDesign = (Design)in.readObject();
	}
	
	@Override
	public String toString() {
		return name;
	}
}
