package quiz.util;

import network.Connection;
import user.model.User;

@SuppressWarnings("serial")

public class ClientCreateEvent extends UserEvent {
	
	private String username;
	private String password;
	private Connection connection;
	
	public ClientCreateEvent(User user, Connection connection) {
		super();
		this.connection = connection;
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.type = "CLIENT_CREATE";
	}

	public Connection getConnection() {
		return connection;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
