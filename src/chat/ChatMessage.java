package chat;

import java.io.Serializable;
import quiz.util.UserEvent;

@SuppressWarnings("serial")
public class ChatMessage extends UserEvent implements Serializable {

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
