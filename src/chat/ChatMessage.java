package chat;

import java.io.Serializable;

import eventbroker.clientevent.ClientEvent;

@SuppressWarnings("serial")
public class ChatMessage extends ClientEvent implements Serializable {

	public final static String CLIENTTYPE = "CLIENT_CHAT";
	public final static String SERVERTYPE = "SERVER_CHAT";

	private String sender;

	public ChatMessage(String sender, String message) {
		super();
		this.type = CLIENTTYPE;
		this.message = message;
		this.sender = sender;
	}

	// Getter
	public String getSender() {
		return sender;
	}

}
