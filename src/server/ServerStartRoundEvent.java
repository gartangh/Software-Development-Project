package server;

@SuppressWarnings("serial")
public class ServerStartRoundEvent extends ServerEvent {

	public ServerStartRoundEvent() {
		super();
		this.type = "SERVER_START_ROUND";
	}
	
}
