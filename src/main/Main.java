package main;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

import eventbroker.EventBroker;
import eventbroker.clientevent.ClientNewTeamEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import network.Network;
import quiz.view.CreateQuizController;
import quiz.view.JoinQuizController;
import quiz.view.LogInController;
import quiz.view.NewTeamController;
import quiz.view.QuestionFormController;
import quiz.view.QuizRoomController;
import quiz.view.RoundMakerController;
import quiz.view.ScoreboardController;
import quiz.view.WaitHostController;
import quiz.view.WaitRoundController;

public class Main extends Application {

	public final static String QUIZNAME = "Quiz";
	public final static boolean DEBUG = true;
	public final static boolean LOCAL = true;
	// public final static String SERVERADDRESS = "10.10.131.52";
	public final static String SERVERADDRESS = "192.168.1.30";
	public final static int SERVERPORT = 1025;

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle(QUIZNAME);

		// Connect to network with randomly generated client port between
		// SERVERPORT + 1 and 65535 (2^16 - 1) and type CLIENT
		Network network = new Network(new Random().nextInt(65535 - SERVERPORT + 2) + 1026, Network.CLIENTTYPE);
		Context.getContext().setNetwork(network);

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
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	// Methods
	private void initRootLayout() {
		try {
			// Load root layout from fxml file
			FXMLLoader rootLayoutloader = new FXMLLoader();
			rootLayoutloader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) rootLayoutloader.load();

			// Show the scene containing the root layout
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);

			FXMLLoader menuLoader = new FXMLLoader();
			menuLoader.setLocation(Main.class.getResource("view/Menu.fxml"));
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
	public void showLogInScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../quiz/view/LogIn.fxml"));
			BorderPane logIn = (BorderPane) loader.load();
			LogInController controller = loader.getController();
			controller.setMainApp(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(logIn);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showCreateQuizScene() {
		try {
			FXMLLoader createQuizLoader = new FXMLLoader();
			createQuizLoader.setLocation(Main.class.getResource("../quiz/view/CreateQuiz.fxml"));
			BorderPane createQuiz = (BorderPane) createQuizLoader.load();
			CreateQuizController createQuizController = createQuizLoader.getController();
			createQuizController.setMainApp(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(createQuiz);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showJoinQuizScene() {
		try {
			FXMLLoader joinQuizLoader = new FXMLLoader();
			joinQuizLoader.setLocation(Main.class.getResource("../quiz/view/JoinQuiz.fxml"));
			BorderPane joinQuiz = (BorderPane) joinQuizLoader.load();
			JoinQuizController joinQuizController = joinQuizLoader.getController();
			joinQuizController.setMainApp(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(joinQuiz);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showQuizroomScene() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../quiz/view/Quizroom.fxml"));
			BorderPane content = (BorderPane) loader.load();
			QuizRoomController quizcontroller = loader.getController();
			quizcontroller.setMain(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(content);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showScoreboardScene() {
		try {
			FXMLLoader scoreboardLoader = new FXMLLoader();
			scoreboardLoader.setLocation(Main.class.getResource("../quiz/view/Scoreboard.fxml"));
			BorderPane scoreboardRoot = (BorderPane) scoreboardLoader.load();
			ScoreboardController scoreboardController = scoreboardLoader.getController();
			scoreboardController.setMainApp(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(scoreboardRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean showNewTeam(ClientNewTeamEvent teamevent) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("../quiz/view/NewTeam.fxml"));
			BorderPane newteam = (BorderPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Team");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(newteam);
			dialogStage.setScene(scene);

			// Set the person into the controller
			NewTeamController controller = loader.getController();
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

	public void showQuestionForm() {
		try {
			FXMLLoader questionFormLoader = new FXMLLoader();
			questionFormLoader.setLocation(Main.class.getResource("../quiz/view/QuestionForm.fxml"));
			BorderPane questionFormRoot = (BorderPane) questionFormLoader.load();
			QuestionFormController questionFormController = questionFormLoader.getController();
			questionFormController.setMainApp(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(questionFormRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showWaitRound() {
		try {
			FXMLLoader waitRoundLoader = new FXMLLoader();
			waitRoundLoader.setLocation(Main.class.getResource("../quiz/view/WaitRound.fxml"));
			BorderPane waitRoundRoot = (BorderPane) waitRoundLoader.load();
			WaitRoundController waitRoundController = waitRoundLoader.getController();
			waitRoundController.setMain(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(waitRoundRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showWaitHost() {
		try {
			FXMLLoader waitHostLoader = new FXMLLoader();
			waitHostLoader.setLocation(Main.class.getResource("../quiz/view/WaitHost.fxml"));
			BorderPane waitHostRoot = (BorderPane) waitHostLoader.load();
			WaitHostController waitHostController = waitHostLoader.getController();
			waitHostController.setMain(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(waitHostRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showCreateRound() {
		try {
			FXMLLoader roundMakerLoader = new FXMLLoader();
			roundMakerLoader.setLocation(Main.class.getResource("../quiz/view/RoundMaker.fxml"));
			BorderPane roundMakerRoot = (BorderPane) roundMakerLoader.load();
			RoundMakerController roundMakerController = roundMakerLoader.getController();
			roundMakerController.setMain(this);

			Platform.runLater(new Runnable() {
				public void run() {
					rootLayout.setCenter(roundMakerRoot);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
