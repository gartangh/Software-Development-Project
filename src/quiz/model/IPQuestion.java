package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public class IPQuestion extends Question {

	// TODO: Add pictures
	private String correctAnswer;

	public IPQuestion(Difficulty difficulty, Theme theme) {
		super(difficulty, theme);
		// TODO: Get pictures (with correct difficulty and theme) and answer
	}

	public String getAnswer() {
		return correctAnswer;
	}

	// TODO: Add getPictures()

	// Methods
	public boolean checkCorrectAnswer(String answer) {
		return correctAnswer.equals(answer);
	}

}
