package chat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import network.Client;
import server.ServerReturnUserIDEvent;
import user.model.User;

final public class ChatController extends EventPublisher {

	@FXML
	private TextArea chatTextArea;
	@FXML
	private TextField chatTextField;
	@FXML
	private Button chatSendButton;

	private ChatEventHandler chatEventHandler;
	private ChatModel chatModel;

	ArrayList<String> prohibitedWords = new ArrayList<>();

	public ChatController() {
		this.chatEventHandler = new ChatEventHandler();
		this.chatModel = new ChatModel();
		try (BufferedReader br = new BufferedReader(new FileReader("./Files/swearWords.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				prohibitedWords.add(line);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Getters
	public ChatEventHandler getChatEventHandler() {
		return chatEventHandler;
	}

	public ChatModel getChatModel() {
		return chatModel;
	}

	/**
	 * Called when the user clicks on the send button.
	 */
	@FXML
	public void handle(ActionEvent e) {
		// Get message from chatTextField
		String message = chatTextField.getText();
		if (message != null && message.length() > 0) {
			message = checkMessage(message);
			sendMessage(message);
		}
	}

	// TO DO: Change: for all prohibitedWords do: if contains, loop! else next
	// word => faster!
	private String checkMessage(String message) {
		int lengthMessage = message.length();
		String oldMessage = message;
		String newMessage = message.toLowerCase();
		for (int k = 0; k < prohibitedWords.size(); k++) {
			if (newMessage.contains(prohibitedWords.get(k)))
				for (int i = 0; i < lengthMessage - 1; i++)
					for (int j = i + 1; j <= lengthMessage; j++)
						if (newMessage.substring(i, j).equals(prohibitedWords.get(k))) {
							newMessage = oldMessage.substring(0, i);
							for (int l = 0; l < j - i; l++)
								newMessage += "*";
							if (j < lengthMessage)
								newMessage += oldMessage.substring(j);
							oldMessage = newMessage;
						}
		}
		String tempMessage = newMessage;
		newMessage = newMessage.substring(0, 1).toUpperCase();
		if (tempMessage.length() > 1)
			newMessage += tempMessage.substring(1);
		return newMessage;
	}

	public void sendMessage(String message) {
		// Publish to the event broker
		publishEvent(new ChatMessage(chatModel.getName(), message));

		// Update local GUI
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// Clear chatTextField
				chatTextField.setText("");
			}
		});

		EventBroker eventBroker = EventBroker.getEventBroker();
		synchronized (eventBroker) {
			eventBroker.setProceed(true);
			eventBroker.notifyAll();
		}
	}

	@FXML
	private void initialize() {
		chatTextArea.textProperty().bind(chatModel.chatTextProperty());
	}

	// Inner class
	private class ChatEventHandler implements EventListener {

		@Override
		public void handleEvent(Event e) {
			ChatMessage chatMessage;

			switch (e.getType()) {
			case "CLIENT_CREATE":
				e.setType("SERVER_CLIENT_CREATE");
				publishEvent(e);
				System.out.println("Event received and handled: " + e.getType());
				break;
			case "SERVER_RETURN_USERID":
				ServerReturnUserIDEvent serverCreate = (ServerReturnUserIDEvent) e;
				User user = Client.getUser();
				// TODO: What does this case? Is it trying to change the primary key of the user?
				//user.setUserID(serverCreate.getUsername());
				Client.setUser(user);
				Client.getNetwork().getUserIDConnectionIDMap().put(serverCreate.getUsername(), 0);
				System.out.println("Event received and handled: " + e.getType());
				break;
			case "CLIENT_CHAT":
				chatMessage = (ChatMessage) e;
				chatModel.addMessage(chatMessage);

				// Update local GUI
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update messages in chatTextArea
						chatModel.update();
					}
				});
				System.out.println("Event received and handled: " + e.getType());
				break;
			case "SERVER_CHAT":
				chatMessage = (ChatMessage) e;
				chatModel.addMessage(chatMessage);

				// Update local GUI
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update messages in chatTextArea
						chatModel.update();
					}
				});
				System.out.println("Event received and handled: " + e.getType());
				break;
			}
		}

	}

}
