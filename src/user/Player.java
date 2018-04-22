package user;

import main.Context;

@SuppressWarnings("serial")
public class Player extends Guest {

	private int roundScore;
	private int quizScore;

	public Player(Guest guest) {
		// Copy constructor
		super(guest);

		this.roundScore = 0;
		this.quizScore = 0;
	}

	// Getters
	public int getRoundScore() {
		return roundScore;
	}

	public int getQuizScore() {
		return quizScore;
	}

	// Setters
	public void resetRoundScore() {
		this.roundScore = 0; // Should be called when a new round starts
	}

	public void resetQuizScore() {
		this.quizScore = 0; // Should be called when a new quiz starts
	}

	public void addRoundScore(int roundScore) {
		this.roundScore += roundScore; // Should be called after each question
		this.quizScore += roundScore;
	}

	// Upcasting
	@Override
	public void castToGuest() {		
		Context.getContext().setUser(new Guest(this));
	}

}
