package quiz.util;

import main.Context;

@SuppressWarnings("serial")
public class QuizzerEvent extends UserEvent {

	protected String teamname;
	protected String quizname;

	public QuizzerEvent() {
		this.teamname = Context.getContext().getTeam().getTeamname();
		this.quizname = Context.getContext().getQuiz().getQuizname();
		this.type = "QUIZZER";
	}

	public String getTeamname() {
		return teamname;
	}

	public String getQuizname() {
		return quizname;
	}
}
