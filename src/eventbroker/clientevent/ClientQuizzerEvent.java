package eventbroker.clientevent;

import main.MainContext;

@SuppressWarnings("serial")
public class ClientQuizzerEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_QUIZZER";

	private int teamID;
	private int quizID;

	// Constructor
	public ClientQuizzerEvent() {
		super.type = EVENTTYPE;
		this.teamID = MainContext.getContext().getTeamID();
		this.quizID = MainContext.getContext().getQuiz().getQuizID();
	}

	// Getters
	public int getTeamID() {
		return teamID;
	}

	public int getQuizID() {
		return quizID;
	}

}
