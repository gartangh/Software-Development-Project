package network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

import chat.ChatPanel;
import eventbroker.EventBroker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import main.Context;
import main.Main;
import quiz.util.ClientCreateAccountEvent;
import quiz.view.ScoreboardController;
import user.model.User;

public class Client extends Application {

	private static final boolean SHOW_SCOREBOARD = true;

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
		

		// TODO: Remove this test
		User.createAccount("Arthur", "test");
		int port = Integer.parseInt("1026");

		// Start event broker
		EventBroker.getEventBroker().start();

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		chatPanel.getChatModel().setName(Context.getContext().getUser().getUsername());

		// Create new network (Server that listens to incoming
		// connections)
		network = new Network(port, "CLIENT");
		connection = connectToNetwork(InetAddress.getLocalHost(), 1025);
		// --> send event over network
		EventBroker.getEventBroker().addEventListener(network);

		TimeUnit.SECONDS.sleep(1);
		ClientCreateAccountEvent clientCreateEvent = new ClientCreateAccountEvent(user);
		EventBroker.getEventBroker().addEvent(network, clientCreateEvent);

		// Create GUI
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(chatPanel.getContent());

		// Create Scene and show on stage
		Scene chatScene = new Scene(borderPane, 600, 400);
		window.setTitle("Chat");
		window.setScene(chatScene);
		
		if(SHOW_SCOREBOARD) {
			FXMLLoader scoreboardLoader = new FXMLLoader();
			scoreboardLoader.setLocation(Main.class.getResource("../quiz/view/Scoreboard.fxml"));
		
			AnchorPane scoreboardRoot = (AnchorPane) scoreboardLoader.load();
			ScoreboardController scoreboardController = scoreboardLoader.getController();
			//scoreboardController.setMainApp(this);
			Scene scene = new Scene(scoreboardRoot);

			window.setScene(scene);
		}
    
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
