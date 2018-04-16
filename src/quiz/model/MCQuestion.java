package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public class MCQuestion extends Question {

	private String question;
	private String[] answers;
	private int correctAnswer;
	
	public MCQuestion(int questionID, String question, String[] answers) {
		super(questionID);
		this.question = question;
		this.answers = answers;
		this.correctAnswer = -1;
	}
	
	public MCQuestion(Difficulty difficulty, Theme theme, int questionID, String question, String[] answers, int correctAnswer) {
		super(difficulty, theme, questionID);
		this.question = question;
		this.answers = answers;
		this.correctAnswer = correctAnswer;
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
