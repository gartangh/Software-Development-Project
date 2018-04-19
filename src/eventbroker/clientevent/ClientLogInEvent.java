package eventbroker.clientevent;

import java.io.Serializable;

import eventbroker.Event;

// This event does not extends a UserEvent, because the userID is not known yet
@SuppressWarnings("serial")
public class ClientLogInEvent extends Event implements Serializable {

	public final static String EVENTTYPE = "CLIENT_LOG_IN";

	private String username;
	private String password;
	private int connectionID;

	// Constructor
	public ClientLogInEvent(String username, String password) {
		super.type = EVENTTYPE;
		this.username = username;
		this.password = password;
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
