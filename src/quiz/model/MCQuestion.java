package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public class MCQuestion extends Question {

	private String question;
	private String[] answers = new String[4];
	private int correctAnswer;

	public MCQuestion(Difficulty difficulty, Theme theme) {
		super(difficulty, theme);
		// TODO: Get a question (with correct difficulty and theme), answers and correctAnswer from database
	}

	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	// Methods
	public boolean checkCorrectAnswer(int answer) {
		return correctAnswer == answer;
	}

}
