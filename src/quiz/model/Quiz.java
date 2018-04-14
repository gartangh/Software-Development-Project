package quiz.model;

import java.io.Serializable;
import java.util.ArrayList;

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

	private String quizName;
	private int amountOfTeams;
	private int maxAmountOfTeams; // minAmountOfTeams = 2;
	private ArrayList<Team> teams = new ArrayList<Team>();
	private int maxAmountofPlayersPerTeam; // maxAmountofPlayersPerTeam = 1;
	private int amountOfRounds;
	private int maxAmountOfRounds; // minAmountOfRounds = 1;
	private ArrayList<Round> rounds = new ArrayList<Round>();
	private int maxAmountofQuestionsPerRound; // minAmountofQuestionsPerRound =
												// 1;
	private Quizmaster quizmaster;

	private Quiz(String quizName, int maxAmountOfRounds, int maxAmountOfQuestionsPerRound, int maxAmountOfTeams,
			int maxAmountOfPlayersPerTeam) {
		this.quizName = quizName;
		this.amountOfTeams = 0;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountofPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
		this.amountOfRounds = 0;
		this.maxAmountofQuestionsPerRound = maxAmountOfQuestionsPerRound;
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
	public String getQuizName() {
		return quizName;
	}

	public int getAmountOfTeams() {
		return amountOfTeams;
	}

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public ArrayList<Team> getTeams() {
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

	public Quizmaster getQuizmaster() {
		return quizmaster;
	}

	// Adders and removers
	public void addTeam(Team team) {
		if (amountOfTeams < maxAmountOfTeams) {
			teams.add(team);
			amountOfTeams++;
			team.setMaxAmountOfPlayers(maxAmountofPlayersPerTeam);
		} else {
			// TODO: Go back and give error
		}
	}

	public void removeTeam(Team team) {
		if (teams.remove(team)) {
			amountOfTeams--;
		}

		// TODO: If remove team from teams worked: amountOfTeams--;
	}

	public void addRound(Round round) {
		if (amountOfRounds < maxAmountOfRounds) {
			rounds.add(round);
			amountOfRounds++;
			round.setMaxAmountOfQuestions(maxAmountofQuestionsPerRound);
		} else {
			// TODO: Go back and give error
		}
	}

	// Methods
	private static boolean isUniqueQuizname(String quizname) {
		// TODO: Check uniqueness of quizname

		return true; // Temporary
	}

}
