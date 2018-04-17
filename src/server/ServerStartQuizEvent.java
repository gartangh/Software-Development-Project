package server;

public class ServerStartQuizEvent extends ServerEvent {
	int quizID;

	public ServerStartQuizEvent(int quizID) {
		super();
		this.quizID = quizID;
		this.type="SERVER_START_QUIZ";
	}

	public int getQuizID() {
		return quizID;
	}






}
