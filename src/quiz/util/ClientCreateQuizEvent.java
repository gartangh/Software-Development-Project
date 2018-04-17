package quiz.util;

import java.io.Serializable;

import main.Context;
import quiz.model.Quiz;

@SuppressWarnings("serial")
public class ClientCreateQuizEvent extends UserEvent implements Serializable {

	private String quizName;
	private int maxAmountOfTeams;
	private int maxAmountOfPlayersPerTeam;
	private int maxAmountOfRounds;
	private int maxAmountOfQuestionsPerRound;

	// Constructor
	public ClientCreateQuizEvent(String quizName, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound) {
		this.type = "CLIENT_CREATE_QUIZ";
		this.quizName = quizName;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountOfPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.maxAmountOfQuestionsPerRound = maxAmountOfQuestionsPerRound;
	}

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
