package quiz.model;

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
import main.MainContext;

public class JoinTeamModel {

	private ObservableList<TeamNameID> teams;
	private int teamID = -1;
	private StringProperty teamname;
	private StringProperty captainname;
	private StringProperty startButtonText;
	private ObjectProperty<Paint> color;
	private ListProperty<String> teamMembers;
	private ListProperty<String> unassignedPlayers;

	// Constructor
	public JoinTeamModel() {
		this.teamname = new SimpleStringProperty();
		this.captainname = new SimpleStringProperty();
		this.startButtonText= new SimpleStringProperty();
		this.color = new SimpleObjectProperty<>(Color.TRANSPARENT);
		this.teams = FXCollections.observableArrayList();
		this.teamMembers = new SimpleListProperty<>();
		this.unassignedPlayers  = new SimpleListProperty<>();
	}

	// Getters
	public ObservableList<TeamNameID> getTeams() {
		return teams;
	}

	public int getTeamID() {
		return teamID;
	}

	public StringProperty getTeamname() {
		return teamname;
	}

	public StringProperty getCaptainname() {
		return captainname;
	}

	public ObjectProperty<Paint> getColor() {
		return color;
	}

	public ListProperty<String> getMembers() {
		return teamMembers;
	}

	public ListProperty<String> getUnassignedPlayers() {
		return unassignedPlayers;
	}

	public void setStartButtonText(MainContext context){
		if (context.getUser().getUserID()==context.getQuiz().getHostID()){
			 startButtonText.setValue("Start");
		}
		else startButtonText.setValue("Ready");
	}

	public StringProperty getStartButtonText(){
		return this.startButtonText;
	}
	// Methods
	public void updateTeams() {
		Quiz quiz = MainContext.getContext().getQuiz();
		Platform.runLater(new Runnable() {
			public void run() {
				teams.clear();
				for (Team team : quiz.getTeamMap().values()) {
					TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(team.getTeamname()),
							team.getTeamID(),new SimpleStringProperty(team.getPlayerMap().get(team.getCaptainID())));
					teams.add(teamNameID);
				}
			}
		});
	}

	public void triggerTeams() {
		Platform.runLater(new Runnable() {
			public void run() {
				TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(""),
						-1,new SimpleStringProperty(""));
				teams.add(teamNameID);
				teams.remove(teamNameID);
			}
		});
	}
	
	public void addTeam(Team team) {
		Platform.runLater(new Runnable() {
			public void run() {
				TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(team.getTeamname()),
							team.getTeamID(),new SimpleStringProperty(team.getPlayerMap().get(team.getCaptainID())));
				teams.add(teamNameID);
			}
		});
	}

	public void deleteTeam(Team team) {
		Platform.runLater(new Runnable() {
			public void run() {
				for (int i=0;i<teams.size();i++){
					if (teams.get(i).getTeamID()==team.getTeamID()) teams.remove(i);
				}
			}
		});
	}

	public void updateUnassignedPlayers(){
		Quiz quiz = MainContext.getContext().getQuiz();
		Platform.runLater(new Runnable() {
			public void run() {
				unassignedPlayers.clear();
				unassignedPlayers.set(FXCollections.observableArrayList());
				unassignedPlayers.addAll(quiz.getUnassignedPlayers().values());
			}
		});
	}

	public void updateTeamDetail(int teamID) {
		this.teamID = teamID;
		Team team = MainContext.getContext().getQuiz().getTeamMap().get(teamID);

		Platform.runLater(new Runnable() {
			public void run() {
				if (team != null){
					teamname.setValue(team.getTeamname());
					captainname.setValue(team.getPlayerMap().get(team.getCaptainID()));
					color.setValue(team.getColor());
					Set<Entry<Integer, String>> r = team.getPlayerMap().entrySet();
					teamMembers.clear();
					teamMembers.set(FXCollections.observableArrayList());
					for (Entry<Integer, String> entry : r)
						teamMembers.add(entry.getValue());
				}
				else {
					teamname.setValue("");
					captainname.setValue("");
					color.setValue(Color.TRANSPARENT);
					teamMembers.clear();
					teamMembers.set(FXCollections.observableArrayList());
				}
			}
		});
	}

}
