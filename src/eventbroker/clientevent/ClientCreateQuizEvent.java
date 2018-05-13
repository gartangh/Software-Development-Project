package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientCreateQuizEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_CREATE_QUIZ";

	private String quizname;
	private int teams;
	private int players;
	private int rounds;
	private String hostname;

	// Constructor
	public ClientCreateQuizEvent(String quizname, int maxAmountOfTeams, int maxAmountOfPlayersPerTeam,
			int maxAmountOfRounds, String hostname) {
		super.type = EVENTTYPE;
		this.quizname = quizname;
		this.teams = maxAmountOfTeams;
		this.players = maxAmountOfPlayersPerTeam;
		this.rounds = maxAmountOfRounds;
		this.hostname = hostname;
	}

	// Getters
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

	public String getHostname() {
		return hostname;
	}

}
