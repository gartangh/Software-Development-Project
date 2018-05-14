package quiz.view;

import java.awt.image.BufferedImage;

import chat.ChatPanel;
import eventbroker.Event;
import eventbroker.EventBroker;
import eventbroker.EventListener;
import eventbroker.EventPublisher;
import eventbroker.serverevent.ServerEndQuizEvent;
import eventbroker.serverevent.ServerNewIPQuestionEvent;
import eventbroker.serverevent.ServerNewMCQuestionEvent;
import eventbroker.serverevent.ServerNewRoundEvent;
import eventbroker.serverevent.ServerVoteAnswerEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import main.Main;
import main.MainContext;
import quiz.model.IPQuestion;
import quiz.model.MCQuestion;
import quiz.model.Team;
import quiz.model.TeamNameID;
import quiz.model.WaitHostModel;
import quiz.util.RoundType;

public class WaitHostController extends EventPublisher {

	@FXML
	private TableView<TeamNameID> answeredTeamsTable;
	@FXML
	private TableColumn<TeamNameID, String> teamnameColumn;
	@FXML
	private TableColumn<TeamNameID, String> answerColumn;
	@FXML
	private VBox rightVBox;
	@FXML
	private Label questionTitle;
	@FXML
	private Text questionText;
	@FXML
	private ImageView imageView;
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
	private NewIPQuestionHandler newIPQuestionHandler = new NewIPQuestionHandler();
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
		eventBroker.addEventListener(ServerNewIPQuestionEvent.EVENTTYPE, newIPQuestionHandler);
		eventBroker.addEventListener(ServerVoteAnswerEvent.EVENTTYPE, updateTeamsHandler);
		eventBroker.addEventListener(ServerEndQuizEvent.EVENTTYPE, endQuizHandler);

		teamnameColumn.setCellValueFactory(cellData -> cellData.getValue().getTeamname());
		answerColumn.setCellValueFactory(cellData -> cellData.getValue().getAnswer());
		questionTitle.textProperty().bind(waitHostModel.getQuestionTitleProperty());
		questionText.textProperty().bind(waitHostModel.getQuestionTextProperty());
		imageView.imageProperty().bind(waitHostModel.getImageProperty());

		answerA.textProperty().bind(waitHostModel.getAnswerPropertyA());
		answerB.textProperty().bind(waitHostModel.getAnswerPropertyB());
		answerC.textProperty().bind(waitHostModel.getAnswerPropertyC());
		answerD.textProperty().bind(waitHostModel.getAnswerPropertyD());
		correctAnswer.textProperty().bind(waitHostModel.getCorrectAnswerProperty());

		// ChatPanel (ChatModel and ChatController) are created
		ChatPanel chatPanel = ChatPanel.createChatPanel();
		mPlaceholder.getChildren().add(chatPanel.getContent());
	}

	public void setRoundType(RoundType roundType) {
		waitHostModel.setRoundType(roundType);
		switch (roundType) {
		case IP:
			rightVBox.getChildren().remove(1);
			imageView.imageProperty().bind(waitHostModel.getImageProperty());
			break;
		case MC:
			rightVBox.getChildren().remove(2);
			questionText.textProperty().bind(waitHostModel.getQuestionTextProperty());
			break;
		}
	}

	private class NewRoundHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(newRoundHandler);
			eventBroker.removeEventListener(newMCQuestionHandler);
			eventBroker.removeEventListener(newIPQuestionHandler);
			eventBroker.removeEventListener(updateTeamsHandler);
			eventBroker.removeEventListener(endQuizHandler);

			MainContext.getContext().setQuestion(null);

			main.showCreateRoundScene();
		}

	}

	private class NewMCQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewMCQuestionEvent sNMCQE = (ServerNewMCQuestionEvent) event;

			int questionID = sNMCQE.getQuestionID();
			String question = sNMCQE.getQuestion();
			String[] answers = sNMCQE.getAnswers();
			int correctAnswer = sNMCQE.getCorrectAnswer();

			waitHostModel.clearTeams();
			waitHostModel.setRoundType(RoundType.MC);

			MainContext context = MainContext.getContext();
			context.setQuestion(new MCQuestion(questionID, question, answers, correctAnswer));
			context.setRoundType(RoundType.MC);

			waitHostModel.updateQuestion();
		}

	}

	private class NewIPQuestionHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerNewIPQuestionEvent sNIPQE = (ServerNewIPQuestionEvent) event;

			int questionID = sNIPQE.getQuestionID();
			BufferedImage bufImage = sNIPQE.getImage();
			String[] answers = sNIPQE.getAnswers();
			int correctAnswer = sNIPQE.getCorrectAnswer();

			waitHostModel.clearTeams();
			waitHostModel.setRoundType(RoundType.IP);

			IPQuestion q = new IPQuestion(questionID, bufImage, true, answers, correctAnswer);
			q.setPixelSize(1);

			MainContext.getContext().setQuestion(q);
			MainContext.getContext().setRoundType(RoundType.IP);
			waitHostModel.updateQuestion();

		}
	}

	private class UpdateTeamsHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			ServerVoteAnswerEvent cAE = (ServerVoteAnswerEvent) event;

			int answerID = cAE.getAnswer();

			Team team = MainContext.getContext().getQuiz().getTeamMap().get(cAE.getTeamID());
			TeamNameID teamNameID = new TeamNameID(new SimpleStringProperty(team.getTeamname()), team.getTeamID(),
					new SimpleStringProperty(team.getPlayerMap().get(team.getCaptainID())));

			StringProperty answer = null;
			if (answerID == 0)
				answer = new SimpleStringProperty("A");
			else if (answerID == 1)
				answer = new SimpleStringProperty("B");
			else if (answerID == 2)
				answer = new SimpleStringProperty("C");
			else if (answerID == 3)
				answer = new SimpleStringProperty("D");
			if (answer != null) {
				teamNameID.setAnswer(answer);
				waitHostModel.addTeam(teamNameID);
			}
		}
	}

	private class EndQuizHandler implements EventListener {

		@Override
		public void handleEvent(Event event) {
			EventBroker eventBroker = EventBroker.getEventBroker();
			eventBroker.removeEventListener(newRoundHandler);
			eventBroker.removeEventListener(endQuizHandler);

			main.showScoreboardScene();
		}

	}

}
