package quiz.util;

import main.Context;

@SuppressWarnings("serial")
public class ClientScoreboardDataEvent extends UserEvent{

	private int quizID;
	
	public ClientScoreboardDataEvent() {
		this.type = "CLIENT_SCOREBOARDDATA";
		this.quizID = Context.getContext().getQuiz().getQuizID();
	}

	public int getQuizID() {
		return quizID;
	}
}
