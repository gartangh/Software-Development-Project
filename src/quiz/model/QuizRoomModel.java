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

public class QuizRoomModel {
	private ObservableList<TeamNameID> teams = FXCollections.observableArrayList();
	private StringProperty teamName;
	private StringProperty captainName;
	private int teamID;
	private ObjectProperty<Paint> teamColor;
	private ListProperty<String> teamMembers = new SimpleListProperty<>();

	public ObservableList<TeamNameID> getTeams() {
		return teams;
	}

	public void updateTeams() {
		Quiz quiz = Context.getContext().getQuiz();
		Platform.runLater(new Runnable() {
			public void run() {
				for (Team team : quiz.getTeamMap().values()) {
					TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(team.getTeamName()),
							team.getTeamID());
					if (!teams.contains(teamNameID))
						teams.add(teamNameID);
				}
			}
		});
	}

	public void updateTeamDetail(int teamID) {
		this.teamID = teamID;

		Platform.runLater(new Runnable() {
			public void run() {
				Team team = Context.getContext().getQuiz().getTeamMap().get(teamID);
				teamName.setValue(team.getTeamName());
				captainName.setValue(team.getPlayerMap().get(team.getCaptainID()));
				teamColor.setValue(team.getColor());
				Set<Entry<Integer, String>> r = team.getPlayerMap().entrySet();
				teamMembers.clear();
				teamMembers.set(FXCollections.observableArrayList());
				for (Entry<Integer, String> entry : r)
					teamMembers.add(entry.getValue());
			}
		});
	}

	public QuizRoomModel() {
		this.teamName = new SimpleStringProperty("");
		this.captainName = new SimpleStringProperty("");
		this.teamColor = new SimpleObjectProperty<Paint>(Color.TRANSPARENT);
		this.teamID = -1;
	}

	public QuizRoomModel(Team team) {
		this.teamName = new SimpleStringProperty(team.getTeamName());
		this.captainName = new SimpleStringProperty(team.getPlayerMap().get(team.getCaptainID()));
		this.teamColor = new SimpleObjectProperty<Paint>(team.getColor());
		this.teamID = team.getTeamID();
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
