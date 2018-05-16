package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

// Listens for incoming connections
public class ConnectionListener implements Runnable {

	private Network network;
	private int serverPort;

	private boolean stop = false;

	ServerSocket listen;

	public ConnectionListener(Network network, int serverPort) {
		this.network = network;
		this.serverPort = serverPort;
	}
	
	public int getServerPort() {
		return serverPort;
	}

	@Override
	public void run() {
		try {
			listen = new ServerSocket(serverPort);
			while (!stop) {
				Socket client = listen.accept();
				network.connect(client);
			}
		} catch (SocketException e) {
			//e.printStackTrace();
			System.err.println("SocketException");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (listen != null)
				try {
					listen.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		System.out.println("ConnectionListener terminated");
	}

	public void terminate() {
		try {
			listen.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
