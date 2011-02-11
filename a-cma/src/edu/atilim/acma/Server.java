package edu.atilim.acma;

import edu.atilim.acma.concurrent.ConcurrentTask;
import edu.atilim.acma.concurrent.ConnectionListener;
import edu.atilim.acma.concurrent.Instance;
import edu.atilim.acma.concurrent.InstanceSet;
import edu.atilim.acma.concurrent.SocketInstance;
import edu.atilim.acma.concurrent.TerminateTask;
import edu.atilim.acma.concurrent.SocketInstance.Listener;

public class Server implements Runnable, ConnectionListener {
	private int port;
	
	private InstanceSet instances;
	
	public Server(int port) {
		this.port = port;
		this.instances = new InstanceSet();
	}

	@Override
	public void run() {
		System.out.printf("[Server] Listening port %d for incoming connections.\n", port);
		
		Listener listener = SocketInstance.tryListen(port, this);
		
		if (listener == null) {
			System.out.printf("Can not listen port %d\n", port);
			return;
		}

		System.out.println("[Server] When ready, please type start to begin operations. After that point, no more clients will be accepted.");
		
		while (true) {
			if (Console.readLine().equalsIgnoreCase("start"))
			{
				if (instances.size() == 0) {
					System.out.println("There are no client instances available.");
					continue;
				}
				break;
			}
			System.out.println("Please enter 'start' to begin operations.");
		}
		
		listener.stop();
		
		System.out.printf("There are %d tasks to be executed in queue.\n", TaskQueue.size());
		
		while (true) {
			ConcurrentTask task = TaskQueue.peek();
			
			if (task == null) {
				System.out.println("All tasks completed. Exiting.");
				run(new TerminateTask());
			}
			
			System.out.printf("Running algorithm task %s, Remaining: %d.\n", task, TaskQueue.size() - 1);
			
			run(task);
			
			TaskQueue.remove(task);
		}
	}
	
	private void run(ConcurrentTask task) {
		instances.broadcast(task);
		task.runMaster(instances);
	}

	@Override
	public void onConnect(Instance instance) {
		instances.add(instance);
		System.out.printf("[Server] Client connected. Total: %d\n", instances.size());
	}
	
	@Override
	public void onDisconnect(Instance instance) {
		System.out.printf("Client %s disconnected. Ceasing operations!\n", instance.getName());
		System.exit(1);
	}
}
