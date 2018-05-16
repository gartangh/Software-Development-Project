package server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import javax.imageio.ImageIO;
import java.util.Random;

import main.Main;
import network.Network;
import quiz.model.IPQuestion;
import quiz.model.MCQuestion;
import quiz.model.Question;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.User;
import quiz.util.Difficulty;
import quiz.util.RoundType;
import quiz.util.Theme;

public class ServerContext {

	// Singleton
	private static ServerContext context = new ServerContext();

	private Map<Integer, User> userMap = new HashMap<Integer, User>();
	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();
	private Map<Integer, Timer> quizTimerMap = new HashMap<Integer, Timer>();
	private Map<Integer, Timer> quizPixelTimerMap = new HashMap<Integer, Timer>();
	// Theme -> Difficulty -> Type -> QuestionID
	private Map<Integer, Map<Integer, Map<Integer, ArrayList<Integer>>>> orderedQuestionMap = new HashMap<Integer, Map<Integer, Map<Integer, ArrayList<Integer>>>>();
	private Map<Integer, Question> allQuestions = new HashMap<Integer, Question>();
	private Map<Integer, Integer> questionTypeMap = new HashMap<Integer, Integer>();
	private Network network;

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

	public Map<Integer, Timer> getQuizTimerMap() {
		return quizTimerMap;
	}

	public Map<Integer, Timer> getQuizPixelTimerMap() {
		return quizPixelTimerMap;
	}

	public Map<Integer, Map<Integer, Map<Integer, ArrayList<Integer>>>> getOrderedQuestionMap() {
		return orderedQuestionMap;
	}

	public Map<Integer, Integer> getQuestionTypeMap() {
		return questionTypeMap;
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
				if (team.getPlayerMap().size() > 0) {
					if (team.getCaptainID() == user.getUserID()) {
						Random random = new Random();
						List<Integer> keys = new ArrayList<Integer>(team.getPlayerMap().keySet());
						Integer randomKey = keys.get(random.nextInt(keys.size()));
						team.setCaptainID(randomKey);
					}
				} else
					quizMap.get(quizID).removeTeam(team.getTeamID());

				return user.getUsername();
			}
		}

		// Nothing to delete if teamID == -1
		return null;
	}

	public void terminateTimers(int quizID) {
		if (this.quizTimerMap.get(quizID) != null)
			this.quizTimerMap.get(quizID).cancel();
		if (this.quizPixelTimerMap.get(quizID) != null)
			this.quizPixelTimerMap.get(quizID).cancel();
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
		System.out.println("Loading questions ...");
		try {
			for (int themeFile = 0; themeFile < themeFiles.length; themeFile++) {
				Map<Integer, Map<Integer, ArrayList<Integer>>> themeMap = new HashMap<>();
				orderedQuestionMap.put(themeFile, themeMap);

				// Substring is to remove file:/ before resource
				BufferedReader bufferedReader = new BufferedReader(new FileReader(
						Main.class.getResource("/server/files/" + themeFiles[themeFile]).toString().substring(6)));
				String line = bufferedReader.readLine();
				int[] count = new int[RoundType.values().length];
				int diff = -1;
				Map<Integer, ArrayList<Integer>> diffMap = null;
				while (line != null) {
					// Skip gaps between difficulties
					if (line.startsWith("-----")) {
						count = new int[RoundType.values().length];
						diff++;
						diffMap = new HashMap<Integer, ArrayList<Integer>>();
						themeMap.put(diff, diffMap);
						bufferedReader.readLine();
						bufferedReader.readLine();
					}
					String questionType = bufferedReader.readLine();
					if (questionType == null)
						break;

					String questionImageString = bufferedReader.readLine();
					String answers[] = { bufferedReader.readLine(), bufferedReader.readLine(),
							bufferedReader.readLine(), bufferedReader.readLine() };
					int correctAnswer = Integer.parseInt(bufferedReader.readLine());

					Theme theme = Theme.values()[themeFile];
					Difficulty difficulty = Difficulty.values()[diff];

					// 4 question types, 256 possible themes and 4 difficulties with each max 2^19
					// questions gives guaranteed unique ID
					int questionID = (int) (themeFile * Math.pow(2, 22) + diff * Math.pow(2, 20));
					Question q = null;
					int roundType = 0;

					switch (questionType) {
					case "IP":
						roundType = RoundType.IP.ordinal();
						questionID += RoundType.IP.ordinal() * Math.pow(2, 30) + count[RoundType.IP.ordinal()];
						String imgPath = "files/" + questionImageString;
						BufferedImage bufImage = ImageIO.read(getClass().getResourceAsStream(imgPath));
						q = new IPQuestion(questionID, theme, difficulty, bufImage, false, answers, correctAnswer);
						if (diffMap.containsKey(RoundType.IP.ordinal())) {
							diffMap.get(RoundType.IP.ordinal()).add(questionID);
						} else {
							ArrayList<Integer> IPList = new ArrayList<Integer>();
							IPList.add(questionID);
							diffMap.put(RoundType.IP.ordinal(), IPList);
						}
						count[RoundType.IP.ordinal()]++;
						break;
					case "MC":
						roundType = RoundType.MC.ordinal();
						questionID += RoundType.MC.ordinal() * Math.pow(2, 30) + count[RoundType.MC.ordinal()];
						q = new MCQuestion(questionID, theme, difficulty, questionImageString, answers, correctAnswer);
						if (diffMap.containsKey(RoundType.MC.ordinal())) {
							diffMap.get(RoundType.MC.ordinal()).add(questionID);
						} else {
							ArrayList<Integer> MCList = new ArrayList<Integer>();
							MCList.add(questionID);
							diffMap.put(RoundType.MC.ordinal(), MCList);
						}
						count[RoundType.MC.ordinal()]++;
						break;
					}
					allQuestions.put(questionID, q);
					questionTypeMap.put(questionID, roundType);

					// Read next line
					line = bufferedReader.readLine();
				}

				bufferedReader.close();
			}
			System.out.println("Questions loaded.");
		} catch (IOException e) {
			System.out.println("Failed to load all questions.");
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
		if (quiz != null) {
			for (Team team : quiz.getTeamMap().values())
				r.addAll(team.getPlayerMap().keySet());

			r.addAll(quiz.getUnassignedPlayers().keySet());

			r.add(quiz.getHostID());
		}

		return r;
	}

	/**
	 * Gets the users from team.
	 *
	 * @param quizID
	 *            the quiz ID
	 * @param teamID
	 *            the team ID
	 * @return the users from team
	 */
	public ArrayList<Integer> getUsersFromTeam(int quizID, int teamID) {
		ArrayList<Integer> r = new ArrayList<>();

		Quiz quiz = quizMap.get(quizID);
		if (quiz != null) {
			Team team = quiz.getTeamMap().get(teamID);
			if (team != null)
				r.addAll(team.getPlayerMap().keySet());
		}

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
		return allQuestions.get(questionID);
	}

}
