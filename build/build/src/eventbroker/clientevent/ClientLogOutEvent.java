package eventbroker.clientevent;

@SuppressWarnings("serial")
public class ClientLogOutEvent extends ClientEvent {
	
	public final static String EVENTTYPE = "CLIENT_LOG_OUT";
	
	// Constructor
	public ClientLogOutEvent() {
		super.type = EVENTTYPE;
	}
}
