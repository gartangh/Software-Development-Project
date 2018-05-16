package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerVoteEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_VOTE";

	private int userID;
	private int teamID;
	private int quizID;
	private int vote;

	// Constructor
	public ServerVoteEvent(int userID, int teamID, int quizID, int vote) {
		super.type = EVENTTYPE;
		this.userID = userID;
		this.teamID = teamID;
		this.quizID = quizID;
		this.vote = vote;
	}

	// Getters
	public int getTeamID() {
		return teamID;
	}

	public int getUserID() {
		return userID;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getVote() {
		return vote;
	}

}
