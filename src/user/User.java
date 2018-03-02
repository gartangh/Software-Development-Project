package user;

public class User {

	private String username;
	private String password;
	private int level;
	private long xp;

	User() {
		// Empty default constructor
	}

	public User(String username, String password) {
		// TODO: check uniqueness of username
		if (username.matches("[a-zA-Z0-9]{4,8}"))
			this.username = username;
		else {
			// TODO: Go back and show error
		}

		if (password.matches("[a-zA-Z0-9]{4,8}"))
			this.password = password;
		else {
			// TODO: Go back and show error
		}

		this.level = 1;
		this.xp = 0L;
	}

	User(User user) {
		// Copy constructor
		this.username = user.username;
		this.password = user.password;
		this.level = user.level;
		this.xp = user.xp;
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
