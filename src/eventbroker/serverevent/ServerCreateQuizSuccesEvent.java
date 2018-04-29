package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerCreateQuizSuccesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_QUIZ";

	private int quizID;
	private String quizname;
	private int maxAmountOfTeams;
	private int maxAmountOfPlayersPerTeam;
	private int maxAmountOfRounds;
	private int hostID;
	private String hostname;

	// Constructor
	public ServerCreateQuizSuccesEvent(int quizID, String quizname, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam,
			int maxAmountOfRounds, int hostID, String hostname) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.quizname = quizname;
		this.maxAmountOfTeams = maxAmountOfTeams;
		this.maxAmountOfPlayersPerTeam = maxAmountOfPlayersPerTeam;
		this.maxAmountOfRounds = maxAmountOfRounds;
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

	public int getMaxAmountOfTeams() {
		return maxAmountOfTeams;
	}

	public int getMaxAmountOfPlayersPerTeam() {
		return maxAmountOfPlayersPerTeam;
	}

	public int getMaxAmountOfRounds() {
		return maxAmountOfRounds;
	}

	public int getHostID() {
		return hostID;
	}

	public String getHostname() {
		return hostname;
	}

}
