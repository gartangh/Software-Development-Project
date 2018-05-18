package chat;

import java.io.Serializable;

import eventbroker.clientevent.ClientEvent;

@SuppressWarnings("serial")
public class ChatMessage extends ClientEvent implements Serializable {

	public final static String CLIENTTYPE = "CLIENT_CHAT";
	public final static String SERVERTYPE = "SERVER_CHAT";

	private String username;
	private String message;
	private String receiverType;
	private int quizID;
	
	public ChatMessage(String username, String message, String receiverType, int quizID) {
		super.type = CLIENTTYPE;
		this.username = username;
		this.message = message;
		this.receiverType = receiverType;
		this.quizID = quizID;
	}

	// Getters
	public String getUsername() {
		return username;
	}
	
	public String getMessage() {
		return message;
	}

	public String getReceiverType() {
		return receiverType;
	}

	public int getQuizID() {
		return quizID;
	}

}
