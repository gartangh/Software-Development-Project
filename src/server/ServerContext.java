package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import network.Network;
import quiz.model.MCQuestion;
import quiz.model.Question;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.util.Difficulty;
import quiz.util.Theme;
import user.model.Host;
import user.model.User;

public class ServerContext {
	
	// Singleton
	private static ServerContext context = new ServerContext();

	private Map<Integer, User> userMap = new HashMap<Integer, User>();
	private Map<Integer, Quiz> quizMap = new HashMap<Integer, Quiz>();
	private Map<Integer, MCQuestion> mcQuestionMap = new HashMap<Integer, MCQuestion>();
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
	
	public void loadData() {
		BufferedReader reader;
		String locationPrefix = "D:\\Documents\\Universiteit\\Bachelor3\\Softwareontwikkeling\\project-1718-groep9\\src\\server\\";
		String[] themeFiles = {"QUESTIONS_CULTURE.txt", "QUESTIONS_SPORTS.txt"};
		try {
			for(int tF = 0; tF < themeFiles.length; tF++) {
				reader = new BufferedReader(new FileReader(locationPrefix+themeFiles[tF]));
				String line = reader.readLine();
				int i = 0;
				int diff = -1;
				while (line != null) {
					if(line.startsWith("-----")) { // Skip gaps between difficulties
						i = 0;
						diff++;
						reader.readLine();
						reader.readLine();
					}
					String question = reader.readLine();
					if(question == null) break;
					String answers[] = {reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine()};
					int correctAnswer = Integer.parseInt(reader.readLine());
					Theme t = Theme.values()[tF];
					Difficulty d = Difficulty.values()[diff];
					int questionID = tF * (2^24) + diff * (2^22) + i; // 256 possible themes and 4 difficulties with each 2^21 questions gives unique ID
					MCQuestion q = new MCQuestion(d, t, questionID, question, answers, correctAnswer);
					
					mcQuestionMap.put(questionID, q);
					
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
}
