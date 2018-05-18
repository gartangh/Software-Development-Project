package quiz.model;

import quiz.util.Difficulty;
import quiz.util.Theme;

public abstract class Question {

	int questionID;
	Theme theme = null;
	Difficulty difficulty = null;

	public Question(int questionID) {
		this.questionID = questionID;
	}

	public Question(int questionID, Theme theme, Difficulty difficulty) {
		this(questionID);
		this.theme = theme;
		this.difficulty = difficulty;
	}

	public int getQuestionID() {
		return questionID;
	}

	public Theme getTheme() {
		return theme;
	}
	
	public Difficulty getDifficulty() {
		return difficulty;
	}

}
