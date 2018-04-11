package chat;

import java.io.IOException;

import eventbroker.EventBroker;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class ChatPanel {

	private AnchorPane content;
	private ChatModel chatModel;
	private ChatController chatController;

	public ChatPanel() {
		// Empty default constructor
	}

	public static ChatPanel createChatPanel() {
		ChatPanel chatPanel = new ChatPanel();

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ChatPanel.class.getResource("ChatPane.fxml"));

		try {
			chatPanel.content = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		chatPanel.chatController = loader.getController();
		chatPanel.chatModel = chatPanel.chatController.getChatModel();

		EventBroker.getEventBroker().addEventListener(ChatMessage.TYPE_CHAT,
				chatPanel.chatController.getChatEventHandler());

		return chatPanel;
	}

	// Getters and setters
	public AnchorPane getContent() {
		return content;
	}

	public void setContent(AnchorPane content) {
		this.content = content;
	}

	public ChatModel getChatModel() {
		return chatModel;
	}

	public void setChatModel(ChatModel chatModel) {
		this.chatModel = chatModel;
	}

	public ChatController getChatController() {
		return chatController;
	}

	public void setChatController(ChatController chatController) {
		this.chatController = chatController;
	}

}
