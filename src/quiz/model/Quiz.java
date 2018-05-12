package quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.Context;
import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.util.RoundType;
import server.ServerContext;

@SuppressWarnings("serial")
public class Quiz implements Serializable {

	public final static int MINROUNDS = 1;
	public final static int MAXROUNDS = 5;
	public final static int MINTEAMS = 2;
	public final static int MAXTEAMS = 5;
	public final static int MINPLAYERS = 1;
	public final static int MAXPLAYERS = 5;
	public final static String QUIZNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private int quizID;
	private String quizname;
	// Maximum amount of rounds
	private int rounds;
	private ArrayList<Round> roundList = new ArrayList<>();
	private int currentRound = -1;
	// Maximum amount of teams
	private int teams;
	// Map(teamID -> team)
	private Map<Integer, Team> teamMap = new HashMap<>();
	// Maximum amount of players per team
	private int players;
	private int hostID;
	private String hostname;
	// Map(teamID -> Map(userID -> vote))
	private Map<Integer, Map<Integer, Integer>> votes = new HashMap<>();
	// Map(userID -> username)
	private Map<Integer, String> unassignedPlayers = new HashMap<>();
	private boolean running = false;

	// Constructor
	private Quiz(int quizID, String quizname, int rounds, int teams, int players, int hostID, String hostname) {
		this.quizID = quizID;
		this.quizname = quizname;
		this.rounds = rounds;
		this.teams = teams;
		this.players = players;
		this.hostID = hostID;
		this.hostname = hostname;
	}

	// Factory methods
	public static int createServerQuiz(String quizname, int rounds, int teams, int players, int hostID,
			String hostname) {
		// Server side
		// Generate random quizID
		ServerContext context = ServerContext.getContext();
		int quizID;
		do
			quizID = (int) (Math.random() * Integer.MAX_VALUE);
		while (context.getQuizMap().containsKey(quizID));

		context.getQuizMap().put(quizID, new Quiz(quizID, quizname, rounds, teams, players, hostID, hostname));

		return quizID;
	}

	public static Quiz createQuiz(int quizID, String quizname, int rounds, int teams, int players, int hostID,
			String hostname) {
		// Client side
		Quiz quiz = new Quiz(quizID, quizname, rounds, teams, players, hostID, hostname);
		Context.getContext().setQuiz(quiz);

		return quiz;
	}

	// Getters and setters
	public int getQuizID() {
		return quizID;
	}

	public String getQuizname() {
		return quizname;
	}

	public int getAmountOfRounds() {
		return roundList.size();
	}

	public int getRounds() {
		return rounds;
	}

	public ArrayList<Round> getRoundList() {
		return roundList;
	}

	public int getCurrentRound() {
		return currentRound;
	}

	public int getAmountOfTeams() {
		return teamMap.size();
	}

	public int getTeams() {
		return teams;
	}

	public Map<Integer, Team> getTeamMap() {
		return teamMap;
	}

	public int getPlayers() {
		return players;
	}

	public int getHostID() {
		return hostID;
	}

	public String getHostname() {
		return hostname;
	}

	public Map<Integer, Map<Integer, Integer>> getVotes() {
		return votes;
	}

	public Map<Integer, String> getUnassignedPlayers() {
		return unassignedPlayers;
	}

	public boolean getRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	// Adders and removers
	public void addTeam(Team team) {
		if (teamMap.size() < teams) {
			teamMap.put(team.getTeamID(), team);
			team.setPlayers(players);
		} else {
			// TODO Go back and show error
		}
	}

	public void removeTeam(int teamID) {
		if (teamMap.get(teamID) != null) {
			teamMap.remove(teamID);
		} else {
			// TODO Go back and show error
		}
	}

	public void addRound(RoundType roundType, Theme theme, Difficulty difficulty, int questions) {
		if (roundList.size() < rounds) {
			Round round = new Round(roundType, theme, difficulty, questions);
			roundList.add(round);
			round.addQuestions(questions);
			currentRound++;
		} else {
			// TODO Go back and show error
		}
	}

	public void addVote(int userID, int teamID, int vote) {
		Map<Integer, Integer> teamVotes = votes.get(teamID);
		if (teamVotes == null)
			teamVotes = new HashMap<Integer, Integer>();

		teamVotes.put(userID, vote);
		votes.put(teamID, teamVotes);
	}

	public void addUnassignedPlayer(int userID, String username) {
		unassignedPlayers.put(userID, username);
	}

	public void removeUnassignedPlayer(int userID) {
		unassignedPlayers.remove(userID);
	}

	// Methods
	public boolean isAnsweredByAll() {
		if (roundList.get(currentRound).getNumberOfAnswers() == teamMap.size())
			return true;

		return false;
	}

	public void resetVotes() {
		this.votes = new HashMap<Integer, Map<Integer, Integer>>();
	}

	public void clearUnassignedPlayers() {
		unassignedPlayers.clear();
	}

	public void addAnswer(int teamID, int questionID, int answer) {
		roundList.get(currentRound).addAnswer(teamID, questionID, answer);
	}

	public int addPoints(int teamID, int questionID, int pixelSize, int answer) {
		int qType = ServerContext.getContext().getQuestionTypeMap().get(questionID);
		int points = 0;
		if(qType == RoundType.IP.ordinal()) {
			IPQuestion iPQ = (IPQuestion) ServerContext.getContext().getQuestion(questionID);
			if (answer == iPQ.getCorrectAnswer()) {
				points = (int) Math.floor(Math.log(pixelSize) / Math.log(2.0)) + 1;
				
				if (pixelSize > 10 && pixelSize < 100) {
					points = (int) Math.ceil(points * 1.25);
				} else if (pixelSize >= 100){
					points = (int) Math.ceil(points * 1.75);
				}
			}
				
		} else if(qType == RoundType.MC.ordinal()){
			MCQuestion mCQ = (MCQuestion) ServerContext.getContext().getQuestion(questionID);
			if (answer == mCQ.getCorrectAnswer())
				points = 1;
		}
		
		if(points > 0) teamMap.get(teamID).addPoints(points);
		return points;
	}
}
