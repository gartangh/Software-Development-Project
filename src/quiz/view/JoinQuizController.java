package quiz.view;

import eventbroker.EventPublisher;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Main;
import quiz.model.Quiz;
import quiz.util.ClientGetQuizzesEvent;

public class JoinQuizController extends EventPublisher {

	@FXML
	private VBox mQuizzes;
	@FXML
	private Button mBack;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {
		ClientGetQuizzesEvent cGQE = new ClientGetQuizzesEvent();
		publishEvent(cGQE);

		/*
		for (Quiz quiz : quizzes) {
			GridPane mQuiz = new GridPane();

			// Quizname label
			Label quiznameLabel = new Label("Quiz name");
			GridPane.setConstraints(quiznameLabel, 0, 0);
			// Quizname output / join button
			Button mQuizname = new Button(quiz.getQuizname());
			// Lambda expression
			mQuizname.setOnAction(e -> {
				handleJoin(quiz.getQuizID());
			});
			GridPane.setConstraints(mQuizname, 1, 0);

			// Rounds label
			Label roundsLabel = new Label("Rounds");
			GridPane.setConstraints(roundsLabel, 0, 1);
			// Rounds output
			TextField mRounds = new TextField(Integer.toString(quiz.getAmountOfRounds()));
			GridPane.setConstraints(mRounds, 1, 1);

			// Questions label
			Label questionsLabel = new Label("Questions per round");
			GridPane.setConstraints(questionsLabel, 0, 2);
			// Questions output
			TextField mQuestions = new TextField(Integer.toString(quiz.getMaxAmountofQuestionsPerRound()));
			GridPane.setConstraints(mQuestions, 1, 2);

			// Teams label
			Label teamsLabel = new Label("Teams");
			GridPane.setConstraints(questionsLabel, 0, 3);
			// Teams output
			TextField mTeams = new TextField(Integer.toString(quiz.getAmountOfTeams()));
			GridPane.setConstraints(mTeams, 1, 3);

			// Players label
			Label playersLabel = new Label("Teams");
			GridPane.setConstraints(playersLabel, 0, 4);
			// Players output
			TextField mPlayers = new TextField(Integer.toString(quiz.getMaxAmountofPlayersPerTeam()));
			GridPane.setConstraints(mPlayers, 1, 4);

			// Add all constraints to mQuiz
			mQuiz.getChildren().addAll(quiznameLabel, mQuizname, roundsLabel, mRounds, questionsLabel, mQuestions,
					teamsLabel, mTeams, playersLabel, mPlayers);

			mQuizzes.getChildren().add(mQuiz);
		}
		*/
	}

	@FXML
	private void handleJoin(int quizID) {
		// TODO: Handle join
		// Context.getContext().setQuiz();
		main.showJoinTeamScene();
	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		main.showModeSelectorScene();
	}

}
