package quiz.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.Main;
import main.view.AlertBox;
import quiz.model.Quiz;

public class CreateQuizController {

	// Text fields
	@FXML
	private TextField mName;
	@FXML
	private TextField mRounds;
	@FXML
	private TextField mQuestions;
	@FXML
	private TextField mTeams;
	@FXML
	private TextField mPlayers;

	// Buttons
	@FXML
	private Button mCreateQuiz;
	@FXML
	private Button mBack;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {
		// Empty initialize
	}

	@FXML
	private void handleCreateQuiz() {
		try {
			int rounds = Integer.parseInt(mRounds.getText());
			int questions = Integer.parseInt(mQuestions.getText());
			int teams = Integer.parseInt(mTeams.getText());
			int players = Integer.parseInt(mPlayers.getText());

			switch (Quiz.createQuiz(mName.getText(), rounds, questions, teams, players)) {
			case 0:
				// TODO: What if quiz is created successfully?
				break;
			case 1:
				AlertBox.display("Error", "Quizname is invalid!");
				break;
			case 2:
				AlertBox.display("Error", "Quizname is not unique!");
				break;
			case 3:
				AlertBox.display("Error", "Rounds cannot be smaller than 1!");
				break;
			case 4:
				AlertBox.display("Error", "Rounds cannot be bigger than " + Quiz.MAXROUNDS + "!");
				break;
			case 5:
				AlertBox.display("Error", "Questions cannot be smaller than 1!");
				break;
			case 6:
				AlertBox.display("Error", "Questions cannot be bigger than " + Quiz.MAXQUESTIONS + "!");
				break;
			case 7:
				AlertBox.display("Error", "Teams cannot be smaller than 1!");
				break;
			case 8:
				AlertBox.display("Error", "Teams cannot be bigger than " + Quiz.MAXTEAMS + "!");
				break;
			case 9:
				AlertBox.display("Error", "Players cannot be smaller than 1!");
				break;
			case 10:
				AlertBox.display("Error", "Players cannot be bigger than " + Quiz.MAXPLAYERS + "!");
				break;
			default:
				AlertBox.display("Error", "Something went wrong!");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// TODO: Show alert (invalid input)
		}

	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		main.showModeSelectorScene();
	}

}
