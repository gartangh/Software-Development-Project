package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerCreateQuizSuccesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_QUIZ_SUCCES";

	private int quizID;
	private String quizname;
	private int teams;
	private int players;
	private int rounds;
	private int hostID;
	private String hostname;

	// Constructor
	public ServerCreateQuizSuccesEvent(int quizID, String quizname, int teams, int players, int rounds, int hostID,
			String hostname) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.quizname = quizname;
		this.teams = teams;
		this.players = players;
		this.rounds = rounds;
		this.hostID = hostID;
		this.hostname = hostname;
	}

	// Getters
	public int getQuizID() {
		return quizID;
	}

	public String getQuizname() {
		return quizname;
	}

	public int getTeams() {
		return teams;
	}

	public int getPlayers() {
		return players;
	}

	public int getRounds() {
		return rounds;
	}

	public int getHostID() {
		return hostID;
	}

	public String getHostname() {
		return hostname;
	}

}
