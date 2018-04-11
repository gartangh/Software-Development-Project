package server;

@SuppressWarnings("serial")
public class ServerVoteEvent extends ServerEvent {
	
	protected int userID, teamID, quizID, vote;

	public ServerVoteEvent(int userID, int teamID, int quizID, int vote) {
		super();
		this.userID = userID;
		this.teamID = teamID;
		this.quizID = quizID;
		this.vote = vote;
		this.type = "SERVER_VOTE";
		this.message = "";
	}

	public int getVote() {
		return vote;
	}
	
	public int getQuizID() {
		return quizID;
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	public int getUserID() {
		return userID;
	}
}
