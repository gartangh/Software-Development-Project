package server;

@SuppressWarnings("serial")
public class ServerVoteEvent extends ServerEvent {
    
	private int userID;
	private int teamID;
	private int quizID;
	private int vote;

	public ServerVoteEvent(int userID, int teamID, int quizID, int vote) {
		this.type = "SERVER_VOTE";
		this.userID = userID;
		this.teamID = teamID;
		this.quizID = quizID;
		this.vote = vote;
	}

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
