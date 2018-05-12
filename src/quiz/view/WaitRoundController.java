package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import javafx.fxml.FXML;
import main.Context;
import main.Main;
import quiz.model.MCQuestion;

public class WaitRoundController extends EventPublisher {

	private StartRoundHandler startRoundHandler;

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	public void initialize() {
		startRoundHandler = new StartRoundHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerStartRoundEvent.EVENTTYPE, startRoundHandler);
	}

	private class StartRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerStartRoundEvent sSRE = (ServerStartRoundEvent) event;

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(startRoundHandler);

			main.showQuestionScene(sSRE.getRoundType());
		}

	}

}
