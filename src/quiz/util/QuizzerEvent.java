package quiz.util;

import main.Context;

@SuppressWarnings("serial")
public class QuizzerEvent extends UserEvent {

	protected int teamID;
	protected int quizID;

	public QuizzerEvent() {
		this.teamID = Context.getContext().getTeamID();
		this.quizID = Context.getContext().getQuiz().getQuizID();

		this.type = "CLIENT_QUIZZER";
	}

	public int getTeamID() {
		return teamID;
	}

	public int getQuizID() {
		return quizID;
	}
}
