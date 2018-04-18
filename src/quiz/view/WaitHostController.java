package quiz.view;

import eventbroker.Event;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import main.Main;
import quiz.view.CreateQuizController.CreateQuizHandler;

public class WaitHostController extends EventPublisher  {

	
	// Reference to the main application
		private Main main;
		private CreateQuizHandler createQuizHandler;
		
		public void setMain(Main main) {
			this.main = main;
		}
	
	public WaitHostController() {
		
	}
	
	public class WaitHostHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
				case "SERVER_END_QUIZ":
					main.showScoreboardScene();
			}
			
		}
		
	}
}
