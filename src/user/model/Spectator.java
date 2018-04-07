package user.model;

import main.Context;

public class Spectator extends Guest {

	public Spectator(Guest guest) {
		// Copy constructor
		super(guest);
	}

	// Upcasting
	public Guest castToGuest() {
		Guest guest = new Guest(this);
		
		Context.getContext().setUser(guest);
		
		return guest;
	}

}
