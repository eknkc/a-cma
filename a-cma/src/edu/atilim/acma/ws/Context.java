package edu.atilim.acma.ws;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.UUID;

import edu.atilim.acma.RunConfig;
import edu.atilim.acma.design.Design;
import edu.atilim.acma.ui.ConfigManager;

public class Context implements Externalizable {
	private UUID id;
	private Design design;
	private Design finalDesign;
	private RunConfig runConfig;
	private ContextState state;
	private String email;
	
	public UUID getId() {
		return id;
	}

	public Design getDesign() {
		return design;
	}
	
	public Design getFinalDesign() {
		return finalDesign;
	}
	
	public void setDesign(Design design) {
		this.design = design;
	}

	void setFinalDesign(Design finalDesign) {
		this.finalDesign = finalDesign;
	}

	public RunConfig getRunConfig() {
		return runConfig;
	}

	public ContextState getState() {
		return state;
	}
	
	void setState(ContextState state) {
		this.state = state;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public static Context create() {
		Context context = new Context();
		ContextManager.register(context);
		return context;
	}
	
	public static Context create(Design design) {
		Context context = create();
		context.setDesign(design);
		return context;
	}

	public Context() {
		id = UUID.randomUUID();
		design = null;
		runConfig = ConfigManager.getRunConfig("Default");
		state = ContextState.WAITING;
		email = "";
	}
	
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = (UUID)in.readObject();
		design = (Design)in.readObject();
		finalDesign = (Design)in.readObject();
		runConfig = (RunConfig)in.readObject();
		state = (ContextState)in.readObject();
		email = in.readUTF();
	}
	
	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeObject(id);
		out.writeObject(design);
		out.writeObject(finalDesign);
		out.writeObject(runConfig);
		out.writeObject(state);
		out.writeUTF(email);
	}
}
