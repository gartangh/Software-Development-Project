package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientDeleteTeamEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_DELETE_TEAM";

	private int teamID;
	private int quizID;

	public ClientDeleteTeamEvent(int teamID,int quizID) {
		super.type=EVENTTYPE;
		this.teamID = teamID;
		this.quizID = quizID;
	}

	public int getTeamID() {
		return teamID;
	}

	public int getQuizID(){
		return quizID;
	}

}
