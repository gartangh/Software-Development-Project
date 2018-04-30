package quiz.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class Team implements Serializable {

	private int teamID;
	private String teamname;
	private int amountOfPlayers = 0;
	// Maximum amount of players
	private int players;
	private Map<Integer, String> playerMap = new HashMap<Integer, String>();
	// To make the color serializable
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int quizScore = 0;
	private int captainID;

	// Constructor
	public Team(int teamID, String teamname, Color color, int captainID, String captainName) {
		this.teamID = teamID;
		this.teamname = teamname;
		this.captainID = captainID;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.playerMap.put(captainID, captainName);
		this.amountOfPlayers++;
	}

	// Getters and setter
	public int getTeamID() {
		return teamID;
	}

	public String getTeamname() {
		return teamname;
	}

	public int getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
	}

	public Map<Integer, String> getPlayerMap() {
		return playerMap;
	}

	public int getQuizScore() {
		return quizScore;
	}

	public int getCaptainID() {
		return captainID;
	}

	// Adder and remover
	public void addPlayer(int userID, String userName) {
		if (amountOfPlayers < players) {
			playerMap.put(userID, userName);
			amountOfPlayers++;
		}
	}

	public void removePlayer(int playerID) {
		if (playerMap.remove(playerID) != null)
			amountOfPlayers--;
	}

	// Method
	public void addPoints(int i) {
		this.quizScore += i;
	}

}
