package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerCreateTeamFailEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_TEAM_FAIL";

	// Constructor
	public ServerCreateTeamFailEvent() {
		super.type = EVENTTYPE;
	}
}
