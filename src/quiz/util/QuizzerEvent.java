package quiz.util;

import main.Context;

@SuppressWarnings("serial")
public class QuizzerEvent extends UserEvent{
	
	protected int teamID;
	protected int quizID;
	
	public QuizzerEvent(){
		super();
		this.teamID = Context.getContext().getTeam().getID();
		this.quizID = Context.getContext().getQuiz().getID();
		this.type = "QUIZZER";
	}
	
	public int getTeamID() {
		return teamID;
	}
	
	public int getquizID() {
		return quizID;
	}
}
