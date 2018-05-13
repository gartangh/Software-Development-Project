package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientChangeTeamEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_CHANGE_TEAM";

	private int quizID;
	private int newTeamID;
	private int oldTeamID;

	// Constructor
	public ClientChangeTeamEvent(int quizID, int newTeamID, int oldTeamID) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.newTeamID = newTeamID;
		this.oldTeamID = oldTeamID;
	}

	// Getters and setters
	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public int getNewTeamID() {
		return newTeamID;
	}

	public void setNewTeamID(int newTeamID) {
		this.newTeamID = newTeamID;
	}

	public int getOldTeamID() {
		return oldTeamID;
	}

	public void setOldTeamID(int oldTeamID) {
		this.oldTeamID = oldTeamID;
	}

}
