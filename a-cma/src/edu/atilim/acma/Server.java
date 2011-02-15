package edu.atilim.acma;

import edu.atilim.acma.concurrent.ConcurrentTask;
import edu.atilim.acma.concurrent.ConnectionListener;
import edu.atilim.acma.concurrent.Instance;
import edu.atilim.acma.concurrent.InstanceSet;
import edu.atilim.acma.concurrent.SocketInstance;
import edu.atilim.acma.concurrent.TaskInterruptedException;
import edu.atilim.acma.concurrent.TerminateTask;
import edu.atilim.acma.concurrent.SocketInstance.Listener;

public class Server implements Runnable, ConnectionListener {
	private int port;
	private int autostart;
	
	private InstanceSet instances;
	private ConcurrentTask currentTask = null;
	private Listener listener = null;
	private volatile boolean running = false;
	
	public Server(int port) {
		this.port = port;
		this.instances = new InstanceSet();
		this.autostart = -1;
	}
	
	@Override
	public void run() {
		boolean first = true;
		while(true) {
			try {
				Server server = new Server(port);
				if (!first)
					server.autostart = 30;
				first = false;
				
				server.runInternal();
				server.dispose();
				
				try { Thread.sleep(5000); } catch (InterruptedException e) { }
			} catch (TaskInterruptedException e) {
			}
		}
	}

	private void runInternal() {
		System.out.printf("[Server] Listening port %d for incoming connections.\n", port);
		
		listener = SocketInstance.tryListen(port, this);
		
		if (listener == null) {
			System.out.printf("[Server] Can not listen port %d\n", port);
			return;
		}
		
		if (autostart <= 0)
			System.out.println("[Server] When ready, please type start to begin operations. After that point, no more clients will be accepted.");
		
		if (autostart > 0) {
			System.out.printf("[Server] Operations will start after %d seconds of waiting for client connections.\n", autostart);
			long startTime = System.currentTimeMillis();
			while (true) {
				long curTime = System.currentTimeMillis();
				
				if ((curTime - startTime) > (autostart * 1000)) {
					if (instances.size() > 0)
						break;
					
					startTime = curTime;
				}
				
				try { Thread.sleep(100); } catch (InterruptedException e) { }
			}
		}
		else
		{
			while (true) {
				if (Console.readLine().equalsIgnoreCase("start"))
				{
					if (instances.size() == 0) {
						System.out.println("[Server] There are no client instances available.");
						continue;
					}
					break;
				}
				System.out.println("[Server] Please enter 'start' to begin operations.");
			}
		}
		
		listener.stop();
		
		running = true;
		System.out.printf("[Server] There are %d tasks to be executed in queue.\n", TaskQueue.size());
		
		while (true) {
			currentTask = TaskQueue.peek();
			
			if (currentTask == null) {
				System.out.println("[Server] All tasks completed. Exiting.");
				run(new TerminateTask());
			}
			
			currentTask.clearInterrupt();
			
			System.out.printf("[Server] Running algorithm task %s, Remaining: %d.\n", currentTask, TaskQueue.size() - 1);
			
			run(currentTask);
			
			TaskQueue.remove(currentTask);
			
			currentTask = null;
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
		if (running) {
			System.out.printf("[Server] Client %s disconnected. Ceasing operations!\n", instance.getName());
			dispose();
		} else {
			instances.remove(instance);
			System.out.printf("[Server] Client disconnected. Total: %d\n", instances.size());
		}
	}
	
	private volatile boolean disposed = false;
	
	public synchronized void dispose() {
		if (!disposed) {
			disposed = true;
			
			System.out.println("[Server] Disposing server.");
			try { instances.dispose(); } catch (Exception e) { }
			try { currentTask.interrupt(); } catch (Exception e) { }
			try { listener.stop(); } catch (Exception e) { }
		}
	}
}
