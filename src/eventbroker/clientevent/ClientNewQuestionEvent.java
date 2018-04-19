package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientNewQuestionEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_NEW_QUESTION";

	// Constructor
	public ClientNewQuestionEvent() {
		super.type = EVENTTYPE;
	}

}