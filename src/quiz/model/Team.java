package quiz.model;

import java.io.Serializable;
//import java.awt.Color;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import user.model.Player;

public class Team implements Serializable {

	private int amountOfPlayers;
	private int maxAmountOfPlayers; // minAmountOfTeamPlayers = 1;
	private ObservableMap<Integer,String> teamMembers=FXCollections.observableHashMap();
	private StringProperty name;
	private int colorRed; //to make it seriazable
	private int colorGreen;
	private int colorBlue;
	private int captainID;
	private int roundScore;
	private int quizScore;
	private int teamID;

	public Team(int teamID,StringProperty name, Color color,int captainID,String captainname,int maxamount) {
		this.teamID=teamID;
		this.name = name;
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
		this.captainID=captainID;
		this.teamMembers.put(captainID,captainname);
		this.roundScore = 0;
		this.quizScore = 0;
		this.amountOfPlayers=1;//voorlopig
		this.maxAmountOfPlayers=maxamount;
	}
	
	public Team(int teamID,String name, Color color,int captainID,String captainname) {
		this.teamID=teamID;
		this.name = new SimpleStringProperty(name);
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
		this.captainID=captainID;
		this.teamMembers.put(captainID,captainname);
		this.roundScore = 0;
		this.quizScore = 0;
		this.amountOfPlayers=1;//voorlopig
		this.maxAmountOfPlayers=5;
	}

	public Team(int teamID,StringProperty name, Color color,int captainID,String captainname) {
		this.teamID=teamID;
		this.name = name;
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
		this.captainID=captainID;
		this.teamMembers.put(captainID,captainname);
		this.roundScore = 0;
		this.quizScore = 0;
		this.amountOfPlayers=1;//voorlopig
		this.maxAmountOfPlayers=5;
	}

	public Team(StringProperty name, Color color,int captainID,String captainname,int maxamount) {
		this.name = name;
		this.colorRed = (int) (color.getRed()*255);
		this.colorGreen = (int) (color.getGreen()*255);
		this.colorBlue = (int) (color.getBlue()*255);
		this.captainID=captainID;
		this.teamMembers.put(captainID,captainname);
		this.roundScore = 0;
		this.quizScore = 0;
		this.amountOfPlayers=1;//voorlopig
		this.maxAmountOfPlayers=maxamount;
	}


	// Getters
	public StringProperty getNameProperty() {
		return name;
	}

	public int getCaptainID(){
		return captainID;
	}

	public int getID(){
		return this.teamID;
	}

	public String getName(){
		return name.get();
	}

	public Color getColor() {
		return Color.rgb(colorRed,colorGreen,colorBlue);
	}

	public int getAmountOfPlayers() {
		return amountOfPlayers;
	}

	public int getMaxAmountOfPlayers() {
		return maxAmountOfPlayers;
	}

	public ObservableMap<Integer,String> getTeamMembers(){
		return teamMembers;
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

	public void addTeamMember(int userID,String userName){
		if (amountOfPlayers <maxAmountOfPlayers){
			teamMembers.put(userID,userName);
			amountOfPlayers++;
		}
	}

	public void setName(String teamname){
		this.name.setValue(teamname);
	}

	public void setColor(Color color){
		this.colorRed = ((int) color.getRed()*255);
		this.colorGreen = ((int) color.getGreen()*255);
		this.colorBlue = ((int) color.getBlue()*255);
	}
	// Removers
	public void removePlayer(Player player) {
		// TODO: If remove player from players worked: amountOfPlayers--;
	}

}
