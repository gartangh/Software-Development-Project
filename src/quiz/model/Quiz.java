package quiz.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Quiz {
	private int quizID;
	private int amountOfTeams;
	private int maxAmountOfTeams; // minAmountOfTeams = 2;
	private Map<Integer,Team> teams=new HashMap<Integer,Team>();
	private int maxAmountofPlayersPerTeam; // maxAmountofPlayersPerTeam = 1;
	private int amountOfRounds;
	private int maxAmountOfRounds; // minAmountOfRounds = 1;
	private ArrayList<Round> rounds=new ArrayList<Round>();
	private int maxAmountofQuestionsPerRound; // minAmountofQuestionsPerRound = 1;
	private int quizmasterID;

	public Quiz(int quizID, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		this.quizID=quizID;
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountofPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountofQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.quizmasterID = hostID;
	}

	// Getters
	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public Map<Integer,Team> getTeams(){
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

	public int getQuizmasterID() {
		return quizmasterID;
	}

	public int getID() {
		return quizID;
}

	// Adders
	public boolean addTeam(int teamID,Team team) {
		if (amountOfTeams < maxAmountOfTeams) {
			teams.put(teamID,team);
			amountOfTeams++;
			team.setMaxAmountOfPlayers(maxAmountofPlayersPerTeam);
			return true;
		}
		else {
			// TODO: Go back and give error
			return false;
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
	public void removeTeam(Integer teamID) {
		if (teams.get(teamID) != null) {
			teams.remove(teamID);
			amountOfTeams--;
		}
		// TODO: If remove team from teams worked: amountOfTeams--;
	}

}
