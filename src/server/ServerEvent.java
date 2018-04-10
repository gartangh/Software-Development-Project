package server;

import eventbroker.Event;

@SuppressWarnings("serial")
public class ServerEvent extends Event{
	
	public ServerEvent() {
		super();
		this.type = "SERVER";
	}
}
