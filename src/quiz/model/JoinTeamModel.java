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
import main.Context;

public class JoinTeamModel {

	private ObservableList<TeamNameID> teams = FXCollections.observableArrayList();
	private int teamID = -1;
	private StringProperty teamname;
	private StringProperty captainname;
	private ObjectProperty<Paint> color;
	private ListProperty<String> members = new SimpleListProperty<>();

	// Constructor
	public JoinTeamModel() {
		this.teamname = new SimpleStringProperty("");
		this.captainname = new SimpleStringProperty("");
		this.color = new SimpleObjectProperty<>(Color.TRANSPARENT);
	}

	public JoinTeamModel(Team team) {
		this.teamname = new SimpleStringProperty(team.getTeamname());
		this.captainname = new SimpleStringProperty(team.getPlayerMap().get(team.getCaptainID()));
		this.color = new SimpleObjectProperty<>(team.getColor());
		this.teamID = team.getTeamID();
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
		return members;
	}

	// Methods
	public void updateTeams() {
		Quiz quiz = Context.getContext().getQuiz();
		Platform.runLater(new Runnable() {
			public void run() {
				teams.clear();
				for (Team team : quiz.getTeamMap().values()) {
					TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(team.getTeamname()),
							team.getTeamID());
					teams.add(teamNameID);
				}
			}
		});
	}

	public void updateTeamDetail(int teamID) {
		this.teamID = teamID;

		Platform.runLater(new Runnable() {
			public void run() {
				if (teamID != -1){
					Team team = Context.getContext().getQuiz().getTeamMap().get(teamID);
					teamname.setValue(team.getTeamname());
					captainname.setValue(team.getPlayerMap().get(team.getCaptainID()));
					color.setValue(team.getColor());
					Set<Entry<Integer, String>> r = team.getPlayerMap().entrySet();
					members.clear();
					members.set(FXCollections.observableArrayList());
					for (Entry<Integer, String> entry : r)
						members.add(entry.getValue());
				}
				else {
					teamname.setValue("");
					captainname.setValue("");
					color.setValue(Color.TRANSPARENT);
					members.clear();
					members.set(FXCollections.observableArrayList());
				}
			}
		});
	}

}
