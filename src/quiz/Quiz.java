package quiz;

import user.Host;
import user.Quizmaster;

public class Quiz {

	private int amountOfTeams;
	private int maxAmountOfTeams; // minAmountOfTeams = 2;
	// TODO: add teams
	private int maxAmountofPlayersPerTeam; // maxAmountofPlayersPerTeam = 1;
	private int amountOfRounds;
	private int maxAmountOfRounds; // minAmountOfRounds = 1;
	// TODO: add rounds
	private int maxAmountofQuestionsPerRound; // minAmountofQuestionsPerRound = 1;
	private Quizmaster quizmaster;

	public Quiz(int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, Host host) {
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountofPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountofQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.quizmaster = host.castToQuizmaster();
	}

	// Getters
	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	// TODO: add getTeams()
	
	public int getMaxAmountOfPlayersPerTeam() {
		return maxAmountofPlayersPerTeam;
	}

	public int getAmountOfRounds() {
		return amountOfRounds;
	}

	public int getMaxAmountOfRounds() {
		return maxAmountOfRounds;
	}

	// TODO: Add getRounds()
	
	public int getMaxAmountOfQuestionsPerRound() {
		return maxAmountofQuestionsPerRound;
	}

	public Quizmaster getQuizmaster() {
		return quizmaster;
	}

	// Adders
	public void addTeam(Team team) {
		if (amountOfTeams < maxAmountOfTeams) {
			// TODO: Add team to teams
			amountOfTeams++;
			team.setMaxAmountOfPlayers(maxAmountofPlayersPerTeam);
		}
		else {
			// TODO: Go back and give error
		}
	}

	public void addRound(Round round) {
		if (amountOfRounds < maxAmountOfRounds) {
			// TODO: Add round to rounds
			amountOfRounds++;
			round.setMaxAmountOfQuestions(maxAmountofQuestionsPerRound);
		}
		else {
			// TODO: Go back and give error
		}
	}

	// Removers
	public void removeTeam(Team team) {
		// TODO: If remove team from teams worked: amountOfTeams--;
	}

}
