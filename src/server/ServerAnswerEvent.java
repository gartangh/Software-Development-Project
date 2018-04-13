package server;

public class ServerAnswerEvent extends ServerEvent {
	protected int questionID, teamID, answer, correctAnswer;

	public ServerAnswerEvent(int teamID, int questionID, int answer, int correctAnswer) {
		super();
		this.teamID = teamID;
		this.questionID = questionID;
		this.answer = answer;
		this.correctAnswer = correctAnswer;
		this.type = "SERVER_ANSWER";
	}

	public int getQuestionID() {
		return questionID;
	}

	public int getTeamID() {
		return teamID;
	}

	public int getAnswer() {
		return answer;
	}
	
	public int getCorrectAnswer() {
		return correctAnswer;
	}
}
