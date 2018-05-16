package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientHostReadyEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_HOST_READY";

	// Constructor
	public ClientHostReadyEvent(int quizID) {
		super.type = EVENTTYPE;
	}

}
