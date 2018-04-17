package server;

import user.model.Host;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javafx.scene.paint.Color;
import network.Network;
import quiz.model.MCQuestion;
import quiz.model.Question;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.util.Difficulty;
import quiz.util.Theme;
import user.model.User;

public class ServerContext {

	// Singleton
	private static ServerContext context = new ServerContext();

	private Map<Integer, User> userMap = new HashMap<Integer, User>();
	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();

	private Map<Integer, Map<Integer, Map<Integer, MCQuestion>>> orderedMCQuestionMap = new HashMap<Integer, Map<Integer, Map<Integer, MCQuestion>>>();
	private Map<Integer, MCQuestion> allMCQuestions = new HashMap<Integer, MCQuestion>();
	private Network network;

	// Constructors
	private ServerContext() {
		// Empty default constructor
	}

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
	public int addUser(String username, String password) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while (userMap.containsKey(newID));

		User newUser = new User(newID, username, password);
		userMap.put(newID, newUser);
		return newID;
	}

	// testing code for hannes
	/*
	 * public void addQuizwithQuizID(int quizID){ Quiz quiz=new
	 * Quiz(1,5,5,5,5,20); quizMap.put(quizID,quiz); Team team1 = new
	 * Team(1,"Deborah leemans",Color.rgb(0,0,255),2,"james",quiz.
	 * getMaxAmountOfPlayersPerTeam()); Team team2 = new
	 * Team(2,"Team2",Color.rgb(255,0,0),4,"Precious",quiz.
	 * getMaxAmountOfPlayersPerTeam()); quiz.addTeam(1,team1);
	 * quiz.addTeam(2,team2); }
	 * 
	 * public void addUserwithUserID(int userID){ userMap.put(userID,new
	 * User(1,"hannes","1234")); }
	 */

	public String changeTeam(int quizID, int teamID, int userID, char type) {// returns
																				// username
																				// for
																				// serverEventHandler
		if (quizMap.containsKey(quizID) && userMap.containsKey(userID) && teamID != -1) {// teamID==-1:
																							// nothing
																							// to
																							// delete
			Quiz quiz = quizMap.get(quizID);
			User user = userMap.get(userID);
			Team team = null;
			team = quiz.getTeams().get(teamID);

			if (team != null) {
				if (type == 'a') {// add
					team.addPlayer(user.getID(), user.getUsername());
					return user.getUsername();
				} else if (type == 'd') {// Delete
					team.removePlayer(user.getID());
					return user.getUsername();
				}
			}
		} // team, quiz or user not found
		return null;
	}

	public int addQuiz(String quizName, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds,
			int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while (quizMap.containsKey(newID));

		Quiz newQuiz = new Quiz(newID, quizName, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds,
				maxAmountOfQuestionsPerRound, hostID);
		quizMap.put(newID, newQuiz);
		return newID;
	}

	public int addTeam(int quizID, String teamName, Color color, int captainID) {
		if (quizMap.containsKey(quizID) && userMap.containsKey(captainID)) {
			Quiz q = quizMap.get(quizID);

			int newID;
			boolean unique;
			do {
				unique = true;
				newID = (int) (Math.random() * Integer.MAX_VALUE);
				for (Team t : q.getTeams().values()) {
					if (t.getTeamID() == newID)
						unique = false;
				}
			} while (!unique);

			// Team team = new Team(newID, teamName, color, captainID,
			// userMap.get(captainID).getUsername());
			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setMaxAmountOfPlayers(q.getMaxAmountOfPlayersPerTeam());
			q.addTeam(team);
			quizMap.put(quizID, q);
			return newID;
		} else
			return -1;
	}

	// Methods
	public User getUser(int userID) {
		return userMap.get(userID);
	}

	public Quiz getQuiz(int quizID) {
		return quizMap.get(quizID);
	}

	public Map<Integer, Map<Integer, Map<Integer, MCQuestion>>> getOrderedMCQuestionMap() {
		return orderedMCQuestionMap;
	}

	public void loadData() {
		BufferedReader reader;
		String locationPrefix = "./Files/";
		String[] themeFiles = { "QUESTIONS_CULTURE.txt", "QUESTIONS_SPORTS.txt" };
		try {
			for (int tF = 0; tF < themeFiles.length; tF++) {
				Map<Integer, Map<Integer, MCQuestion>> themeMap = new HashMap<Integer, Map<Integer, MCQuestion>>();
				orderedMCQuestionMap.put(tF, themeMap);
				reader = new BufferedReader(new FileReader(locationPrefix + themeFiles[tF]));
				String line = reader.readLine();
				int i = 0;
				int diff = -1;
				Map<Integer, MCQuestion> diffMap = null;
				while (line != null) {
					if (line.startsWith("-----")) { // Skip gaps between
													// difficulties
						i = 0;
						diff++;
						diffMap = new HashMap<Integer, MCQuestion>();
						themeMap.put(diff, diffMap);
						reader.readLine();
						reader.readLine();
					}
					String question = reader.readLine();
					if (question == null)
						break;
					String answers[] = { reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine() };
					int correctAnswer = Integer.parseInt(reader.readLine());
					Theme t = Theme.values()[tF];
					Difficulty d = Difficulty.values()[diff];
					int questionID = tF * (2 ^ 24) + diff * (2 ^ 22) + i; // 256
																			// possible
																			// themes
																			// and
																			// 4
																			// difficulties
																			// with
																			// each
																			// 2^21
																			// questions
																			// gives
																			// unique
																			// ID
					MCQuestion q = new MCQuestion(d, t, questionID, question, answers, correctAnswer);

					diffMap.put(questionID, q);
					allMCQuestions.put(questionID, q);

					// read next line
					i++;
					line = reader.readLine();
				}
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Integer> getUsersFromQuiz(int quizID) {
		ArrayList<Integer> r = new ArrayList<>();
		Quiz quiz = quizMap.get(quizID);
		for (Team team : quiz.getTeams().values())
			for (int userID : team.getPlayers().keySet())
				r.add(userID);

		for (int userID : quiz.getUnassingendPlayers().keySet())
			r.add(userID);

		return r;

	}

	public Question getQuestion(int questionID) {
		return allMCQuestions.get(questionID);
	}
}
