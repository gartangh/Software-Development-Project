package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerStartRoundEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_START_ROUND";

	// Constructor
	public ServerStartRoundEvent() {
		super.type = EVENTTYPE;
	}

}
