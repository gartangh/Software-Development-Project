package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientJoinQuizEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_JOIN_QUIZ";

	private String username;
	private int quizID;

	// Constructor
	public ClientJoinQuizEvent(int userID, String username, int quizID) {
		super.type = EVENTTYPE;
		this.username = username;
		this.quizID = quizID;
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public int getQuizID() {
		return quizID;
	}

}
