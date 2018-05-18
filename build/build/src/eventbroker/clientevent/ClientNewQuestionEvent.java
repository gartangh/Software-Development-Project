package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientNewQuestionEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_NEW_QUESTION";

	private int questionID;
	
	// Constructor
	public ClientNewQuestionEvent(int questionID) {
		super.type = EVENTTYPE;
		this.questionID = questionID;
	}

	public int getQuestionID() {
		return questionID;
	}

}