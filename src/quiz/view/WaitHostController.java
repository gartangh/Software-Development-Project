package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import javafx.fxml.FXML;
import main.Main;

public class WaitHostController extends EventPublisher {

	private NewRoundHandler newRoundHandler;
	private EndQuizHandler endQuizHandler;

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	// Method
	@FXML
	private void initialize() {
		newRoundHandler = new NewRoundHandler();
		endQuizHandler = new EndQuizHandler();

		EventBroker.getEventBroker().addEventListener(ServerNewRoundEvent.EVENTTYPE, newRoundHandler);
		EventBroker.getEventBroker().addEventListener(ServerEndQuizEvent.EVENTTYPE, endQuizHandler);
	}

	// Inner classes
	private class NewRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			EventBroker.getEventBroker().removeEventListener(newRoundHandler);
			EventBroker.getEventBroker().removeEventListener(endQuizHandler);

			main.showCreateRound();
		}

	}

	private class EndQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			EventBroker.getEventBroker().removeEventListener(newRoundHandler);
			EventBroker.getEventBroker().removeEventListener(endQuizHandler);

			main.showScoreboardScene();
		}

	}

}
