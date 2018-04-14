package user.model;

import java.io.Serializable;

import main.Context;

@SuppressWarnings("serial")
public class User implements Serializable {

	private final static String USERNAMEREGEX = "^[a-zA-Z0-9._-]{3,}$";
	private final static String PASSWORDREGEX = "^[a-zA-Z0-9._-]{3,}$";

	private String username;
	private String password;
	private int level;
	private long xp;

	public User(String username, String password) {
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

	// Upcasting
	// Factory method
	public static User createUser(User user) {
		User newUser = new User(user);

		Context.getContext().setUser(newUser);

		return newUser;
	}

	// Downcasting
	public Guest castToGuest() {
		Guest guest = new Guest(this);

		Context.getContext().setUser(guest);

		return guest;
	}

	public Host castToHost() {
		Host host = new Host(this);

		Context.getContext().setUser(host);

		return host;
	}

	// Getters and setters
	public String getUsername() {
		return username;
	}

	public int setUsername(String username) {
		if (!username.matches(USERNAMEREGEX)) {
			// TODO: Go back and show error

			return 1;
		} else if (!isUniqueUsername(username)) {
			// TODO: Go back and show error

			return 2;
		}

		this.username = username;

		return 0;
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

	// Factory method
	public static int createAccount(String username, String password) {
		if (!username.matches(USERNAMEREGEX))
			return 1;
		else if (!password.matches(PASSWORDREGEX))
			return 2;
		else if (!isUniqueUsername(username))
			return 3;

		// Everything is valid
		User user = new User(username, password);

		// TODO: Add User to database

		Context.getContext().setUser(user);

		return 0;
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
