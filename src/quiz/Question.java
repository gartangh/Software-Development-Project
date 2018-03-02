package quiz;

public abstract class Question {

	Difficulty difficulty;
	Theme theme;

	public Question(Difficulty difficulty, Theme theme) {
		this.difficulty = difficulty;
		this.theme = theme;
	}

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}

}
