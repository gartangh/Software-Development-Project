package main;

import network.Network;
import quiz.model.Quiz;
import quiz.model.User;
import quiz.util.RoundType;
import quiz.model.Question;

public class Context {

	// Singleton
	private static Context context = new Context();

	private User user;
	private Quiz quiz;
	private int teamID;
	private Question question;
	private boolean answered = false;
	private RoundType roundType;
	private Network network;

	// Getters and setters
	public static Context getContext() {
		return context;
	}

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

	public int getTeamID() {
		return teamID;
	}

	public void setTeamID(int teamID) {
		this.teamID = teamID;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
		this.quiz.resetVotes();
	}
	
	public boolean isAnswered() {
		return this.answered;
	}
	
	public void setAnswered(boolean answered) {
		this.answered = answered;
	}
	
	public RoundType getRoundType() {
		return roundType;
	}
	
	public void setRoundType(RoundType roundType) {
		this.roundType = roundType;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

}
