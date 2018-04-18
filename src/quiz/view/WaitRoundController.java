package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import main.Context;
import main.Main;
import quiz.model.MCQuestion;
import server.ServerNewQuestionEvent;
import server.ServerStartRoundEvent;

public class WaitRoundController extends EventPublisher {

	private WaitRoundHandler waitRoundHandler;

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	public void initialize() {
		waitRoundHandler = new WaitRoundHandler();
		EventBroker.getEventBroker().addEventListener(waitRoundHandler);
	}

	public WaitRoundController(){
	}

	public class WaitRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
				case "SERVER_START_ROUND":
					// This is the roundHandler for a player. You need to show questions here.
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
}
