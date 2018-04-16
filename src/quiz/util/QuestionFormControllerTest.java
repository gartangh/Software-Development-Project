package quiz.util;

import java.io.IOException;
import java.net.InetAddress;

import eventbroker.EventBroker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.Network;
import quiz.model.Quiz;
import quiz.view.QuestionFormController;
import user.model.User;

public class QuestionFormControllerTest extends Application {

	private Stage primaryStage;
	private Network network;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			this.primaryStage = primaryStage;
			primaryStage.setTitle("QuestionFormControllerTest");
			network = new Network();
			network.connect(InetAddress.getByName("127.0.0.1"), 1025);
			EventBroker.getEventBroker().addEventListener(network);

			User.createAccount("André", "André123");
			Quiz.createQuiz("TestQuiz", 4, 4, 4, 4);
			// TODO: Add StringProperty name in args
			// Team team = new Team("André and the boys", , Color.BLUE,
			// Context.getContext().getUser().getUsername());
			// Context.getContext().getQuiz().addTeam(team);
			// Context.getContext().setTeam(team);

			EventBroker.getEventBroker().start();
			showQuestionFormController();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showQuestionFormController() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(QuestionFormControllerTest.class.getResource("/quiz/view/QuestionForm.fxml"));
		AnchorPane questionForm = null;

		try {
			questionForm = (AnchorPane) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		QuestionFormController controller = loader.getController();

		// Show the scene containing the root layout.
		Scene scene = new Scene(questionForm);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
