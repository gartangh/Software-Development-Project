package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerNewQuestionEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import javafx.fxml.FXML;
import main.Context;
import main.Main;
import quiz.model.MCQuestion;

public class WaitRoundController extends EventPublisher {

	private StartRoundHandler startRoundHandler;
	private NewQuestionHandler newQuestionHandler;

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	public void initialize() {
		startRoundHandler = new StartRoundHandler();
		newQuestionHandler = new NewQuestionHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerStartRoundEvent.EVENTTYPE, startRoundHandler);
		eventBroker.addEventListener(ServerNewQuestionEvent.EVENTTYPE, newQuestionHandler);
	}

	private class StartRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerStartRoundEvent sSRE = (ServerStartRoundEvent) event;

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(startRoundHandler);
			eventBroker.removeEventListener(newQuestionHandler);

			main.showQuestionForm();
		}

	}

	public class NewQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewQuestionEvent sNQE = (ServerNewQuestionEvent) event;

			int questionID = sNQE.getQuestionID();
			String question = sNQE.getQuestion();
			String[] answers = sNQE.getAnswers();

			MCQuestion q = new MCQuestion(questionID, question, answers);
			Context.getContext().setQuestion(q);
		}

	}
}
