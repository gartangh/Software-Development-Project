package eventbroker.clientevent;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ClientGetQuizzesEvent extends ClientEvent implements Serializable {

	public final static String EVENTTYPE = "CLIENT_GET_QUIZZES";

	// Constructor
	public ClientGetQuizzesEvent() {
		super.type = EVENTTYPE;
	}

}
