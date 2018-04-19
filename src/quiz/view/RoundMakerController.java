package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateRoundEvent;
import eventbroker.serverevent.ServerNewQuestionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Alert.AlertType;
import main.Context;
import main.Main;
import quiz.model.MCQuestion;
import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.view.WaitRoundController.WaitRoundHandler;

public class RoundMakerController extends EventPublisher{
	@FXML
	ChoiceBox<String> themeChoiceBox;
	@FXML
	ChoiceBox<String> diffChoiceBox;
	@FXML
	ChoiceBox<String> numberChoiceBox;
	@FXML
	Button confirmButton;

	Main main;

	private RoundMakerHandler roundMakerHandler;

	public class RoundMakerHandler implements EventListener {
		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
				case "SERVER_START_ROUND":
					// This is the roundHandler for a player. You need to show a Quizmaster screen here.
					// TODO: Make Special screen for Quizmaster. Now it is only a waiting screen until the round has finished
					EventBroker.getEventBroker().removeEventListener(roundMakerHandler);
					main.showWaitHost();
					break;
				case "SERVER_NEW_QUESTION":
					ServerNewQuestionEvent sNQE = (ServerNewQuestionEvent) event;
					MCQuestion q = new MCQuestion(sNQE.getQuestionID(), sNQE.getQuestion(), sNQE.getAnswers());
					Context.getContext().setQuestion(q);
					break;
			}
		}
	}

	public RoundMakerController() {
		// Empty constructor
	}

	@FXML
	private void initialize() {
		ObservableList<String> themes = FXCollections.observableArrayList("Culture","Sports");
		ObservableList<String> diffs = FXCollections.observableArrayList("Easy","Average","Hard");
		ObservableList<String> numbers = FXCollections.observableArrayList("1","2","3","4","5");
		themeChoiceBox.setItems(themes);
		diffChoiceBox.setItems(diffs);
		numberChoiceBox.setItems(numbers);

		RoundMakerHandler roundMakerHandler= new RoundMakerHandler();
		EventBroker.getEventBroker().addEventListener(roundMakerHandler);
	}

	public void setMain(Main main){
		this.main=main;
	}

	@FXML
	private void handleConfirm() {
		boolean blank = false;
		Theme theme = Theme.CULTURE;
		switch((String) themeChoiceBox.getValue()) {
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
		switch((String) diffChoiceBox.getValue()) {
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
		switch((String) numberChoiceBox.getValue()) {
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
		if(!blank) {
			ClientCreateRoundEvent cCRE = new ClientCreateRoundEvent(theme, diff, numberOfQuestions);
			this.publishEvent(cCRE);
		}
		else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(main.getPrimaryStage());
			alert.setTitle("Create Round Error");
			alert.setHeaderText("You can't create this round");
			alert.setContentText("Please fill in all required fields to create this round");

			alert.showAndWait();
		}
	}
}
