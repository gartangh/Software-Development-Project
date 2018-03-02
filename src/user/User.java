package user;

public class User {

	protected String username;
	protected String password;
	protected Integer level;
	protected Long xp;

	protected User() {
		// Empty default constructor
	}

	public User(String username, String password) {
		// TODO: check if username is unique
		if (username.matches("[a-zA-Z0-9]{4,8}"))
				this.username = username;
		else
			// TODO: Go back and show error

		if (password.matches("[a-zA-Z0-9]{4,8}"))
			this.password = password;
		else
			// TODO: Go back and show error 
		
		this.level = 1;
		this.xp = 0L;
	}
	
	protected User(User user) {
		// Copy constructor
		this.username = user.username;
		this.password = user.password;
		this.level = user.level;
		this.xp = user.xp;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setUsername(String username) {
		// TODO: check if username is valid and unique
		this.username = username;
	}

	public void setPassword(String password1, String password2) {
		if (password1 == password2) {
			// TODO: check if password is valid
			this.password = password1;
		}
	}
	
	public Integer getLevel() {
		return level;
	}
	
	public void levelUp() {
		this.level++;
	}
	
	public Long getXp() {
		return xp;
	}
	
	public void addXp(Integer xp) {
		this.xp += xp;
		
		if (this.xp >= level * 1000) {
			this.xp -= level * 1000;
			this.level++;
		}
	}
	
	// Downcasting
	public Guest castToGuest() {
		return new Guest(this);
	}
	
	public Host castToHost() {
		return new Host(this);
	}

}
