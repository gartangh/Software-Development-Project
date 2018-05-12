package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import eventbroker.EventBroker;
import eventbroker.clientevent.ClientCreateTeamEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Network;
import quiz.view.CreateQuizController;
import quiz.view.JoinQuizController;
import quiz.view.LogInController;
import quiz.view.CreateTeamController;
import quiz.view.QuestionController;
import quiz.view.JoinTeamController;
import quiz.view.CreateRoundController;
import quiz.view.ScoreboardController;
import quiz.view.WaitHostController;
import quiz.view.WaitRoundController;

public class Main extends Application {

	/** The Constant QUIZNAME represents the name of the quiz. */
	public final static String QUIZNAME = "Quiz";

	/**
	 * The Constant DEBUG. True is for development. False is for releases.
	 */
	public final static boolean DEBUG = true;

	/**
	 * The Constant LOCAL. True is for local development. False is for network
	 * development and releases.
	 */
	public final static boolean LOCAL = true;

	/** The Constant SERVERADDRESS represents the IP address of the server. */
	// On the iVisitor network at iGent
	// public final static String SERVERADDRESS = "10.10.131.52";
	// On the Proximus network at Emiel
	public final static String SERVERADDRESS = "192.168.1.30";

	/** The Constant SERVERPORT represents the port on the server. */

	public final static int SERVERPORT = 1025;

	private Stage primaryStage;
	private BorderPane rootLayout;
	private static final String MEDIA_URL = "BackgroundGrass.mp4";
	
	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(QUIZNAME);
		this.primaryStage.setMaximized(true);
		this.primaryStage.setResizable(false);
		
		// Connect to network with randomly generated client port between
		// SERVERPORT + 1 and 65535 (2^16 - 1) and type CLIENT
		Network network = new Network(new Random().nextInt(65535 - SERVERPORT + 2) + 1026, Network.CLIENTTYPE);
		MainContext.getContext().setNetwork(network);

		// Close button
		this.primaryStage.setOnCloseRequest(e -> {
			EventBroker.getEventBroker().stop();
			network.terminate();
		});

		try {
			if (Main.LOCAL) {
				System.out.println(InetAddress.getLocalHost());
				network.connect(InetAddress.getLocalHost(), Main.SERVERPORT);
			} else {
				System.out.println(InetAddress.getLocalHost().getHostAddress());
				network.connect(SERVERADDRESS, Main.SERVERPORT);
			}

			System.out.println(Integer.toString(network.getConnectionListener().getServerPort()));

			// Send event over network
			EventBroker.getEventBroker().addEventListener(network);

			// Start event broker
			EventBroker.getEventBroker().start();

			initRootLayout();

			showLogInScene();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	// Getters
	/**
	 * Gets the primary stage.
	 *
	 * @return the primary stage
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	// Methods
	private void initRootLayout() {
		try {
			// Load root layout from fxml file
			FXMLLoader rootLayoutloader = new FXMLLoader();
			rootLayoutloader.setLocation(Main.class.getResource("../quiz/view/RootLayout.fxml"));
			rootLayout = (BorderPane) rootLayoutloader.load();
			rootLayout.getStylesheets().add("css/root.css");
			rootLayout.getStyleClass().add("root");
			
			// Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			
			FXMLLoader menuLoader = new FXMLLoader();
			menuLoader.setLocation(Main.class.getResource("../quiz/view/Menu.fxml"));
			AnchorPane menu = (AnchorPane) menuLoader.load();

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setTop(menu);
				}
			});

			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Show scenes
	/**
	 * Show log in scene.
	 */
	public void showLogInScene() {
		try {
			FXMLLoader logInloader = new FXMLLoader();
			logInloader.setLocation(Main.class.getResource("../quiz/view/LogIn.fxml"));
			StackPane logIn = (StackPane) logInloader.load();
			LogInController logInController = logInloader.getController();
			logInController.setMainApp(this);
			logIn.getStylesheets().add("css/root.css");
		    logIn.getStyleClass().add("root");

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(logIn);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show join quiz scene.
	 */
	public void showJoinQuizScene() {
		try {
			FXMLLoader joinQuizLoader = new FXMLLoader();
			joinQuizLoader.setLocation(Main.class.getResource("../quiz/view/JoinQuiz.fxml"));
			BorderPane joinQuiz = (BorderPane) joinQuizLoader.load();
			JoinQuizController joinQuizController = joinQuizLoader.getController();
			joinQuizController.setMain(this);
			joinQuiz.getStylesheets().add("css/root.css");
			joinQuiz.getStyleClass().add("root");
		    
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(joinQuiz);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show create quiz scene.
	 */
	public void showCreateQuizScene() {
		try {
			FXMLLoader createQuizLoader = new FXMLLoader();
			createQuizLoader.setLocation(Main.class.getResource("../quiz/view/CreateQuiz.fxml"));
			BorderPane createQuiz = (BorderPane) createQuizLoader.load();
			CreateQuizController createQuizController = createQuizLoader.getController();
			createQuizController.setMainApp(this);
			createQuiz.getStylesheets().add("css/root.css");
			createQuiz.getStyleClass().add("root");

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(createQuiz);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show join team scene.
	 */
	public void showJoinTeamScene() {
		try {
			FXMLLoader joinTeamLoader = new FXMLLoader();
			joinTeamLoader.setLocation(Main.class.getResource("../quiz/view/JoinTeam.fxml"));
			BorderPane joinTeam = (BorderPane) joinTeamLoader.load();
			JoinTeamController joinTeamController = joinTeamLoader.getController();
			joinTeamController.setMain(this);
			joinTeam.getStylesheets().add("css/root.css");
			joinTeam.getStyleClass().add("root");
			
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(joinTeam);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show create team scene.
	 */
	public boolean showCreateTeamScene(ClientCreateTeamEvent teamevent) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../quiz/view/CreateTeam.fxml"));
			BorderPane newteam = (BorderPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Team");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(newteam);
			dialogStage.setScene(scene);
			newteam.getStylesheets().add("css/root.css");
			newteam.getStyleClass().add("root");

			// Set the person into the controller
			CreateTeamController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTeamEvent(teamevent);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * Show scoreboard scene.
	 */
	public void showScoreboardScene() {
		try {
			FXMLLoader scoreboardLoader = new FXMLLoader();
			scoreboardLoader.setLocation(Main.class.getResource("../quiz/view/Scoreboard.fxml"));
			BorderPane scoreboard = (BorderPane) scoreboardLoader.load();
			ScoreboardController scoreboardController = scoreboardLoader.getController();
			scoreboardController.setMainApp(this);
			scoreboard.getStylesheets().add("css/root.css");
			scoreboard.getStyleClass().add("root");
			
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(scoreboard);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show question scene.
	 */
	public void showQuestionScene() {
		try {
			FXMLLoader questionFormLoader = new FXMLLoader();
			questionFormLoader.setLocation(Main.class.getResource("../quiz/view/Question.fxml"));
			BorderPane questionFormRoot = (BorderPane) questionFormLoader.load();
			QuestionController questionFormController = questionFormLoader.getController();
			questionFormController.setMainApp(this);
			questionFormRoot.getStylesheets().add("css/root.css");
			questionFormRoot.getStyleClass().add("root");
			
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(questionFormRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show wait round scene.
	 */
	public void showWaitRoundScene() {
		try {
			FXMLLoader waitRoundLoader = new FXMLLoader();
			waitRoundLoader.setLocation(Main.class.getResource("../quiz/view/WaitRound.fxml"));
			BorderPane waitRoundRoot = (BorderPane) waitRoundLoader.load();
			WaitRoundController waitRoundController = waitRoundLoader.getController();
			waitRoundController.setMain(this);
			waitRoundRoot.getStylesheets().add("css/root.css");
			waitRoundRoot.getStyleClass().add("root");
			
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(waitRoundRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show wait host scene.
	 */
	public void showWaitHostScene() {
		try {
			FXMLLoader waitHostLoader = new FXMLLoader();
			waitHostLoader.setLocation(Main.class.getResource("../quiz/view/WaitHost.fxml"));
			BorderPane waitHostRoot = (BorderPane) waitHostLoader.load();
			WaitHostController waitHostController = waitHostLoader.getController();
			waitHostController.setMain(this);
			waitHostRoot.getStylesheets().add("css/root.css");
			waitHostRoot.getStyleClass().add("root");
			
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(waitHostRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show create round scene.
	 */
	public void showCreateRoundScene() {
		try {
			FXMLLoader roundMakerLoader = new FXMLLoader();
			roundMakerLoader.setLocation(Main.class.getResource("../quiz/view/CreateRound.fxml"));
			BorderPane roundMakerRoot = (BorderPane) roundMakerLoader.load();
			CreateRoundController roundMakerController = roundMakerLoader.getController();
			roundMakerController.setMain(this);
			roundMakerRoot.getStylesheets().add("css/root.css");
			roundMakerRoot.getStyleClass().add("root");
			
			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(roundMakerRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * On connection lost.
	 *
	 * @param userID
	 *            the user ID
	 */
	public void onConnectionLost() {
		// Reset everything
		MainContext context = MainContext.getContext();
		context.setQuestion(null);
		context.setQuiz(null);
		context.setTeam(null);
		context.setUser(null);
		
		// TODO Remove all eventListeners

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Connection lost!");
				alert.setContentText("You lost connection with the server. Please restore connection and try again.");
				alert.showAndWait();
			}
		});
		
		showLogInScene();
	}

}
