package edu.atilim.acma.concurrent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketInstance implements Instance, Runnable {
	private Socket socket;
	private LinkedBlockingQueue<Serializable> receiveBuffer;
	private InstanceListener instanceListener;
	private ConnectionListener connectionListener;
	private Thread readThread;
	private UUID id;
	
	private ByteArrayOutputStream buffer;
	private ObjectOutputStream out;
	
	public static SocketInstance tryConnect(String hostname, int port) {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(hostname, port), 1000);
			return new SocketInstance(socket);
		} catch (Exception e) {}
		return null;
	}
	
	public static Listener tryListen(final int port, final ConnectionListener cl) {
		final Listener listener = new Listener();
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ServerSocket socket = new ServerSocket(port);
					socket.setSoTimeout(1000);
					
					while (listener.isRunning()) {
						try {
							Socket client = socket.accept();
							SocketInstance si = new SocketInstance(client);
							si.setConnectionListener(cl);
							cl.onConnect(si);
						} catch (SocketTimeoutException e) {}
					}
					
					socket.close();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		thread.start();
		
		return listener;
	}
	
	@Override
	public void setInstanceListener(InstanceListener instanceListener) {
		this.instanceListener = instanceListener;
	}
	
	@Override
	public void setConnectionListener(ConnectionListener connectionListener) {
		this.connectionListener = connectionListener;
	}

	@Override
	public String getName() {
		return socket.getRemoteSocketAddress().toString();
	}

	public int available() {
		return receiveBuffer.size();
	}
	
	public SocketInstance(Socket socket) {
		this.socket = socket;
		this.receiveBuffer = new LinkedBlockingQueue<Serializable>();
		this.id = UUID.randomUUID();
		
		this.buffer = new ByteArrayOutputStream(2048);
		try {
			this.out = new ObjectOutputStream(buffer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		readThread = new Thread(this);
		readThread.start();
	}
	
	@Override
	public void send(Serializable message) {
		try {
			out.writeObject(message);
			out.flush();
		} catch (IOException e) { }
		
		try {
			buffer.writeTo(socket.getOutputStream());
			buffer.reset();
		} catch (IOException e) {
			dispose();
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T receive(Class<T> cls) {
		try {
			Serializable item = receiveBuffer.take();
			return (T)item;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void run() {
		try {
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			while (true) {
				receiveBuffer.put((Serializable)in.readObject());
				onReceived();
			}
		} catch (IOException e) {
			dispose();
		} catch (InterruptedException e) {
			dispose();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void onReceived() {
		if (instanceListener != null)
			instanceListener.onReceive(this);
	}
	
	public void dispose() {
		try { socket.close(); } catch (Exception e) { }
		receiveBuffer.clear();
		
		if (connectionListener != null)
			connectionListener.onDisconnect(this);
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
	public static class Listener {
		private boolean running;
		
		public boolean isRunning() {
			return running;
		}

		private Listener() {
			this.running = true;
		}

		public void stop() {
			running = false;
		}
	}
}
