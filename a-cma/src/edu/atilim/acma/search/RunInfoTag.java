package edu.atilim.acma.search;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class RunInfoTag implements Externalizable {
	private long runDuration;
	private String runInfo;
	
	public long getRunDuration() {
		return runDuration;
	}

	public String getRunInfo() {
		return runInfo;
	}

	public RunInfoTag(long runDuration, String runInfo) {
		this.runDuration = runDuration;
		this.runInfo = runInfo;
	}

	public RunInfoTag() {
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		runDuration = in.readLong();
		runInfo = in.readUTF();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(runDuration);
		out.writeUTF(runInfo);
	}
}
