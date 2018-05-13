package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateRoundEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import main.MainContext;
import main.Main;
import quiz.model.MCQuestion;
import quiz.model.Round;
import quiz.util.Difficulty;
import quiz.util.RoundType;
import quiz.util.Theme;

public class CreateRoundController extends EventPublisher {

	@FXML
	ChoiceBox<RoundType> roundTypeChoiceBox;
	@FXML
	ChoiceBox<Theme> themeChoiceBox;
	@FXML
	ChoiceBox<Difficulty> difficultyChoiceBox;
	@FXML
	ChoiceBox<Integer> numberChoiceBox;

	private StartRoundHandler startRoundHandler = new StartRoundHandler();
	private NewMCQuestionHandler newMCQuestionHandler = new NewMCQuestionHandler();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	private void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerStartRoundEvent.EVENTTYPE, startRoundHandler);
		eventBroker.addEventListener(ServerNewMCQuestionEvent.EVENTTYPE, newMCQuestionHandler);

		roundTypeChoiceBox.setItems(FXCollections.observableArrayList(RoundType.values()));
		roundTypeChoiceBox.getSelectionModel().selectFirst();
		
		themeChoiceBox.setItems(FXCollections.observableArrayList(Theme.values()));
		themeChoiceBox.getSelectionModel().selectFirst();
		
		difficultyChoiceBox.setItems(FXCollections.observableArrayList(Difficulty.values()));
		difficultyChoiceBox.getSelectionModel().selectFirst();
		
		ObservableList<Integer> numbers = FXCollections.observableArrayList();
		for (int i = Round.MINQUESTIONS; i <= Round.MAXQUESTIONS; i++)
			numbers.add(i);
		numberChoiceBox.setItems(numbers);
		numberChoiceBox.getSelectionModel().selectFirst();
	}

	@FXML
	private void handleCreateRound() {
		RoundType roundType;
		switch (roundTypeChoiceBox.getValue()) {
		case IP:
			roundType = RoundType.IP;
			break;
		default:
			roundType = RoundType.MC;
		}

		Theme theme;
		switch (themeChoiceBox.getValue()) {
		case SPORTS:
			theme = Theme.SPORTS;
			break;
		default:
			theme = Theme.CULTURE;
		}

		Difficulty difficulty;
		switch (difficultyChoiceBox.getValue()) {
		case EASY:
			difficulty = Difficulty.EASY;
			break;
		case HARD:
			difficulty = Difficulty.HARD;
			break;
		default:
			difficulty = Difficulty.AVERAGE;
		}

		int numberOfQuestions = numberChoiceBox.getValue();

		main.showWaitHostScene(roundType);
		ClientCreateRoundEvent cCRE = new ClientCreateRoundEvent(roundType, theme, difficulty, numberOfQuestions);
		publishEvent(cCRE);
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
			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(startRoundHandler);
			eventBroker.removeEventListener(newMCQuestionHandler);

		}

	}

	private class NewMCQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewMCQuestionEvent sNQE = (ServerNewMCQuestionEvent) event;

			int questionID = sNQE.getQuestionID();
			String question = sNQE.getQuestion();
			String[] answers = sNQE.getAnswers();

			MCQuestion mCQuestion = new MCQuestion(questionID, question, answers);
			MainContext.getContext().setQuestion(mCQuestion);
		}

	}

}
