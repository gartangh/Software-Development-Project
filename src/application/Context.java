package application;

import quiz.Quiz;
import user.User;

public class Context {

	private static Context context = new Context(); // Singleton
	
	private User user;
	private Quiz quiz;

	private Context() {
		// Empty default constructor
	}
	
	public static Context getContext() {
		return context;
	}

	// Getters
	public User getUser() {
		return user;
	}
	
	public Quiz getQuiz() {
		return quiz;
	}

	// Setters
	public void setUser(User user) {
		this.user = user;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

}
