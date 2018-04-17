package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import chat.ChatPanel;
import eventbroker.EventBroker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.view.MenuController;
import main.view.RootLayoutController;
import network.Network;
import quiz.view.CreateQuizController;
import quiz.view.JoinQuizController;
import quiz.view.JoinTeamController;
import quiz.view.Quizroom;
import quiz.view.ScoreboardController;
import user.view.LogInController;
import user.view.ModeSelectorController;

public class Main extends Application {

	public final static boolean DEBUG = true;
	private final static int SERVERPORT = 1025;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private Quizroom quizroom;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		// TODO: Randomize port
		Network network = new Network(1026, "CLIENT");
		Context.getContext().setNetwork(network);

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		// chatPanel.getChatModel().setName(Context.getContext().getUser().getUsername());

		try {
			network.connect(InetAddress.getLocalHost(), SERVERPORT);

			// --> send event over network
			EventBroker.getEventBroker().addEventListener(network);

			// Start event broker
			EventBroker.getEventBroker().start();

			initRootLayout();

			showLogInScene();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	// Getters
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	private void initRootLayout() {
		try {
			// Load root layout from fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			// Give the controller access to the main
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Show scenes
	public void showLogInScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../user/view/LogIn.fxml"));
			VBox logIn = (VBox) loader.load();
			LogInController controller = loader.getController();
			controller.setMainApp(this);

			rootLayout.setCenter(logIn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showModeSelectorScene() {
		try {
			FXMLLoader modeSelectorLoader = new FXMLLoader();
			modeSelectorLoader.setLocation(Main.class.getResource("../user/view/ModeSelector.fxml"));
			VBox modeSelector = (VBox) modeSelectorLoader.load();
			ModeSelectorController modeSelectorController = modeSelectorLoader.getController();
			modeSelectorController.setMainApp(this);
			rootLayout.setCenter(modeSelector);

			FXMLLoader menuLoader = new FXMLLoader();
			menuLoader.setLocation(Main.class.getResource("view/Menu.fxml"));
			AnchorPane menu = (AnchorPane) menuLoader.load();
			MenuController menuController = menuLoader.getController();
			menuController.setMainApp(this);
			rootLayout.setTop(menu);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showCreateQuizScene() {
		try {
			FXMLLoader createQuizLoader = new FXMLLoader();
			createQuizLoader.setLocation(Main.class.getResource("../quiz/view/CreateQuiz.fxml"));
			VBox createQuiz = (VBox) createQuizLoader.load();
			CreateQuizController createQuizController = createQuizLoader.getController();
			createQuizController.setMainApp(this);
			rootLayout.setCenter(createQuiz);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showJoinQuizScene() {
		try {
			FXMLLoader joinQuizLoader = new FXMLLoader();
			joinQuizLoader.setLocation(Main.class.getResource("../quiz/view/JoinQuiz.fxml"));
			VBox joinQuiz = (VBox) joinQuizLoader.load();
			JoinQuizController joinQuizController = joinQuizLoader.getController();
			joinQuizController.setMainApp(this);
			rootLayout.setCenter(joinQuiz);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showJoinTeamScene() {
		try {
			FXMLLoader joinTeamLoader = new FXMLLoader();
			joinTeamLoader.setLocation(Main.class.getResource("../quiz/view/JoinTeam.fxml"));
			VBox joinTeam = (VBox) joinTeamLoader.load();
			JoinTeamController joinTeamController = joinTeamLoader.getController();
			joinTeamController.setMainApp(this);
			rootLayout.setCenter(joinTeam);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showQuizroomScene() {
		// Quizroom is created
		try {
			quizroom = new Quizroom(Context.getContext().getQuiz(), this);
			rootLayout.setCenter(quizroom.getContent());
		} catch (IOException e) {
			// TODO: Go back and show error
			e.printStackTrace();
		}
	}

	public void showScoreboardScene() {
		try {
			FXMLLoader scoreboardLoader = new FXMLLoader();
			scoreboardLoader.setLocation(Main.class.getResource("../quiz/view/Scoreboard.fxml"));
			AnchorPane scoreboardRoot = (AnchorPane) scoreboardLoader.load();
			ScoreboardController scoreboardController = scoreboardLoader.getController();
			scoreboardController.setMainApp(this);
			rootLayout.setCenter(scoreboardRoot);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
