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

	@Override
	public void run() {
		try {
			// Server 3
			listen = new ServerSocket(serverPort);
			while (!stop) {
				// Server 4.1
				Socket client = listen.accept();
				// Server 4.2
				network.connect(client);
			}
		} catch (SocketException e) {
			//e.printStackTrace();
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
