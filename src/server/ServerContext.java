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

	private Map<String, User> userMap = new HashMap<String, User>();
	private Map<String, Quiz> quizMap = new HashMap<String, Quiz>();
	private Network network;

	// Getters and setters
	public static ServerContext getContext() {
		return context;
	}

	public Map<String, User> getUserMap() {
		return userMap;
	}

	public Map<String, Quiz> getQuizMap() {
		return quizMap;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	// Adders
	public void addUser(User user) {
		userMap.put(user.getUsername(), user);
	}

	public void addQuiz(Quiz quiz) {
		quizMap.put(quiz.getQuizname(), quiz);
	}

	// Methods
	public Quiz getQuiz(String quizname) {
		return quizMap.get(quizname);
	}

	public void updateQuiz(Quiz updatedQuiz) {
		quizMap.replace(updatedQuiz.getQuizname(), updatedQuiz);
	}

	public boolean addTeam(String quizname, Team team) {
		if (!quizMap.containsKey(quizname))
			return false;

		Quiz quiz = quizMap.get(quizname);
		quiz.addTeam(team);
		quizMap.replace(quizname, quiz);

		return true;
	}

}
