package server;

import java.util.HashMap;
import java.util.Map;

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

	public void setNetwork(Network network) {
		this.network = network;
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

	// Adders
	public void addUser(User user) {
		userMap.put(user.getUserID(), user);
	}

	public void addQuiz(Quiz quiz) {
		quizMap.put(quiz.getQuizID(), quiz);
	}

	// Methods
	public User getUser(int userID) {
		return userMap.get(userID);
	}

	public Quiz getQuiz(int quizID) {
		return quizMap.get(quizID);
	}

	public boolean addTeam(int quizID, Team team) {
		if (!quizMap.containsKey(quizID))
			return false;

		Quiz quiz = quizMap.get(quizID);
		quiz.addTeam(team);

		return true;
	}

}
