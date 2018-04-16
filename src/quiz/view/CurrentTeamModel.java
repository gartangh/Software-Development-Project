package quiz.view;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import main.Context;
import quiz.model.Quiz;
import quiz.model.Team;

public class CurrentTeamModel {
	//private Context context;
	private StringProperty teamName;
	private StringProperty captainName;
	private int teamID;
	private ObjectProperty<Paint> teamColor;
	private ListProperty<String> teamMembers=new SimpleListProperty<String>();

	public CurrentTeamModel(){
		this.teamName=new SimpleStringProperty("");
		this.captainName=new SimpleStringProperty("");
		this.teamColor=new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		this.teamID=-1;
	}

	public CurrentTeamModel(Team team){
		this.teamName=new SimpleStringProperty(team.getName());
		this.captainName=new SimpleStringProperty(team.getTeamMembers().get(team.getCaptainID()));
		this.teamColor=new SimpleObjectProperty<Paint>(team.getColor());
		this.teamID=team.getID();
	}


	public int getTeamID() {
		return teamID;
	}

	public void updateTeamDetail(int teamID){//for the selected team
		this.teamID=teamID;
		Team team=Context.getContext().getQuiz().getTeams().get(teamID);
		Platform.runLater(new Runnable(){
			public void run(){
				teamName.setValue(team.getName());
				captainName.setValue(team.getName());
				teamMembers=(ListProperty<String>) team.getTeamMembers();
				teamColor.setValue(team.getColor());
			}
		});
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

