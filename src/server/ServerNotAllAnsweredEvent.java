package server;

public class ServerNotAllAnsweredEvent extends ServerEvent {

	public ServerNotAllAnsweredEvent() {
		super();
		this.type = "SERVER_NOT_ALL_ANSWERED";
	}
	
}
