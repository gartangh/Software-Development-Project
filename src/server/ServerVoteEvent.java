package server;

@SuppressWarnings("serial")
public class ServerVoteEvent extends ServerEvent {
	
	protected int newVote, prevVote;
	protected boolean update; // To know if the voter already voted before

	public ServerVoteEvent(int newVote, boolean update, int prevVote) {
		super();
		this.newVote = newVote;
		this.update = update;
		this.prevVote = prevVote;
		this.type = "SERVER_VOTE";
	}

	public int getNewVote() {
		return newVote;
	}

	public int getPrevVote() {
		return prevVote;
	}

	public boolean isUpdate() {
		return update;
	}
}
