package chat;

import java.io.Serializable;

import eventbroker.clientevent.ClientEvent;

@SuppressWarnings("serial")
public class ChatMessage extends ClientEvent implements Serializable {

	private String sender;

	public ChatMessage(String sender, String message) {
		super();
		this.type = "CLIENT_CHAT";
		this.message = message;
		this.sender = sender;
	}

	// Getters
	public String getSender() {
		return sender;
	}

}
