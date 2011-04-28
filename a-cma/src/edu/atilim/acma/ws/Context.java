package edu.atilim.acma.ws;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import edu.atilim.acma.design.Design;

public class Context implements Externalizable {
	private UUID id;
	private Design design;
	private HashMap<String, String> parameters;
	private ContextState state;
	
	public Context() {
		
	}
	
	public Context(Design design, HashMap<String, String> parameters) {
		this.id = UUID.randomUUID();
		this.design = design;
		this.parameters = parameters;
		this.state = ContextState.WAITING;
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = (UUID)in.readObject();
		design = (Design)in.readObject();
		state = (ContextState)in.readObject();
		
		parameters = new HashMap<String, String>();
		int count = in.readInt();
		for (int i = 0; i < count; i++) {
			String key = in.readUTF();
			String value = in.readUTF();
			
			parameters.put(key, value);
		}
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(design);
		out.writeObject(state);
		
		out.writeInt(parameters.size());
		for (Entry<String, String> e : parameters.entrySet()) {
			out.writeUTF(e.getKey());
			out.writeUTF(e.getValue());
		}
	}
}
