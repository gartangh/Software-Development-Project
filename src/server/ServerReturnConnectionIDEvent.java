package server;

@SuppressWarnings("serial")
public class ServerReturnConnectionIDEvent extends ServerEvent{
	
	private int connectionID;
	
	public ServerReturnConnectionIDEvent(int connectionID) {
		super();
		this.connectionID = connectionID;
		this.type = "SERVER_RETURN_CONNECTIONID";
	}
	
	public int getConnectionID() {
		return this.connectionID;
	}
}
