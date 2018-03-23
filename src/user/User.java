package user;

import application.Context;

public class User {

	private String username;
	private String password;
	private int level;
	private long xp;

	public User(String username, String password) {
		// TODO: check uniqueness of username
		String regex = "^[a-zA-Z0-9._-]{3,}$";
		if (username.matches(regex))
			this.username = username;
		else {
			// TODO: Go back and show error
		}

		if (password.matches(regex))
			this.password = password;
		else {
			// TODO: Go back and show error
		}

		this.level = 1;
		this.xp = 0L;
		
		Context.getContext().setUser(this);
	}

	User(User user) {
		// Copy constructor
		this.username = user.username;
		this.password = user.password;
		this.level = user.level;
		this.xp = user.xp;
		
		Context.getContext().setUser(this);
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
	public void setUsername(String username) {
		// TODO: check uniqueness of username
		if (username.matches("[a-zA-Z0-9]{4,8}"))
			this.username = username;
		else {
			// TODO: Go back and show error
		}
	}

	public void setPassword(String password1, String password2) {
		if (password1.equals(password2) && password1.matches("[a-zA-Z0-9]{4,8}"))
			this.password = password1;
		else {
			// TODO: Go back and show error
		}
	}

	public void levelUp() {
		this.level++;
	}

	public void addXp(int xp) {
		this.xp += xp;

		if (this.xp >= level * 1000)
			// TODO: Show level up
			this.level++;
	}

	// Downcasting
	public Guest castToGuest() {
		return new Guest(this);
	}

	public Host castToHost() {
		return new Host(this);
	}

}
