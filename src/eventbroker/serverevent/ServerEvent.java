package eventbroker.serverevent;

import eventbroker.Event;

@SuppressWarnings("serial")
public class ServerEvent extends Event {

	public final static String EVENTTYPE = "SERVER";

	// Constructor
	public ServerEvent() {
		super.type = EVENTTYPE;
	}

}
