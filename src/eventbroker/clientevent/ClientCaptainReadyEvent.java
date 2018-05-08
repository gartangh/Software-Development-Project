package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientCaptainReadyEvent extends ClientHostReadyEvent {

	public final static String EVENTTYPE = "CLIENT_CAPTAIN_READY";

	private int teamID;

	// Constructor
	public ClientCaptainReadyEvent(int quizID, int teamID) {
		super(quizID);
		super.type = EVENTTYPE;
		this.teamID = teamID;
	}

	public int getTeamID() {
		return teamID;
	}

}
