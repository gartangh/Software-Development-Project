package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerNewMCQuestionEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_MC_QUESTION";

	private int questionID;
	private String question;
	private String[] answers;
	private int correctAnswer;

	// Constructor
	public ServerNewMCQuestionEvent(int questionID, String question, String[] answers, int correctAnswer) {
		super.type = EVENTTYPE;
		this.questionID = questionID;
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
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

	public int getCorrectAnswer() {
		return correctAnswer;
	}

}
