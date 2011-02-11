package edu.atilim.acma.search;

import java.io.BufferedWriter;
import java.io.Externalizable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.concurrent.ConcurrentTask;
import edu.atilim.acma.design.Design;

public abstract class ConcurrentAlgorithm implements ConcurrentTask, Externalizable {
	private String name;
	private RunConfig config;
	private Design initialDesign;
	
	public String getName() {
		return name;
	}
	
	protected RunConfig getConfig() {
		return config;
	}
	
	protected Design getInitialDesign() {
		return initialDesign;
	}
	
	public ConcurrentAlgorithm() {
	}
	
	public ConcurrentAlgorithm(String name, RunConfig config, Design initialDesign) {
		this.name = name;
		this.config = config;
		this.initialDesign = initialDesign;
	}
	
	protected void onFinish(Design fDesign) {
		String pathName = String.format("./data/results/%s/", getName().replace('/', '-'));
		String runName = String.format("%sresults.txt", pathName);
		
		File dir = new File(pathName);
		if (!dir.exists()) dir.mkdirs();
		
		SolutionDesign initialDesign = new SolutionDesign(this.initialDesign, config);
		SolutionDesign finalDesign = new SolutionDesign(fDesign, config);
		
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(runName, true));
			bw.write(String.format("Initial Design Score: %.8f\n", initialDesign.getScore()));
			bw.write(String.format("Final Design Score: %.8f\n\n", finalDesign.getScore()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try { bw.close(); } catch (Exception e) { }
		}
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
