package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientHostReadyEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_HOST_READY";

	private int quizID;
	private int userID;

	// Constructor
	public ClientHostReadyEvent(int quizID, int userID) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.userID = userID;
	}

	// Getters
	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
