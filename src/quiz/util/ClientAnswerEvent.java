package quiz.util;

@SuppressWarnings("serial")
public class ClientAnswerEvent extends QuizzerEvent {
	int answer;
	int questionID;
	
	public ClientAnswerEvent(int questionID, int answer, int teamID) {
		super();
		this.answer = answer;
		this.questionID = questionID;
		this.type = "CLIENT_ANSWER";
	}

	public int getAnswer() {
		return answer;
	}
	
	public int getQuestionID() {
		return questionID;
	}
}
