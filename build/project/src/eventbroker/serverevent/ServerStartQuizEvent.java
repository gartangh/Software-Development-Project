package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerStartQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_START_QUIZ";

	private int quizID;

	// Constructor
	public ServerStartQuizEvent(int quizID) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.type = "SERVER_START_QUIZ";
	}

	// Getter
	public int getQuizID() {
		return quizID;
	}

}
