package server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quiz.model.Quiz;
import quiz.model.ScoreboardTeam;
import quiz.model.Team;

@SuppressWarnings("serial")
public class ServerScoreboardDataEvent extends ServerEvent {

	ArrayList<ScoreboardTeam> scoreboardTeams = new ArrayList<>();

	/*
	 * Adds all teams to teams and sets all recipients from the quiz :)
	 */
	public ServerScoreboardDataEvent(int quizID) {
		super();
		this.type = "SERVER_SCOREBOARDDATA";
		Quiz quiz = ServerContext.getContext().getQuizMap().get(quizID);
		ObservableList<Team> quizTeams = quiz.getTeams();
		Collections.sort(quizTeams, new Comparator<Team>() {

			@Override
			public int compare(Team teamA, Team teamB) {
				if (teamA.getQuizScore() < (teamB.getQuizScore()))
					return 1;
				else if (teamA.getQuizScore() == (teamB.getQuizScore()))
					return 0;
				else
					return -1;
			}

		});

		for (int i = 0; i < quizTeams.size(); i++) {
			Team team = quizTeams.get(i);
			ScoreboardTeam scoreboardTeam = new ScoreboardTeam(i + 1, team.getTeamname(), team.getTeamID(),
					team.getQuizScore());
			scoreboardTeams.add(scoreboardTeam);
		}
		
		this.addRecipient();
	}

	public ArrayList<ScoreboardTeam> getScoreboardTeams() {
		return scoreboardTeams;
	}
}
