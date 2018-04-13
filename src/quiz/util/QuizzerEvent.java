package quiz.util;

import main.Context;

@SuppressWarnings("serial")
public class QuizzerEvent extends UserEvent{
	
	protected int teamID;
	protected int quizID;
	
	public QuizzerEvent(){
		super();
		this.teamID = Context.getContext().getTeamID();
		this.quizID = Context.getContext().getQuiz().getID();
		this.type = "QUIZZER";
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	public int getQuizID() {
		return quizID;
	}
}
