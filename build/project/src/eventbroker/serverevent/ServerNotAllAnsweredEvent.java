package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerNotAllAnsweredEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_NOT_ALL_ANSWERED";

	public ServerNotAllAnsweredEvent() {
		super.type = EVENTTYPE;
	}

}
