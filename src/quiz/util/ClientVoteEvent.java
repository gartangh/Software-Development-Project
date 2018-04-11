package quiz.util;

@SuppressWarnings("serial")
public class ClientVoteEvent extends QuizzerEvent{
	
	protected int vote;
	
	public ClientVoteEvent(int vote) {
		super();
		this.vote = vote;
		this.type = "CLIENT_VOTE";
		this.message = "";
	}
	
	public int getVote() {
		return vote;
	}
}
