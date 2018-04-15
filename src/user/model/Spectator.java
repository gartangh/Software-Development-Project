package user.model;

import main.Context;

@SuppressWarnings("serial")
public class Spectator extends Guest {

	public Spectator(Guest guest) {
		// Copy constructor
		super(guest);
	}

	// Upcasting
	@Override
	public void castToGuest() {
		Context.getContext().setUser(new Guest(this));
	}

}
