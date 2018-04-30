package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerNewIPQuestionEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_IP_QUESTION";

	private int questionID;
	private String question;
	private String[] answers;

	// Constructor
	public ServerNewIPQuestionEvent(int questionID, String question, String[] answers) {
		super.type = EVENTTYPE;
		this.questionID = questionID;
		this.question = question;
		this.answers = answers;
	}

	// Getters
	public int getQuestionID() {
		return questionID;
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

}
