package eventbroker.serverevent;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerCreateQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_QUIZ";

	private Quiz quiz;

	// Constructor
	public ServerCreateQuizEvent(Quiz quiz) {
		super.type = EVENTTYPE;
		this.quiz = quiz;
	}

	// Getter
	public Quiz getQuiz() {
		return this.quiz;
	}
}
