package eventbroker.clientevent;

import main.Context;

@SuppressWarnings("serial")
public class ClientQuizzerEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_QUIZZER";

	private int teamID;
	private int quizID;

	// Constructor
	public ClientQuizzerEvent() {
		super.type = EVENTTYPE;
		this.teamID = Context.getContext().getTeamID();
		this.quizID = Context.getContext().getQuiz().getQuizID();
	}

	// Getters
	public int getTeamID() {
		return teamID;
	}

	public int getQuizID() {
		return quizID;
	}

}
