package quiz.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.Context;
import quiz.model.Quiz;
import quiz.model.Team;

public class QuizRoomModel {
	private ObservableList<TeamNameID> teams=FXCollections.observableArrayList();
	private StringProperty teamName;
	private StringProperty captainName;
	private int teamID;
	private ObjectProperty<Paint> teamColor;
	private ListProperty<String> teamMembers=new SimpleListProperty<String>();


	public ObservableList<TeamNameID> getTeams(){
		return teams;
	}

	public void updateTeams(){
		Quiz quiz=Context.getContext().getQuiz();
		Platform.runLater(new Runnable(){
			public void run(){
				for (Team team:quiz.getTeams().values()){
					TeamNameID teamNameID=new TeamNameID(new SimpleStringProperty(team.getName()),team.getID());
					if (!teams.contains(teamNameID)) teams.add(teamNameID);
				}
			}
		});
	}

	public void updateTeamDetail(int teamID){//for the selected team
		this.teamID=teamID;
		Team team=Context.getContext().getQuiz().getTeams().get(teamID);
		Platform.runLater(new Runnable(){
			public void run(){
				teamName.setValue(team.getName());
				captainName.setValue(team.getTeamMembers().get(team.getCaptainID()));
				teamColor.setValue(team.getColor());
				Set<Entry<Integer,String>> r=team.getTeamMembers().entrySet();
				teamMembers.clear();
				teamMembers.set(FXCollections.observableArrayList());
				for (Entry<Integer,String> entry:r){
					teamMembers.add(entry.getValue());
				}
			}
		});
	}


	public QuizRoomModel(){
		this.teamName=new SimpleStringProperty("");
		this.captainName=new SimpleStringProperty("");
		this.teamColor=new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		this.teamID=-1;
	}

	public QuizRoomModel(Team team){
		this.teamName=new SimpleStringProperty(team.getName());
		this.captainName=new SimpleStringProperty(team.getTeamMembers().get(team.getCaptainID()));
		this.teamColor=new SimpleObjectProperty<Paint>(team.getColor());
		this.teamID=team.getID();
	}


	public int getTeamID() {
		return teamID;
	}

	public StringProperty getTeamName() {
		return teamName;
	}

	public StringProperty getCaptainName() {
		return captainName;
	}

	public ObjectProperty<Paint> getTeamColor() {
		return teamColor;
	}

	public ListProperty<String> getTeamMembers() {
		return teamMembers;
	}

	public Object getNameProperty() {
		return teamName;
	}

}
