package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerStartRoundEvent;
import javafx.fxml.FXML;
import main.MainContext;
import main.Main;
import quiz.model.MCQuestion;

public class WaitRoundController extends EventPublisher {

	private StartRoundHandler startRoundHandler = new StartRoundHandler();
	private NewQuestionHandler newQuestionHandler = new NewQuestionHandler();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	@FXML
	public void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerStartRoundEvent.EVENTTYPE, startRoundHandler);
		eventBroker.addEventListener(ServerNewMCQuestionEvent.EVENTTYPE, newQuestionHandler);
	}

	private class StartRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			@SuppressWarnings("unused")
			ServerStartRoundEvent sSRE = (ServerStartRoundEvent) event;

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(startRoundHandler);
			eventBroker.removeEventListener(newQuestionHandler);

			main.showQuestionScene();
		}

	}

	public class NewQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewMCQuestionEvent sNQE = (ServerNewMCQuestionEvent) event;

			int questionID = sNQE.getQuestionID();
			String question = sNQE.getQuestion();
			String[] answers = sNQE.getAnswers();

			MCQuestion q = new MCQuestion(questionID, question, answers);
			MainContext.getContext().setQuestion(q);
		}

	}

}
