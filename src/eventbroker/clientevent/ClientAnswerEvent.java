package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientAnswerEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_ANSWER";

	private int answer;
	private int questionID;

	// Constructor
	public ClientAnswerEvent(int questionID, int answer) {
		super.type = EVENTTYPE;
		this.answer = answer;
		this.questionID = questionID;
	}

	// Getters
	public int getAnswer() {
		return answer;
	}

	public int getQuestionID() {
		return questionID;
	}

}
