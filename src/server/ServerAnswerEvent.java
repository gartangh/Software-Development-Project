package server;

@SuppressWarnings("serial")
public class ServerAnswerEvent extends ServerEvent {

	private int teamID;
	private int questionID;
	private int answer;
	private int correctAnswer;

	// Constructors
	public ServerAnswerEvent(int teamID, int questionID, int answer, int correctAnswer) {
		super();
		this.teamID = teamID;
		this.questionID = questionID;
		this.answer = answer;
		this.correctAnswer = correctAnswer;
		this.type = "SERVER_ANSWER";
	}

	// Getters
	public int getTeamID() {
		return teamID;
	}

	public int getQuestionID() {
		return questionID;
	}

	public int getAnswer() {
		return answer;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

}
