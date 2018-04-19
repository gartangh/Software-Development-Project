package eventbroker.clientevent;

import eventbroker.Event;
import main.Context;

@SuppressWarnings("serial")
public class ClientEvent extends Event {

	public final static String EVENTTYPE = "CLIENT_USER";

	private int userID;

	// Constructor
	public ClientEvent() {
		super.type = EVENTTYPE;
		this.userID = Context.getContext().getUser().getUserID();
	}

	// Getter
	public int getUserID() {
		return userID;
	}
}
