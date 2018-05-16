package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerCreateAccountFailEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_ACCOUNT_FAIL";

	private int connectionID;

	// Constructor
	public ServerCreateAccountFailEvent(int connectionID) {
		super.type = EVENTTYPE;
		this.connectionID = connectionID;
	}

	// Getter
	public int getConnectionID() {
		return connectionID;
	}

}
