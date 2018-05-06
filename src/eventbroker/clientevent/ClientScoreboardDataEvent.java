package eventbroker.clientevent;

import main.Context;

@SuppressWarnings("serial")
public class ClientScoreboardDataEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_SCOREBOARDDATA";

	private int quizID;

	// Constructor
	public ClientScoreboardDataEvent() {
		super.type = EVENTTYPE;
		this.quizID = Context.getContext().getQuiz().getQuizID();
	}

	// Getters
	public int getQuizID() {
		return quizID;
	}

}
