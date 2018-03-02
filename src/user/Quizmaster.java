package user;

public class Quizmaster extends Host {

	public Quizmaster() {

	}

	public Quizmaster(Host host) {
		super(host);
	}

	// Upcasting
	public Host castToHost() {
		return new Host(this);
	}

}
