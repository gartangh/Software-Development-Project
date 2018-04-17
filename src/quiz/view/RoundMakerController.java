package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import main.Context;
import main.Main;
import quiz.model.MCQuestion;
import quiz.util.ClientCreateRoundEvent;
import quiz.util.Difficulty;
import quiz.util.Theme;
import quiz.view.WaitRoundController.WaitRoundHandler;
import server.ServerNewQuestionEvent;

public class RoundMakerController extends EventPublisher{
	@FXML
	ChoiceBox themeChoiceBox;
	@FXML
	ChoiceBox diffChoiceBox;
	@FXML
	ChoiceBox numberChoiceBox;
	@FXML
	Button confirmButton;

	Main main;

	private RoundMakerHandler roundMakerHandler;

	public class RoundMakerHandler implements EventListener {
		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
				case "SERVER_START_ROUND":
					main.showQuestionForm();
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

	private void initialize() {
		themeChoiceBox.setItems(FXCollections.observableArrayList("Culture","Sports"));
		diffChoiceBox.setItems(FXCollections.observableArrayList("Easy","Average","Hard"));
		diffChoiceBox.setItems(FXCollections.observableArrayList("1","2","3","4","5"));

		RoundMakerHandler roundMakerHandler= new RoundMakerHandler();
		EventBroker.getEventBroker().addEventListener(roundMakerHandler);
	}

	public void setMain(Main main){
		this.main=main;
	}

	@FXML
	private void handleConfirm() {
		Theme theme = Theme.CULTURE;
		switch((String) themeChoiceBox.getValue()) {
		case "Culture":
			theme = Theme.CULTURE;
			break;
		case "Sports":
			theme = Theme.SPORTS;
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
		}

		int numberOfQuestions = 3;
		switch((String) themeChoiceBox.getValue()) {
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
		}
		ClientCreateRoundEvent cCRE = new ClientCreateRoundEvent(theme, diff, numberOfQuestions);
		this.publishEvent(cCRE);
	}
}
