package eventbroker.serverevent;

import quiz.util.RoundType;

@SuppressWarnings("serial")
public class ServerStartRoundEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_START_ROUND";
	
	private RoundType roundType;

	// Constructor
	public ServerStartRoundEvent(RoundType roundType) {
		super.type = EVENTTYPE;
		this.roundType = roundType;
	}
	
	public RoundType getRoundType() {
		return roundType;
	}

}
