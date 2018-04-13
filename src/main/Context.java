package main;

import network.Network;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.AnswerVoteModel;
import quiz.model.Question;
import user.model.User;

public class Context {

	// Singleton
	private static Context context = new Context();

	private User user;
	private Quiz quiz;
	private int teamID;
	private boolean captain = true;
	private Question question;
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
