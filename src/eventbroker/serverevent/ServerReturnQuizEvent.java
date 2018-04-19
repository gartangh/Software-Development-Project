package eventbroker.serverevent;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerReturnQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_RETURN_QUIZ";

	private Quiz quiz;

	// Constructor
	public ServerReturnQuizEvent(Quiz quiz) {
		super.type = EVENTTYPE;
		this.quiz = quiz;
	}

	// Getter
	public Quiz getQuiz() {
		return this.quiz;
	}
}
