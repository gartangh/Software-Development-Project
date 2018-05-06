package eventbroker.serverevent;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ServerJoinQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_JOIN_QUIZ";

	private Quiz quiz;

	// Constructor
	public ServerJoinQuizEvent(Quiz quiz) {
		super.type = EVENTTYPE;
		this.quiz = quiz;
	}

	// Getter
	public Quiz getQuiz() {
		return quiz;
	}

}
