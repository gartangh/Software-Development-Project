package quiz.model;

import main.MainContext;
import server.ServerContext;

public class User {

	public final static String USERNAMEREGEX = "^[a-zA-Z0-9._-]{3,10}$";
	public final static String PASSWORDREGEX = "^[a-zA-Z0-9._-]{3,10}$";

	private int userID;
	private String username;
	private String password;
	private int level = 1;
	private long xp = 0L;
	// Only the server uses this
	private boolean loggedIn = true;

	// Constructors
	private User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
	}

	private User(int userID, String username, String password, int level, long xp) {
		this(userID, username, password);
		this.level = level;
		this.xp = xp;
	}

	// Factory methods
	public static int createUser(String username, String password, int connectionID) {
		// Server side
		// Generate random userID
		ServerContext context = ServerContext.getContext();
		int userID;
		do
			userID = (int) (Math.random() * Integer.MAX_VALUE);
		while (context.getUserMap().containsKey(userID));
		
		context.getUserMap().put(userID, new User(userID, username, password));
		context.getNetwork().getUserIDConnectionIDMap().put(userID, connectionID);
		
		return userID;
	}
	
	public static void createAccount(int userID, String username, String password) {
		// Client side
		MainContext context = MainContext.getContext();
		context.setUser(new User(userID, username, password));
		context.getNetwork().getUserIDConnectionIDMap().put(userID, 0);
	}
	
	public static void logIn(int userID, String username, String password, int level, long xp) {
		// Client side
		MainContext context = MainContext.getContext();
		context.setUser(new User(userID, username, password, level, xp));
		context.getNetwork().getUserIDConnectionIDMap().put(userID, 0);
	}

	// Getters and setters
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
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
	
	public boolean isLoggedIn() {
		return loggedIn;
	}
	
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	// Methods
	public void addXp(int xp) {
		this.xp += xp;

		if (this.xp >= level * 1000) {
			this.level++;
			this.xp -= level * 1000;
		}
	}

	@Override
	public String toString() {
		// For TableView in QuizRoom
		return username;
	}

}
