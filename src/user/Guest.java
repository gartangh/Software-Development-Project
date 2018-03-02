package user;

public class Guest extends User {
	
	public Guest() {
		// Empty default constructor
	}
	
	public Guest(User user) {
		super(user);
	}
	
	// Upcasting
	public User castToUser() {
		return new User(this);
	}
	
	// Downcasting
	public Player castToPlayer() {
		return new Player(this);
	}
	
	public Spectator castToSpectator() {
		return new Spectator(this);
	}
	
}
