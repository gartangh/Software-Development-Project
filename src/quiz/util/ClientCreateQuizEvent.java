package quiz.util;

import java.io.Serializable;

import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ClientCreateQuizEvent extends UserEvent implements Serializable {

	private Quiz quiz;

	// Constructor
	public ClientCreateQuizEvent(Quiz quiz) {
		this.quiz = quiz;
		this.type = "CLIENT_CREATE_QUIZ";
	}

	// Getters and setters
	public Quiz getQuiz() {
		return quiz;
	}

}
