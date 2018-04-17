package quiz.util;

@SuppressWarnings("serial")
public class ClientCreateRoundEvent extends QuizzerEvent{
	Theme theme;
	Difficulty diff;
	int numberOfQuestions;
	
	public ClientCreateRoundEvent(Theme theme, Difficulty diff, int numberOfQuestions) {
		super();
		this.theme = theme;
		this.diff = diff;
		this.numberOfQuestions = numberOfQuestions;
		this.type = "CLIENT_CREATE_ROUND";
	}

	public Theme getTheme() {
		return theme;
	}

	public Difficulty getDiff() {
		return diff;
	}

	public int getNumberOfQuestions() {
		return numberOfQuestions;
	}
}
