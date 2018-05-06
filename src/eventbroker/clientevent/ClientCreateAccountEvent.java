package eventbroker.clientevent;

import eventbroker.Event;

// This event does not extends a ClientEvent, because the userID is not known yet
@SuppressWarnings("serial")
public class ClientCreateAccountEvent extends Event {

	public final static String EVENTTYPE = "CLIENT_CREATE_ACCOUNT";

	private String userName;
	private String userPassword;
	private int connectionID;

	// Constructor
	public ClientCreateAccountEvent(String userName, String userPassword) {
		super.type = EVENTTYPE;
		this.userName = userName;
		this.userPassword = userPassword;
	}

	// Getters and setters
	public String getUsername() {
		return userName;
	}

	public String getPassword() {
		return userPassword;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
