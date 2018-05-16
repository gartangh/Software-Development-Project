package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientVoteEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_VOTE";

	private int vote;

	// Constructor
	public ClientVoteEvent(int vote) {
		super.type = EVENTTYPE;
		this.vote = vote;
	}

	// Getter
	public int getVote() {
		return vote;
	}

}
