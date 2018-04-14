package user.model;

import main.Context;

@SuppressWarnings("serial")
public class Quizmaster extends Host {

	public Quizmaster(Host host) {
		// Copy constructor
		super(host);
	}

	// Upcasting
	@Override
	public Host castToHost() {
		Host host = new Host(this);
		
		Context.getContext().setUser(host);
		
		return host;
	}

}
