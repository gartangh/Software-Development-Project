package quiz.view;

import javafx.beans.property.ObjectProperty;
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

public class QuizRoomModel {
	//private Context context;
	private ObservableList<Team> teams=FXCollections.observableArrayList();
	private StringProperty teamName;
	private StringProperty captainName;
	private ObjectProperty<Paint> teamColor;
	private ObservableMap<Integer,String> teamMembers=FXCollections.observableHashMap();

	public QuizRoomModel(){
		this.teamName=new SimpleStringProperty("");
		this.captainName=new SimpleStringProperty("");
		this.teamColor=new SimpleObjectProperty<Paint>(Color.BLACK);
	}

	public void updateTeams(){
		Quiz quiz=Context.getContext().getQuiz();
		teams=(ObservableList<Team>) Context.getContext().getQuiz().getTeams().values();
	}

	public void updateTeamDetail(int teamID){//for the selected team
		Team team=Context.getContext().getQuiz().getTeams().get(teamID);
		teamName.setValue(team.getName());
		captainName.setValue(team.getName());
		teamMembers=team.getTeamMembers();
		teamColor.setValue(team.getColor());
	}


	}

