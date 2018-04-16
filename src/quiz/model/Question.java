package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public abstract class Question {

	Difficulty difficulty;
	Theme theme;
	int questionID;

	public Question(Difficulty difficulty, Theme theme, int questionID) {
		this.difficulty = difficulty;
		this.theme = theme;
		this.questionID = questionID;
	}
	
	public Question(int questionID) {
		this.difficulty = null;
		this.theme = null;
		this.questionID = questionID;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}

	public int getQuestionID() {
		return questionID;
	}
}
