package eventbroker.serverevent;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ServerLogInFailEvent extends ServerEvent implements Serializable {

	public final static String EVENTTYPE = "SERVER_LOG_IN_FAIL";

	public ServerLogInFailEvent() {
		super.type = EVENTTYPE;
	}

}
