package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.Main;
import network.Network;
import quiz.model.MCQuestion;
import quiz.model.Question;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.User;
import quiz.util.Difficulty;
import quiz.util.Theme;

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
	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public static ServerContext getContext() {
		return context;
	}

	/**
	 * Gets the network.
	 *
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * Sets the network.
	 *
	 * @param network
	 *            the new network
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

	/**
	 * Gets the user map.
	 *
	 * @return the user map
	 */
	public Map<Integer, User> getUserMap() {
		return userMap;
	}

	/**
	 * Gets the quiz map.
	 *
	 * @return the quiz map
	 */
	public Map<Integer, Quiz> getQuizMap() {
		return quizMap;
	}

	/**
	 * Gets the ordered MC question map.
	 *
	 * @return the ordered MC question map
	 */
	public Map<Integer, Map<Integer, Map<Integer, MCQuestion>>> getOrderedMCQuestionMap() {
		return orderedMCQuestionMap;
	}

	// Methods
	/**
	 * Change team.
	 *
	 * @param quizID
	 *            the quiz ID
	 * @param teamID
	 *            the team ID
	 * @param userID
	 *            the user ID
	 * @param type
	 *            the type
	 * @return the string username for server event handler
	 */
	public String changeTeam(int quizID, int teamID, int userID, char type) {
		if (quizMap.containsKey(quizID) && userMap.containsKey(userID) && teamID != -1) {
			Quiz quiz = quizMap.get(quizID);
			User user = userMap.get(userID);
			Team team = quiz.getTeamMap().get(teamID);

			if (team != null) {
				if (type == 'a')
					// Add
					team.addPlayer(user.getUserID(), user.getUsername());
				else if (type == 'd')
					// Delete
					team.removePlayer(user.getUserID());

				return user.getUsername();
			}
		}

		// Nothing to delete if teamID == -1
		return null;
	}

	// Methods
	/**
	 * Gets the user.
	 *
	 * @param userID
	 *            the user ID
	 * @return the user
	 */
	public User getUser(int userID) {
		return userMap.get(userID);
	}

	/**
	 * Gets the quiz.
	 *
	 * @param quizID
	 *            the quiz ID
	 * @return the quiz
	 */
	public Quiz getQuiz(int quizID) {
		return quizMap.get(quizID);
	}

	/**
	 * Load data.
	 */
	public void loadData() {
		String[] themeFiles = { "QUESTIONS_CULTURE.txt", "QUESTIONS_SPORTS.txt" };

		try {
			for (int themeFile = 0; themeFile < themeFiles.length; themeFile++) {
				Map<Integer, Map<Integer, MCQuestion>> themeMap = new HashMap<>();
				orderedMCQuestionMap.put(themeFile, themeMap);

				// Substring is to remove file:/ before resource
				BufferedReader bufferedReader = new BufferedReader(new FileReader(
						Main.class.getResource("../server/" + themeFiles[themeFile]).toString().substring(6)));
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

					Theme theme = Theme.values()[themeFile];
					Difficulty difficulty = Difficulty.values()[diff];
					// 256 possible themes and 4 difficulties with each 2^21
					// questions gives unique ID
					int questionID = themeFile * (2 ^ 24) + diff * (2 ^ 22) + i;
					MCQuestion mCQuestion = new MCQuestion(questionID, theme, difficulty, question, answers,
							correctAnswer);

					diffMap.put(questionID, mCQuestion);
					allMCQuestions.put(questionID, mCQuestion);

					// Read next line
					i++;
					line = bufferedReader.readLine();
				}

				bufferedReader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the users from quiz.
	 *
	 * @param quizID
	 *            the quiz ID
	 * @return the users from quiz
	 */
	public ArrayList<Integer> getUsersFromQuiz(int quizID) {
		ArrayList<Integer> r = new ArrayList<>();
		Quiz quiz = quizMap.get(quizID);

		for (Team team : quiz.getTeamMap().values())
			for (int userID : team.getPlayerMap().keySet())
				r.add(userID);

		for (int userID : quiz.getUnassignedPlayers().keySet())
			r.add(userID);

		r.add(quiz.getHostID());

		return r;
	}

	/**
	 * Gets the question.
	 *
	 * @param questionID
	 *            the question ID
	 * @return the question
	 */
	public Question getQuestion(int questionID) {
		return allMCQuestions.get(questionID);
	}

}
