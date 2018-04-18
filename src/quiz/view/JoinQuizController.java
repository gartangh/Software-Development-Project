package quiz.view;

import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import main.Context;
import main.Main;
import quiz.model.Quiz;
import quiz.model.ScoreboardTeam;
import quiz.model.Team;
import quiz.util.ClientGetQuizzesEvent;
import quiz.util.ClientJoinQuizEvent;
import quiz.view.ScoreboardController.ScoreboardEventHandler;
import server.ServerGetQuizzesEvent;
import server.ServerJoinQuizEvent;
import server.ServerSendQuizEvent;
import server.ServerStartQuizEvent;

public class JoinQuizController extends EventPublisher {

	@FXML
	private TableView<Quiz> quizTable;
	@FXML
	private TableColumn<Quiz, String> quiznameColumn;
	@FXML
	private TableColumn<Quiz, String> quizmasternameColumn;
	@FXML
	private Button mBack;
	@FXML
	private Button mJoin;
	@FXML
	private Label mQuizname;
	@FXML
	private Label mRounds;
	@FXML
	private Label mQuestionsPerRound;
	@FXML
	private Label mTeams;
	@FXML
	private Label mPlayersPerTeam;

	// Reference to the main application
	private Main main;
	private JoinQuizModel joinQuizModel = new JoinQuizModel();
	private JoinQuizEventHandler joinQuizeventHandler = new JoinQuizEventHandler();
	private Quiz selectedQuiz;

	public void setMainApp(Main main) {
		this.main = main;
		quizTable.setItems(joinQuizModel.getQuizzes());
		Context.getContext().setTeamID(-1);
	}

	@FXML
	private void initialize() {
		EventBroker.getEventBroker().addEventListener(joinQuizeventHandler);

		mQuizname.textProperty().bind(joinQuizModel.getQuiznameProperty());
		mRounds.textProperty().bind(joinQuizModel.getQuizRoundsProperty());
		mQuestionsPerRound.textProperty().bind(joinQuizModel.getQuestionsPerRoundProperty());
		mTeams.textProperty().bind(joinQuizModel.getTeamProperty());
		mPlayersPerTeam.textProperty().bind(joinQuizModel.getPlayersPerTeamProperty());
		mJoin.disableProperty().bind(joinQuizModel.getJoinDisableProperty());
		quiznameColumn.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getQuizname())));
		quizmasternameColumn
				.setCellValueFactory(cellData -> (new SimpleStringProperty(cellData.getValue().getQuizMasterName())));

		quizTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showQuizDetails(newValue));

		ClientGetQuizzesEvent cGQE = new ClientGetQuizzesEvent();
		publishEvent(cGQE);
	}

	public void showQuizDetails(Quiz quiz){
	    if (quiz != null) {
	    	selectedQuiz=quiz;
	    	joinQuizModel.updateQuizDetail(quiz);
	    }
	    else {
	    	// TODO
	    }
	   }

	@FXML
	private void handleJoin() {
		ClientJoinQuizEvent cjqe=new ClientJoinQuizEvent(Context.getContext().getUser().getUserID(),selectedQuiz.getQuizID(),Context.getContext().getUser().getUsername());
		publishEvent(cjqe);
	}

	@FXML
	private void handleBack() {
		// TODO: Handle back
		// TODO: set context quiz back to null;
		Context.getContext().setQuiz(null);
		main.showModeSelectorScene();
	}

	public class JoinQuizEventHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			Quiz quiz;
			switch (event.getType()) {
			case "SERVER_GET_QUIZZES":
				ServerGetQuizzesEvent sGQE = (ServerGetQuizzesEvent) event;

				for (Entry<Integer, Quiz> entry : sGQE.getQuizMap().entrySet())
					joinQuizModel.addQuiz(entry.getValue());

				System.out.println("Event received and handled: " + event.getType());
				break;

			case "SERVER_SEND_QUIZ":
				ServerSendQuizEvent sSQE = (ServerSendQuizEvent) event;
				quiz = sSQE.getQuiz();
				joinQuizModel.addQuiz(quiz);
				break;

			case "SERVER_JOIN_QUIZ":
				ServerJoinQuizEvent sJQE = (ServerJoinQuizEvent) event;
				quiz = sJQE.getQuiz();
				quiz.addUnassignedPlayer(Context.getContext().getUser().getUserID(),
						Context.getContext().getUser().getUsername());
				Context.getContext().setQuiz(quiz);
				main.showQuizroomScene();
				break;
			case "SERVER_START_QUIZ":
				ServerStartQuizEvent sSTQE=(ServerStartQuizEvent) event;
				joinQuizModel.deleteQuiz(sSTQE.getQuizID());
				break;
			default:
				System.out.println("Event received but left unhandled: " + event.getType());
			}
		}
	}
}
