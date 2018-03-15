package user;

import application.Context;

public class Guest extends User {

	Guest(User user) {
		super(user);
		
		Context.getContext().setUser(this);
	}

	Guest(Guest guest) {
		// Copy constructor
		super(guest);
		
		Context.getContext().setUser(this);
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
