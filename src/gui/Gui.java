package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import user.model.User;

public class Gui extends Application implements Runnable {

	Stage window;
	Scene loginScene;
	Scene quizListScene;

	public Gui() {
		// Empty constructor
	}

	public void startContextGUI() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Quiz");
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});

		// loginScene
		GridPane loginLayout = new GridPane();
		loginLayout.setPadding(new Insets(10, 10, 10, 10));
		loginLayout.setVgap(8);
		loginLayout.setHgap(10);

		Label labelUsername = new Label("Username: ");
		GridPane.setConstraints(labelUsername, 0, 0);
		TextField mUsername = new TextField();
		mUsername.setPromptText("John Cena");
		mUsername.setFocusTraversable(false);
		GridPane.setConstraints(mUsername, 1, 0);

		Label labelPassword = new Label("Password: ");
		GridPane.setConstraints(labelPassword, 0, 1);
		PasswordField mPassword = new PasswordField();
		mPassword.setPromptText("���������");
		mPassword.setFocusTraversable(false);
		GridPane.setConstraints(mPassword, 1, 1);

		FocusedButton mCreateAccount = new FocusedButton("CREATE ACCOUNT");
		mCreateAccount.defaultButtonProperty().bind(mCreateAccount.focusedProperty());
		mCreateAccount.setOnAction(e -> {
		});

		FocusedButton mSignIn = new FocusedButton("SIGN IN");
		mSignIn.defaultButtonProperty().bind(mSignIn.focusedProperty());
		mSignIn.setOnAction(e -> {
		});

		GridPane.setConstraints(mCreateAccount, 0, 2);
		loginLayout.getChildren().addAll(labelUsername, mUsername, labelPassword, mPassword, mCreateAccount);
		loginScene = new Scene(loginLayout, 400, 267);

		// quizListScene
		StackPane quizListLayout = new StackPane();
		FocusedButton buttonBack = new FocusedButton("Log out");
		buttonBack.setOnAction(e -> window.setScene(loginScene));
		quizListLayout.getChildren().add(buttonBack);
		quizListScene = new Scene(quizListLayout, 900, 600);

		window.setScene(loginScene);
		window.show();
	}

	public void closeProgram() {
		Boolean answer = ConfirmBox.display("Terminate", "Are you sure?");
		if (answer)
			window.close();
	}

}
