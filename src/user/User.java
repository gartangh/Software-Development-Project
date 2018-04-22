package user;

import java.io.Serializable;

import main.Context;
import server.ServerContext;

@SuppressWarnings("serial")
public class User implements Serializable {

	public final static String USERNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";
	public final static String PASSWORDREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private int userID;
	private String username;
	private String password;
	private int level = 1;
	private long xp = 0L;

	// Constructors
	public User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
	}

	private User(int userID, String username, String password, int level, long xp) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.level = level;
		this.xp = xp;
	}

	User(User user) {
		// Copy constructor
		this.username = user.username;
		this.password = user.password;
		this.level = user.level;
		this.xp = user.xp;
		this.userID = user.userID;
	}

	// Factory methods
	public static int createUser(String username, String password, int connectionID) {
		// Generate random userID
		int userID;
		do {
			userID = (int) (Math.random() * Integer.MAX_VALUE);
		} while (ServerContext.getContext().getUserMap().containsKey(userID));

		ServerContext.getContext().getUserMap().put(userID, new User(userID, username, password));
		ServerContext.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, connectionID);
		
		return userID;
	}
	
	public static void createAccount(int userID, String username, String password) {
		Context.getContext().setUser(new User(userID, username, password));
		Context.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, 0);
	}
	
	public static void logIn(int userID, String username, String password, int level, long xp) {
		Context.getContext().setUser(new User(userID, username, password, level, xp));
		Context.getContext().getNetwork().getUserIDConnectionIDMap().put(userID, 0);
	}

	// Upcasting
	// Factory method
	public static void createUser(User user) {
		Context.getContext().setUser(new User(user));
	}

	// Downcasting
	public void castToGuest() {
		Context.getContext().setUser(new Guest(this));
	}

	public void castToHost() {
		Context.getContext().setUser(new Host(this));
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

	// Password cannot be changed for the moment
	/*public int setPassword(String password1, String password2) {
		if (!password1.equals(password2)) {
			// TODO: Go back and show error

			return 1;
		} else if (!password1.matches(PASSWORDREGEX)) {
			// TODO: Go back and show error

			return 2;
		}

		this.password = password1;

		return 0;
	}*/

	public int getLevel() {
		return level;
	}

	public long getXp() {
		return xp;
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
		// For tableview in Quizroom
		return username;
	}

}
