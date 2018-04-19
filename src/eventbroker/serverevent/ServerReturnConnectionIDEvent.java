package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerReturnConnectionIDEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_RETURN_CONNECTIONID";

	private int connectionID;

	// Constructor
	public ServerReturnConnectionIDEvent(int connectionID) {
		super.type = EVENTTYPE;
		this.connectionID = connectionID;
	}

	// Getter
	public int getConnectionID() {
		return this.connectionID;
	}
}
