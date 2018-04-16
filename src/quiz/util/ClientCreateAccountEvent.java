package quiz.util;

import java.io.Serializable;

import user.model.User;

@SuppressWarnings("serial")
public class ClientCreateAccountEvent extends UserEvent implements Serializable {

	private String userName;
	private String userPassword;
	private int connectionID;

	// Constructor
	public ClientCreateAccountEvent(String userName, String userPassword) {
		this.userName = userName;
		this.userPassword = userPassword;
		this.type = "CLIENT_CREATE_ACCOUNT";
	}
	
	public ClientCreateAccountEvent(User u) { // TODO: Has to be deleted, calls need to be changed
	}

	// Getters and setters
	public String getUserName() {
		return userName;
	}
	
	public String getUserPassword() {
		return userPassword;
	}

	public int getConnectionID() {
		return connectionID;
	}

	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

}
