package user.model;

import main.Context;

public class Quizmaster extends Host {

	public Quizmaster(Host host) {
		// Copy constructor
		super(host);
	}

	// Upcasting
	public Host castToHost() {
		Host host = new Host(this);
		
		Context.getContext().setUser(host);
		
		return host;
	}

}
