package quiz.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;

@SuppressWarnings("serial")
public class Team implements Serializable {

	private String teamName;
	private int teamID;
	private int amountOfPlayers = 0;
	// minAmountOfTeamPlayers = 1;
	private int maxAmountOfPlayers;
	//private transient ObservableMap<Integer, String> players = FXCollections.observableHashMap();
	private Map<Integer,String> players=new HashMap<Integer,String>();
	// To make it serializable
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int quizScore = 0;
	private int captainID;

	// Constructors
	public Team(int teamID, String teamName, Color color, int captainID, String captainName) {
		this.teamID = teamID;
		this.teamName = teamName;
		this.captainID = captainID;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.players.put(captainID, captainName);
		this.amountOfPlayers++;
	}

	// Getters and setters
	public String getTeamName() {
		return teamName;
	}

	public int getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public int getMaxAmountOfPlayers() {
		return maxAmountOfPlayers;
	}

	public void setMaxAmountOfPlayers(int maxAmountofPlayers) {
		this.maxAmountOfPlayers = maxAmountofPlayers;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
	}

	/*public ObservableMap<Integer, String> getPlayers() {
		return players;
	}*/

	public Map<Integer, String> getPlayers() {
		return players;
	}

	public int getQuizScore() {
		return quizScore;
	}

	// Adders
	public void addPlayer(int userID, String userName) {
		if (amountOfPlayers < maxAmountOfPlayers) {
			players.put(userID, userName);
			amountOfPlayers++;
		}
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setColor(Color color) {
		this.colorRed = ((int) color.getRed() * 255);
		this.colorGreen = ((int) color.getGreen() * 255);
		this.colorBlue = ((int) color.getBlue() * 255);
	}

	// Removers
	public void removePlayer(int playerID) {
		if (players.remove(playerID) != null)
			amountOfPlayers--;
	}

	// Test
	public void setQuizScore(int quizScore) {
		this.quizScore = quizScore;
	}

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public int getCaptainID() {
		return captainID;
	}

	public void addPoints(int i) {
		this.quizScore += i;
	}

}
