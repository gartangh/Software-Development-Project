package main;

import network.Network;
import quiz.model.Quiz;
import quiz.model.User;
import quiz.model.Question;

public class MainContext {

	// Singleton
	private static MainContext context = new MainContext();

	private User user;
	private Quiz quiz;
	private int teamID = -1;
	private Question question;
	private Network network;

	// Getters and setters
	/**
	 * Gets the context.
	 *
	 * @return the context
	 */
	public static MainContext getContext() {
		return context;
	}

	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the user.
	 *
	 * @param user
	 *            the new user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Gets the quiz.
	 *
	 * @return the quiz
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * Sets the quiz.
	 *
	 * @param quiz
	 *            the new quiz
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	/**
	 * Gets the team ID.
	 *
	 * @return the team ID
	 */
	public int getTeamID() {
		return teamID;
	}

	/**
	 * Sets the team ID.
	 *
	 * @param teamID
	 *            the new team ID
	 */
	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	/**
	 * Gets the question.
	 *
	 * @return the question
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * Sets the question.
	 *
	 * @param question
	 *            the new question
	 */
	public void setQuestion(Question question) {
		this.question = question;
		this.quiz.resetVotes();
	}

	/**
	 * Gets the network.
	 *
	 * @return the network
	 */
	public Network getNetwork() {
		return network;
	}

	/**
	 * Sets the network.
	 *
	 * @param network
	 *            the new network
	 */
	public void setNetwork(Network network) {
		this.network = network;
	}

}
