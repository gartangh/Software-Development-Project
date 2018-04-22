package chat;

import java.util.LinkedList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ChatModel {

	private String name;
	private LinkedList<ChatMessage> messages;
	private StringProperty chatTextProperty;

	public ChatModel() {
		this.messages = new LinkedList<ChatMessage>();
		this.chatTextProperty = new SimpleStringProperty();
	}

	// Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addMessage(ChatMessage message) {
		messages.add(message);
	}

	public LinkedList<ChatMessage> getMessages() {
		return this.messages;
	}

	public StringProperty chatTextProperty() {
		return chatTextProperty;
	}

	public void update() {
		// Add the latest message to chatTextArea

		ChatMessage chatMessage = messages.poll();
		if (chatTextProperty.getValue() == null)
			chatTextProperty.setValue(chatMessage.getSender() + ": " + chatMessage.getMessage() + "\n");
		else
			chatTextProperty.setValue(
					chatTextProperty.getValue() + chatMessage.getSender() + ": " + chatMessage.getMessage() + "\n");
	}

}
