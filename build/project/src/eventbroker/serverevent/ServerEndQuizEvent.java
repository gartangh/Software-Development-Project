package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerEndQuizEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_END_QUIZ";

	// Constructor
	public ServerEndQuizEvent() {
		super.type = EVENTTYPE;
	}

}
