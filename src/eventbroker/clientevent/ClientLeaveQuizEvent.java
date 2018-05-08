package eventbroker.clientevent;

public class ClientLeaveQuizEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_LEAVE_QUIZ";

	private int userID;
	private int quizID;
	private int teamID;

	public ClientLeaveQuizEvent(int userID, int quizID, int teamID) {
		super.type=EVENTTYPE;
		this.teamID = teamID;
		this.quizID = quizID;
		this.userID = userID;
	}

	public int getTeamID() {
		return teamID;
	}

	public int getQuizID(){
		return quizID;
	}

	public int getUserID() {
		return userID;
	}

}
