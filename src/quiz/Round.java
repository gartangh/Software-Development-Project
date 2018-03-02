package quiz;

public class Round {

	private int amountOfQuestions;
	private int maxAmountOfQuestions;
	// TODO: add questions
	private Type type;
	private Difficulty difficulty;
	private Theme theme;

	public Round(int maxAmountOfQuestions, Type type, Difficulty difficulty, Theme theme) {
		this.maxAmountOfQuestions = maxAmountOfQuestions;
		this.type = type;
		this.difficulty = difficulty;
		this.theme = theme;

		makeQuestions();
	}

	// Getters
	public int getAmountOfQuestions() {
		return amountOfQuestions;
	}

	public int getMaxAmountOfQuestions() {
		return maxAmountOfQuestions;
	}

	// TODO: Add getQuestions()

	public Difficulty getDifficulty() {
		return difficulty;
	}

	public Theme getTheme() {
		return theme;
	}
	
	// Setters
	public void setMaxAmountOfQuestions(int maxAmountOfQuestions) {
		this.maxAmountOfQuestions = maxAmountOfQuestions;
	}

	// Methods
	public void makeQuestions() {
		Question question;
		for (amountOfQuestions = 0; amountOfQuestions < maxAmountOfQuestions; amountOfQuestions++) {
			switch (type) {
			case MC:
				new MCQuestion(difficulty, theme);
				break;
			case IP:
				new IPQuestion(difficulty, theme);
				break;

			default:
				// TODO: ERROR! No such type
				break;
			}

			// TODO: Add question to questions
		}
	}

}
