package server;

import java.util.Map;

import quiz.model.Quiz;
import user.model.User;

@SuppressWarnings("serial")
public class ServerSendQuizEvent extends ServerEvent {

	// TODO: Change to necessary fields instead of Quiz
	private Quiz quiz;
	
	public ServerSendQuizEvent(Quiz quiz) {
		this.quiz = quiz;
		this.type = "SERVER_SEND_QUIZ";
	}

	public Quiz getQuiz() {
		return quiz;
	}
}
