package classes;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.File;
import java.io.IOException;

public class TimeTrackerServer implements Runnable {

	private int serverPort = 4455;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;

	public TimeTrackerServer(int port) {
		createSaveFolder();
		serverPort = port;
	}

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection",
						e);
			}
			new Thread(new WorkerRunnable(clientSocket, "Multithreaded Server"))
					.start();
		}
		System.out.println("Server Stopped.");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(serverPort);
			System.out.println("Server listening on port: " + serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port 8080", e);
		}
	}

	private void createSaveFolder() {
		String desktopDirectory = System.getProperty("user.home") + "/Desktop";
		String saveFolder = desktopDirectory + "/TimeTrackerLogs";
		File saveDir = new File(saveFolder);

		// if the directory does not exist, create it
		if (!saveDir.exists()) {
			System.out.println("creating directory: " + saveFolder);
			boolean result = false;

			try {
				saveDir.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}
		}
	}

}