package user;

import application.Context;

public class Quizmaster extends Host {

	public Quizmaster(Host host) {
		super(host);
		
		//Context.getContext().setUser(this);
	}

	// Upcasting
	public Host castToHost() {
		return new Host(this);
	}

}
