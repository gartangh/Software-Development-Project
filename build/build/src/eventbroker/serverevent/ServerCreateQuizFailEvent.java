package eventbroker.serverevent;

@SuppressWarnings("serial")
public class ServerCreateQuizFailEvent extends ServerEvent {

	public final static String EVENTTYPE = "SERVER_CREATE_QUIZ_FAIL";

	// Constructor
	public ServerCreateQuizFailEvent() {
		super.type = EVENTTYPE;
	}

}
