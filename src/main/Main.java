package main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.view.RootLayoutController;
import test.Test;
import user.view.LogInController;

public class Main extends Application {

	public final static boolean DEBUG = true;

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		initRootLayout();

		showLogInScene();
	}

	public static void main(String[] args) {
		// Tests
		if (DEBUG && !Test.test())
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
	private void showLogInScene() {
		try {
			// Load person overview
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../user/view/LogIn.fxml"));
			VBox logIn = (VBox) loader.load();

			// Set person overview into the center of root layout
			rootLayout.setCenter(logIn);

			// Give the controller access to the main app
			LogInController controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
