package quiz.util;

import main.Context;

@SuppressWarnings("serial")
public class QuizzerEvent extends UserEvent{
	
	protected int teamID;
	protected int quizID;
	
	public QuizzerEvent(){
		this.teamID = Context.getContext().getTeam().getTeamID();
		this.quizID = Context.getContext().getQuiz().getQuizID();

		this.type = "QUIZZER";
	}

	public String getTeamname() {
		return teamname;
	}

	public String getQuizname() {
		return quizname;
	}
}
