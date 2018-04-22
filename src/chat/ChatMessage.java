package chat;

import java.io.Serializable;

import eventbroker.clientevent.ClientEvent;

@SuppressWarnings("serial")
public class ChatMessage extends ClientEvent implements Serializable {

	public final static String EVENTTYPE = "CLIENT_CHAT";
	public final static String EVENTTYPESERVER = "SERVER_CHAT";
	
	private String sender;

	public ChatMessage(String sender, String message) {
		super();
		this.type = EVENTTYPE;
		this.message = message;
		this.sender = sender;
	}

	// Getters
	public String getSender() {
		return sender;
	}

}
