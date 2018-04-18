package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import main.Main;

public class WaitHostController extends EventPublisher  {

	
	// Reference to the main application
		private Main main;
		private WaitHostHandler waitHostHandler;
		
		public void setMain(Main main) {
			this.main = main;
		}
	
	public WaitHostController() {
		
	}
	
	@FXML
	private void initialize() {
		waitHostHandler = new WaitHostHandler();
		EventBroker.getEventBroker().addEventListener(waitHostHandler);
	}
	
	public class WaitHostHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
			case "SERVER_NEW_ROUND":
				EventBroker.getEventBroker().removeEventListener(waitHostHandler);
				main.showCreateRound();
				break;
			case "SERVER_END_QUIZ":
				EventBroker.getEventBroker().removeEventListener(waitHostHandler);
				main.showScoreboardScene();
				break;
			}
			
		}
		
	}
}
