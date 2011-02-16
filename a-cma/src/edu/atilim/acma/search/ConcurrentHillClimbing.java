package edu.atilim.acma.search;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.design.Design;

public class ConcurrentHillClimbing extends ConcurrentMultiRunAlgorithm {
	private int resCount;
	private int resDepth;
	
	public ConcurrentHillClimbing() {
	}

	public ConcurrentHillClimbing(String name, RunConfig config, Design initialDesign, int resCount, int resDepth, int runCount) {
		super(name, config, initialDesign, runCount);
		
		this.resCount = resCount;
		this.resDepth = resDepth;
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		
		out.writeInt(0); //version
		out.writeInt(resCount);
		out.writeInt(resDepth);
	}
	
	@Override
	public AbstractAlgorithm spawnAlgorithm() {
		HillClimbingAlgorithm algo = new HillClimbingAlgorithm(new SolutionDesign(getInitialDesign(), getConfig()), null);
		algo.setRestartCount(resCount);
		algo.setRestartDepth(resDepth);
		return algo;
	}
	
	@Override
	public String getRunInfo() {
		return String.format("Hill Climbing. Restart Count: %d, Depth: %d.", resCount, resDepth);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		
		in.readInt();
		resCount = in.readInt();
		resDepth = in.readInt();
	}
}
