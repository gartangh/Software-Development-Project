package server;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerJoinQuizEvent extends ServerEvent{

	private Quiz quiz;
	
	public ServerJoinQuizEvent(Quiz quiz) {
		this.type = "SERVER_JOIN_QUIZ";
		this.quiz = quiz;
	}

	public Quiz getQuiz() {
		return quiz;
	}

}
