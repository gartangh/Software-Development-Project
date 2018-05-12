package server;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.imageio.ImageIO;

import javafx.scene.paint.Color;
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

	private Map<Integer, Map<Integer, Map<Integer, ArrayList<Integer>>>> orderedQuestionMap = new HashMap<Integer, Map<Integer, Map<Integer, ArrayList<Integer>>>>(); // Theme -> Diff -> Type -> QuestionID
	private Map<Integer, Question> allQuestions = new HashMap<Integer, Question>();
	private Map<Integer, Integer> questionTypeMap = new HashMap<Integer, Integer>();
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
	
	public Map<Integer, Timer> getQuizTimerMap() {
		return quizTimerMap;
	}

	public Map<Integer, Map<Integer, Map<Integer, ArrayList<Integer>>>> getOrderedQuestionMap() {
		return orderedQuestionMap;
	}
	
	public Map<Integer, Integer> getQuestionTypeMap(){
		return questionTypeMap;
	}

	// Methods
	// Return username for serverEventHandler
	public String changeTeam(int quizID, int teamID, int userID, char type) {
		// Nothing to delete if teamID == -1
		if (quizMap.containsKey(quizID) && userMap.containsKey(userID) && teamID != -1) {
			Quiz quiz = quizMap.get(quizID);
			User user = userMap.get(userID);
			Team team = quiz.getTeamMap().get(teamID);

			if (team != null) {
				if (type == 'a') {// add
					team.addPlayer(user.getUserID(), user.getUsername());

					return user.getUsername();
				} else if (type == 'd') {// Delete
					team.removePlayer(user.getUserID());

					return user.getUsername();
				}
			}
		}

		return null;
	}

	public int addTeam(int quizID, String teamName, Color color, int captainID) {
		if (quizMap.containsKey(quizID) && userMap.containsKey(captainID)) {
			Quiz q = quizMap.get(quizID);

			int newID;
			boolean unique;
			do {
				unique = true;
				newID = (int) (Math.random() * Integer.MAX_VALUE);
				for (Team t : q.getTeamMap().values()) {
					if (t.getTeamID() == newID)
						unique = false;
				}
			} while (!unique);

			Team team = new Team(newID, teamName, color, captainID, userMap.get(captainID).getUsername());
			team.setPlayers(q.getPlayers());
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

	public void loadData() {
		String[] themeFiles = { "QUESTIONS_CULTURE.txt", "QUESTIONS_SPORTS.txt" };
		System.out.println("Loading questions ...");
		try {
			for (int themeFile = 0; themeFile < themeFiles.length; themeFile++) {
				Map<Integer, Map<Integer, ArrayList<Integer>>> themeMap = new HashMap<>();
				orderedQuestionMap.put(themeFile, themeMap);

				// Substring is to remove file:/ before resource
				BufferedReader bufferedReader = new BufferedReader(new FileReader(
						Main.class.getResource("../server/files/" + themeFiles[themeFile]).toString().substring(6)));
				String line = bufferedReader.readLine();
				int [] count = new int[RoundType.values().length];
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
					
					switch(questionType) {
					case "IP":
						roundType = RoundType.IP.ordinal();
						questionID += RoundType.IP.ordinal()*Math.pow(2, 30) + count[RoundType.IP.ordinal()];
						String imgPath = ".\\files\\"+questionImageString;
						BufferedImage bufImage = ImageIO.read(getClass().getResourceAsStream(imgPath));
						q = new IPQuestion(questionID, theme, difficulty, bufImage, false, answers, correctAnswer);
						if(diffMap.containsKey(RoundType.IP.ordinal())) {
							diffMap.get(RoundType.IP.ordinal()).add(questionID);
						}
						else {
							ArrayList<Integer> IPList = new ArrayList<Integer>();
							IPList.add(questionID);
							diffMap.put(RoundType.IP.ordinal(), IPList);
						}
						count[RoundType.IP.ordinal()]++;
						break;
					case "MC":
						roundType = RoundType.MC.ordinal();
						questionID += RoundType.MC.ordinal()*Math.pow(2, 30) + count[RoundType.MC.ordinal()];
						q = new MCQuestion(questionID, theme, difficulty, questionImageString, answers, correctAnswer);
						if(diffMap.containsKey(RoundType.MC.ordinal())) {
							diffMap.get(RoundType.MC.ordinal()).add(questionID);
						}
						else {
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

	public Question getQuestion(int questionID) {
		return allQuestions.get(questionID);
	}

}
