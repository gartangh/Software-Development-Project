package eventbroker.clientevent;

import main.MainContext;

@SuppressWarnings("serial")
public class ClientQuizzerEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_QUIZZER";

	private int quizID;
	private int teamID;

	// Constructor
	public ClientQuizzerEvent() {
		super.type = EVENTTYPE;
		this.quizID = MainContext.getContext().getQuiz().getQuizID();
		this.teamID = MainContext.getContext().getTeam().getTeamID();
	}

	// Getters
	public int getQuizID() {
		return quizID;
	}

	public int getTeamID() {
		return teamID;
	}

}
