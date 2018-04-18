package quiz.view;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.Context;
import main.Main;
import main.view.AlertBox;
import quiz.model.Quiz;
import quiz.util.ClientCreateAccountEvent;
import quiz.util.ClientCreateQuizEvent;
import server.ServerReturnQuizEvent;
import user.model.Host;
import user.model.Quizmaster;

public class CreateQuizController extends EventPublisher {

	// Text fields
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

	// Buttons
	@FXML
	private Button mCreateQuiz;
	@FXML
	private Button mBack;

	// Reference to the main application
	private Main main;
	private CreateQuizHandler createQuizHandler;
	
	public void setMainApp(Main main) {
		this.main = main;
	}

	@FXML
	private void initialize() {
		createQuizHandler = new CreateQuizHandler();
		
		EventBroker.getEventBroker().addEventListener(createQuizHandler);
	}

	@FXML
	private void handleCreateQuiz() {
		ClientCreateQuizEvent cCQE = new ClientCreateQuizEvent(mName.getText(), Integer.parseInt(mTeams.getText()), Integer.parseInt(mPlayers.getText()), Integer.parseInt(mRounds.getText()), Integer.parseInt(mQuestions.getText()));
		publishEvent(cCQE);
	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		((Host) Context.getContext().getUser()).castToUser();
		main.showModeSelectorScene();
	}
	
	public class CreateQuizHandler implements EventListener{

		@Override
		public void handleEvent(Event event) {
			switch(event.getType()) {
				case "SERVER_RETURN_QUIZ":
					ServerReturnQuizEvent sRQE = (ServerReturnQuizEvent) event;
					Context.getContext().setQuiz(sRQE.getQuiz());
					main.showQuizroomScene();
				break;
			}
		}
		
	}

}
