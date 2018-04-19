package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import main.Main;
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

	// Getters and setters
	public static ServerContext getContext() {
		return context;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
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

	// Methods
	// Return username for serverEventHandler
	public String changeTeam(int quizID, int teamID, int userID, char type) {
		// Nothing to delete if teamID == -1
		if (quizMap.containsKey(quizID) && userMap.containsKey(userID) && teamID != -1) {
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
		}

		return null;
	}

	public int addQuiz(String quizName, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds,
			int maxAmountOfQuestionsPerRound, int hostID) {
		int newID;
		do {
			newID = (int) (Math.random() * Integer.MAX_VALUE);
		} while (quizMap.containsKey(newID));

		Quiz newQuiz = new Quiz(newID, quizName, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds,
				maxAmountOfQuestionsPerRound, hostID, userMap.get(hostID).getUsername());
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

			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setMaxAmountOfPlayers(q.getMaxAmountOfPlayersPerTeam());
			q.addTeam(team);
			quizMap.put(quizID, q);

			return newID;
		}

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
		String[] themeFiles = { "QUESTIONS_CULTURE.txt", "QUESTIONS_SPORTS.txt" };
		try {
			for (int tF = 0; tF < themeFiles.length; tF++) {
				Map<Integer, Map<Integer, MCQuestion>> themeMap = new HashMap<Integer, Map<Integer, MCQuestion>>();
				orderedMCQuestionMap.put(tF, themeMap);

				// Substring is to remove file:/ before resource
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader(Main.class.getResource("../server/" + themeFiles[tF]).toString().substring(6)));
				String line = bufferedReader.readLine();
				int i = 0;
				int diff = -1;
				Map<Integer, MCQuestion> diffMap = null;
				while (line != null) {
					// Skip gaps between difficulties
					if (line.startsWith("-----")) {
						i = 0;
						diff++;
						diffMap = new HashMap<Integer, MCQuestion>();
						themeMap.put(diff, diffMap);
						bufferedReader.readLine();
						bufferedReader.readLine();
					}

					String question = bufferedReader.readLine();
					if (question == null)
						break;

					String answers[] = { bufferedReader.readLine(), bufferedReader.readLine(),
							bufferedReader.readLine(), bufferedReader.readLine() };
					int correctAnswer = Integer.parseInt(bufferedReader.readLine());
					
					Theme t = Theme.values()[tF];
					Difficulty d = Difficulty.values()[diff];
					// 256 possible themes and 4 difficulties with each 2^21
					// questions gives unique ID
					int questionID = tF * (2 ^ 24) + diff * (2 ^ 22) + i;
					MCQuestion q = new MCQuestion(d, t, questionID, question, answers, correctAnswer);

					diffMap.put(questionID, q);
					allMCQuestions.put(questionID, q);

					// read next line
					i++;
					line = bufferedReader.readLine();
				}

				bufferedReader.close();
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

		for (int userID : quiz.getUnassignedPlayers().keySet())
			r.add(userID);

		r.add(quiz.getQuizmaster());

		return r;
	}

	public Question getQuestion(int questionID) {
		return allMCQuestions.get(questionID);
	}
}
