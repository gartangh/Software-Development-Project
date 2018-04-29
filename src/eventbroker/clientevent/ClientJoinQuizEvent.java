package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientJoinQuizEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_JOIN_QUIZ";

	private int userID;
	private String username;
	private int quizID;

	// Constructor
	public ClientJoinQuizEvent(int userID, String username, int quizID) {
		super.type = EVENTTYPE;
		this.userID = userID;
		this.username = username;
		this.quizID = quizID;
	}

	// Getters and setters
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

}
