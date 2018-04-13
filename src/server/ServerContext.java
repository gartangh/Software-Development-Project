package server;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

import network.Network;
import network.Connection;
import quiz.model.Quiz;
import quiz.model.Team;
import user.model.Host;
import user.model.User;

public class ServerContext {
	
	// Singleton
	private static ServerContext context = new ServerContext();

	private Map<Integer, User> userMap = new HashMap<Integer, User>();
	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();
	private Network network;

	// Constructors
	private ServerContext() {
		// Empty default constructor
	}
	
	public static ServerContext getContext() {
		return context;
	}
	
	public void getSimpleUsers() {
		Map<Integer, String> res = new HashMap<Integer, String>();
		// TODO
	}
    
	public int addUser(String username, String password) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(userMap.containsKey(newID));
		
		newID = 1; // Testing purposes

		User newUser = new User(newID, username, password);
		userMap.put(newID, newUser);
		return newID;
	}
	

	public int addQuiz(int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(quizMap.containsKey(newID));

		newID = 1; // Testing purposes
		
		Quiz newQuiz = new Quiz(newID, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, maxAmountOfQuestionsPerRound, hostID);
		quizMap.put(newID, newQuiz);
		return newID;
	}
	
	public int addTeam(int quizID, String teamName, Color color, int captainID) {
		if(quizMap.containsKey(quizID)) {
			Quiz q = quizMap.get(quizID);
			
			int newID;
			boolean unique;
			do {
				unique = true;
				newID = (int) (Math.random() * Integer.MAX_VALUE);
				for(Team t : q.getTeams()) {
					if(t.getID() == newID) unique = false;
				}
			} while(!unique);

			newID = 1; // Testing purposes

			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setMaxAmountOfPlayers(q.getMaxAmountOfPlayersPerTeam());
			q.addTeam(team);
			quizMap.put(quizID, q);			
			return newID;
		}
		return -1;
	}
	
	public Quiz getQuiz(int quizID) {
		return quizMap.get(quizID);
	}
	
	public void updateQuiz(Quiz updatedQuiz) {
		quizMap.put(updatedQuiz.getID(), updatedQuiz);
	}

	public Map<Integer, User> getUserMap() {
		return userMap;
	}
	
	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}
}
