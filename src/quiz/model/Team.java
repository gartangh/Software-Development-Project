package quiz.model;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javafx.util.Pair;
import user.model.Player;

public class Team {

	private int amountOfPlayers;
	private int maxAmountOfPlayers; // minAmountOfTeamPlayers = 1;
	// TODO: Add players
	private Map<Integer, String> players;
	private String name;
	private Color color;
	private int captainID;
	private int roundScore;
	private int quizScore;
	private int teamID;

	public Team(int teamID, String name, Color color, int captainID, String captainUserName) {
		this.teamID = teamID;
		this.name = name;
		this.color = color;
		this.captainID = captainID;
		this.roundScore = 0;
		this.quizScore = 0;
		this.players = new HashMap<Integer, String>();
		maxAmountOfPlayers = 1;
		amountOfPlayers = 0;
		this.addPlayer(captainID, captainUserName);
	}

	// Getters
	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public int getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public int getMaxAmountOfPlayers() {
		return maxAmountOfPlayers;
	}

	public Map<Integer, String> getPlayers(){
		return players;
	}

	public int getCaptainID() {
		return captainID;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public int getQuizScore() {
		return quizScore;
	}
	
	public int getID() {
		return teamID;
	}

	// Setters
	public void setMaxAmountOfPlayers(int maxAmountOfPlayers) {
		if(maxAmountOfPlayers > amountOfPlayers) this.maxAmountOfPlayers = maxAmountOfPlayers;
	}

	// Adders
	public void addPlayer(int playerID, String playerName) {
		if (amountOfPlayers < maxAmountOfPlayers) {
			players.put(playerID, playerName);
			amountOfPlayers++;
		}
	}

	// Removers
	public void removePlayer(int playerID) {
		if(players.remove(playerID)!=null) amountOfPlayers--;
	}

}
