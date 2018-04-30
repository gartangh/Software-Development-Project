package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerNewMCQuestionEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_QUESTION";

	protected int questionID;
	protected String question;
	protected String[] answers;
	protected int[] answerPermutation;

	// Constructor
	public ServerNewMCQuestionEvent(int questionID, String question, String[] answers, int[] answerPermutation) {
		super.type = EVENTTYPE;
		this.questionID = questionID;
		this.question = question;
		this.answers = answers;
		this.answerPermutation = answerPermutation;
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

	public int[] getAnswerPermutation() {
		return answerPermutation;
	}

}
