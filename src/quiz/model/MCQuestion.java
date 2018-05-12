package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public class MCQuestion extends Question {

	private String question;
	private String[] answers;
	private int correctAnswer = -1;

	// Constructors
	public MCQuestion(int questionID, String question, String[] answers) {
		super(questionID);
		this.question = question;
		this.answers = answers;
	}

	public MCQuestion(int questionID, Theme theme, Difficulty difficulty, String question, String[] answers,
			int correctAnswer) {
		super(questionID, theme, difficulty);
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
	}
	
	public MCQuestion(int questionID, String question, String[] answers, int correctAnswer) {
		super(questionID);
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
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
	
	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	// Method
	public boolean checkCorrectAnswer(int answer) {
		return correctAnswer == answer;
	}

}
