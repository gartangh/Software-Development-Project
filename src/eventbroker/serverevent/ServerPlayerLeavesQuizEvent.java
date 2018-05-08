package eventbroker.serverevent;

public class ServerPlayerLeavesQuizEvent extends ServerEvent {
	public final static String EVENTTYPE = "SERVER_PLAYER_LEAVES_QUIZ";
	private int quizID;
	private int userID;
	private int teamID;
	private int newCaptainID;

	public ServerPlayerLeavesQuizEvent(int quizID,int userID,int teamID,int newCaptainID) {
		super.type=EVENTTYPE;
		this.quizID=quizID;
		this.userID=userID;
		this.teamID=teamID;
		this.newCaptainID=newCaptainID;
	}

	public int getNewCaptainID() {
		return newCaptainID;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getUserID() {
		return userID;
	}

	public int getTeamID() {
		return teamID;
	}

}

