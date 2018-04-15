package main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.view.MenuController;
import main.view.RootLayoutController;
import quiz.view.ScoreboardController;
import user.view.LogInController;
import user.view.ModeSelectorController;

public class Main extends Application {

	public final static boolean DEBUG = true;

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		// TODO: Set up network connection
		/*Network network = new Network();
		Context.getContext().setNetwork(network);

		try {
			network.connect(InetAddress.getLocalHost(), 1025);

			initRootLayout();

			showLogInScene();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}*/
		
		Context.getContext().setTeamID(0);
		showScoreboard();
	}

	public static void main(String[] args) {
		// Tests
		if (DEBUG && false)
			return;

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
	
	public void showScoreboard() {
		try {
			FXMLLoader scoreboardLoader = new FXMLLoader();
			scoreboardLoader.setLocation(Main.class.getResource("../quiz/view/Scoreboard.fxml"));
		
			AnchorPane scoreboardRoot = (AnchorPane) scoreboardLoader.load();
			ScoreboardController scoreboardController = scoreboardLoader.getController();
			//scoreboardController.setMainApp(this);
			Scene scene = new Scene(scoreboardRoot);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
