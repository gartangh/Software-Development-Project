package quiz.view;

import java.util.ArrayList;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientGetQuizzesEvent;
import eventbroker.clientevent.ClientJoinQuizEvent;
import eventbroker.clientevent.ClientLeaveQuizEvent;
import eventbroker.clientevent.ClientLogOutEvent;
import eventbroker.serverevent.ServerGetQuizzesEvent;
import eventbroker.serverevent.ServerJoinQuizEvent;
import eventbroker.serverevent.ServerNewQuizEvent;
import eventbroker.serverevent.ServerStartQuizEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.MainContext;
import main.Main;
import quiz.model.JoinQuizModel;
import quiz.model.Quiz;
import quiz.model.QuizModel;
import quiz.model.User;

public class JoinQuizController extends EventPublisher {

	@FXML
	private TableView<QuizModel> quizTable;
	@FXML
	private TableColumn<QuizModel, String> quiznameColumn;
	@FXML
	private TableColumn<QuizModel, String> hostnameColumn;
	@FXML
	private Button mJoin;
	@FXML
	private Label mQuizname;
	@FXML
	private Label mRounds;
	@FXML
	private Label mTeams;
	@FXML
	private Label mPlayers;

	private QuizModel selectedQuiz;
	private JoinQuizModel joinQuizModel = new JoinQuizModel();
	private NewQuizHandler newQuizHandler = new NewQuizHandler();
	private JoinQuizHandler joinQuizHandler = new JoinQuizHandler();
	private GetQuizzesHandler getQuizzesHandler = new GetQuizzesHandler();
	private StartQuizHandler startQuizHandler = new StartQuizHandler();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
	}

	// Methods
	@FXML
	private void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerJoinQuizEvent.EVENTTYPE, joinQuizHandler);
		eventBroker.addEventListener(ServerGetQuizzesEvent.EVENTTYPE, getQuizzesHandler);
		eventBroker.addEventListener(ServerNewQuizEvent.EVENTTYPE, newQuizHandler);
		eventBroker.addEventListener(ServerStartQuizEvent.EVENTTYPE, startQuizHandler);
		
		// Ask server for list of quizzes
		ClientGetQuizzesEvent cGQE = new ClientGetQuizzesEvent();
		publishEvent(cGQE);

		quizTable.setItems(joinQuizModel.getQuizzes());

		mQuizname.textProperty().bind(joinQuizModel.getQuiznameProperty());
		mRounds.textProperty().bind(joinQuizModel.getQuizRoundsProperty());
		mTeams.textProperty().bind(joinQuizModel.getTeamProperty());
		mPlayers.textProperty().bind(joinQuizModel.getPlayersProperty());
		mJoin.disableProperty().bind(joinQuizModel.getJoinDisableProperty());
		quiznameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getQuizname())));
		hostnameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getHostname())));

		quizTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showQuizDetails(newValue));
	}

	@FXML
	public void handleCreateQuiz() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(joinQuizHandler);
		eventBroker.removeEventListener(getQuizzesHandler);
		eventBroker.removeEventListener(newQuizHandler);
		eventBroker.removeEventListener(startQuizHandler);
		
		main.showCreateQuizScene();
	}

	public void showQuizDetails(QuizModel quiz) {
		if (quiz != null) {
			selectedQuiz = quiz;
			joinQuizModel.updateQuizDetail(quiz.getQuizname(), quiz.getRounds(), quiz.getTeams(), quiz.getPlayers());
		}
	}

	@FXML
	private void handleJoin() {
		User user = MainContext.getContext().getUser();
		ClientJoinQuizEvent cJQE = new ClientJoinQuizEvent(user.getUserID(), user.getUsername(),
				selectedQuiz.getQuizID());
		publishEvent(cJQE);
	}

	@FXML
	private void handleBack() {
		MainContext context = MainContext.getContext();
		context.setQuiz(null);
		// Log user out
		ClientLogOutEvent cLOE = new ClientLogOutEvent();
		publishEvent(cLOE);
		context.setUser(null);

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(joinQuizHandler);
		eventBroker.removeEventListener(getQuizzesHandler);
		eventBroker.removeEventListener(newQuizHandler);
		eventBroker.removeEventListener(startQuizHandler);

		main.showLogInScene();
	}

	// Inner classes
	private class NewQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewQuizEvent sNQE = (ServerNewQuizEvent) event;

			QuizModel quiz = sNQE.getQuiz();

			joinQuizModel.addQuiz(quiz);
		}

	}

	private class JoinQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerJoinQuizEvent sJQE = (ServerJoinQuizEvent) event;

			Quiz quiz = sJQE.getQuiz();

			MainContext context = MainContext.getContext();
			quiz.addUnassignedPlayer(context.getUser().getUserID(), context.getUser().getUsername());
			context.setQuiz(quiz);

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(joinQuizHandler);
			eventBroker.removeEventListener(getQuizzesHandler);
			eventBroker.removeEventListener(newQuizHandler);
			eventBroker.removeEventListener(startQuizHandler);

			main.showJoinTeamScene();
		}

	}

	private class GetQuizzesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerGetQuizzesEvent sGQE = (ServerGetQuizzesEvent) event;

			ArrayList<QuizModel> quizzes = sGQE.getQuizzes();
			joinQuizModel.addQuizzes(quizzes);
		}

	}

	private class StartQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerStartQuizEvent sSTQE = (ServerStartQuizEvent) event;

			int quizID = sSTQE.getQuizID();

			joinQuizModel.removeQuiz(quizID);
		}

	}

}
