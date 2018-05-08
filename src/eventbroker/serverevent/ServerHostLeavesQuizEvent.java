package eventbroker.serverevent;

public class ServerHostLeavesQuizEvent extends ServerEvent {
	public final static String EVENTTYPE = "SERVER_HOST_LEAVES_QUIZ";
	private int quizID;

	public ServerHostLeavesQuizEvent(int quizID) {
		super.type=EVENTTYPE;
		this.quizID=quizID;
	}

	public int getQuizID() {
		return quizID;
	}

}
