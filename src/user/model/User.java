package user.model;

import main.Context;

public class User {

	private final static String regex = "^[a-zA-Z0-9._-]{3,}$";

	private String username;
	private String password;
	private int level;
	private long xp;

	private User(String username, String password) {
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
	public static int createAccount(String username, String password) {
		if (!username.matches(regex))
			return 1;
		else if (!password.matches(regex))
			return 2;
		else if (!isUnique(username))
			return 3;

		// Everything is valid
		User user = new User(username, password);

		// TODO: Add User to database

		Context.getContext().setUser(user);

		return 0;
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

	// Getters
	public String getUsername() {
		return username;
	}

	public int getLevel() {
		return level;
	}

	public long getXp() {
		return xp;
	}

	// Setters
	public boolean setUsername(String username) {
		if (!username.matches(regex)) {
			// TODO: Go back and show error

			return false;
		} else if (!isUnique(username)) {
			// TODO: Go back and show error

			return false;
		}

		this.username = username;

		return true;
	}

	public boolean setPassword(String password1, String password2) {
		if (!password1.equals(password2)) {
			// TODO: Go back and show error

			return false;
		} else if (!password1.matches(regex)) {
			// TODO: Go back and show error

			return false;
		}

		this.password = password1;

		return true;
	}

	public void addXp(int xp) {
		this.xp += xp;

		if (this.xp >= level * 1000)
			this.level++;
	}

	// Methods
	private static boolean isUnique(String username) {
		// TODO: Check uniquenesss of username

		return true; // Temporary
	}

	public static int signIn(String username, String password) {
		// TODO: Check if username and password exist for a certain user
		if (true) { // Temporary
			Context.getContext().setUser(new User(username, password));
			
			return 1;
		}
		
		return 0;
	}

}
