package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateRoundEvent;
import eventbroker.serverevent.ServerNewQuestionEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import main.Context;
import main.Main;
import quiz.model.MCQuestion;
import quiz.util.Difficulty;
import quiz.util.Theme;

public class CreateRoundController extends EventPublisher {
	@FXML
	ChoiceBox<String> themeChoiceBox;
	@FXML
	ChoiceBox<String> diffChoiceBox;
	@FXML
	ChoiceBox<String> numberChoiceBox;

	private StartRoundHandler startRoundHandler;
	private NewQuestionHandler newQuestionHandler;

	// Reference to the main application
	Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	private void initialize() {
		startRoundHandler = new StartRoundHandler();
		newQuestionHandler = new NewQuestionHandler();
		EventBroker.getEventBroker().addEventListener(ServerStartRoundEvent.EVENTTYPE, startRoundHandler);
		EventBroker.getEventBroker().addEventListener(ServerNewQuestionEvent.EVENTTYPE, newQuestionHandler);

		ObservableList<String> themes = FXCollections.observableArrayList("Culture", "Sports");
		ObservableList<String> diffs = FXCollections.observableArrayList("Easy", "Average", "Hard");
		ObservableList<String> numbers = FXCollections.observableArrayList("1", "2", "3", "4", "5");
		themeChoiceBox.setItems(themes);
		diffChoiceBox.setItems(diffs);
		numberChoiceBox.setItems(numbers);
	}

	@FXML
	private void handleCreateRound() {
		boolean blank = false;
		Theme theme = Theme.CULTURE;
		switch ((String) themeChoiceBox.getValue()) {
		case "Culture":
			theme = Theme.CULTURE;
			break;
		case "Sports":
			theme = Theme.SPORTS;
			break;
		default:
			blank = true;
			break;
		}

		Difficulty diff = Difficulty.EASY;
		switch ((String) diffChoiceBox.getValue()) {
		case "Easy":
			diff = Difficulty.EASY;
			break;
		case "Average":
			diff = Difficulty.AVERAGE;
			break;
		case "Hard":
			diff = Difficulty.HARD;
			break;
		default:
			blank = true;
			break;
		}

		int numberOfQuestions = 3;
		switch ((String) numberChoiceBox.getValue()) {
		case "1":
			numberOfQuestions = 1;
			break;
		case "2":
			numberOfQuestions = 2;
			break;
		case "3":
			numberOfQuestions = 3;
			break;
		case "4":
			numberOfQuestions = 4;
			break;
		case "5":
			numberOfQuestions = 5;
			break;
		default:
			blank = true;
			break;
		}

		if (!blank) {
			ClientCreateRoundEvent cCRE = new ClientCreateRoundEvent(theme, diff, numberOfQuestions);

			this.publishEvent(cCRE);
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Create Round Error");
			alert.setHeaderText("You can't create this round");
			alert.setContentText("Please fill in all required fields to create this round");

			alert.showAndWait();
		}
	}

	// Inner classes
	private class StartRoundHandler implements EventListener {
		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerStartRoundEvent sSRE = (ServerStartRoundEvent) event;

			// This is the roundHandler for a player. You need to show a
			// Quizmaster screen here.
			// TODO Make Special screen for Quizmaster. Now it is only a
			// waiting screen until the round has finished
			EventBroker.getEventBroker().removeEventListener(startRoundHandler);
			EventBroker.getEventBroker().removeEventListener(newQuestionHandler);

			main.showWaitHostScene();
		}
	}

	private class NewQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewQuestionEvent sNQE = (ServerNewQuestionEvent) event;

			int questionID = sNQE.getQuestionID();
			String question = sNQE.getQuestion();
			String[] answers = sNQE.getAnswers();

			MCQuestion q = new MCQuestion(questionID, question, answers);
			Context.getContext().setQuestion(q);
		}

	}

}
