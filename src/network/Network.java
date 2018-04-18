package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;

// TODO: End ReceiverThread Connection Chat
public class Network extends EventPublisher implements EventListener {

	private String TYPE;
	// Map(connectionID -> Connection)
	private Map<Integer, Connection> connectionMap = new HashMap<>();
	// Map(userID -> ConnectionID)
	private Map<Integer, Integer> UserIDConnectionIDMap = new HashMap<>();
	private ConnectionListener connectionListener;
	private InetAddress networkAddress;

	public Network() {
		// Empty default constructor
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
	public Map<Integer, Integer> getUserIDConnectionIDMap() {
		return UserIDConnectionIDMap;
	}

	public Map<Integer, Connection> getConnectionMap() {
		return connectionMap;
	}

	public ConnectionListener getConnectionListener() {
		return connectionListener;
	}

	public InetAddress getNetworkAddress() {
		return networkAddress;
	}

	// Methods
	// Local
	public void connect(InetAddress address, int port) {
		networkAddress = address;

		try {
			// Client 2.1
			Socket socket = new Socket(address, port);
			// Client 2.2
			Connection connection = new Connection(socket, this);
			// Client 2.3

			connection.receive();

			if (TYPE == "CLIENT") {
				// Client always has connectionID 0
				connection.setConnectionID(0);
				connectionMap.put(0, connection);
			} else if (TYPE == "SERVER") {
				int newServerUserConnectionID;
				do {
					newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
				} while (connectionMap.containsKey(newServerUserConnectionID));

				connection.setConnectionID(newServerUserConnectionID);
				connectionMap.put(newServerUserConnectionID, connection);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Network
	public void connect(String address, int port) {
		try {
			// Parsing from String to InetAddress
			networkAddress = InetAddress.getByName(address);

			// Client 2.1
			Socket socket = new Socket(address, port);
			// Client 2.2
			Connection connection = new Connection(socket, this);
			// Client 2.3

			connection.receive();

			if (TYPE == "CLIENT") {
				// Client always has connectionID 0
				connection.setConnectionID(0);
				connectionMap.put(0, connection);
			} else if (TYPE == "SERVER") {
				int newServerUserConnectionID;
				do {
					newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
				} while (connectionMap.containsKey(newServerUserConnectionID));

				connection.setConnectionID(newServerUserConnectionID);
				connectionMap.put(newServerUserConnectionID, connection);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
			for (int userID : e.getRecipients())
				connectionMap.get(UserIDConnectionIDMap.get(userID)).send(e);
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
