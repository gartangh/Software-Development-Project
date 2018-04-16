package server;

@SuppressWarnings("serial")
public class ServerNewQuestionEvent extends ServerEvent {
	protected int questionID;
	protected String question;
	protected String [] answers;
	protected int[] answerPermutation;

	public ServerNewQuestionEvent(int questionID, String question, String [] answers, int[] answerPermutation) {
		super();
		this.questionID = questionID;
		this.question = question;
		this.answers = answers;
		this.answerPermutation = answerPermutation;
		this.type = "SERVER_NEW_QUESTION";
	}

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
