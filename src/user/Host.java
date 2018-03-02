package user;

public class Host extends User {
	
	public Host() {
		// Empty default constructor
	}
	
	public Host(User user) {
		super(user);
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
