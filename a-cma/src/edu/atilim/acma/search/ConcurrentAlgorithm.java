package edu.atilim.acma.search;

import java.io.Externalizable;
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
	
	protected void onFinish(Design finalDesign) {
		
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
