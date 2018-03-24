package user;

import application.Context;

public class Host extends User {

	public Host(User user) {
		super(user);
		
		//Context.getContext().setUser(this);
	}

	Host(Host host) {
		// Copy constructor
		super(host);
		
		//Context.getContext().setUser(this);
	}

	// Upcasting
	public User castToUser() {
		return new User(this);
	}

	// Downcasting
	public Quizmaster castToQuizmaster() {
		return new Quizmaster(this);
	}

}
