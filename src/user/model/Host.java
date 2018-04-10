package user.model;

import main.Context;

@SuppressWarnings("serial")
public class Host extends User {

	public Host(User user) {
		// Copy constructor
		super(user);
	}

	// Upcasting
	public User castToUser() {
		User user = new User(this);

		Context.getContext().setUser(user);

		return user;
	}

	// Downcasting
	public Quizmaster castToQuizmaster() {
		Quizmaster quizmaster = new Quizmaster(this);

		Context.getContext().setUser(quizmaster);

		return quizmaster;
	}

}
