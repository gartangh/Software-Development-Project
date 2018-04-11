package quiz.util;

import java.io.IOException;
import java.net.InetAddress;

import eventbroker.EventBroker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.Context;
import network.Network;
import quiz.model.Quiz;
import quiz.model.Team;
import quiz.model.VoteModel;
import quiz.view.QuestionFormController;
import user.model.User;

public class QuestionFormControllerTest extends Application {

	private Stage primaryStage;
	private Network network = null;
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			this.primaryStage=primaryStage;
			primaryStage.setTitle("QuestionFormControllerTest");
			network = new Network();
			network.connect(InetAddress.getByName("127.0.0.1"), 1025);
			EventBroker.getEventBroker().addEventListener(network);
			
			User user = new User(1, "André", "");
			Quiz quiz = new Quiz(1, 8, 4, 1, 20, 1);
			Team team = new Team(1, "André en de boys", Color.BLUE, 1, "André");
			quiz.addTeam(team);
			Context.getContext().setUser(user);
			Context.getContext().setQuiz(quiz);
			Context.getContext().setTeamID(team.getID());

			
			EventBroker.getEventBroker().start();
			showQuestionFormController();
		} catch(Exception e) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        QuestionFormController controller = loader.getController();

        // Show the scene containing the root layout.
        Scene scene = new Scene(questionForm);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
