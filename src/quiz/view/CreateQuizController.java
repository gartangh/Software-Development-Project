package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateQuizEvent;
import eventbroker.serverevent.ServerCreateQuizEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import quiz.model.Quiz;
import user.Host;

public class CreateQuizController extends EventPublisher {

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

	private CreateQuizHandler createQuizHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	// Getter
	public CreateQuizHandler getCreateQuizHandler() {
		return createQuizHandler;
	}

	// Methods
	@FXML
	private void initialize() {
		createQuizHandler = new CreateQuizHandler();

		EventBroker.getEventBroker().addEventListener(ServerCreateQuizEvent.EVENTTYPE, createQuizHandler);
	}

	@FXML
	private void handleCreateQuiz() {
		String name = mName.getText();
		int teams = Integer.parseInt(mTeams.getText());
		int players = Integer.parseInt(mPlayers.getText());
		int rounds = Integer.parseInt(mRounds.getText());
		int questions = Integer.parseInt(mRounds.getText());

		ClientCreateQuizEvent cCQE = new ClientCreateQuizEvent(name, teams, players, rounds, questions);
		publishEvent(cCQE);
	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		((Host) Context.getContext().getUser()).castToUser();
		main.showJoinQuizScene();
	}

	// Inner class
	public class CreateQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerCreateQuizEvent sRQE = (ServerCreateQuizEvent) event;

			Quiz quiz = sRQE.getQuiz();

			Context.getContext().setQuiz(quiz);

			EventBroker.getEventBroker().removeEventListener(createQuizHandler);

			main.showQuizroomScene();
		}

	}

}
