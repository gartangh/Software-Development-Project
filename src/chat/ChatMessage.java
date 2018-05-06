package chat;

import java.io.Serializable;

import eventbroker.clientevent.ClientEvent;

@SuppressWarnings("serial")
public class ChatMessage extends ClientEvent implements Serializable {

	public final static String CLIENTTYPE = "CLIENT_CHAT";
	public final static String SERVERTYPE = "SERVER_CHAT";

	private String sender;
	private String receiverType;
	private int quizID;
	
	public ChatMessage(String sender, String message, String receiverType, int quizID) {
		super();
		this.type = CLIENTTYPE;
		this.receiverType = receiverType;
		this.message = message;
		this.sender = sender;
		this.quizID = quizID;
	}

	// Getter
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
