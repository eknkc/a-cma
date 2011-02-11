package edu.atilim.acma.concurrent;

import java.io.Serializable;

public interface Instance {
	public String getName();
	public void setConnectionListener(ConnectionListener connectionListener);
	public void setInstanceListener(InstanceListener listener);
	public void send(Serializable message);
	public <T extends Serializable> T receive(Class<T> cls);
	public Serializable receive();
}
