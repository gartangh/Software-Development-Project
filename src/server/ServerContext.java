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

	public Map<String, User> getUserMap() {
		return userMap;
	}

	public Map<String, Quiz> getQuizMap() {
		return quizMap;
	}
  
	public int addQuiz(int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while(quizMap.containsKey(newID));

		// newID = 1; // Testing purposes
		
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
		userMap.put(user.getUsername(), user);
	}

	public void addQuiz(Quiz quiz) {
		quizMap.put(quiz.getQuizname(), quiz);
	}

	// Methods
	public Quiz getQuiz(String quizname) {
		return quizMap.get(quizname);
	}
	
	public Map<Integer, Quiz> getQuizMap() {
		return quizMap;
	}
	
	public Network getNetwork() {
		return network;
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
