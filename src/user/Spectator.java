package user;

public class Spectator extends Guest {

	public Spectator() {

	}

	public Spectator(Guest guest) {
		super (guest);
	}

	// Upcasting
	public Guest castToGuest() {
		return new Guest(this);
	}

}
