package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientCreateQuizEvent;
import eventbroker.serverevent.ServerReturnQuizEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import user.model.Host;

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

	private CreateQuizEventHandler createQuizHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {
		createQuizHandler = new CreateQuizEventHandler();
		EventBroker.getEventBroker().addEventListener(createQuizHandler);
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
	public class CreateQuizEventHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			String type = event.getType();
			System.out.println("Event with type " + type + " received");
			switch (type) {
			case "SERVER_RETURN_QUIZ":
				ServerReturnQuizEvent sRQE = (ServerReturnQuizEvent) event;
				Context.getContext().setQuiz(sRQE.getQuiz());
				EventBroker.getEventBroker().removeEventListener(createQuizHandler);
				
				main.showQuizroomScene();
				break;
				
			default:
				System.out.println("Event with type " + type + " was left unhandled");
			}
		}

	}

}
