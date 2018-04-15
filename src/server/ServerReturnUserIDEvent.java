package server;

@SuppressWarnings("serial")
public class ServerReturnUserIDEvent extends ServerEvent {

	private String username;
	private int connectionID;

	public ServerReturnUserIDEvent(String username, int connectionID) {
		super();
		this.username = username;
		this.connectionID = connectionID;
		this.type = "SERVER_RETURN_USERID";
	}

	public String getUsername() {
		return this.username;
	}

	public int getConnectionID() {
		return connectionID;
	}
}
