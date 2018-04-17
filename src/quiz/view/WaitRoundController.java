package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import main.Main;

public class WaitRoundController extends EventPublisher {

	private WaitRoundHandler waitRoundHandler;
	
	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}
	
	public void initialize() {
		waitRoundHandler = new WaitRoundHandler();
		EventBroker.getEventBroker().addEventListener(waitRoundHandler);
	}
	
	public class WaitRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
				case "SERVER_START_ROUND":
					// TODO: Start round
					break;
			}
		}
		
	}
}
