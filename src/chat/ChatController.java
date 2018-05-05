package chat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
import main.Context;
import main.Main;

final public class ChatController extends EventPublisher {

	@FXML
	private TextArea chatTextArea;
	@FXML
	private TextField chatTextField;
	@FXML
	private Button chatSendButton;

	private ChatModel chatModel = new ChatModel();
	private ChatHandler chatHandler = new ChatHandler();
	private ArrayList<String> prohibitedWords = new ArrayList<>();

	// Getters
	public ChatModel getChatModel() {
		return chatModel;
	}

	// Methods
	@FXML
	private void initialize() {
		EventBroker.getEventBroker().addEventListener(ChatMessage.SERVERTYPE, chatHandler);

		chatTextArea.textProperty().bind(chatModel.getChatText());

		try {
			// Substring is to remove file:/ before resource
			BufferedReader bufferedReader = new BufferedReader(
					new FileReader(Main.class.getResource("../chat/swearWords.txt").toString().substring(6)));

			String line;
			while ((line = bufferedReader.readLine()) != null)
				prohibitedWords.add(line);

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handle(ActionEvent e) {
		// Get message from chatTextField
		String message = chatTextField.getText();
		if (message != null && message.length() > 0)
			sendMessage(checkMessage(message));
	}

	private String checkMessage(String message) {
		String[] words = message.toLowerCase().split(" ");
		String censored = "";
		for (int i = 0; i < words.length; i++) {
			if (prohibitedWords.contains(words[i]))
				// Found a prohibited word
				words[i] = String.join("", Collections.nCopies(words[i].length(), "*"));

			censored += words[i] + " ";
		}

		return censored;
	}

	public void sendMessage(String message) {
		// Publish to the event broker
		publishEvent(new ChatMessage(Context.getContext().getUser().getUsername(), message));

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

	// Inner class
	private class ChatHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ChatMessage chatMessage = (ChatMessage) event;
			chatModel.addMessage(chatMessage);

			// Update local GUI
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					// Update messages in chatTextArea
					chatModel.update();
				}
			});
		}

	}

}
