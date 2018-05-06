package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public class IPQuestion extends Question {

	private String question;
	private String[] answers;
	private int correctAnswer = -1;
	// TODO Add pictures

	// Constructor
	public IPQuestion(int questionID, Theme theme, Difficulty difficulty, String question, String[] answers,
			int correctAnswer) {
		super(questionID, theme, difficulty);
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
		// TODO Get pictures (with correct difficulty and theme) and answer
	}

	// Getters
	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	// TODO Add getPictures()

	// Methods
	public boolean checkCorrectAnswer(int answer) {
		return correctAnswer == answer;
	}

}
