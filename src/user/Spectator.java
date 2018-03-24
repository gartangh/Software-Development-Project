package user;

import application.Context;

public class Spectator extends Guest {

	public Spectator(Guest guest) {
		super(guest);

		Context.getContext().setUser(this); // Problems with multi-threating
	}

	// Upcasting
	public Guest castToGuest() {
		return new Guest(this);
	}

}
