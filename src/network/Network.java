package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;

public class Network extends EventPublisher implements EventListener {

	private String TYPE;
	// Map(connectionID -> Connection)
	private Map<Integer, Connection> connectionMap = new HashMap<>();
	// Map(username -> ConnectionID)
	private Map<String, Integer> UserIDConnectionIDMap = new HashMap<>();
	private ConnectionListener connectionListener;
	private InetAddress networkAddress;

	// Constructors
	public Network() {
		// Empty default constrcutor
	}

	// A factory method would be a better solution
	public Network(int serverPort, String type) {
		// Server 2
		// Not safe when multi-threaded
		TYPE = type;
		connectionListener = new ConnectionListener(this, serverPort);
		new Thread(connectionListener).start();
	}

	// Getters
	public Map<String, Integer> getUserIDConnectionIDMap() {
		return UserIDConnectionIDMap;
	}

	public Map<Integer, Connection> getConnectionMap() {
		return connectionMap;
	}

	public InetAddress getNetworkAddress() {
		return networkAddress;
	}

	// Methods
	public Connection connect(InetAddress address, int port) {
		networkAddress = address;

		try {
			// Client 2.1
			Socket socket = new Socket(address, port);
			// Client 2.2
			Connection connection = new Connection(socket, this);
			// Client 2.3

			connection.receive();

			if (TYPE == "CLIENT")
				connectionMap.put(0, connection);
			else if (TYPE == "SERVER") {
				int newServerUserConnectionID;
				do {
					newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
				} while (connectionMap.containsKey(newServerUserConnectionID));
				connection.setConnectionID(newServerUserConnectionID);
				connectionMap.put(newServerUserConnectionID, connection);
			}

			return connection;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	// Package local would be safer
	public Connection connect(Socket socket) {
		// Server 4.2.1
		Connection connection = new Connection(socket, this);
		// Server 4.2.2
		connection.receive();

		if (TYPE == "CLIENT") {
			connectionMap.put(connection.getConnectionID(), connection);
		} else if (TYPE == "SERVER") {
			int newServerUserConnectionID;
			do {
				newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
			} while (connectionMap.containsKey(newServerUserConnectionID));
			connection.setConnectionID(newServerUserConnectionID);
			connectionMap.put(newServerUserConnectionID, connection);
		}

		return connection;
	}

	/*
	 * Network handler: client only has 1 connection in connectionMap with ID ==
	 * 0 (the server) Server needs recipients inside (serverEvent) event
	 */
	@Override
	public void handleEvent(Event e) {
		if (TYPE == "CLIENT") {
			connectionMap.get(0).send(e);
		} else if (TYPE == "SERVER") {
			for (String username : e.getRecipients()) {
				connectionMap.get(UserIDConnectionIDMap.get(username)).send(e);
			}
		}
		System.out.println("Event received and handled: " + e.getType());
	}

	public void terminate() {
		if (connectionListener != null)
			connectionListener.terminate();

		System.out.println("Network closed");

		for (Map.Entry<Integer, Connection> entry : connectionMap.entrySet())
			entry.getValue().close();
	}
}
