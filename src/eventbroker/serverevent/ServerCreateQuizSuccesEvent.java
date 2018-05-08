package eventbroker.serverevent;

import quiz.model.QuizModel;

@SuppressWarnings("serial")
public class ServerCreateQuizSuccesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_QUIZ_SUCCES";

	private QuizModel quiz;

	// Constructor
	public ServerCreateQuizSuccesEvent(QuizModel quiz) {
		super.type = EVENTTYPE;
		this.quiz = quiz;
	}

	// Getters
	public QuizModel getQuiz() {
		return quiz;
	}

}
