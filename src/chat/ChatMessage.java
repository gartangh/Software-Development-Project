package chat;

import java.io.Serializable;
import quiz.util.UserEvent;

@SuppressWarnings("serial")
public class ChatMessage extends UserEvent implements Serializable {

	public final static String TYPE_CHAT = "CLIENT_CHAT";

	private String sender;

	public ChatMessage(String sender, String message) {
		super();
		this.type = "TYPE_CHAT";
		this.message = message;
		this.sender = sender;
	}

	// Getters
	public String getSender() {
		return sender;
	}

}
