package quiz;

import java.util.ArrayList;

import user.Host;
import user.Quizmaster;

public class Quiz {
	private String quizName;
	private int quizID;
	private int amountOfTeams;
	private int maxAmountOfTeams; // minAmountOfTeams = 2;
	private ArrayList<Team> teams=new ArrayList<Team>();
	private int maxAmountofPlayersPerTeam; // maxAmountofPlayersPerTeam = 1;
	private int amountOfRounds;
	private int maxAmountOfRounds; // minAmountOfRounds = 1;
	private ArrayList<Round> rounds=new ArrayList<Round>();
	private int maxAmountofQuestionsPerRound; // minAmountofQuestionsPerRound = 1;
	private Quizmaster quizmaster;

	public Quiz(String quizName, int quizID, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, Host host) {
		this.quizName = quizName;
		this.quizID=quizID;
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountofPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountofQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.quizmaster = host.castToQuizmaster();
	}

	// Getters
	public String getQuizName() {
		return quizName;
	}
	
	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public ArrayList<Team> getTeams(){
		return teams;
	}
	
	public int getMaxAmountOfPlayersPerTeam() {
		return maxAmountofPlayersPerTeam;
	}

	public int getAmountOfRounds() {
		return amountOfRounds;
	}

	public int getMaxAmountOfRounds() {
		return maxAmountOfRounds;
	}

	public ArrayList<Round> getRound(){
		return rounds;
	}
	
	public int getMaxAmountOfQuestionsPerRound() {
		return maxAmountofQuestionsPerRound;
	}

	public Quizmaster getQuizmaster() {
		return quizmaster;
	}

	// Adders
	public void addTeam(Team team) {
		if (amountOfTeams < maxAmountOfTeams) {
			teams.add(team);
			amountOfTeams++;
			team.setMaxAmountOfPlayers(maxAmountofPlayersPerTeam);
		}
		else {
			// TODO: Go back and give error
		}
	}

	public void addRound(Round round) {
		if (amountOfRounds < maxAmountOfRounds) {
			rounds.add(round);
			amountOfRounds++;
			round.setMaxAmountOfQuestions(maxAmountofQuestionsPerRound);
		}
		else {
			// TODO: Go back and give error
		}
	}

	// Removers
	public void removeTeam(Team team) {
		if (teams.remove(team)) {
			amountOfTeams--;
		}
		// TODO: If remove team from teams worked: amountOfTeams--;
	}

}
