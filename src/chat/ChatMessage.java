package chat;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChatMessage extends eventbroker.Event implements Serializable {

	public final static String TYPE_CHAT = "CHAT";

	private String sender;

	public ChatMessage(String sender, String message) {
		super(TYPE_CHAT, message);
		this.sender = sender;
	}

	// Getters
	public String getSender() {
		return sender;
	}

}
