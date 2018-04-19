package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerQuizNewPlayer extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_QUIZ_NEW_PLAYER";

	private int userID;
	private String username;

	// Constructor
	public ServerQuizNewPlayer(int userID, String userName) {
		super.type = EVENTTYPE;
		this.userID = userID;
		this.username = userName;
	}

	// Getters
	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

}
