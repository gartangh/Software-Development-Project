package quiz.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class QuizModel implements Serializable {

	private int quizID;
	private String quizname;
	private int rounds;
	private int teams;
	private int players;
	private int hostID;
	private String hostname;

	public QuizModel(int quizID, String quizname, int rounds, int teams, int players, int hostID, String hostname) {
		this.quizID = quizID;
		this.quizname = quizname;
		this.rounds = rounds;
		this.teams = teams;
		this.players = players;
		this.hostID = hostID;
		this.hostname = hostname;
	}

	public int getQuizID() {
		return quizID;
	}

	public String getQuizname() {
		return quizname;
	}

	public int getRounds() {
		return rounds;
	}

	public int getTeams() {
		return teams;
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

}
