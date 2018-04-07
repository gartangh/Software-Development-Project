package quiz;

import java.awt.Color;

import user.model.Player;

public class Team {

	private int amountOfPlayers;
	private int maxAmountOfPlayers; // minAmountOfTeamPlayers = 1;
	// TODO: Add players
	private String name;
	private Color color;
	private Player captain;
	private int roundScore;
	private int quizScore;

	public Team(String name, Color color, Player captain) {
		this.name = name;
		this.color = color;
		this.captain = captain;
		this.roundScore = 0;
		this.quizScore = 0;
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

	// TODO: Add getPlayers()

	public Player getCaptain() {
		return captain;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public int getQuizScore() {
		return quizScore;
	}

	// Setters
	public void setMaxAmountOfPlayers(int maxAmountOfPlayers) {
		this.maxAmountOfPlayers = maxAmountOfPlayers;
	}

	// Adders
	public void addPlayer(Player player) {
		if (amountOfPlayers < maxAmountOfPlayers) {
			// TODO: Add player to players
			amountOfPlayers++;
		}
	}

	// Removers
	public void removePlayer(Player player) {
		// TODO: If remove player from players worked: amountOfPlayers--;
	}

}
