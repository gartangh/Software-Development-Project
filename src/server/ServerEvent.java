package server;

import eventbroker.Event;

@SuppressWarnings("serial")
public class ServerEvent extends Event{

	public ServerEvent() {
		this.type = "SERVER";
		this.message = "";
	}
}
