package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public class IPQuestion extends Question {

	// TODO Add pictures
	private String correctAnswer;

	// Constructor
	public IPQuestion(int questionID, Theme theme, Difficulty difficulty) {
		super(questionID, theme, difficulty);
		// TODO Get pictures (with correct difficulty and theme) and answer
	}

	// Getters
	public String getCorrectAnswer() {
		return correctAnswer;
	}

	// TODO Add getPictures()

	// Methods
	public boolean checkCorrectAnswer(String answer) {
		return correctAnswer.equals(answer);
	}

}
