package quiz.view;

import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.clientevent.ClientAnswerEvent;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import main.Main;
import main.MainContext;
import quiz.model.MCQuestion;
import quiz.model.Team;
import quiz.model.TeamNameID;
import quiz.model.WaitHostModel;

public class WaitHostController extends EventPublisher {
	
	@FXML
	private TableView<TeamNameID> answeredTeamsTable;
	@FXML
	private TableColumn<TeamNameID, String> teamnameColumn;
	@FXML
	private TableColumn<TeamNameID, String> answerColumn;
	@FXML
	private Label questionTitle;
	@FXML
	private Text questionText;
	@FXML
	private Label answerA;
	@FXML
	private Label answerB;
	@FXML
	private Label answerC;
	@FXML
	private Label answerD;
	@FXML
	private Label correctAnswer;
	@FXML
	private AnchorPane mPlaceholder;
	
	private WaitHostModel waitHostModel = new WaitHostModel();
	private NewMCQuestionHandler newMCQuestionHandler = new NewMCQuestionHandler();
	private UpdateTeamsHandler updateTeamsHandler = new UpdateTeamsHandler();
	private NewRoundHandler newRoundHandler = new NewRoundHandler();
	private EndQuizHandler endQuizHandler = new EndQuizHandler();

	// Reference to the main application
	private Main main;

	public void setMain(Main main) {
		this.main = main;
		
		answeredTeamsTable.setItems(waitHostModel.getTeams());
	}
	
	@FXML
	private void initialize() {
		EventBroker eventBroker = EventBroker.getEventBroker();
		eventBroker.addEventListener(ServerNewRoundEvent.EVENTTYPE, newRoundHandler);
		eventBroker.addEventListener(ServerNewMCQuestionEvent.EVENTTYPE, newMCQuestionHandler);
		eventBroker.addEventListener(ServerVoteAnswerEvent.EVENTTYPE, updateTeamsHandler);
		eventBroker.addEventListener(ServerEndQuizEvent.EVENTTYPE, endQuizHandler);
		
		teamnameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamname());
		answerColumn.setCellValueFactory(cellData -> cellData.getValue().getAnswer());
		questionTitle.textProperty().bind(waitHostModel.getQuestionTitleProperty());
		questionText.textProperty().bind(waitHostModel.getQuestionTextProperty());

		answerA.textProperty().bind(waitHostModel.getAnswerPropertyA());
		answerB.textProperty().bind(waitHostModel.getAnswerPropertyB());
		answerC.textProperty().bind(waitHostModel.getAnswerPropertyC());
		answerD.textProperty().bind(waitHostModel.getAnswerPropertyD());
		// TODO (also WaitHostModel: 47)
		correctAnswer.textProperty().bind(waitHostModel.getCorrectAnswerProperty());
		
		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());
	}

	private void pokeTeam(TeamNameID newValue) {
		// TODO Send poke to team :)
	}

	private class NewRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(newRoundHandler);
			eventBroker.removeEventListener(newMCQuestionHandler);
			eventBroker.removeEventListener(updateTeamsHandler);
			eventBroker.removeEventListener(endQuizHandler);

			main.showCreateRoundScene();
		}

	}
	
	private class NewMCQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			// TODO: clear the table
			ServerNewMCQuestionEvent sNMCQE = (ServerNewMCQuestionEvent) event;

			int questionID = sNMCQE.getQuestionID();
			String question = sNMCQE.getQuestion();
			String[] answers = sNMCQE.getAnswers();
			int correctAnswer = sNMCQE.getCorrectAnswer();
			
			waitHostModel.clearTeams();
			
			MCQuestion q = new MCQuestion(questionID, question, answers, correctAnswer);
			MainContext.getContext().setQuestion(q);
			waitHostModel.updateQuestion();
		}

	}
	
	private class UpdateTeamsHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			// TODO: add received team to table
			ServerVoteAnswerEvent cAE = (ServerVoteAnswerEvent) event;
			Team team = MainContext.getContext().getQuiz().getTeamMap().get(cAE.getTeamID());
			TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(team.getTeamname()), team.getTeamID());
			int answerID = cAE.getAnswer();
			StringProperty answer = null;
			if(answerID == 0) answer = new SimpleStringProperty("A");
			else if(answerID == 1) answer = new SimpleStringProperty("B");
			else if(answerID == 2) answer = new SimpleStringProperty("C");
			else if(answerID == 3) answer = new SimpleStringProperty("D");
			if(answer != null) {
				teamNameID.setAnswer(answer);
				waitHostModel.addTeam(teamNameID);
			}
		}
	}

	private class EndQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			EventBroker.getEventBroker().removeEventListener(newRoundHandler);
			EventBroker.getEventBroker().removeEventListener(endQuizHandler);

			main.showScoreboardScene();
		}

	}

}
