package quiz.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class Quiz {
	private int quizID;
	private int amountOfTeams;
	private int maxAmountOfTeams; // minAmountOfTeams = 2;
	private ObservableList<Team> teams=FXCollections.observableArrayList();
	private int maxAmountofPlayersPerTeam; // maxAmountofPlayersPerTeam = 1;
	private int amountOfRounds;
	private int maxAmountOfRounds; // minAmountOfRounds = 1;
	private ArrayList<Round> rounds=new ArrayList<Round>();
	private int maxAmountofQuestionsPerRound; // minAmountofQuestionsPerRound = 1;
	private int quizmasterID;
	private Map<Integer, Map<Integer, Integer>> votes;	// Map(teamID -> Map(userID -> vote))
	private int currentRound;
	

	public Quiz(int quizID, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		this.quizID=quizID;
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountofPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountofQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.quizmasterID = hostID;
		this.votes = new HashMap<Integer, Map<Integer, Integer>>();
		this.currentRound = 0;
	}

	// Getters
	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}
	
	public Map<Integer, Map<Integer, Integer>> getVotes(){
		return votes;
	}

	public ObservableList<Team> getTeams(){
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

	public ArrayList<Round> getRounds(){
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

	/*public void addRound(Round round) {
		if (amountOfRounds < maxAmountOfRounds) {
			rounds.add(round);
			amountOfRounds++;
			round.setMaxAmountOfQuestions(maxAmountofQuestionsPerRound);
		}
		else {
			// TODO: Go back and give error
		}
	}*/

	// Removers
	public void removeTeam(Team team) {
		if (teams.remove(team)) {
			amountOfTeams--;
		}
		// TODO: If remove team from teams worked: amountOfTeams--;
	}
	
	public void addVote(int userID, int teamID, int vote) {
		Map<Integer, Integer> teamVotes = votes.get(teamID);
		if(teamVotes == null) {
			teamVotes = new HashMap<Integer, Integer>();
		}
		teamVotes.put(userID, vote);
		votes.put(teamID, teamVotes);
	}
	
	public void addAnswer(int teamID, int questionID, int answer) {
		//Round round = rounds.get(currentRound);
		//round.addAnswer(teamID, questionID, answer);
	}

}
