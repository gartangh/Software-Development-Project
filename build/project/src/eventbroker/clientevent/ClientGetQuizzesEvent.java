package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientGetQuizzesEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_GET_QUIZZES";

	// Constructor
	public ClientGetQuizzesEvent() {
		super.type = EVENTTYPE;
	}

}
