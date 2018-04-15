package server;

@SuppressWarnings("serial")
public class ServerVoteEvent extends ServerEvent {

	String username;
	String teamname;
	int vote;

	public ServerVoteEvent(String username, String teamname, int vote) {
		this.type = "SERVER_VOTE";
		this.username = username;
		this.teamname = teamname;
		this.vote = vote;
	}

	public String getTeamname() {
		return teamname;
	}

	public String getUsername() {
		return username;
	}

	public int getVote() {
		return vote;
	}

}
