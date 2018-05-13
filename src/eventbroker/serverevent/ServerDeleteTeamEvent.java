package eventbroker.serverevent;

public class ServerDeleteTeamEvent extends ServerEvent {
	public final static String EVENTTYPE = "SERVER_DELETE_TEAM";
	private int teamID;

	public ServerDeleteTeamEvent(int teamID) {
		super.type=EVENTTYPE;
		this.teamID = teamID;
	}

	public int getTeamID() {
		return teamID;
	}


}
