package user;

import main.Context;

@SuppressWarnings("serial")
public class Quizmaster extends Host {

	public Quizmaster(Host host) {
		// Copy constructor
		super(host);
	}

	// Upcasting
	@Override
	public void castToHost() {		
		Context.getContext().setUser(new Host(this));
	}

}
