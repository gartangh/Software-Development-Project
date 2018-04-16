package quiz.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import main.Context;
import user.model.Host;
import user.model.Quizmaster;

@SuppressWarnings("serial")
public class Quiz implements Serializable {

	public final static int MAXROUNDS = 10;
	public final static int MAXQUESTIONS = 10;
	public final static int MAXTEAMS = 10;
	public final static int MAXPLAYERS = 10;
	private final static String QUIZNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private String quizname;
	private int quizID;
	private int amountOfRounds = 0;
	// minAmountOfRounds = 1;
	private int maxAmountOfRounds;
	private ArrayList<Round> rounds = new ArrayList<>();
	private int currentRound = 0;
	// minAmountofQuestionsPerRound = 1;
	private int maxAmountofQuestionsPerRound;
	private int amountOfTeams = 0;
	// minAmountOfTeams = 2;
	private int maxAmountOfTeams;
	private ObservableList<Team> teams = FXCollections.observableArrayList();
	// minAmountofPlayersPerTeam = 1;
	private int maxAmountofPlayersPerTeam;
	private Quizmaster quizmaster;
	// Map(teamID -> Map(userID -> vote))
	private Map<Integer, Map<Integer, Integer>> votes = new HashMap<>();

	private Quiz(String quizname, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int maxAmountOfTeams,
			int maxAmountOfPlayersPerTeam) {
		this.quizname = quizname;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.maxAmountofQuestionsPerRound = maxAmountOfQuestionsPerRound;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountofPlayersPerTeam = maxAmountOfPlayersPerTeam;

		((Host) Context.getContext().getUser()).castToQuizmaster();
		this.quizmaster = (Quizmaster) Context.getContext().getUser();
	}

	// Factory method
	public static int createQuiz(String quizname, int rounds, int questions, int teams, int players) {
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

		// TODO: Add Quiz to database

		Context.getContext().setQuiz(quiz);

		return 0;
	}

	// Getters
	public String getQuizname() {
		return quizname;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public Quizmaster getQuizmaster() {
		return quizmaster;
	}

	public Map<Integer, Map<Integer, Integer>> getVotes() {
		return votes;
	}

	public ObservableList<Team> getTeams() {
		return teams;
	}

	public int getMaxAmountOfPlayersPerTeam() {
		return maxAmountofPlayersPerTeam;
	}

	public int getAmountOfRounds() {
		return amountOfRounds;
	}

	public int getMaxAmountOfRounds() {
		return maxAmountOfRounds;
	}

	public ArrayList<Round> getRounds() {
		return rounds;
	}

	public int getMaxAmountOfQuestionsPerRound() {
		return maxAmountofQuestionsPerRound;
	}

	// Adders and removers
	public void addTeam(Team team) {
		if (amountOfTeams < maxAmountOfTeams) {
			teams.add(team);
			amountOfTeams++;
			team.setMaxAmountOfPlayers(maxAmountofPlayersPerTeam);
		} else {
			// TODO: Go back and show error
		}
	}

	public void removeTeam(Team team) {
		if (teams.remove(team))
			amountOfTeams--;
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

	public void addAnswer(int teamID, int questionID, int answer) {
		rounds.get(currentRound).addAnswer(teamID, questionID, answer);
	}

	public void addRound(Round round) {
		if (amountOfRounds < maxAmountOfRounds) {
			rounds.add(round);
			amountOfRounds++;
			round.setMaxAmountOfQuestions(maxAmountofQuestionsPerRound);
		} else {
			// TODO: Go back and show error
		}
	}

	// Methods
	private static boolean isUniqueQuizname(String quizname) {
		// TODO: Check uniqueness of quizname

		return true; // Temporary
	}

	public static ObservableList<Quiz> getQuizzes() {
		ObservableList<Quiz> quizzes = null;

		// TODO: get quizzes from server

		return quizzes;
	}

}
