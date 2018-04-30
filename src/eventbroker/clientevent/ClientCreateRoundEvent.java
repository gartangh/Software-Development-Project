package eventbroker.clientevent;

import quiz.util.Difficulty;
import quiz.util.RoundType;
import quiz.util.Theme;

@SuppressWarnings("serial")
public class ClientCreateRoundEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_CREATE_ROUND";

	private RoundType roundType;
	private Theme theme;
	private Difficulty diff;
	private int numberOfQuestions;

	// Constructor
	public ClientCreateRoundEvent(RoundType roundType, Theme theme, Difficulty diff, int numberOfQuestions) {
		super.type = EVENTTYPE;
		this.roundType = roundType;
		this.theme = theme;
		this.diff = diff;
		this.numberOfQuestions = numberOfQuestions;
	}

	// Getters
	public RoundType getRoundType() {
		return roundType;
	}
	
	public Theme getTheme() {
		return theme;
	}

	public Difficulty getDifficulty() {
		return diff;
	}

	public int getQuestions() {
		return numberOfQuestions;
	}

}
