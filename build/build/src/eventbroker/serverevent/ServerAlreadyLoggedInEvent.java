package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerAlreadyLoggedInEvent extends ServerEvent {
	
	public final static String EVENTTYPE = "SERVER_ALREADY_LOGGED_IN";

	private int connectionID;

	// Constructor
	public ServerAlreadyLoggedInEvent(int connectionID) {
		super.type = EVENTTYPE;
		this.connectionID = connectionID;
	}

	// Getter
	public int getConnectionID() {
		return connectionID;
	}

}
