package server;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import network.Network;
import quiz.model.Quiz;
import quiz.model.Team;
import user.model.User;

public class ServerContext {

	// Singleton
	private static ServerContext context = new ServerContext();

	private Map<Integer, User> userMap = new HashMap<Integer, User>();
	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();

	private Network network;

	// Getters and setters
	public static ServerContext getContext() {
		return context;
	}

	public Network getNetwork() {
		return network;
	}

	public Map<Integer, User> getUserMap() {
		return userMap;
	}

	public Map<Integer, Quiz> getQuizMap() {
		return quizMap;
	}

	public int addQuiz(int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds,
			int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while (quizMap.containsKey(newID));

		// newID = 1; // Testing purposes

		Quiz newQuiz = new Quiz(newID, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds,
				maxAmountOfQuestionsPerRound, hostID);
		quizMap.put(newID, newQuiz);
		return newID;
	}

	public int addTeam(int quizID, String teamName, Color color, int captainID) {
		if (quizMap.containsKey(quizID)) {
			Quiz q = quizMap.get(quizID);

			int newID;
			boolean unique;
			do {
				unique = true;
				newID = (int) (Math.random() * Integer.MAX_VALUE);
				for (Team team : q.getTeams()) {
					if (team.getTeamID() == newID)
						unique = false;
				}
			} while (!unique);

			// newID = 1; // Testing purposes

			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setMaxAmountOfPlayers(q.getMaxAmountOfPlayersPerTeam());
			q.addTeam(team);
			quizMap.put(quizID, q);

			return newID;
		}

		return -1;
	}

	// Adders
	public void addUser(User user) {
		userMap.put(user.getUserID(), user);
	}

	public void addQuiz(Quiz quiz) {
		quizMap.put(quiz.getQuizID(), quiz);
	}

	// Methods
	public Quiz getQuiz(int quizID) {
		return quizMap.get(quizID);
	}

	public boolean addTeam(int quizID, Team team) {
		if (!quizMap.containsKey(quizID))
			return false;

		Quiz quiz = quizMap.get(quizID);
		quiz.addTeam(team);
		quizMap.replace(quizID, quiz);

		return true;
	}

}
