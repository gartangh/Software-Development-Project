package server;

public class ServerReturnUserIDEvent extends ServerEvent{
	
	private int userID;
	
	public ServerReturnUserIDEvent(int userID) {
		super();
		this.userID = userID;
		this.type = "SERVER_RETURN_USERID";
	}
	
	public int getUserID() {
		return this.userID;
	}
}
