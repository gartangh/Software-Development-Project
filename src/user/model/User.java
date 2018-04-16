package user.model;

import java.io.Serializable;

import eventbroker.EventBroker;
import main.Context;
import quiz.util.ClientCreateAccountEvent;

@SuppressWarnings("serial")
public class User implements Serializable {

	private final static String USERNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";
	private final static String PASSWORDREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private String username;
	private int userID;
	private String password;
	private int level;
	private long xp;

	public User(int userID, String username, String password) {
		this.userID = userID;
		this.username = username;
		this.password = password;
		this.level = 1;
		this.xp = 0L;
	}

	private User(String username, String password, int level, long xp) {
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
	}

	// Factory method
	/*public static int createAccount(String username, String password) {
		if (!username.matches(USERNAMEREGEX))
			return 1;
		else if (!password.matches(PASSWORDREGEX))
			return 2;
		else if (!isUniqueUsername(username))
			return 3;

		// Everything is valid
		User user = new User(username, password);
		
		Context.getContext().setUser(user);

		return 0;
	}*/

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
	public String getUsername() {
		return username;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int setPassword(String password1, String password2) {
		if (!password1.equals(password2)) {
			// TODO: Go back and show error

			return 1;
		} else if (!password1.matches(PASSWORDREGEX)) {
			// TODO: Go back and show error

			return 2;
		}

		this.password = password1;

		return 0;
	}

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

	public static int logIn(String username, String password) {
		if (exists(username, password)) {
			// TODO: Get level and xp from database
			int level = 0;
			int xp = 0;

			Context.getContext().setUser(new User(username, password, level, xp));

			return 0;
		}

		return -1;
	}

	private static boolean isUniqueUsername(String username) {
		// TODO: Check uniqueness of username

		return true; // Temporary
	}

	private static boolean exists(String username, String password) {
		// TODO: Check if username and password exist for a certain user

		return true;
	}

}
