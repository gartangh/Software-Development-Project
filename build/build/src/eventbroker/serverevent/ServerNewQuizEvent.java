package eventbroker.serverevent;

import quiz.model.QuizModel;

@SuppressWarnings("serial")
public class ServerNewQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NEW_QUIZ";

	QuizModel quiz;

	// Constructor
	public ServerNewQuizEvent(QuizModel quiz) {
		super.type = EVENTTYPE;
		this.quiz = quiz;
	}

	// Getters
	public QuizModel getQuiz() {
		return quiz;
	}

}
