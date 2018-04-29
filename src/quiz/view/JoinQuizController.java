package quiz.view;

import java.util.Map;
import java.util.Map.Entry;

import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientGetQuizzesEvent;
import eventbroker.clientevent.ClientJoinQuizEvent;
import eventbroker.serverevent.ServerGetQuizzesEvent;
import eventbroker.serverevent.ServerJoinQuizEvent;
import eventbroker.serverevent.ServerSendQuizEvent;
import eventbroker.serverevent.ServerStartQuizEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import main.Context;
import main.Main;
import quiz.model.JoinQuizModel;
import quiz.model.Quiz;

public class JoinQuizController extends EventPublisher {

	@FXML
	private TableView<Quiz> quizTable;
	@FXML
	private TableColumn<Quiz, String> quiznameColumn;
	@FXML
	private TableColumn<Quiz, String> hostnameColumn;
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

	private Quiz selectedQuiz;
	private JoinQuizModel joinQuizModel = new JoinQuizModel();
	private JoinQuizHandler joinQuizHandler;
	private GetQuizzesHandler getQuizzesHandler;
	private SendQuizHandler sendQuizHandler;
	private StartQuizHandler startQuizHandler;

	// Reference to the main application
	private Main main;

	public void setMainApp(Main main) {
		this.main = main;
		quizTable.setItems(joinQuizModel.getQuizzes());
		Context.getContext().setTeamID(-1);
	}

	// Methods
	@FXML
	private void initialize() {
		joinQuizHandler = new JoinQuizHandler();
		getQuizzesHandler = new GetQuizzesHandler();
		sendQuizHandler = new SendQuizHandler();
		startQuizHandler = new StartQuizHandler();

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerJoinQuizEvent.EVENTTYPE, joinQuizHandler);
		eventBroker.addEventListener(ServerGetQuizzesEvent.EVENTTYPE, getQuizzesHandler);
		eventBroker.addEventListener(ServerSendQuizEvent.EVENTTYPE, sendQuizHandler);
		eventBroker.addEventListener(ServerStartQuizEvent.EVENTTYPE, startQuizHandler);

		mQuizname.textProperty().bind(joinQuizModel.getQuiznameProperty());
		mRounds.textProperty().bind(joinQuizModel.getQuizRoundsProperty());
		mTeams.textProperty().bind(joinQuizModel.getTeamProperty());
		mPlayers.textProperty().bind(joinQuizModel.getPlayersProperty());
		mJoin.disableProperty().bind(joinQuizModel.getJoinDisableProperty());
		quiznameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getQuizname())));
		hostnameColumn
				.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getHostname())));

		quizTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showQuizDetails(newValue));

		ClientGetQuizzesEvent cGQE = new ClientGetQuizzesEvent();
		publishEvent(cGQE);
	}

	@FXML
	public void handleCreateQuiz() {
		Context.getContext().getUser().castToHost();

		main.showCreateQuizScene();
	}

	public void showQuizDetails(Quiz quiz) {
		if (quiz != null) {
			selectedQuiz = quiz;
			joinQuizModel.updateQuizDetail(quiz);
		} else {
			// TODO
		}
	}

	@FXML
	private void handleJoin() {
		Context.getContext().getUser().castToGuest();
		ClientJoinQuizEvent cjqe = new ClientJoinQuizEvent(Context.getContext().getUser().getUserID(),
				selectedQuiz.getQuizID(), Context.getContext().getUser().getUsername());
		publishEvent(cjqe);
	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		Context.getContext().setQuiz(null);

		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.removeEventListener(joinQuizHandler);
		eventBroker.removeEventListener(getQuizzesHandler);
		eventBroker.removeEventListener(sendQuizHandler);
		eventBroker.removeEventListener(startQuizHandler);

		main.showLogInScene();
	}

	// Inner classes
	private class JoinQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerJoinQuizEvent sJQE = (ServerJoinQuizEvent) event;

			Quiz quiz = sJQE.getQuiz();

			Context context = Context.getContext();
			quiz.addUnassignedPlayer(context.getUser().getUserID(), context.getUser().getUsername());
			context.setQuiz(quiz);

			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(joinQuizHandler);
			eventBroker.removeEventListener(getQuizzesHandler);
			eventBroker.removeEventListener(sendQuizHandler);
			eventBroker.removeEventListener(startQuizHandler);

			main.showQuizroomScene();
		}

	}

	private class GetQuizzesHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerGetQuizzesEvent sGQE = (ServerGetQuizzesEvent) event;

			Map<Integer, Quiz> quizMap = sGQE.getQuizMap();

			for (Entry<Integer, Quiz> entry : quizMap.entrySet())
				joinQuizModel.addQuiz(entry.getValue());
		}

	}

	private class SendQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerSendQuizEvent sSQE = (ServerSendQuizEvent) event;

			int quizID = sSQE.getQuizID();
			String quizname = sSQE.getQuizname();
			int maxAmountOfTeams = sSQE.getMaxAmountOfTeams();
			int maxAmountOfPlayersPerTeam = sSQE.getMaxAmountOfPlayersPerTeam();
			int maxAmountOfRounds = sSQE.getMaxAmountOfRounds();
			int hostID = sSQE.getHostID();
			String hostname = sSQE.getHostname();

			joinQuizModel.addQuiz(new Quiz(quizID, quizname, maxAmountOfTeams, maxAmountOfPlayersPerTeam, maxAmountOfRounds, hostID, hostname));
		}

	}

	private class StartQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerStartQuizEvent sSTQE = (ServerStartQuizEvent) event;

			int quizID = sSTQE.getQuizID();

			joinQuizModel.deleteQuiz(quizID);
		}

	}

}
