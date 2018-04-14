package quiz.util;

import java.io.Serializable;

import user.model.User;

@SuppressWarnings("serial")
public class ClientCreateEvent extends UserEvent implements Serializable {
	
	private String username;
	private String password;
	private int connectionID;

	public ClientCreateEvent(User user) {
		super();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.type = "CLIENT_CREATE";
	}

	public int getConnectionID() {
		return connectionID;
	}
	
	public void setConnectionID(int connectionID) {
		this.connectionID = connectionID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
