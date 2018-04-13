package server;

@SuppressWarnings("serial")
public class ServerReturnUserIDEvent extends ServerEvent{
	
	private int userID;
	private int connectionID;
	
	public ServerReturnUserIDEvent(int userID, int connectionID) {
		super();
		this.userID = userID;
		this.connectionID = connectionID;
		this.type = "SERVER_RETURN_USERID";
	}
	
	public int getUserID() {
		return this.userID;
	}

	public int getConnectionID() {
		return connectionID;
	}
}
