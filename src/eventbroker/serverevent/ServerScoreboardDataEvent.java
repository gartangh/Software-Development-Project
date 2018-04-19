package eventbroker.serverevent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import quiz.model.Quiz;
import quiz.model.ScoreboardTeam;
import quiz.model.Team;
import server.ServerContext;

@SuppressWarnings("serial")
public class ServerScoreboardDataEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_SCOREBOARDDATA";

	private ArrayList<ScoreboardTeam> scoreboardTeams = new ArrayList<>();

	// Constructor
	public ServerScoreboardDataEvent(int quizID) {
		super.type = EVENTTYPE;

		// Adds all teams to teams and sets all recipients from the quiz
		Quiz quiz = ServerContext.getContext().getQuizMap().get(quizID);
		Map<Integer, Team> quizTeams = quiz.getTeams();

		ArrayList<Team> quizTeamsList = new ArrayList<>();
		for (Entry<Integer, Team> team : quizTeams.entrySet()) {
			quizTeamsList.add(team.getValue());
		}

		Collections.sort(quizTeamsList, new Comparator<Team>() {
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

		for (int i = 0; i < quizTeamsList.size(); i++) {
			Team team = quizTeamsList.get(i);
			ScoreboardTeam scoreboardTeam = new ScoreboardTeam(i + 1, team.getTeamName(), team.getTeamID(),
					team.getQuizScore());
			scoreboardTeams.add(scoreboardTeam);
		}
	}

	// Getter
	public ArrayList<ScoreboardTeam> getScoreboardTeams() {
		return scoreboardTeams;
	}
}
