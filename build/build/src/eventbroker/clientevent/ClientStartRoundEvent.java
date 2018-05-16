package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientStartRoundEvent extends ClientQuizzerEvent {

	public final static String EVENTTYPE = "CLIENT_START_ROUND";

	// Constructor
	public ClientStartRoundEvent() {
		super.type = EVENTTYPE;
	}

}
