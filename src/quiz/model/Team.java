package quiz.model;

import java.io.Serializable;
import javafx.beans.property.StringProperty;
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
	private ObservableMap<Integer, String> players = FXCollections.observableHashMap();
	// To make it serializable
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int roundScore = 0;
	private int quizScore = 0;

	// Constructors
	public Team(int teamID, String teamName, Color color, int captainID, String captainName) {
		this.teamID = teamID;
		this.teamName = teamName;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.players.put(captainID, captainName);
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

	public ObservableMap<Integer, String> getPlayers() {
		return players;
	}

	public int getRoundScore() {
		return roundScore;
	}

	public int getQuizScore() {
		return quizScore;
	}

	// Adders

	public void addPlayer(int userID,String userName){
		if (amountOfPlayers <maxAmountOfPlayers){
			players.put(userID,userName);
			amountOfPlayers++;
		}
	}

	public void setTeamName(String teamName){
		this.teamName = teamName;
	}

	public void setColor(Color color){
		this.colorRed = ((int) color.getRed()*255);
		this.colorGreen = ((int) color.getGreen()*255);
		this.colorBlue = ((int) color.getBlue()*255);
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
		return this.getCaptainID();
	}

}
