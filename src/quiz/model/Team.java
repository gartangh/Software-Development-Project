package quiz.model;

//import java.awt.Color;
import java.util.ArrayList;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import user.model.Player;

public class Team {

	private int amountOfPlayers;
	private int maxAmountOfPlayers; // minAmountOfTeamPlayers = 1;
	private ObservableList<Player> members=FXCollections.observableArrayList();
	private StringProperty name;
	private Color color;
	private Player captain;
	private int roundScore;
	private int quizScore;

	public Team(StringProperty name, Color color, Player captain) {
		this.name = name;
		this.color = color;
		this.captain = captain;
		this.roundScore = 0;
		this.quizScore = 0;
		this.amountOfPlayers=1;//voorlopig
		this.maxAmountOfPlayers=5;
	}


	public Team(StringProperty name, Color color, Player captain,int maxamount) {
		this.name = name;
		this.color = color;
		this.captain = captain;
		this.roundScore = 0;
		this.quizScore = 0;
		this.amountOfPlayers=1;
		this.maxAmountOfPlayers=maxamount; //voorlopig
	}

	// Getters
	public StringProperty getNameProperty() {
		return name;
	}

	public String getName(){
		return name.get();
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

	public ObservableList<Player> getMembers(){
		return members;
	}

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
	public void addMember(Player player) {
		if (amountOfPlayers < maxAmountOfPlayers) {
			members.add(player);
			amountOfPlayers++;
		}
	}

	public void setName(String teamname){
		this.name.setValue(teamname);
	}

	public void setColor(Color c){
		this.color=c;
	}
	// Removers
	public void removePlayer(Player player) {
		// TODO: If remove player from players worked: amountOfPlayers--;
	}

}
