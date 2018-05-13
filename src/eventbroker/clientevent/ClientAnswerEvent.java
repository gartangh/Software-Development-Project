package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientAnswerEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_ANSWER";

	private int answer;
	private int questionID;
	private int pixelSize = 0;

	// Constructor
	public ClientAnswerEvent(int questionID, int answer) {
		super.type = EVENTTYPE;
		this.answer = answer;
		this.questionID = questionID;
	}
	public ClientAnswerEvent(int questionID, int pixelSize, int answer) {
		super.type = EVENTTYPE;
		this.answer = answer;
		this.questionID = questionID;
		this.pixelSize = pixelSize;
	}

	// Getters
	public int getAnswer() {
		return answer;
	}

	public int getQuestionID() {
		return questionID;
	}
	
	public int getPixelSize() {
		return pixelSize;
	}

}
