package server;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import network.Connection;
import network.Network;
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

	private Map<Integer, Connection> connectionMap = new HashMap<Integer, Connection>();

	// Constructors
	private ServerContext() {
		// Empty default constructor
	}
	
	public static ServerContext getContext() {
		return context;
	}
	
	public int addUser(String username, String password) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(userMap.containsKey(newID));
		User newUser = new User(newID, username, password);
		userMap.put(newID, newUser);
		return newID;
	}
	
	public void addQuiz(int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(quizMap.containsKey(newID));
		
		Quiz newQuiz = new Quiz(newID, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, maxAmountOfQuestionsPerRound, hostID);
		quizMap.put(newID, newQuiz);
	}
	
	public void addTeam(int quizID, String teamName, Color color, int captainID) {
		if(quizMap.containsKey(quizID)) {
			Quiz q = quizMap.get(quizID);
			
			int newID;
			boolean unique = true;
			do {
				newID = (int) (Math.random() * Integer.MAX_VALUE);
				for(Team t : q.getTeams()) {
					if(t.getID() == newID) unique = false;
				}
			} while(!unique);
			
			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setMaxAmountOfPlayers(q.getMaxAmountOfPlayersPerTeam());
			q.addTeam(team);
			quizMap.put(quizID, q);
		}
	}
	
	public void addConnection(int userID, Connection connection) {
		connectionMap.put(userID, connection);
	}

	public Map<Integer, Connection> getConnectionMap() {
		return connectionMap;
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
