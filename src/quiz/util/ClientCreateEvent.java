package quiz.util;

import network.Connection;

@SuppressWarnings("serial")

public class ClientCreateEvent extends UserEvent {

	private Connection connection;
	
	public ClientCreateEvent(Connection connection) {
		super();
		this.connection = connection;
		this.type = "CLIENT_CREATE";
	}

	public Connection getConnection() {
		return connection;
	}
}
