package edu.atilim.acma.search;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.concurrent.Instance;
import edu.atilim.acma.concurrent.InstanceSet;
import edu.atilim.acma.design.Design;

public class ConcurrentBeamSearch extends ConcurrentAlgorithm {
	private int beamLength;
	private int randomDepth;
	
	public ConcurrentBeamSearch() {
	}

	public ConcurrentBeamSearch(String name, RunConfig config, Design initialDesign, int beamLength, int randomDepth) {
		super(name, config, initialDesign);
		
		this.beamLength = beamLength;
		this.randomDepth = randomDepth;
	}

	@Override
	public void runMaster(InstanceSet instances) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void runWorker(Instance master) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		super.writeExternal(out);
		
		out.writeInt(0); //version
		out.writeInt(beamLength);
		out.writeInt(randomDepth);
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		
		in.readInt();
		beamLength = in.readInt();
		randomDepth = in.readInt();
	}
}
