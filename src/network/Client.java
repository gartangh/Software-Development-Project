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
	
	private static User user;
	
	public static void main(String[] args) {
		launch(args);
	}

	
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		
		// Valid input
		String name = "Arthur";
		user = new User(0, name, "test");
		Context.getContext().setUser(user);
		int port = Integer.parseInt("1028");

		// Start event broker
		EventBroker.getEventBroker().start();

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		chatPanel.getChatModel().setName(name);

		// Create new network (Server that listens to incoming
		// connections)
		network = new Network(port, "CLIENT");
		connection = connectToNetwork(InetAddress.getLocalHost(), 1025);
				// --> send event over network
		EventBroker.getEventBroker().addEventListener(network);


		TimeUnit.SECONDS.sleep(1);
		ClientCreateEvent clientCreateEvent = new ClientCreateEvent(user);
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
	

	public static User getUser() {
		return user;
	}


	public static void setUser(User newUser) {
		user = newUser;
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
