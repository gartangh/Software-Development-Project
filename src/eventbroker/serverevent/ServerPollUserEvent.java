package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerPollUserEvent extends ServerEvent {
	public final static String EVENTTYPE = "SERVER_POLL_USER";

	private int pollID;

	// Constructor
	public ServerPollUserEvent(int pollID) {
		super.type = EVENTTYPE;
		this.pollID = pollID;
	}

	// Getters
	public int getPollID() {
		return this.pollID;
	}
}
