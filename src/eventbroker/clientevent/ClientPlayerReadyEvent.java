package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientPlayerReadyEvent extends ClientCaptainReadyEvent {

	public final static String EVENTTYPE = "CLIENT_PLAYER_READY";

	public ClientPlayerReadyEvent(int quizID, int teamID) {
		super(quizID, teamID);
		super.type = EVENTTYPE;
	}

}
