package quiz.util;

import java.io.Serializable;

import user.model.User;

@SuppressWarnings("serial")
public class ClientCreateAccountEvent extends UserEvent implements Serializable {

	private User user;
	private int connectionID;

	// Constructor
	public ClientCreateAccountEvent(User user) {
		this.user = user;
		this.type = "CLIENT_CREATE_ACCOUNT";
	}

	// Getters and setters
	public User getUser() {
		return user;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
