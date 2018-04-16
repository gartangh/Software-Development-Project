package server;

@SuppressWarnings("serial")
public class ServerNewRoundEvent extends ServerEvent {
	private int roundNumber;

	public ServerNewRoundEvent(int roundNumber) {
		super();
		this.roundNumber = roundNumber;
		this.type = "SERVER_NEW_ROUND";
	}
	
	public int getRoundNumber() {
		return roundNumber;
	}
}
