package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerNewRoundEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_ROUND";

	private int roundNumber;

	// Constructor
	public ServerNewRoundEvent(int roundNumber) {
		super.type = EVENTTYPE;
		this.roundNumber = roundNumber;
	}

	// Getter
	public int getRoundNumber() {
		return roundNumber;
	}

}
