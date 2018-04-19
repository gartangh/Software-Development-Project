package eventbroker.serverevent;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerSendQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_SEND_QUIZ";

	// TODO: Change to necessary fields instead of Quiz
	private Quiz quiz;

	// Constructor
	public ServerSendQuizEvent(Quiz quiz) {
		super.type = EVENTTYPE;
		this.quiz = quiz;
	}

	// Getters
	public Quiz getQuiz() {
		return quiz;
	}

}
