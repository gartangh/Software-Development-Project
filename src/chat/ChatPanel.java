package chat;

import java.io.IOException;

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

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(ChatPanel.class.getResource("ChatPane.fxml"));
			chatPanel.content = (AnchorPane) loader.load();
			chatPanel.chatController = loader.getController();
			chatPanel.chatModel = chatPanel.chatController.getChatModel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
