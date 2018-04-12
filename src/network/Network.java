package network;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import chat.ChatMessage;
import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.application.Platform;
import main.Main;
import quiz.util.ClientCreateEvent;
import server.ServerContext;
import server.ServerReturnUserIDEvent;
import user.model.User;

public class Network extends EventPublisher implements EventListener {

	// ConnectionID -> Connection
	private Map<Integer, Connection> connectionMap = new HashMap<Integer, Connection>();

	// UserID -> ConnectionID
	private Map<Integer, Integer> UserIDConnectionIDMap = new HashMap<Integer, Integer>();

	private ConnectionListener connectionListener;
	
	private InetAddress networkAddress;

	public Network() {
		// Empty constructor
	}

	// A factory method would be a better solution
	public Network(int serverPort) {
		// Server 2
		// Not safe when multi-threaded
		connectionListener = new ConnectionListener(this, serverPort);
		new Thread(connectionListener).start();
	}

	public Connection connect(InetAddress address, int port) {
		
		networkAddress = address;
		
		try {
			// Client 2.1
			Socket socket = new Socket(address, port);
			// Client 2.2
			Connection connection = new Connection(socket, this);
			// Client 2.3
			
			connection.receive();
			
			connectionMap.put(connection.getClientConnectionID(), connection);
			
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

		connectionMap.put(connection.getClientConnectionID(), connection);
		
		return connection;
	}



	@Override
	public void handleEvent(Event e) {
		connectionMap.get(0).send(e);
		switch(e.getType()) {
		case "CLIENT_CREATE":
			break;
		}
	}
	
	@Override
	public void handleEvent(Event e, ArrayList<Integer> destinations) {
		
		switch(e.getType()) {
			case "SERVER_CREATE":
				ClientCreateEvent serverCreate = (ClientCreateEvent) e;
				Connection connection = connectionMap.get(serverCreate.getConnectionID());
				connectionMap.remove(serverCreate.getConnectionID(), connection);
				
				int newServerUserConnectionID;
				do {
					newServerUserConnectionID = (int) (Math.random() * Integer.MAX_VALUE);
				} while(connectionMap.containsKey(newServerUserConnectionID));
				connection.setClientConnectionID(newServerUserConnectionID);
				connectionMap.put(newServerUserConnectionID, connection);
				
				int userID = ServerContext.getContext().addUser(serverCreate.getUsername(), serverCreate.getPassword());
				UserIDConnectionIDMap.put(userID, newServerUserConnectionID);
				ServerReturnUserIDEvent returnIDEvent = new ServerReturnUserIDEvent(userID);
				connectionMap.get(newServerUserConnectionID).send(returnIDEvent);
				break;
			
			case "CLIENT_CHAT":
				ChatMessage chatMessage = (ChatMessage) e;
				chatMessage.setType("SERVER_CHAT");
				for(Integer userId : destinations)
					connectionMap.get(UserIDConnectionIDMap.get(userId)).send(e);
				break;
		
		}
	}

	public void terminate() {
		if (connectionListener != null) {
			connectionListener.terminate();
		}

		System.out.println("Network closed");
		
		for(Map.Entry<Integer, Connection> entry : connectionMap.entrySet())
			entry.getValue().close();
	}

	public InetAddress getNetworkAddress() {
		return networkAddress;
	}
	
	public Map<Integer, Integer> getUserIDConnectionIDMap() {
		return UserIDConnectionIDMap;
	}

	
	public Map<Integer, Connection> getConnectionMap() {
		return connectionMap;
	}
}
