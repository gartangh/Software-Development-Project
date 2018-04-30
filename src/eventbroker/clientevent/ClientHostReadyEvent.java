package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientHostReadyEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_HOST_READY";

	private int quizID;

	// Constructor
	public ClientHostReadyEvent(int quizID, int userID) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
	}

	// Getters
	public int getQuizID() {
		return quizID;
	}

	public void setQuizID(int quizID) {
		this.quizID = quizID;
	}

}
