package server;

@SuppressWarnings("serial")
public class ServerVoteEvent extends ServerEvent {

	private int userID;
	private int teamID;
	private int vote;

	public ServerVoteEvent(int userID, int teamID, int vote) {
		this.type = "SERVER_VOTE";
		this.userID = userID;
		this.teamID = teamID;
		this.vote = vote;
	}

	public int getTeamID() {
		return teamID;
	}

	public int getUserID() {
		return userID;
	}

	public int getVote() {
		return vote;
	}

}
