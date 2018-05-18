package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientPollAnswerEvent extends ClientEvent {
	public final static String EVENTTYPE = "CLIENT_POLL_ANSWER";

	private int pollID;

	// Constructor
	public ClientPollAnswerEvent(int pollID) {
		super.type = EVENTTYPE;
		this.pollID = pollID;
	}

	// Getters
	public int getPollID() {
		return this.pollID;
	}
}
