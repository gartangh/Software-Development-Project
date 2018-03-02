package user;

public class Guest extends User {

	public Guest() {
		// Empty default constructor
	}

	Guest(User user) {
		super(user);
	}

	Guest(Guest guest) {
		super(guest);
		// Copy constructor
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
