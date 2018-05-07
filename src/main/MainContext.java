package main;

import network.Network;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.User;
import quiz.model.Question;

public class MainContext {

	// Singleton
	private static MainContext context = new MainContext();

	private User user;
	private Quiz quiz;
	private Team team;
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
	 * Gets the team.
	 *
	 * @return the team
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * Sets the team.
	 *
	 * @param team
	 *            the new team
	 */
	public void setTeam(Team team) {
		this.team = team;
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
