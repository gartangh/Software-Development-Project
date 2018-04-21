package chat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
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

	private ChatEventHandler chatEventHandler;
	private ChatModel chatModel;

	ArrayList<String> prohibitedWords = new ArrayList<>();

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	public ChatController() {
		this.chatEventHandler = new ChatEventHandler();
		this.chatModel = new ChatModel();

		try {
			// Substring is to remove file:/ before resource
			BufferedReader bufferedReader = new BufferedReader(new FileReader(Main.class.getResource("../chat/swearWords.txt").toString().substring(6)));
			
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				prohibitedWords.add(line);
			}
			
			bufferedReader.close();
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

	// TODO: Change: for all prohibitedWords do: if contains, loop! else next
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

	@FXML
	private void initialize() {
		EventBroker.getEventBroker().addEventListener(chatEventHandler);

		chatTextArea.textProperty().bind(chatModel.chatTextProperty());

		//chatTextArea.setFont(Font.loadFont(Paths.get(".").toAbsolutePath().normalize().toString() + "\\Files\\OpenSansEmoji.ttf", 15));
	}

	// Inner class
	private class ChatEventHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ChatMessage chatMessage;

			String type = event.getType();
			switch (type) {
			// TODO: Remove this (Should be updated locally, without the event
			// broker)
			case "CLIENT_CHAT":
				chatMessage = (ChatMessage) event;
				chatModel.addMessage(chatMessage);

				// Update local GUI
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update messages in chatTextArea
						chatModel.update();
					}
				});
				System.out.println("Event received and handled: " + type);
				break;

			case "SERVER_CHAT":
				chatMessage = (ChatMessage) event;
				chatModel.addMessage(chatMessage);

				// Update local GUI
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						// Update messages in chatTextArea
						chatModel.update();
					}
				});
				System.out.println("Event received and handled: " + type);
				break;

			default:
				System.out.println("Event received but left unhandled: " + type);
			}
		}

	}

}
