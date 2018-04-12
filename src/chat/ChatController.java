package chat;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
import quiz.util.ClientCreateEvent;
import server.ServerContext;
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

	public ChatController() {
		this.chatEventHandler = new ChatEventHandler();
		this.chatModel = new ChatModel();
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
		if (message != null && message.length() > 0)
			sendMessage(message);
	}

	public void sendMessage(String message) {
		// Publish to the event broker
		publishEvent(new ChatMessage(chatModel.getName(), message));

		// Update local GUI
		Platform.runLater(new Runnable() {
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
			
			switch(e.getType()) {
				case "CLIENT_CREATE":
					Client.getNetwork().handleEvent(e);
					break;
				case "SERVER_RETURN_USERID":
					ServerReturnUserIDEvent serverCreate = (ServerReturnUserIDEvent) e;
					User user = main.Context.getContext().getUser();
					user.setUserID(serverCreate.getUserID());
					main.Context.getContext().setUser(user);
					System.out.println("Nailed it");
					break;
					
				case "CLIENT_CHAT":
					chatMessage = (ChatMessage) e;
					chatModel.addMessage(chatMessage);
					
					// Update local GUI
					Platform.runLater(new Runnable() {
						public void run() {
							// Update messages in chatTextArea
							chatModel.update();
						}
					});
					break;
					
				case "SERVER_CHAT":
					chatMessage = (ChatMessage) e;
					chatModel.addMessage(chatMessage);
					
					// Update local GUI
					Platform.runLater(new Runnable() {
						public void run() {
							// Update messages in chatTextArea
							chatModel.update();
						}
					});
					break;
					
			}
		}

		@Override
		public void handleEvent(Event e, ArrayList<Integer> destinations) {}

	}

}
