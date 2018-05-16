package chat;

import java.util.LinkedList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import main.MainContext;

public class ChatModel {

	private String name;
	private LinkedList<ChatMessage> messages;
	private StringProperty chatText;

	// Constructor
	public ChatModel() {
		this.messages = new LinkedList<ChatMessage>();
		this.chatText = new SimpleStringProperty();
	}

	// Getters and setter
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LinkedList<ChatMessage> getMessages() {
		return messages;
	}

	public StringProperty getChatText() {
		return chatText;
	}

	// Adder
	public void addMessage(ChatMessage message) {
		messages.add(message);
	}

	// Method
	public void update() {
		// Add the latest message to chatTextArea
		ChatMessage chatMessage = messages.poll();

		if(chatMessage.getUserID() == MainContext.getContext().getQuiz().getHostID())
			if (chatText.getValue() == null)
				chatText.setValue("[THE_ALMIGHTY_HOST] " + chatMessage.getUsername() + ": " + chatMessage.getMessage() + "\n");
			else
				chatText.setValue(chatText.getValue() + "[THE_ALMIGHTY_HOST] " + chatMessage.getUsername() + ": " + chatMessage.getMessage() + "\n");
		else {
			if (chatText.getValue() == null)
				chatText.setValue("[" + chatMessage.getReceiverType() + "] " + chatMessage.getUsername() + ": " + chatMessage.getMessage() + "\n");
			else
				chatText.setValue(chatText.getValue() + "[" + chatMessage.getReceiverType() + "] " + chatMessage.getUsername() + ": " + chatMessage.getMessage() + "\n");
		}
	}

}
