package gui;

import java.io.File;

import application.Context;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import user.User;

public class Gui extends Application implements Runnable {

	Stage window;
	Scene loginScene, quizListScene;
	
	public Gui() {
		// TODO Auto-generated constructor stub
	}
	
	public void startContextGUI(){
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
		
		//loginScene
		GridPane loginLayout = new GridPane();
		loginLayout.setPadding(new Insets(10,10,10,10));
		loginLayout.setVgap(8);
		loginLayout.setHgap(10);
		
		Label labelUsername = new Label("Username: ");
		GridPane.setConstraints(labelUsername, 0, 0);
		TextField username = new TextField();
		username.setPromptText("John Cena");
		username.setFocusTraversable(false);
		GridPane.setConstraints(username, 1, 0);
		
		Label labelPassword = new Label("Password: ");
		GridPane.setConstraints(labelPassword, 0, 1);
		PasswordField password = new PasswordField();
		password.setPromptText("•••••••••");
		password.setFocusTraversable(false);
		GridPane.setConstraints(password, 1, 1);
		
		FocusedButton buttonSignIn = new FocusedButton("Sign in");
		buttonSignIn.defaultButtonProperty().bind(buttonSignIn.focusedProperty());
		buttonSignIn.setOnAction(e -> {
			if(verifyUsername(username, username.getText())){
				if(verifyPassword(password, password.getText())){
					// TODO: Add User to ListOfUsernames on server
					// TODO: set Quiz
					User user = new User(username.getText(), password.getText());
					window.setScene(quizListScene);
				}
				else{
					AlertBox.display("Error", "Password can not be empty!");
				}
			}
			else AlertBox.display("Error", "Username must be unique and can not be empty!");
		});
		GridPane.setConstraints(buttonSignIn, 0, 2);
		loginLayout.getChildren().addAll(labelUsername, username, labelPassword, password, buttonSignIn);
		Image image = new Image(Gui.class.getResourceAsStream("images/login_background.jpg"));
		BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
		Background background = new Background(backgroundImage);
		loginLayout.setBackground(background);
		loginLayout.setAlignment(Pos.CENTER);
		loginScene = new Scene(loginLayout, 400, 267);
		
		
		//quizListScene
		StackPane quizListLayout = new StackPane();
		FocusedButton buttonBack = new FocusedButton("Log out");
		buttonBack.setOnAction(e -> window.setScene(loginScene));
		quizListLayout.getChildren().add(buttonBack);
		quizListScene = new Scene(quizListLayout, 900, 600);
		
		window.setScene(loginScene);
		window.show();
	}
	
	public boolean verifyUsername(TextField input, String message){
		// TODO: check for uniqueness!
		if(message.isEmpty())
			return false;
		else return true;
	}
	
	public boolean verifyPassword(TextField input, String message){
		if(message.isEmpty())
			return false;
		else return true;
	}
	
	
	public void closeProgram(){
		Boolean answer = ConfirmBox.display("Terminate", "Are you sure?");
		if(answer)
			window.close();
	}

	

}
