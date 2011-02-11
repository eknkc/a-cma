package edu.atilim.acma;

import edu.atilim.acma.concurrent.ConcurrentTask;
import edu.atilim.acma.concurrent.ConnectionListener;
import edu.atilim.acma.concurrent.Instance;
import edu.atilim.acma.concurrent.SocketInstance;

public class Client implements Runnable, ConnectionListener {
	private String hostname;
	private int port;
	
	private Instance master;
	
	public Client(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}

	@Override
	public void run() {
		System.out.printf("[Client] Connecting to %s:%d\n", hostname, port);
		for(;;) {
			master = SocketInstance.tryConnect(hostname, port);
			if (master == null) {
				System.out.println("[Client] Could not connect. Retrying in 2 seconds...");
				try { Thread.sleep(2000); } catch (InterruptedException e) { }
				continue;
			}
			master.setConnectionListener(this);
			System.out.println("[Client] Connection established.");
			break;
		}
		
		while(true) {
			System.out.println("[Client] Waiting for task assignment.");
			ConcurrentTask task = master.receive(ConcurrentTask.class);
			task.runWorker(master);
		}
	}

	@Override
	public void onConnect(Instance instance) {
	}

	@Override
	public void onDisconnect(Instance instance) {
		System.out.printf("Connection to server %s closed. Ceasing operations!\n", instance.getName());
		System.exit(1);
	}
}
