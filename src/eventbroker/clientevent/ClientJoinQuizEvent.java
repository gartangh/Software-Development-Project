package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientJoinQuizEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_JOIN_QUIZ";

	private int userID;
	private int quizID;
	private String userName;

	// Constructor
	public ClientJoinQuizEvent(int userID, int quizID, String userName) {
		super.type = EVENTTYPE;
		this.userID = userID;
		this.quizID = quizID;
		this.userName = userName;
	}

	// Getters
	public String getUsername() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserID() {
		return userID;
	}

	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
