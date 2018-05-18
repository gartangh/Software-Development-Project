package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerHostLeavesQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_HOST_LEAVES_QUIZ";

	private int quizID;

	// Constructor
	public ServerHostLeavesQuizEvent(int quizID) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
	}

	// Getter
	public int getQuizID() {
		return quizID;
	}

}
