package quiz.util;

@SuppressWarnings("serial")
public class ClientVoteEvent extends QuizzerEvent {

	protected int vote;

	public ClientVoteEvent(int vote) {
		this.vote = vote;
		this.type = "CLIENT_VOTE";
	}

	public int getVote() {
		return vote;
	}

}
