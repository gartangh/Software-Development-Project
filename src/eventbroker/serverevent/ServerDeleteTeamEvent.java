package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerDeleteTeamEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_DELETE_TEAM";

	private int teamID;

	// constructor
	public ServerDeleteTeamEvent(int teamID) {
		super.type = EVENTTYPE;
		this.teamID = teamID;
	}

	// Getter
	public int getTeamID() {
		return teamID;
	}

}
