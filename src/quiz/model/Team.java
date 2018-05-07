package quiz.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;
import main.MainContext;
import server.ServerContext;

@SuppressWarnings("serial")
public class Team implements Serializable {

	public final static String TEAMNAMEREGEX = "^[a-zA-Z0-9._-]{3,10}$";

	private int teamID;
	private String teamname;
	// To make the color serializable
	private int colorRed;
	private int colorGreen;
	private int colorBlue;
	private int quizScore = 0;
	private int captainID;
	// Maximum amount of players
	private int players;
	private Map<Integer, String> playerMap = new HashMap<Integer, String>();

	// Constructor
	private Team(int teamID, String teamname, Color color, int captainID, String captainname, int players) {
		this.teamID = teamID;
		this.teamname = teamname;
		this.captainID = captainID;
		this.colorRed = (int) (color.getRed() * 255);
		this.colorGreen = (int) (color.getGreen() * 255);
		this.colorBlue = (int) (color.getBlue() * 255);
		this.players = players;
		this.playerMap.put(captainID, captainname);
	}

	// Factory methods
	public static int createServerTeam(int quizID, String teamname, Color color, int captainID, String captainname) {
		// Server side
		// Generate random teamID
		ServerContext context = ServerContext.getContext();
		int teamID;
		do
			teamID = (int) (Math.random() * Integer.MAX_VALUE);
		while (context.getQuiz(quizID).getTeamMap().containsKey(teamID));

		// Get maximum amount of players
		int players = context.getQuiz(quizID).getPlayers();
		// Update context
		context.getQuiz(quizID).addTeam(new Team(teamID, teamname, color, captainID, captainname, players));

		return teamID;
	}

	public static Team createTeam(int quizID, int teamID, String teamname, Color color, int captainID,
			String captainname, int players) {
		// Client side
		Team team = new Team(teamID, teamname, color, captainID, captainname, players);
		
		// Update context
		MainContext context = MainContext.getContext();
		context.getQuiz().addTeam(team);
		context.setTeam(team);

		return team;
	}

	// Getters and setter
	public int getTeamID() {
		return teamID;
	}

	public String getTeamname() {
		return teamname;
	}

	public int getPlayers() {
		return players;
	}

	public void setPlayers(int players) {
		this.players = players;
	}

	public Color getColor() {
		return Color.rgb(colorRed, colorGreen, colorBlue);
	}

	public Map<Integer, String> getPlayerMap() {
		return playerMap;
	}

	public int getQuizScore() {
		return quizScore;
	}

	public int getCaptainID() {
		return captainID;
	}

	// Adder and remover
	public void addPlayer(int userID, String username) {
		if (playerMap.size() < players) {
			playerMap.put(userID, username);
		}
	}

	public void removePlayer(int playerID) {
		playerMap.remove(playerID);
	}

	// Method
	public void addPoints(int i) {
		this.quizScore += i;
	}

}
