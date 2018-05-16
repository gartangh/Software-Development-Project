package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerPlayerLeavesQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_PLAYER_LEAVES_QUIZ";

	private int quizID;
	private int userID;
	private int teamID;
	private int newCaptainID;
	private boolean running;

	// Constructor
	public ServerPlayerLeavesQuizEvent(int quizID, int userID, int teamID, int newCaptainID, boolean running) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
		this.userID = userID;
		this.teamID = teamID;
		this.newCaptainID = newCaptainID;
		this.running = running;
	}

	// Getters
	public int getNewCaptainID() {
		return newCaptainID;
	}

	public int getQuizID() {
		return quizID;
	}

	public int getUserID() {
		return userID;
	}

	public int getTeamID() {
		return teamID;
	}
	
	public boolean isRunning() {
		return running;
	}

}
