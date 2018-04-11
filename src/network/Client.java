package network;

import java.net.InetAddress;

import chat.ChatPanel;
import eventbroker.EventBroker;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Client extends Application{

	private Stage window;
	
	private static Network network;

	public static void main(String[] args) {
		launch(args);
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		// Valid input
		String name = "Arthur";
		int port = Integer.parseInt("1235");

		// Start event broker
		EventBroker.getEventBroker().start();

		// Create new network (Server that listens to incoming
		// connections)
		network = new Network(port);
		connectToNetwork(InetAddress.getLocalHost(), 1234);
		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		chatPanel.getChatModel().setName(name);

		// Create GUI
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(chatPanel.getContent());
		
		// Create Scene and show on stage
		Scene chatScene = new Scene(borderPane, 600, 400);
		window.setTitle("Chat");
		window.setScene(chatScene);
		
		window.show();
	}
	

	public static void connectToNetwork(InetAddress ip, int port) {
		network.connect(ip, port);
	}
	


	public static Network getNetwork() {
		return network;
	}
}
