package server;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerReturnQuizEvent extends ServerEvent{
	
	private Quiz quiz;
	
	public ServerReturnQuizEvent(Quiz quiz) {
		this.quiz = quiz;
		this.type = "SERVER_RETURN_QUIZ";
	}
	
	public Quiz getQuiz() {
		return this.quiz;
	}
}
