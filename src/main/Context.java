package main;

import network.Network;
import quiz.Quiz;
import user.model.User;

public class Context {

	// Singleton
	private static Context context = new Context();

	private User user;
	private Quiz quiz;
	private Network network;

	// Constructors
	private Context() {
		// Empty default constructor
	}

	// Getters and setters
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Quiz getQuiz() {
		return quiz;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	public static Context getContext() {
		return context;
	}

}
