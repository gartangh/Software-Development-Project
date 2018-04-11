package gui;

<<<<<<< HEAD
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import application.Context;
=======
>>>>>>> 2687a2ad6392006299b9dda2eff9a2799a9ae5fe
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import quiz.Quiz;
import user.Host;
import user.User;
import user.model.User;

public class Gui extends Application implements Runnable {

	Stage window;
	
	Scene loginScene, quizListScene, quizScene;
	
	boolean readQuizzes=false;
	
	
	// TODO: Get quizzes from server
	private User user1 = new User("MacDonald", "Berger");
	private User user2 = new User("Pickle", "Rick");
	private User user3 = new User("Morty", "Smith");
	private User user4 = new User("AlbusParcivalWolfram", "BertusPerkamentus");
	private Host host1 = new Host(user1);
	private Host host2 = new Host(user2);
	private Quiz quiz1 = new Quiz("FoodQuiz", 1, 10, 4, 2, 5, host1);
	private Quiz quiz2 = new Quiz("MovieQuiz", 1, 10, 4, 2, 5, host2);
	private ArrayList<Quiz> listOfQuizzes = new ArrayList<>(Arrays.asList(quiz1, quiz2));
	private Quiz currentQuiz;
	
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
		
		ListView<String> listView = new ListView<>();
		Thread addQuizzes = new Thread() {
		    public void run() {
				while(readQuizzes){
					// TODO: find optimal way to get list of quizzes from server
					// TODO: put all quizzes in listOfQuizzes, preferably not ArrayList
					listView.setPlaceholder(new Label("No available quizzes!"));
					for(int i=0;i<listOfQuizzes.size();i++){
						listView.getItems().addAll(listOfQuizzes.get(i).getQuizName());
					}
					break;
				}
		    }
		};
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
		buttonBack.setOnAction(e -> {
			readQuizzes=false;
			window.setScene(loginScene);
		});
		quizListLayout.setBottom(buttonBack);
		BorderPane.setAlignment(buttonBack, Pos.CENTER);
		
		quizListScene = new Scene(quizListLayout, 900, 600);
		
		quizListScene.getStylesheets().add(getClass().getResource("css/quizList.css").toExternalForm());
		
		
		//set window
		window = primaryStage;
		window.setTitle("Quiz");
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		window.setScene(loginScene);
		window.show();
	}
	
	public Scene buildQuizScene(){
		//quizScene
		BorderPane quizLayout = new BorderPane();
				
		Label labelQuizName = null;
		if(Context.getContext().getQuiz() != null){
			labelQuizName = new Label(Context.getContext().getQuiz().getQuizName());
			quizLayout.setTop(labelQuizName);
			BorderPane.setAlignment(labelQuizName, Pos.CENTER);
		}
				
		quizScene = new Scene(quizLayout, 900, 600);
		quizScene.getStylesheets().add(getClass().getResource("css/quiz.css").toExternalForm());
				
		return quizScene;	
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

	public void closeProgram() {
		Boolean answer = ConfirmBox.display("Terminate", "Are you sure?");
		if (answer)
			window.close();
	}

}
