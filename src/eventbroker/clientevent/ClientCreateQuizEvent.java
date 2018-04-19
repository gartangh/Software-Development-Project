package eventbroker.clientevent;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientCreateQuizEvent extends ClientEvent implements Serializable {

	public final static String EVENTTYPE = "CLIENT_CREATE_QUIZ";

	private String quizName;
	private int maxAmountOfTeams;
	private int maxAmountOfPlayersPerTeam;
	private int maxAmountOfRounds;
	private int maxAmountOfQuestionsPerRound;

	// Constructor
	public ClientCreateQuizEvent(String quizName, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam,
			int maxAmountOfRounds, int maxAmountOfQuestionsPerRound) {
		super.type = EVENTTYPE;
		this.quizName = quizName;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountOfPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.maxAmountOfQuestionsPerRound = maxAmountOfQuestionsPerRound;
	}

	// Getters
	public String getQuizName() {
		return quizName;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public int getMaxAmountOfPlayersPerTeam() {
		return maxAmountOfPlayersPerTeam;
	}

	public int getMaxAmountOfRounds() {
		return maxAmountOfRounds;
	}

	public int getMaxAmountOfQuestionsPerRound() {
		return maxAmountOfQuestionsPerRound;
	}

}
