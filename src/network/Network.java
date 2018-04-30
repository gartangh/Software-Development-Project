package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerLogInFailEvent;

// TODO: End ReceiverThread Connection Chat
public class Network extends EventPublisher implements EventListener {

	public final static String CLIENTTYPE = "CLIENT";
	public final static String SERVERTYPE = "SERVER";

	private String type;
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
		this.type = type;
		this.connectionListener = new ConnectionListener(this, serverPort);
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

			if (type.equals(CLIENTTYPE)) {
				// Client always has connectionID 0
				connection.setConnectionID(0);
				connectionMap.put(0, connection);
			} else if (type.equals(SERVERTYPE)) {
				int newServerUserConnectionID;
				do
					newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
				while (connectionMap.containsKey(newServerUserConnectionID));

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

			if (type.equals(CLIENTTYPE)) {
				// Client always has connectionID 0
				connection.setConnectionID(0);
				connectionMap.put(0, connection);
			} else if (type.equals(SERVERTYPE)) {
				int newServerUserConnectionID;
				do
					newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
				while (connectionMap.containsKey(newServerUserConnectionID));

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

		if (type == "CLIENT") {
			connectionMap.put(connection.getConnectionID(), connection);
		} else if (type == "SERVER") {
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
	public void handleEvent(Event event) {
		if (type.equals(CLIENTTYPE))
			connectionMap.get(0).send(event);
		else if (type.equals(SERVERTYPE)) {
			if (event.getType().equals(ServerCreateAccountFailEvent.EVENTTYPE)) {
				ServerCreateAccountFailEvent sCAFE = (ServerCreateAccountFailEvent) event;

				int connectionID = sCAFE.getConnectionID();

				for (Entry<Integer, Connection> connection : connectionMap.entrySet())
					if (connection.getValue().getConnectionID() == connectionID) {
						connection.getValue().send(event);
						break;
					}
			} else if (event.getType().equals(ServerLogInFailEvent.EVENTTYPE)) {
				ServerLogInFailEvent sLIFE = (ServerLogInFailEvent) event;

				int connectionID = sLIFE.getConnectionID();

				for (Entry<Integer, Connection> connection : connectionMap.entrySet())
					if (connection.getValue().getConnectionID() == connectionID) {
						connection.getValue().send(event);
						break;
					}
			} else
				for (int userID : event.getRecipients())
					connectionMap.get(UserIDConnectionIDMap.get(userID)).send(event);
		}
	}

	public void terminate() {
		if (connectionListener != null)
			connectionListener.terminate();

		System.out.println("Network closed");

		for (Map.Entry<Integer, Connection> entry : connectionMap.entrySet())
			entry.getValue().close();
	}

}
