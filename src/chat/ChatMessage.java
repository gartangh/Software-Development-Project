package chat;

import java.io.Serializable;
import quiz.util.UserEvent;

@SuppressWarnings("serial")
public class ChatMessage extends UserEvent implements Serializable {

	private String sender;
	private String receiverType;
	private int quizID;
	
	public ChatMessage(String sender, String message, String receiverType, int quizID) {
		super();
		this.type = "CLIENT_CHAT";
		this.receiverType = receiverType;
		this.message = message;
		this.sender = sender;
		this.quizID = quizID;
	}

	// Getters
	public String getSender() {
		return sender;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public int getQuizID() {
		return quizID;
	}

}
