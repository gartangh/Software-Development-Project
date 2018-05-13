package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerVoteAnswerEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_ANSWER";

	private int teamID;
	private int questionID;
	private int answer;
	private int correctAnswer;
	private int points;

	// Constructors
	public ServerVoteAnswerEvent(int teamID, int questionID, int answer, int correctAnswer, int points) {
		super.type = EVENTTYPE;
		this.teamID = teamID;
		this.questionID = questionID;
		this.answer = answer;
		this.correctAnswer = correctAnswer;
		this.points = points;
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
	
	public int getPoints() {
		return correctAnswer;
	}

}
