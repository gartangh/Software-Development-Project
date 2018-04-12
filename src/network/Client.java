package network;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import chat.ChatPanel;
import eventbroker.EventBroker;
import eventbroker.EventPublisher;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Context;
import quiz.util.ClientCreateEvent;
import server.ServerContext;
import user.model.User;

public class Client extends Application {

	private Stage window;
	
	private static Network network;
	private static Connection connection;
	
	public static void main(String[] args) {
		launch(args);
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		// Valid input
		String name = "Arthur";
		User user = new User(0, name, "test");
		Context.getContext().setUser(user);
		int port = Integer.parseInt("1029");

		// Start event broker
		EventBroker.getEventBroker().start();

		// Create new network (Server that listens to incoming
		// connections)
		network = new Network(port);
		connection = connectToNetwork(InetAddress.getLocalHost(), 1025);
		ClientCreateEvent clientCreateEvent = new ClientCreateEvent(user, connection);
				// --> send event over network
		EventBroker.getEventBroker().addEventListener(network);

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		chatPanel.getChatModel().setName(name);

		TimeUnit.SECONDS.sleep(1);
		EventBroker.getEventBroker().addEvent(network, clientCreateEvent);
		
		// Create GUI
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(chatPanel.getContent());
		
		// Create Scene and show on stage
		Scene chatScene = new Scene(borderPane, 600, 400);
		window.setTitle("Chat");
		window.setScene(chatScene);
		
		window.show();
	}
	

	public static Connection connectToNetwork(InetAddress ip, int port) {
		return network.connect(ip, port);
	}
	


	public static Network getNetwork() {
		return network;
	}
	
	public static Connection getConnection() {
		return connection;
	}
}
