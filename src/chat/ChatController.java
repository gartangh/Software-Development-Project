package chat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
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
import javafx.scene.text.Font;
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
		boolean team = true;
		
		if(message.length() > 1) {
			if(message.charAt(0) == '*') {
				message = message.substring(1);
					team = false;
			}
		} else if(message.length() == 1) {
			if(message.charAt(0) == '*')
				message = null;
		}


		if((Context.getContext().getTeamID() == -1))
			team = false;
		

		if (message != null && message.length() > 0) {
			message = checkMessage(message);
			String finalMessage = null;
			if(message.contains(":)")) {
				for(int i=0;i<message.length()-1;i++) {
					if(message.charAt(i)==':' && message.charAt(i+1) == ')') {
						finalMessage = message.substring(0, i);
						finalMessage += '\u263A';
						if(i+2 < message.length())
							finalMessage += message.substring(i+2);
					}
				}
			}
			if(finalMessage != null)
				sendMessage(finalMessage, team);
			else sendMessage(message, team);
		}
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

	public void sendMessage(String message, boolean team) {
		// Publish to the event broker
		if(team)
			publishEvent(new ChatMessage(Context.getContext().getUser().getUsername(), message, "TEAM", Context.getContext().getQuiz().getQuizID()));
		else
			publishEvent(new ChatMessage(Context.getContext().getUser().getUsername(), message, "ALL", Context.getContext().getQuiz().getQuizID()));

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
