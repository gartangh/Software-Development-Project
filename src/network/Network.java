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
import eventbroker.serverevent.ServerAlreadyLoggedInEvent;
import eventbroker.serverevent.ServerCreateAccountFailEvent;
import eventbroker.serverevent.ServerLogInFailEvent;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import main.Main;

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
	
	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}
	
	public Main getMainApp() {
		return main;
	}

	// Constructors
	public Network() {
		// Empty default constructor
	}

	// A factory method would be a better solution
	public Network(int serverPort, String type) {
		// Not safe when multi-threaded
		this.type = type;
		this.connectionListener = new ConnectionListener(this, serverPort);
		new Thread(connectionListener).start();
	}

	// Getters
	public String getType() {
		return type;
	}
	
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
			Socket socket = new Socket(address, port);
			Connection connection = new Connection(socket, this);
			connection.receive();

			if (type.equals(CLIENTTYPE)) {
				// Client always has connectionID 0
				connection.setConnectionID(0);
				connectionMap.put(0, connection);
			} else if (type.equals(SERVERTYPE)) {
				int connectionID;
				do
					connectionID = (int) (Math.random() * Integer.MAX_VALUE);
				while (connectionMap.containsKey(connectionID));

				connection.setConnectionID(connectionID);
				connectionMap.put(connectionID, connection);
			}
		} catch (IOException e) {
			System.err.println("IOException");
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Could not connect to network!");
					alert.setContentText("Please restart the app and try again.");
					alert.showAndWait();
					
					// Exit the app
					System.exit(0);
				}
			});
		}
	}

	// Network
	public void connect(String address, int port) {
		try {
			// Parsing from String to InetAddress
			networkAddress = InetAddress.getByName(address);

			Socket socket = new Socket(address, port);
			Connection connection = new Connection(socket, this);
			connection.receive();

			if (type.equals(CLIENTTYPE)) {
				// Client always has connectionID 0
				connection.setConnectionID(0);
				connectionMap.put(0, connection);
			} else if (type.equals(SERVERTYPE)) {
				int connectionID;
				do
					connectionID = (int) (Math.random() * Integer.MAX_VALUE);
				while (connectionMap.containsKey(connectionID));

				connection.setConnectionID(connectionID);
				connectionMap.put(connectionID, connection);
			}
		} catch (UnknownHostException e) {
			System.err.println("UnknownHostException");

			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Could not connect to network!");
					alert.setContentText("We could not find the host. Please restart the app and try again.");
					alert.showAndWait();
					
					// Exit the app
					System.exit(0);
				}
			});
		} catch (IOException e) {
			System.err.println("IOException");
			
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Could not connect to network!");
					alert.setContentText("Please restart the app and try again.");
					alert.showAndWait();
					
					// Exit the app
					System.exit(0);
				}
			});
		}
	}

	// Package local would be safer
	public Connection connect(Socket socket) {
		Connection connection = new Connection(socket, this);
		connection.receive();

		if (type == "CLIENT")
			connectionMap.put(connection.getConnectionID(), connection);
		else if (type == "SERVER") {
			// Generate random connectionID
			int connectionID;
			do
				connectionID = (int) (Math.random() * Integer.MAX_VALUE);
			while (connectionMap.containsKey(connectionID));

			connection.setConnectionID(connectionID);
			connectionMap.put(connectionID, connection);
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

				for (Connection connection : connectionMap.values())
					if (connection.getConnectionID() == connectionID) {
						connection.send(event);
						break;
					}
			} else if (event.getType().equals(ServerLogInFailEvent.EVENTTYPE)) {
				ServerLogInFailEvent sLIFE = (ServerLogInFailEvent) event;

				int connectionID = sLIFE.getConnectionID();

				for (Connection connection : connectionMap.values())
					if (connection.getConnectionID() == connectionID) {
						connection.send(event);
						break;
					}
			} else if (event.getType().equals(ServerAlreadyLoggedInEvent.EVENTTYPE)) {
				ServerAlreadyLoggedInEvent sALIE = (ServerAlreadyLoggedInEvent) event;
				
				int connectionID = sALIE.getConnectionID();
				
				for (Connection connection : connectionMap.values())
					if (connection.getConnectionID() == connectionID) {
						connection.send(event);
						break;
					}
			} else
				for (int userID : event.getRecipients())
					if (UserIDConnectionIDMap.containsKey(userID) && connectionMap.containsKey(UserIDConnectionIDMap.get(userID)))
						connectionMap.get(UserIDConnectionIDMap.get(userID)).send(event);
		}
	}

	public void terminate() {
		if (connectionListener != null)
			connectionListener.terminate();

		for (Connection connection : connectionMap.values())
			connection.close();
		
		System.out.println("Network closed");
	}

}
