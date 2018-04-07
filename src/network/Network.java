package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;

public class Network extends EventPublisher implements EventListener {

	private Connection connection;
	private boolean isConnected = false;
	
	private ConnectionListener connectionListener;

	public Network() {
		// Empty constructor
	}

	// A factory method would be a better solution
	public Network(int serverPort) {
		// Server 2
		// Not safe when multi-threated
		connectionListener = new ConnectionListener(this, serverPort);
		new Thread(connectionListener).start();
	}

	public Connection connect(InetAddress address, int port) {
		try {
			// Client 2.1
			Socket socket = new Socket(address, port);
			// Client 2.2
			connection = new Connection(socket, this);
			// Client 2.3
			connection.receive();

			isConnected = true;
			
			return connection;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Package local would be safer
	public Connection connect(Socket socket) {
		// Server 4.2.1
		connection = new Connection(socket, this);
		// Server 4.2.2
		connection.receive();

		isConnected = true;
		return connection;
	}

	@Override
	public void handleEvent(Event event) {
		// Client 7
		connection.send(event);
	}

	public void terminate() {
		if (connectionListener != null) {
			connectionListener.terminate();
		}

		System.out.println("Network closed");
		connection.close();
	}

	public boolean isConnected() {
		return isConnected;
	}
}
