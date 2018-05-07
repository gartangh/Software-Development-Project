package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientHostReadyEvent extends ClientEvent {

	public final static String EVENTTYPE = "CLIENT_HOST_READY";

	private int quizID;

	// Constructor
	public ClientHostReadyEvent(int quizID) {
		super.type = EVENTTYPE;
		this.quizID = quizID;
	}

	// Getter
	public int getQuizID() {
		return quizID;
	}

}
