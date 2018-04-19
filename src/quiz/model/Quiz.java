package quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.RoundType;
import server.ServerContext;

@SuppressWarnings("serial")
public class Quiz implements Serializable {

	private final static int MAXROUNDS = 10;
	private final static int MAXQUESTIONS = 10;
	private final static int MAXTEAMS = 10;
	private final static int MAXPLAYERS = 10;
	private final static String QUIZNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private String quizName;
	private int quizID;
	private int amountOfRounds = 0;
	// minAmountOfRounds = 1;
	private int maxAmountOfRounds;
	private transient ArrayList<Round> rounds = new ArrayList<>();
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
	private Map<Integer, String> unassignedPlayers = new HashMap<>();
	private boolean isRunning;
	private String quizMasterName;

	public Quiz(int quizID, String quizName, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam, int maxAmountOfRounds,
			int maxAmountOfQuestionsPerRound, int hostID, String hostName) {
		this.quizID = quizID;
		this.quizName = quizName;
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountOfPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountOfQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.quizmasterID = hostID;
		this.quizMasterName = hostName;
		this.votes = new HashMap<Integer, Map<Integer, Integer>>();
		this.currentRound = -1;
		this.isRunning = false;
	}

	// Factory method
	/*
	 * public static int createQuiz(String quizname, int rounds, int questions,
	 * int teams, int players) { if (!quizname.matches(QUIZNAMEREGEX)) return 1;
	 * else if (!isUniqueQuizname(quizname)) return 2; else if (rounds < 1)
	 * return 3; else if (rounds > MAXROUNDS) return 4; else if (questions < 1)
	 * return 5; else if (questions > MAXQUESTIONS) return 6; else if (teams <
	 * 2) return 7; else if (teams > MAXTEAMS) return 8; else if (players < 1)
	 * return 9; else if (players > MAXPLAYERS) return 10;
	 *
	 * // Everything is valid Quiz quiz = new Quiz(quizname, rounds, questions,
	 * teams, players);
	 *
	 * quiz.quizID = n++;
	 *
	 * Context.getContext().setQuiz(quiz);
	 *
	 * return 0; }
	 */

	// Factory method
	/*
	 * public static void createServerQuiz(String quizname, int quizID, int
	 * rounds, int questions, int teams, int players) { Quiz quiz = new
	 * Quiz(quizname, rounds, questions, teams, players);
	 *
	 * quiz.quizID = quizID;
	 *
	 * ServerContext.getContext().addQuiz(quiz); }
	 */

	// Getters
	public static int getMaxrounds() {
		return MAXROUNDS;
	}

	public static int getMaxquestions() {
		return MAXQUESTIONS;
	}

	public Map<Integer, Team> getTeams() {
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

	public Round getRound() {
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

	public boolean isAnsweredByAll() {
		int nOA = this.getRound().getNumberOfAnswers();
		if (nOA == teams.size())
			return true;
		else
			return false;
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
			currentRound++;
			amountOfRounds++;
		}
	}

	// Removers
	public void removeTeam(int teamID) {
		if (teams.get(teamID) != null) {
			teams.remove(teamID);
			amountOfTeams--;
		} else {
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

	public void addPoints(int teamID, int questionID, int answer) {
		MCQuestion q = (MCQuestion) ServerContext.getContext().getQuestion(questionID);
		if (answer == q.getCorrectAnswer()) {
			teams.get(teamID).addPoints(1);
		}
	}

	// Methods
	/*
	 * private static boolean isUniqueQuizname(String quizname) { // TODO: Check
	 * uniqueness of quizname
	 *
	 * return true; // Temporary }
	 */

	public void addUnassignedPlayer(int userID, String username) {
		unassignedPlayers.put(userID, username);
	}

	public void removeUnassignedPlayer(int userID) {
		unassignedPlayers.remove(userID);
	}

	public Map<Integer, String> getUnassignedPlayers() {
		return unassignedPlayers;
	}

	public void clearUnassignedPlayers() {
		unassignedPlayers.clear();
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getQuizMasterName() {
		return quizMasterName;
	}
}
