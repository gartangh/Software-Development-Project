package server;

@SuppressWarnings("serial")
public class ServerNewQuestionEvent extends ServerEvent{
	protected int questionID;
	protected String questionText;
	protected String [] answers;
	protected int[] answerPermutation;

	public ServerNewQuestionEvent(int questionID, String questionText, String [] answers, int[] answerPermutation) {
		super();
		this.questionID = questionID;
		this.questionText = questionText;
		this.answers = answers;
		this.answerPermutation = answerPermutation;
		this.type = "SERVER_NEW_QUESTION";
	}

	public int getQuestionID() {
		return questionID;
	}

	public String getQuestionText() {
		return questionText;
	}

	public String[] getAnswers() {
		return answers;
	}

	public int[] getAnswerPermutation() {
		return answerPermutation;
	}
}
