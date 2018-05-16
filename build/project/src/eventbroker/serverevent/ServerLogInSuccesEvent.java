package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerLogInSuccesEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_LOG_IN_SUCCES";

	private int userID;
	private String username;
	private String password;
	private int level;
	private long xp;

	// Constructor
	public ServerLogInSuccesEvent(int userID, String username, String password, int level, long xp) {
		super.type = EVENTTYPE;
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.level = level;
		this.xp = xp;
	}

	// Getters
	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public int getLevel() {
		return level;
	}

	public long getXp() {
		return xp;
	}

}
