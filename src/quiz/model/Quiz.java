package quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.RoundType;

import main.Context;
import server.ServerContext;
import user.model.Quizmaster;

@SuppressWarnings("serial")
public class Quiz implements Serializable {

	private final static int MAXROUNDS = 10;
	private final static int MAXQUESTIONS = 10;
	private final static int MAXTEAMS = 10;
	private final static int MAXPLAYERS = 10;
	private final static String QUIZNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";
	private transient static ObservableList<Quiz> quizzes;
	private static int n = 0;

	private String quizName;
	private int quizID;
	private int amountOfRounds = 0;
	// minAmountOfRounds = 1;
	private int maxAmountOfRounds;
	private ArrayList<Round> rounds = new ArrayList<>();
	private int currentRound = 0;
	// minAmountofQuestionsPerRound = 1;
	private int maxAmountOfQuestionsPerRound;
	private int amountOfTeams = 0;
	// minAmountOfTeams = 2;
	private int maxAmountOfTeams;
	// minAmountofPlayersPerTeam = 1;
	private int maxAmountOfPlayersPerTeam;
	private int quizmasterID;
	// Map(teamID -> team)
	private Map<Integer, Team> teams = new HashMap<Integer, Team>();
	// Map(teamID -> Map(userID -> vote))
	private Map<Integer, Map<Integer, Integer>> votes = new HashMap<>();

	public Quiz(int quizID, String quizName, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int hostID) {
		this.quizID=quizID;
		this.quizName = quizName;
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountOfPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountOfQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.quizmasterID = hostID;
		this.votes = new HashMap<Integer, Map<Integer, Integer>>();
		this.currentRound = 0;
	}

	// Factory method
	/*public static int createQuiz(String quizname, int rounds, int questions, int teams, int players) {
		if (!quizname.matches(QUIZNAMEREGEX))
			return 1;
		else if (!isUniqueQuizname(quizname))
			return 2;
		else if (rounds < 1)
			return 3;
		else if (rounds > MAXROUNDS)
			return 4;
		else if (questions < 1)
			return 5;
		else if (questions > MAXQUESTIONS)
			return 6;
		else if (teams < 2)
			return 7;
		else if (teams > MAXTEAMS)
			return 8;
		else if (players < 1)
			return 9;
		else if (players > MAXPLAYERS)
			return 10;

		// Everything is valid
		Quiz quiz = new Quiz(quizname, rounds, questions, teams, players);

		quiz.quizID = n++;

		Context.getContext().setQuiz(quiz);

		return 0;
	}*/

	// Factory method
	/*public static void createServerQuiz(String quizname, int quizID, int rounds, int questions, int teams,
			int players) {
		Quiz quiz = new Quiz(quizname, rounds, questions, teams, players);

		quiz.quizID = quizID;

		ServerContext.getContext().addQuiz(quiz);
	}*/

	// Getters
	public static int getMaxrounds() {
		return MAXROUNDS;
	}

	public static int getMaxquestions() {
		return MAXQUESTIONS;
	}


	public Map<Integer,Team> getTeams(){
		return teams;
	}
    
	public static int getMaxteams() {
		return MAXTEAMS;
	}

	public static int getMaxplayers() {
		return MAXPLAYERS;
	}

	public static String getQuiznameregex() {
		return QUIZNAMEREGEX;
	}

	public String getQuizname() {
		return quizName;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getAmountOfRounds() {
		return amountOfRounds;
	}

	public int getMaxAmountOfRounds() {
		return maxAmountOfRounds;
	}

	public Round getRound(){
		return rounds.get(currentRound);
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public int getMaxAmountofQuestionsPerRound() {
		return maxAmountOfQuestionsPerRound;
	}

	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public int getMaxAmountOfPlayersPerTeam() {
		return maxAmountOfPlayersPerTeam;
	}

	public int getQuizmaster() {
		return quizmasterID;
	}

	public void setQuizmaster(int quizmasterID) {
		this.quizmasterID = quizmasterID;
	}

	public Map<Integer, Map<Integer, Integer>> getVotes() {
		return votes;
	}

	// Adders and removers
	public void addTeam(Team team) {
		if (amountOfTeams < maxAmountOfTeams) {
			teams.put(team.getTeamID(), team);
			amountOfTeams++;
			team.setMaxAmountOfPlayers(maxAmountOfPlayersPerTeam);
		} else {
			// TODO: Go back and show error
		}
	}

	public void addRound(Difficulty diff, Theme theme) {
		if (amountOfRounds < maxAmountOfRounds) {
			Round round = new Round(RoundType.MC, diff, theme);
			rounds.add(round);
			round.addQuestions(maxAmountOfQuestionsPerRound);
			amountOfRounds++;
		}
	}

	// Removers
	public void removeTeam(int teamID) {
		if (teams.get(teamID) != null) {
			teams.remove(teamID);
			amountOfTeams--;
		}
		else {
			// TODO: Go back and show error
		}
	}

	public void addVote(int userID, int teamID, int vote) {
		Map<Integer, Integer> teamVotes = votes.get(teamID);
		if (teamVotes == null)
			teamVotes = new HashMap<Integer, Integer>();

		teamVotes.put(userID, vote);
		votes.put(teamID, teamVotes);
	}
	
	public void resetVotes() {
		this.votes = new HashMap<Integer, Map<Integer, Integer>>();
	}
	
	public void addAnswer(int teamID, int questionID, int answer) {
		rounds.get(currentRound).addAnswer(teamID, questionID, answer);
	}

	// Methods
	/*private static boolean isUniqueQuizname(String quizname) {
		// TODO: Check uniqueness of quizname

		return true; // Temporary
	}*/
}
