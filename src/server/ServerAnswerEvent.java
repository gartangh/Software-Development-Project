package server;

@SuppressWarnings("serial")
public class ServerAnswerEvent extends ServerEvent {

	private String teamname;
	private int questionID;
	private int answer;
	private int correctAnswer;

	public ServerAnswerEvent(String teamname, int questionID, int answer, int correctAnswer) {
		super();
		this.teamname = teamname;
		this.questionID = questionID;
		this.answer = answer;
		this.correctAnswer = correctAnswer;
		this.type = "SERVER_ANSWER";
	}

	public int getQuestionID() {
		return questionID;
	}

	public String getTeamname() {
		return teamname;
	}

	public int getAnswer() {
		return answer;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

}
