package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerLogInFailEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_LOG_IN_FAIL";

	private int connectionID;

	// Constructor
	public ServerLogInFailEvent(int connectionID) {
		super.type = EVENTTYPE;
		this.connectionID = connectionID;
	}

	// Getter
	public int getConnectionID() {
		return connectionID;
	}

}
