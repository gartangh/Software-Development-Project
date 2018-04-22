package user;

import main.Context;

@SuppressWarnings("serial")
public class Guest extends User {

	Guest(User user) {
		// Copy constructor
		super(user);
	}

	// Upcasting
	public User castToUser() {
		User user = new User(this);

		Context.getContext().setUser(user);

		return user;
	}

	// Downcasting
	public Player castToPlayer() {
		Player player = new Player(this);

		Context.getContext().setUser(player);

		return player;
	}

}
