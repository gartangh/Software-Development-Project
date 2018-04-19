package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerCreateAccountSuccesEvent extends ServerEvent {
	
	public final static String EVENTTYPE = "SERVER_CREATE_ACCOUNT_SUCCES";

	private int userID;
	private String username;
	private String password;
	private int connectionID;

	// Constructor
	public ServerCreateAccountSuccesEvent(int userID, String username, String password, int connectionID) {
		super.type = EVENTTYPE;
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.connectionID = connectionID;
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

	public int getConnectionID() {
		return connectionID;
	}

}
